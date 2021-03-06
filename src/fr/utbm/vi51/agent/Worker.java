package fr.utbm.vi51.agent;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.janusproject.kernel.status.Status;
import org.janusproject.kernel.status.StatusFactory;

import fr.utbm.vi51.configs.Consts;
import fr.utbm.vi51.environment.Direction;
import fr.utbm.vi51.environment.DropFood;
import fr.utbm.vi51.environment.DropPheromone;
import fr.utbm.vi51.environment.EatFood;
import fr.utbm.vi51.environment.Food;
import fr.utbm.vi51.environment.InsectBody;
import fr.utbm.vi51.environment.InsectBodyType;
import fr.utbm.vi51.environment.Message;
import fr.utbm.vi51.environment.MobileObject;
import fr.utbm.vi51.environment.Move;
import fr.utbm.vi51.environment.Perception;
import fr.utbm.vi51.environment.Pheromone;
import fr.utbm.vi51.environment.Side;
import fr.utbm.vi51.environment.Square;
import fr.utbm.vi51.environment.TakeFood;
import fr.utbm.vi51.environment.WorldObject;
import fr.utbm.vi51.util.PathFinder;
import fr.utbm.vi51.util.Point3D;

/**
 * @author Theo
 * 
 */
enum WorkerBehaviour {
    GO_HOME, SEARCH_FOOD,
}

/**
 * @author Theo
 * 
 */
public class Worker extends Ant {
    private static final long serialVersionUID = 6981140970183171037L;
    private WorkerBehaviour currentBehaviour;
    private Point3D lastPosition;
    private Point3D relativeStartingPointPosition; // Remembers the position of
                                                   // the starting point (where
                                                   // food was taken, where home
                                                   // was...)
    @SuppressWarnings("unused")
    private Logger log = Logger.getLogger(MobileObject.class.getName());

    public Worker(Point3D position, int speed, Side side) {
        super(side.getWorkerTexture(), position, speed, side);
        this.currentBehaviour = WorkerBehaviour.GO_HOME;
        this.lastPosition = new Point3D(position);
    }

    @Override
    public Status activate(Object... params) {
        return StatusFactory.ok(this);
    }

    @Override
    public Status live() {
        super.live();
        // Worker will only act every ANTACTIONDELAY milliseconds
        if (this.getTimeManager().getCurrentDate().getTime() - this.lastTime < Consts.ANTACTIONDELAY
                && this.lastTime != 0) {
            return null;
        }

        InsectBody body = this.getBody();

        //If their is no body, the agent is waiting to die
        if (body == null) {
            return null;
        }

        // If the side is defeated
        if (body.getSide().isDefeated()) {
            // Stop all
            return null;
        }

        // If an action is already planned, wait for it to be resolved
        if (body.getAction() != null) {
            return null;
        }

        if (this.relativeStartingPointPosition != null) {
            this.relativeStartingPointPosition.x -= body.getPosition().x
                    - this.lastPosition.x;
            this.relativeStartingPointPosition.y -= body.getPosition().y
                    - this.lastPosition.y;
        }
        this.lastPosition = new Point3D(body.getPosition());

        Perception currentPerception = this.getBody().getPerception();

        if (dropPheromoneIfNeeded()) {
            return null;
        }

        if (this.movementPath != null && !this.movementPath.isEmpty()) {
            this.lastTime = this.getTimeManager().getCurrentDate().getTime();
            Move m = new Move(body, this.movementPath.removeFirst());
            body.setAction(m);
            return null;
        }

        if (body.isHungry()) {
            if (body.getCarriedObject() instanceof Food) {
                body.setAction(new EatFood(body));
                return null;
            }
            this.currentBehaviour = WorkerBehaviour.GO_HOME;
        }

        switch (this.currentBehaviour) {
            case GO_HOME:
                goHome();
                break;
            case SEARCH_FOOD:
                searchFood();
                break;
            default:
                break;
        }

        /*
         * If after behavior functions, no action has been defined and movement
         * path is empty, the worker doesn't know what to do, so move randomly .
         */
        if (body.getAction() == null
                && (this.movementPath == null || this.movementPath.isEmpty())) {
            Square[][][] perceivedMap = currentPerception.getPerceivedMap();
            // Find a crossable square
            int x;
            int y;
            do {
                x = (int) Math.floor(Math.random() * perceivedMap.length);
                y = (int) Math.floor(Math.random() * perceivedMap[0].length);
            } while (!perceivedMap[x][y][0].getLandType().isCrossable());
            this.movementPath = PathFinder.findPath(currentPerception
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
        if (this.currentBehaviour == WorkerBehaviour.GO_HOME
                && this.getBody().getCarriedObject() == null) {
            return false;
        }

        Square[][][] perceivedMap = currentPerception.getPerceivedMap();
        // Represents the targets position, the nature of the target depends on
        // the current behaviour (food for search food...)
        boolean validUtilityPheromoneFound = false;
        boolean validDangerPheromoneFound = false;
        boolean dangerPheromoneRequired = false;
        Point3D dangerPosition = null;
        Point3D targetPosition = null;
        for (int i = 0; i < perceivedMap.length
                && !(validDangerPheromoneFound && validUtilityPheromoneFound); ++i) {
            for (int j = 0; j < perceivedMap[0].length
                    && !(validDangerPheromoneFound && validUtilityPheromoneFound); ++j) {
                for (WorldObject wo : perceivedMap[i][j][0].getObjects()) {
                    if (this.currentBehaviour == WorkerBehaviour.GO_HOME
                            && wo instanceof Food
                            || this.currentBehaviour == WorkerBehaviour.SEARCH_FOOD
                            && wo.getTexturePath().equals(
                                    this.getBody().getSide().getQueenTexture())
                            && ((InsectBody) wo).getSide().equals(
                                    this.getBody().getSide())) {
                        targetPosition = new Point3D(wo.getPosition());
                    }
                    if (wo instanceof Pheromone) {
                        Pheromone p = (Pheromone) wo;
                        // Check validity of the pheromone : strength is
                        // sufficient and is of correct type
                        if (p.getStrength() / p.getStartingStrength() > acceptedOldPh
                                && Point3D.euclidianDistance(p.getPosition(),
                                        this.getBody().getPosition()) <= acceptedDistancePh) {
                            // If the pheromone is valid and close enough to the
                            // body's position, no need to create one
                            if ((this.currentBehaviour == WorkerBehaviour.GO_HOME && p
                                    .getMessage() == Message.FOOD)
                                    || (this.currentBehaviour == WorkerBehaviour.SEARCH_FOOD && p
                                            .getMessage() == Message.HOME)) {
                                validUtilityPheromoneFound = true;
                            } else if (p.getMessage() == Message.DANGER) {
                                validDangerPheromoneFound = true;
                            }
                        }
                    } else if (wo instanceof InsectBody) {
                        InsectBody ib = (InsectBody) wo;
                        if (ib.getType() == InsectBodyType.WARRIOR
                                && !ib.getSide().equals(
                                        this.getBody().getSide())) {
                            dangerPheromoneRequired = true;
                            dangerPosition = ib.getPosition();
                        }
                    }
                }
            }
        }

        if (validUtilityPheromoneFound && validDangerPheromoneFound) {
            return false;
        }

        if (dangerPheromoneRequired && !validDangerPheromoneFound) {
            this.getBody().setAction(
                    new DropPheromone(this.getBody().getSide(), this.getBody()
                            .getPosition(), Message.DANGER, Direction
                            .toDirection(this.getBody().getPosition(),
                                    dangerPosition)));
            return true;
        }

        // If no close and valid pheromone has been found, create one
        Message m;
        switch (this.currentBehaviour) {
            case GO_HOME:
                m = Message.FOOD;
                break;
            case SEARCH_FOOD:
                m = Message.HOME;
                break;
            default:
                m = null;
        }
        // If no close and valid pheromone has been found, create one
        // No target defined
        if (targetPosition == null
                && this.relativeStartingPointPosition == null
                || validUtilityPheromoneFound) {
            // Cannot drop pheromone
            return false;
        }

        // If the target position is visible, place a pheromone pointing to it.
        // Else, point the pheromone to the position of the insect a few moves
        // ago.
        getBody().setAction(
                new DropPheromone(this.getBody().getSide(), this.getBody()
                        .getPosition(), m, (targetPosition != null) ? Direction
                        .toDirection(this.getBody().getPosition(),
                                targetPosition) : Direction.toDirection(
                        new Point3D(0, 0, 0),
                        this.relativeStartingPointPosition)));
        return true;
    }

    private void searchFood() {
        Perception currentPerception = this.getBody().getPerception();
        Square[][][] perceivedMap = currentPerception.getPerceivedMap();
        Point3D positionInPerceivedMap = currentPerception
                .getPositionInPerceivedMap();
        Food foodFound = null;
        Pheromone currentBestFoodPheromone = null;
        Point3D currentBestPheromonePositionInPerceivedMap = null;

        // If the body is already carrying an object, go home
        if (this.getBody().getCarriedObject() != null) {
            this.currentBehaviour = WorkerBehaviour.GO_HOME;
            goHome();
            return;
        }

        // If there is food on the same square as the body, take it
        boolean foodOnSameSquare = false;
        for (WorldObject wo : perceivedMap[positionInPerceivedMap.x][positionInPerceivedMap.y][0]
                .getObjects()) {
            if (wo.getTexturePath().equals(
                    this.getBody().getSide().getQueenTexture())
                    && ((InsectBody) wo).getSide().equals(
                            this.getBody().getSide())) {
                foodOnSameSquare = false;
                break;
            }
            if (wo instanceof Food) {
                foodOnSameSquare = true;
            }
        }
        if (foodOnSameSquare) {
            this.getBody().setAction(new TakeFood(this.getBody()));
            this.currentBehaviour = WorkerBehaviour.GO_HOME;
            this.relativeStartingPointPosition = new Point3D(0, 0, 0);
            return;
        }
        // Searching the map for food or food pheromones
        for (int i = 0; i < perceivedMap.length; ++i) {
            for (int j = 0; j < perceivedMap[0].length; ++j) {
                for (WorldObject wo : perceivedMap[i][j][0].getObjects()) {
                    // Avoid perceiving the food on the queen's square as it is
                    // already where it needs to be
                    if (wo.getTexturePath().equals(
                            this.getBody().getSide().getQueenTexture())
                            && ((InsectBody) wo).getSide().equals(
                                    this.getBody().getSide())) {
                        foodFound = null;
                        break;
                    }
                    if (foodFound == null && wo instanceof Food) {
                        foodFound = (Food) wo;
                    } else if (wo instanceof Pheromone) {
                        Pheromone p = (Pheromone) wo;
                        if (p.getMessage() == Message.FOOD
                                && p.getSide().equals(this.getBody().getSide())) {
                            currentBestFoodPheromone = Pheromone
                                    .closestToSubject(p,
                                            currentBestFoodPheromone);
                            if (currentBestFoodPheromone == p) {
                                currentBestPheromonePositionInPerceivedMap = new Point3D(
                                        i, j, 0);
                            }
                        } else if (p.getMessage() == Message.DANGER
                                && p.getSide().equals(this.getBody().getSide())) {
                            //Trying to flee
                            LinkedList<Direction> fleeDirections = new LinkedList<Direction>();
                            fleeDirections.add(p.getDirection().opposite());
                            fleeDirections.add(p.getDirection().opposite()
                                    .east());
                            fleeDirections.add(p.getDirection().opposite()
                                    .west());
                            while (!fleeDirections.isEmpty()) {
                                if (perceivedMap[positionInPerceivedMap.x
                                        + fleeDirections.getFirst().dx][positionInPerceivedMap.y
                                        + fleeDirections.getFirst().dy][positionInPerceivedMap.z]
                                        .getLandType().isCrossable()) {
                                    movementPath = new LinkedList<>();
                                    movementPath.add(fleeDirections.getFirst());
                                    return;
                                }
                                fleeDirections.pollFirst();
                            }
                        }
                    }
                }
                if (foodFound != null) {
                    this.movementPath = PathFinder.findPath(
                            currentPerception.getPositionInPerceivedMap(),
                            new Point3D(i, j, 0), perceivedMap);
                    return;
                }
            }
        }
        if (currentBestFoodPheromone != null
                && currentBestPheromonePositionInPerceivedMap != null
                && currentBestFoodPheromone.getMessage() == Message.FOOD) {
            this.movementPath = PathFinder.findPath(
                    currentPerception.getPositionInPerceivedMap(),
                    currentBestPheromonePositionInPerceivedMap, perceivedMap);
        } else if (currentBestFoodPheromone != null
                && currentBestFoodPheromone.getMessage() == Message.HOME) {
            //Moves to anywhere but the pheromone direction
            /*int x = 0;
            int y = 0;
            ArrayList<Direction> possibleValues = new ArrayList<Direction>(
                    Arrays.asList(Direction.values()));
            possibleValues.remove(Direction.NONE);
            possibleValues.remove(currentBestFoodPheromone.getDirection());
            possibleValues.remove(currentBestFoodPheromone.getDirection()
                    .west());
            possibleValues.remove(currentBestFoodPheromone.getDirection()
                    .east());
            Direction randomDirection;
            do {
                if (possibleValues.isEmpty()) {
                    possibleValues.add(currentBestFoodPheromone.getDirection());
                    possibleValues.add(currentBestFoodPheromone.getDirection()
                            .west());
                    possibleValues.add(currentBestFoodPheromone.getDirection()
                            .east());
                }
                randomDirection = possibleValues.get((CustomRandom
                        .getNextInt(possibleValues.size())));
                x = positionInPerceivedMap.x + randomDirection.dx;
                y = positionInPerceivedMap.y + randomDirection.dy;
                if (!perceivedMap[x][y][0].getLandType().isCrossable()) {
                    possibleValues.remove(randomDirection);
                    continue;
                }
            } while (!perceivedMap[x][y][0].getLandType().isCrossable());
            for (int i = 0; i < (CustomRandom
                    .getNextInt(5))
                    && perceivedMap[x][y][0].getLandType().isCrossable(); ++i) {
                x = positionInPerceivedMap.x + randomDirection.dx;
                y = positionInPerceivedMap.y + randomDirection.dy;
            }
            //When we get there, the current square is not crossable anymore, so we go back one step
            x = positionInPerceivedMap.x + randomDirection.opposite().dx;
            y = positionInPerceivedMap.y + randomDirection.opposite().dy;

            movementPath = PathFinder.findPath(currentPerception
                    .getPositionInPerceivedMap(), new Point3D(x, y, 0),
                    perceivedMap);*/
            if (currentBestPheromonePositionInPerceivedMap != null) {
                this.movementPath = PathFinder.findPath(
                        currentPerception.getPositionInPerceivedMap(),
                        currentBestPheromonePositionInPerceivedMap,
                        perceivedMap);
            }
        }

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
                            this.getBody().getSide().getQueenTexture())
                    && ((InsectBody) wo).getSide().equals(
                            this.getBody().getSide())) {
                if (this.getBody().getCarriedObject() != null) {
                    this.getBody().setAction(new DropFood(this.getBody()));
                }
                this.currentBehaviour = WorkerBehaviour.SEARCH_FOOD;
                this.relativeStartingPointPosition = new Point3D(0, 0, 0);
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
                                this.getBody().getSide().getQueenTexture())
                                && ((InsectBody) wo).getSide().equals(
                                        this.getBody().getSide())) {
                            this.movementPath = PathFinder.findPath(
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
                            this.movementPath = PathFinder.findPath(
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
            this.movementPath = PathFinder.findPath(
                    currentPerception.getPositionInPerceivedMap(),
                    currentBestPheromonePositionInPerceivedMap, perceivedMap);
        }
    }
}
