package fr.utbm.vi51.agent;

import java.util.List;

import org.janusproject.kernel.status.Status;
import org.janusproject.kernel.status.StatusFactory;

import fr.utbm.vi51.configs.Consts;
import fr.utbm.vi51.environment.Direction;
import fr.utbm.vi51.environment.DropFood;
import fr.utbm.vi51.environment.EatFood;
import fr.utbm.vi51.environment.Food;
import fr.utbm.vi51.environment.InsectBody;
import fr.utbm.vi51.environment.KillEnemy;
import fr.utbm.vi51.environment.Message;
import fr.utbm.vi51.environment.Move;
import fr.utbm.vi51.environment.Perception;
import fr.utbm.vi51.environment.Pheromone;
import fr.utbm.vi51.environment.Side;
import fr.utbm.vi51.environment.Square;
import fr.utbm.vi51.environment.WorldObject;
import fr.utbm.vi51.util.PathFinder;
import fr.utbm.vi51.util.Point3D;

/**
 * @author Top-K
 *
 */
enum WarriorBehaviour {
    GO_HOME, PATROL,
}

/**
 * @author Top-K
 *
 */
public class Warrior extends Ant {
    /**
     *
     */
    private static final long serialVersionUID = -9113010531820084357L;

    private WarriorBehaviour currentBehaviour;
    private Point3D lastPosition;
    private Point3D relativeStartingPointPosition; // Remembers the position of

    public Warrior(Point3D position, int speed, Side side) {
        super(side.getWarriorTexture(), position, speed, side);
        currentBehaviour = WarriorBehaviour.GO_HOME;
    }

    @Override
    public Status activate(Object... params) {
        lastTime = this.getTimeManager().getCurrentDate().getTime();
        return StatusFactory.ok(this);
    }

    @Override
    public Status live() {
        super.live();
        InsectBody body = this.getBody();

        //If their is no body, the agent is waiting to die
        if (body == null) {
            return null;
        }

        // If an action is already planned, wait for it to be resolved
        if (body.getAction() != null) {
            return null;
        }

        // Worker will only act every ANTACTIONDELAY milliseconds
        if (this.getTimeManager().getCurrentDate().getTime() - lastTime < Consts.ANTACTIONDELAY
                && lastTime != 0) {
            return null;
        }

        if (relativeStartingPointPosition != null) {
            relativeStartingPointPosition.x -= body.getPosition().x
                    - lastPosition.x;
            relativeStartingPointPosition.y -= body.getPosition().y
                    - lastPosition.y;
        }
        lastPosition = new Point3D(body.getPosition());

        Perception currentPerception = this.getBody().getPerception();

        /*if (dropPheromoneIfNeeded()) {
            return null;
        }*/

        if (body.isHungry()) {
            if (body.getCarriedObject() instanceof Food) {
                body.setAction(new EatFood(body));
                lastTime = this.getTimeManager().getCurrentDate().getTime();
                return null;
            }
            currentBehaviour = WarriorBehaviour.GO_HOME;
        }

        if (dropPheromoneIfNeeded()) {
            return null;
        }

        switch (currentBehaviour) {
            case GO_HOME:
                goHome();
                break;
            case PATROL:
                patrol();
                break;
            default:
                break;
        }

        if (movementPath != null && !movementPath.isEmpty()) {
            lastTime = this.getTimeManager().getCurrentDate().getTime();
            Move m = new Move(body, movementPath.removeFirst());
            body.setAction(m);
            return null;
        }

        /*
         * If after behavior functions, no action has been defined and movement
         * path is empty, the worker doesn't know what to do, so move randomly .
         */
        if (body.getAction() == null
                && (movementPath == null || movementPath.isEmpty())) {
            Square[][][] perceivedMap = currentPerception.getPerceivedMap();
            // Find a crossable square
            int x;
            int y;
            do {
                x = (int) Math.floor(Math.random() * perceivedMap.length);
                y = (int) Math.floor(Math.random() * perceivedMap[0].length);
            } while (!perceivedMap[x][y][0].getLandType().isCrossable());
            movementPath = PathFinder.findPath(currentPerception
                    .getPositionInPerceivedMap(), new Point3D(x, y, 0),
                    perceivedMap);
        }
        return null;

    }

    /**
     * Drops a pheromone if the closest pheromone with a strenght/maxStrength. >
     * 0.5 is at a euclidian distance > 2
     *
     * @return true if a pheromone will be dropped, false else.
     */
    private boolean dropPheromoneIfNeeded() {
        Perception currentPerception = this.getBody().getPerception();

        // Variables for a pheromone validity
        final float acceptedOldPh = 0.5f;
        final int acceptedDistancePh = 2;

        // If we are looking for home and the body is not carrying food, no need
        // to create pheromone
        if (currentBehaviour == WarriorBehaviour.GO_HOME) {
            return false;
        }

        Square[][][] perceivedMap = currentPerception.getPerceivedMap();
        // Represents the targets position, the nature of the target depends on
        // the current behaviour (food for search food...)
        Point3D targetPosition = null;
        for (int i = 0; i < perceivedMap.length; ++i) {
            for (int j = 0; j < perceivedMap[0].length; ++j) {
                for (WorldObject wo : perceivedMap[i][j][0].getObjects()) {
                    if (currentBehaviour == WarriorBehaviour.PATROL
                            && wo.getTexturePath().equals(
                                    this.getBody().getSide().getQueenTexture())) {
                        targetPosition = new Point3D(wo.getPosition());
                    }
                    if (wo instanceof Pheromone) {
                        Pheromone p = (Pheromone) wo;
                        // Check validity of the pheromone : strength is
                        // sufficient and is of correct type
                        if (p.getMessage() == Message.HOME
                                && p.getSide().equals(this.getBody().getSide())
                                && p.getStrength()
                                        / Consts.STARTINGPHEROMONEVALUE > acceptedOldPh) {
                            // If the pheromone is valid and close enough to the
                            // body's position, no need to create one
                            if (Point3D.euclidianDistance(p.getPosition(), this
                                    .getBody().getPosition()) <= acceptedDistancePh) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        // If no close and valid pheromone has been found, create one
        Message m = Message.HOME;
        assert m != null;

        // If the target position is visible, place a pheromone pointing to it.
        // Else, point the pheromone to the position of the insect a few moves
        // ago.
        if (targetPosition != null) {
            new Pheromone(this.getBody().getPosition(), m,
                    Direction.toDirection(this.getBody().getPosition(),
                            targetPosition),
                    (int) Consts.STARTINGPHEROMONEVALUE, this.getBody()
                            .getSide());
            return true;
        } else if (relativeStartingPointPosition != null) {
            new Pheromone(this.getBody().getPosition(), m,
                    Direction.toDirection(new Point3D(0, 0, 0),
                            relativeStartingPointPosition),
                    (int) Consts.STARTINGPHEROMONEVALUE, this.getBody()
                            .getSide());
            return true;
        }
        return false;

    }

    private void goHome() {
        Perception currentPerception = this.getBody().getPerception();
        Square[][][] perceivedMap = currentPerception.getPerceivedMap();
        Pheromone currentBestPheromone = null;
        Point3D currentBestPheromonePositionInPerceivedMap = null;

        // If the body is carrying food and we are on the queen's square, drop
        // the food
        // If the body is hungry and there is food on the same square, eat it 
        for (WorldObject wo : perceivedMap[currentPerception
                .getPositionInPerceivedMap().x][currentPerception
                .getPositionInPerceivedMap().y][0].getObjects()) {
            if (this.getBody().isHungry() && wo instanceof Food) {
                this.getBody().setAction(new EatFood(this.getBody()));
                return;
            }
            if (!this.getBody().isHungry()
                    && wo.getTexturePath().equals(
                            this.getBody().getSide().getQueenTexture())) {
                if (this.getBody().getCarriedObject() != null) {
                    this.getBody().setAction(new DropFood(this.getBody()));
                }
                currentBehaviour = WarriorBehaviour.PATROL;
                relativeStartingPointPosition = new Point3D(0, 0, 0);
                return;
            }
        }

        // Look for the queen, for home pheromones, or if hungry for food
        for (int i = 0; i < perceivedMap.length; ++i) {
            for (int j = 0; j < perceivedMap[0].length; ++j) {
                List<WorldObject> objects = perceivedMap[i][j][0].getObjects();
                synchronized (objects) {
                    for (WorldObject wo : objects) {
                        if (wo.getTexturePath().equals(
                                this.getBody().getSide().getQueenTexture())) {
                            movementPath = PathFinder.findPath(
                                    currentPerception
                                            .getPositionInPerceivedMap(),
                                    new Point3D(i, j, 0), perceivedMap);
                            return;
                        } else if (wo instanceof Pheromone) {
                            Pheromone p = (Pheromone) wo;
                            if (p.getMessage() == Message.HOME
                                    && p.getSide().equals(
                                            this.getBody().getSide())) {
                                currentBestPheromone = Pheromone
                                        .closestToSubject(p,
                                                currentBestPheromone);
                                if (currentBestPheromone == p) {
                                    currentBestPheromonePositionInPerceivedMap = new Point3D(
                                            i, j, 0);
                                }
                            }
                        } else if (this.getBody().isHungry()
                                && wo instanceof Food) {
                            movementPath = PathFinder.findPath(
                                    currentPerception
                                            .getPositionInPerceivedMap(),
                                    new Point3D(i, j, 0), perceivedMap);
                            return;
                        }
                    }
                }
            }
        }
        if (currentBestPheromone != null
                && currentBestPheromonePositionInPerceivedMap != null) {
            movementPath = PathFinder.findPath(
                    currentPerception.getPositionInPerceivedMap(),
                    currentBestPheromonePositionInPerceivedMap, perceivedMap);
        }
    }

    private void patrol() {
        Perception p = this.getBody().getPerception();
        Square[][][] perceivedMap = p.getPerceivedMap();
        Point3D positionInPerceivedMap = p.getPositionInPerceivedMap();
        InsectBody closestEnemyBody = null;
        double closestEnemyDistance = Double.POSITIVE_INFINITY;
        Point3D closestEnemyPositionInPerceivedMap = null;
        for (int i = 0; i < perceivedMap.length; ++i) {
            for (int j = 0; j < perceivedMap[0].length; ++j) {
                List<WorldObject> objectsPerceived = perceivedMap[i][j][0]
                        .getObjects();
                for (WorldObject wo : objectsPerceived) {
                    if (wo instanceof InsectBody) {
                        InsectBody ib = (InsectBody) wo;
                        if (!ib.getSide().equals(this.getBody().getSide())) {
                            double distance = Point3D.euclidianDistance(this
                                    .getBody().getPosition(), ib.getPosition());
                            if (distance < closestEnemyDistance) {
                                closestEnemyBody = ib;
                                closestEnemyDistance = distance;
                                closestEnemyPositionInPerceivedMap = new Point3D(
                                        i, j, 0);
                            }
                        }
                    }
                }
            }
        }
        if (positionInPerceivedMap != null
                && closestEnemyPositionInPerceivedMap != null
                && perceivedMap != null) {
            movementPath = PathFinder.findPath(positionInPerceivedMap,
                    closestEnemyPositionInPerceivedMap, perceivedMap);
        }

        if (closestEnemyBody != null && movementPath != null) {
            if (movementPath.size() == 0) {
                this.getBody().setAction(
                        new KillEnemy(this.getBody(), Direction.NONE));
                lastTime = this.getTimeManager().getCurrentDate().getTime();
                return;
            } else if (movementPath.size() == 1) {
                this.getBody().setAction(
                        new KillEnemy(this.getBody(), movementPath.getFirst()));
                lastTime = this.getTimeManager().getCurrentDate().getTime();
                return;
            }
        }

        if (relativeStartingPointPosition != null
                && Point3D.euclidianDistance(new Point3D(0, 0, 0),
                        relativeStartingPointPosition) > 20) {
            currentBehaviour = WarriorBehaviour.GO_HOME;
        } else if (closestEnemyBody != null) {
            movementPath = PathFinder.findPath(positionInPerceivedMap,
                    closestEnemyPositionInPerceivedMap, perceivedMap);
        } else if (relativeStartingPointPosition != null
                && Point3D.euclidianDistance(new Point3D(0, 0, 0),
                        relativeStartingPointPosition) > 10) {
            currentBehaviour = WarriorBehaviour.GO_HOME;
        } else {
            this.getBody().setAction(
                    new Move(this.getBody(), Direction.random()));
            lastTime = this.getTimeManager().getCurrentDate().getTime();
        }
    }
}
