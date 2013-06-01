package fr.utbm.vi51.agent;

import java.util.List;

import org.janusproject.kernel.status.Status;
import org.janusproject.kernel.status.StatusFactory;

import fr.utbm.vi51.configs.Consts;
import fr.utbm.vi51.environment.Direction;
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

enum WarriorBehaviour {
    GO_HOME, PATROL,FIGHT,
}

/**
 * @author Top-K
 * 
 */
public class Warrior extends Ant {
    private WarriorBehaviour currentBehaviour;
    private Point3D lastPosition;
    private Point3D relativeStartingPointPosition; // Remembers the position of
    private int attackPoints;

    public Warrior(Point3D position, int speed, Side side) {
        super(side.getWarriorTexture(), position, speed, side);
        currentBehaviour = WarriorBehaviour.PATROL;
        attackPoints = 1;
    }

    @Override
    public Status activate(Object... params) {
        lastTime = this.getTimeManager().getCurrentDate().getTime();
        return StatusFactory.ok(this);
    }

    @Override
    public Status live() {
        super.live();
        if (this.getBody() == null) {
        	return null;
        }
        InsectBody body = this.getBody();
        // If an action is already planned, wait for it to be resolved
        if (body.getAction() != null) {
            return null;
        }
        if (this.getTimeManager().getCurrentDate().getTime() - lastTime < Consts.ANTACTIONDELAY
                && lastTime != 0) {
            return null;
        }
        Perception currentPerception = this.getBody().getPerception();
        Square[][][] perceivedMap = currentPerception.getPerceivedMap();
        Point3D positionInPerceivedMap = currentPerception
                .getPositionInPerceivedMap();
        
        // If perceive a warrior not from its side then set the currentBehaviour to FIGHT
        for (int i = 0; i < perceivedMap.length; ++i) {
        	for (int j = 0; j < perceivedMap[0].length; ++j) {
        		List<WorldObject> objects = perceivedMap[i][j][0].getObjects();
        		synchronized (objects) {
        			for (WorldObject wo : objects) {
        				if (((InsectBody) wo).getSide() != body.getSide()) {
        					currentBehaviour = WarriorBehaviour.FIGHT;
        				}
        			}
        		}
        	}
        }
        
        if (movementPath != null && !movementPath.isEmpty()) {
        	lastTime = this.getTimeManager().getCurrentDate().getTime();
        	Move m = new Move(body, movementPath.removeFirst());
        	body.setAction(m);
        	return null;
        }

        if (relativeStartingPointPosition != null) {
            relativeStartingPointPosition.x -= this.getBody().getPosition().x
                    - lastPosition.x;
            relativeStartingPointPosition.y -= this.getBody().getPosition().y
                    - lastPosition.y;
        }
        lastPosition = new Point3D(this.getBody().getPosition());

        switch (currentBehaviour) {
            case PATROL:
                patrol();
                break;
            case GO_HOME:
                goHome();
                break;
            case FIGHT:
            	fight();
            	break;
            default:
                assert false;
                break;
        }
        for (WorldObject wo : perceivedMap[positionInPerceivedMap.x][positionInPerceivedMap.y][0]
                .getObjects()) {
            if (wo.getTexturePath().equals(this.getBody().getSide().getQueenTexture()) && ((InsectBody) wo).getSide() == this.getBody().getSide()) {
                relativeStartingPointPosition = new Point3D(0, 0, 0);
                break;
            }

        }
        

        lastTime = this.getTimeManager().getCurrentDate().getTime();

        return null;
    }
    

	private void goHome() {
	    Perception currentPerception = this.getBody().getPerception();
        Square[][][] perceivedMap = currentPerception.getPerceivedMap();
        Pheromone currentBestPheromone = null;
        Point3D currentBestPheromonePositionInPerceivedMap = null;

        // If the body is hungry and there is food on the same square, eat it 
        for (WorldObject wo : perceivedMap[currentPerception
                .getPositionInPerceivedMap().x][currentPerception
                .getPositionInPerceivedMap().y][0].getObjects()) {
            if (wo instanceof Food) {
                this.getBody().setAction(new EatFood(this.getBody()));
                return;
            }
        }

        // Look for the queen, for home pheromones, or if hungry for food
        for (int i = 0; i < perceivedMap.length; ++i) {
            for (int j = 0; j < perceivedMap[0].length; ++j) {
                List<WorldObject> objects = perceivedMap[i][j][0].getObjects();
                synchronized (objects) {
                    for (WorldObject wo : objects) {
                        if (wo.getTexturePath().equals(this.getBody().getSide().getQueenTexture()) && ((InsectBody) wo).getSide() == this.getBody().getSide()) {
                            movementPath = PathFinder.findPath(
                                    currentPerception
                                            .getPositionInPerceivedMap(),
                                    new Point3D(i, j, 0), perceivedMap);
                            return;
                        } else if (wo instanceof Pheromone) {
                            Pheromone p = (Pheromone) wo;
                            if (p.getMessage() == Message.HOME) {
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
        if (currentBestPheromone != null) {
            movementPath = PathFinder.findPath(
                    currentPerception.getPositionInPerceivedMap(),
                    currentBestPheromonePositionInPerceivedMap, perceivedMap);
        }
    }

    private void patrol() {
        Move m;
        if (relativeStartingPointPosition != null
                && Point3D.euclidianDistance(this.getBody().getPosition(),
                        relativeStartingPointPosition) < 10) {
            m = new Move(this.getBody(), Direction.random());
        } else {
            m = new Move(this.getBody(), Direction.random());
        }
        float difftime = this.getTimeManager().getCurrentDate().getTime()
                - lastTime;

        this.getBody().setAction(m);
    }
    
    private void fight() {
        Perception currentPerception = this.getBody().getPerception();
		Square[][][] perceivedMap = currentPerception.getPerceivedMap();
		Point3D positionInPerceivedMap = currentPerception.getPositionInPerceivedMap();
		
		//If on the same square as the enemy, then attack him
		
		/*for (WorldObject wo : perceivedMap[positionInPerceivedMap.x][positionInPerceivedMap.y][0].getObjects()) {
			if (wo.getTexturePath().equals("img/Ants/warrior.png") && ((InsectBody) wo).getSide() != this.getBody().getSide()) {
				InsectBody enemy = ((InsectBody) wo);
				enemy.setHealthPoints(enemy.getHealthPoints() - this.attackPoints);
				//if the enemy has no HP then he dies
				if (enemy.getHealthPoints() <= 0) {
					this.getBody().setAction(new KillEnemy(this.getBody()));
					
				}
			}
		}*/
		
		for (int i = 0; i < perceivedMap.length; ++i) {
			for (int j = 0; j < perceivedMap[0].length; ++j) {
				List<WorldObject> objectsPerceived = perceivedMap[i][j][0].getObjects();
				synchronized (objectsPerceived) {
					for (WorldObject wo : objectsPerceived) {
						if (wo.getTexturePath().equals("img/Ants/warrior.png") && ((InsectBody) wo).getSide() != this.getBody().getSide()) {
							//if the enemy is not on the same square then try to reach it
							if (Point3D.euclidianDistance(this.getBody().getPosition(), wo.getPosition()) != 0) {
								System.out.println("enemy side :" + ((InsectBody) wo).getSide());
								System.out.println("side :" + this.getBody().getSide());
								System.out.println("enemy pos : " + wo.getPosition());
								System.out.println("pos : " + this.getBody().getPosition());
								movementPath = PathFinder.findPath(currentPerception.getPositionInPerceivedMap(), new Point3D(i, j, 0), perceivedMap);
								return;
							} else {
								//else fight the enemy
								System.out.println("FIGHT");
								InsectBody enemy = ((InsectBody) wo);
								enemy.setHealthPoints(enemy.getHealthPoints() - this.attackPoints);
								System.out.println("HP :" + enemy.getHealthPoints());
								if (enemy.getHealthPoints() <= 0) {
									//enemy dies !
									this.getBody().setAction(new KillEnemy(this.getBody()));
								}
								
							}
						}
					}
				}
			}
		}
		
	}

}
