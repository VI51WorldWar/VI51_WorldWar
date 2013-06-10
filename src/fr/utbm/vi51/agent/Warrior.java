package fr.utbm.vi51.agent;

import java.util.List;

import org.janusproject.kernel.status.Status;
import org.janusproject.kernel.status.StatusFactory;

import fr.utbm.vi51.configs.Consts;
import fr.utbm.vi51.environment.Direction;
import fr.utbm.vi51.environment.EatFood;
import fr.utbm.vi51.environment.Food;
import fr.utbm.vi51.environment.InsectBody;
import fr.utbm.vi51.environment.InsectBodyType;
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
	private static final long serialVersionUID = 6837234120747826328L;
    // Variables for a pheromone validity
    private final float acceptedOldPh = 0.5f;
    private final int acceptedDistancePh = 2;
    
    
	private WarriorBehaviour 	currentBehaviour;
    private Point3D 			lastPosition;
    private Point3D 			relativeStartingPointPosition;
//    private static final int attackPoints = 1;
    
    /**
     * Constructor for the warrior Agent
     * @param position
     * @param speed
     * @param side
     */
    public Warrior(Point3D position, int speed, Side side) {
        super(side.getWarriorTexture(), position, speed, side);
        this.currentBehaviour = WarriorBehaviour.GO_HOME;
    }

    @Override
    public Status activate(Object... params) {
        this.lastTime = this.getTimeManager().getCurrentDate().getTime();
        return StatusFactory.ok(this);
    }

    @Override
    public Status live() {
        super.live();
        // Insects will only act every ANTACTIONDELAY milliseconds
        if (	this.getTimeManager().getCurrentDate().getTime() - this.lastTime < Consts.ANTACTIONDELAY
                && this.lastTime != 0) {
            return null;
        }
        
        InsectBody body = this.getBody();
        //If their is no body, the agent is waiting to die
        if (body == null) {
            return null;
        }
        
        // If the side is defeated
        if(body.getSide().isDefeated()) {
        	// Stop all
        	return null;
        }

        // If an action is already planned, wait for it to be resolved
        if (body.getAction() != null) {
            return null;
        }

        // Update the relative and current position
        updatePositions(body);

        
        if(eatIfNeed(body)) {
        	return null;
        }

        if (dropPheromoneIfNeeded()) {
            return null;
        }

        switch (this.currentBehaviour) {
            case GO_HOME:	goHome();					break;
            case PATROL:	patrol();					break;
            //case FIGHT:		fight();					break;
            default:		generateWanderMovement();	break;
        }
        
        doNextMovement(body);
        // Success
        return null;
    }

    /**
     * Function doNextMovement
     * Transform the next movement in path into the move action
     * !! WARNING !! The movement path MUST be filled when you call this function
     * @param body
     */
	private void doNextMovement(InsectBody body) {
		// If an action is already planed
		if(this.getBody().getAction() != null) {
			// Do nothing
			return;
		}
		if(this.movementPath == null) {
			System.out.println("Error in warrior algorithm 1." + this.currentBehaviour.toString()); //$NON-NLS-1$
			return;
		}
		if(this.movementPath.isEmpty()) {
			System.out.println("Error in warrior algorithm 2." + this.currentBehaviour.toString()); //$NON-NLS-1$
			return;
		}
		// Do next movement in the pathfinding list
		this.lastTime = this.getTimeManager().getCurrentDate().getTime();
        Move m = new Move(body, this.movementPath.removeFirst());
        body.setAction(m);
	}

	/**
	 * Function updatePositions
	 * Update the relative position of the starting point
	 * and the current position of the body
	 * @param body
	 */
	private void updatePositions(InsectBody body) {
		if (this.relativeStartingPointPosition != null) {
            this.relativeStartingPointPosition.x -= body.getPosition().x - this.lastPosition.x;
            this.relativeStartingPointPosition.y -= body.getPosition().y - this.lastPosition.y;
        }
        this.lastPosition = new Point3D(body.getPosition());
	}

    /**
     * Drops a pheromone if the closest pheromone with a strenght/maxStrength. >
     * 0.5 is at a euclidian distance > 2
     *
     * @return true if a pheromone will be dropped, false else.
     */
    private boolean dropPheromoneIfNeeded() {
        // If we are looking for home no need to create pheromone
        if (this.currentBehaviour == WarriorBehaviour.GO_HOME) {
            return false;
        }

        Square[][][] perceivedMap = this.getBody().getPerception().getPerceivedMap();
        // Represents the targets position, the nature of the target depends on
        // the current behaviour (food for search food...)
        Point3D targetPosition = null;
        for (int i = 0; i < perceivedMap.length; ++i) {
            for (int j = 0; j < perceivedMap[0].length; ++j) {
                for (WorldObject wo : perceivedMap[i][j][0].getObjects()) {
                    if (	this.currentBehaviour == WarriorBehaviour.PATROL
                            && wo.getTexturePath().equals(this.getBody().getSide().getQueenTexture())) {
                        targetPosition = new Point3D(wo.getPosition());
                    }
                    if (wo instanceof Pheromone) {
                        Pheromone p = (Pheromone) wo;
                        // Check validity of the pheromone : 
                        // strength is sufficient and is of correct type
                        if (p.getMessage() == Message.HOME 
                        	&& p.getSide().equals(this.getBody().getSide()) 
                        	&& p.getStrength() / Consts.STARTINGPHEROMONEVALUE > this.acceptedOldPh) {
                            // If the pheromone is valid and close enough to the
                            // body's position, no need to create one
                            if (Point3D.euclidianDistance(p.getPosition(),this.getBody().getPosition()) <= this.acceptedDistancePh) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        // If no close and valid pheromone has been found, create one

        // If the target position is visible, place a pheromone pointing to it.
        // Else, point the pheromone to the position of the insect a few moves
        // ago.
        @SuppressWarnings("unused")
		Pheromone p = null;
        if (targetPosition != null) {
            p = new Pheromone(	this.getBody().getPosition(),
				            				Message.HOME,
				                    		Direction.toDirection(this.getBody().getPosition(),targetPosition),
				                    		(int) Consts.STARTINGPHEROMONEVALUE, 
				                    		this.getBody().getSide());
            return true;
        } else if (this.relativeStartingPointPosition != null) {
            p = new Pheromone(	this.getBody().getPosition(),
	            				Message.HOME,
	            				Direction.toDirection(new Point3D(0, 0, 0),this.relativeStartingPointPosition),
	            				(int) Consts.STARTINGPHEROMONEVALUE,
	            				this.getBody().getSide());
            return true;
        }
        return false;

    }

    private void goHome() {
        Perception currentPerception = this.getBody().getPerception();
        Square[][][] perceivedMap = currentPerception.getPerceivedMap();
        Pheromone currentBestPheromone = null;
        Point3D currentBestPheromonePositionInPerceivedMap = null;
        
        boolean isOnQueenSquare = false;
        // If the body is hungry and there is food on the same square, eat it 
        for (WorldObject wo : perceivedMap	[currentPerception.getPositionInPerceivedMap().x]
        									[currentPerception.getPositionInPerceivedMap().y]
        									[0].getObjects()) {
        	// Eat the food which is on square
            if (this.getBody().isHungry() && wo instanceof Food) {
                this.getBody().setAction(new EatFood(this.getBody()));
                // Back to patrol
                this.currentBehaviour = WarriorBehaviour.PATROL;
                return;
            }
            // A object on the current square is the queen
            if( wo instanceof InsectBody) {
            	InsectBody insectBody = (InsectBody) wo;
            	if(	this.getBody().getSide().equals(insectBody.getSide()) 
            		&& insectBody.getType() == InsectBodyType.QUEEN) {
            		isOnQueenSquare = true;
            	}
            }
        }
        
        // If warrior is on queen's square
        if(isOnQueenSquare) {
        	// Go patrol
        	this.currentBehaviour = WarriorBehaviour.PATROL;
        	patrol();
        	return;
        }

        // Look for the queen, for home pheromones, or if hungry for food
        for (int i = 0; i < perceivedMap.length; ++i) {
            for (int j = 0; j < perceivedMap[0].length; ++j) {
                for (WorldObject wo : perceivedMap[i][j][0].getObjects()) {
                	// Queen is founded
                    if (wo.getTexturePath().equals(this.getBody().getSide().getQueenTexture())) {
                        // Try to reach it
                    	this.movementPath = PathFinder.findPath(currentPerception.getPositionInPerceivedMap(),
                                								new Point3D(i, j, 0), 
                                								perceivedMap);
                    	// If a good path has been generated
                    	if(this.movementPath != null) {
                    		if(!this.movementPath.isEmpty()) {
                    			// Go out
                    			return;
                    		}
                    	}
                    	System.out.println("Error in goHome() for Warrior l.248 reached"); //$NON-NLS-1$
                        return;
                    // Search the best pheromone for home 
                    } else if (wo instanceof Pheromone) {
                        Pheromone p = (Pheromone) wo;
                        if (	p.getMessage() == Message.HOME && 
                        		p.getSide().equals(this.getBody().getSide())) {
                            currentBestPheromone = Pheromone.closestToSubject(p,currentBestPheromone);
                            if (currentBestPheromone == p) {
                                currentBestPheromonePositionInPerceivedMap = new Point3D(i, j, 0);
                            }
                        }
                    // If Hungry, search for food
                    } else if (this.getBody().isHungry() && wo instanceof Food) {
                        this.movementPath = PathFinder.findPath(currentPerception.getPositionInPerceivedMap(),
                                								new Point3D(i, j, 0), 
                                								perceivedMap);
                     // If a good path has been generated
                    	if(this.movementPath != null) {
                    		if(!this.movementPath.isEmpty()) {
                    			// Go out
                    			return;
                    		}
                    	}
                    	System.out.println("Error in goHome() for Warrior l.299 reached"); //$NON-NLS-1$
                        return;
                    }
                }            
            }
        }
        
        if (currentBestPheromone != null && currentBestPheromonePositionInPerceivedMap != null) {
            this.movementPath = PathFinder.findPath(currentPerception.getPositionInPerceivedMap(),
                    								currentBestPheromonePositionInPerceivedMap, 
                    								perceivedMap);
            // If a good path has been generated
        	if(this.movementPath != null) {
        		if(!this.movementPath.isEmpty()) {
        			// Go out
        			return;
        		}
        	}
        }
        // In case of no pheromone or no food or no queen, do wander
        generateWanderMovement();
    }

    private void patrol() {
        Perception p = this.getBody().getPerception();
        Square[][][] perceivedMap = p.getPerceivedMap();
        Point3D positionInPerceivedMap = p.getPositionInPerceivedMap();
        
        InsectBody closestEnemyBody = null;
        double closestEnemyDistance = Double.POSITIVE_INFINITY;
        Point3D closestEnemyPositionInPerceivedMap = null;
        // Search the closest enemy in perception field
        for (int i = 0; i < perceivedMap.length; ++i) {
            for (int j = 0; j < perceivedMap[0].length; ++j) {
                List<WorldObject> objectsPerceived = perceivedMap[i][j][0].getObjects();
                for (WorldObject wo : objectsPerceived) {
                	//
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
        // If no enemy has been found in perceived field
        if(closestEnemyBody == null) {
        	// If the warrior is too far from its queen 
            if (	this.relativeStartingPointPosition != null
                    && Point3D.euclidianDistance(new Point3D(0, 0, 0),this.relativeStartingPointPosition) > 20) {
                // Return to home
            	goHome();
            }
        	// Do a wander movement
        	generateWanderMovement();
        	return;
        }

        // An enemy has been found -> generate the path to reach its position
        this.movementPath = PathFinder.findPath(positionInPerceivedMap,closestEnemyPositionInPerceivedMap, perceivedMap);
        // No path to target are available
        if(this.movementPath == null) {
        	// Generate a random movement
        	generateWanderMovement();
        	return;
        }
        // If the enemy is on the same square as the Warrior
        if(this.movementPath.size()  == 0) {
        		// Attack him
                this.getBody().setAction(new KillEnemy(this.getBody(),Direction.NONE));
                this.lastTime = this.getTimeManager().getCurrentDate().getTime();
        // Else if there is only 1 movement between the warrior and its target
        } else if(this.movementPath.size() == 1) {
                this.getBody().setAction(new KillEnemy(this.getBody(),this.movementPath.getFirst()));
                this.lastTime = this.getTimeManager().getCurrentDate().getTime();
        }
    }

    private void fight() {
    	// Function to go attack another camp is the number of warrior is big
    	// AT THE MOMENT MUST NOT BE CALLED
    	System.out.println("Error: call to fight() for Warrior"); //$NON-NLS-1$
    }
    
    /**
     * Function to check if the insect is hungry
     * @return true if the body can eat the food he's carrying false otherwise
     */
    private boolean eatIfNeed(InsectBody body) {
    	if (body.isHungry()) {
//            if (body.getCarriedObject() instanceof Food) {
//                body.setAction(new EatFood(body));
//                this.lastTime = this.getTimeManager().getCurrentDate().getTime();
//                return true;
//            }
            this.currentBehaviour = WarriorBehaviour.GO_HOME;
        }
    	return false;
    }
}
