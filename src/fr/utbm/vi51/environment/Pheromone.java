package fr.utbm.vi51.environment;

import fr.utbm.vi51.util.Point3D;

/**
 * @author Theo
 *
 */
public class Pheromone extends WorldObject {
    protected int strength;
    private Message message;
    private Direction direction;
    private Side side;
    private final int startingStrength;

    public Pheromone(Point3D position, Message mess, Direction direction,
            int strength, Side side) {
        super(position, "img/Objects/pheromone.png");
        this.message = mess;
        this.direction = direction;
        this.strength = strength;
        this.startingStrength = strength;
        this.side = side;
    }
    
    public boolean isEqual(Pheromone pheromone) {
    	return this.isEqual(pheromone.message, pheromone.direction, pheromone.side);
    }
    
    public boolean isEqual(Message message,Direction direction,Side side) {
    	if(message != this.message) {
    		return false;
    	}
    	if(direction != this.direction) {
    		return false;
    	}
    	if(side != this.side) {
    		return false;
    	}
    	return true;
    }
    
    public void refresh() {
    	this.strength = startingStrength;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public Message getMessage() {
        return message;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getStrength() {
        return strength;
    }
    
    public float getStartingStrength() {
        return startingStrength;
    }

    public static Pheromone closestToSubject(Pheromone a, Pheromone b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }

        Point3D bPos = b.getPosition();
        Point3D aPos = a.getPosition();
        switch (a.direction.dx) {
            case -1:
                switch (a.direction.dy) {
                    case -1:
                        if (bPos.x <= aPos.x && bPos.y <= aPos.y) {
                            return b;
                        } else {
                            return a;
                        }
                    case 0:
                        if (bPos.x <= aPos.x) {
                            return b;
                        } else {
                            return a;
                        }
                    case 1:
                        if (bPos.x <= aPos.x && bPos.y >= aPos.y) {
                            return b;
                        } else {
                            return a;
                        }
                    default:
                        return null;
                }
            case 0:
                switch (a.direction.dy) {
                    case -1:
                        if (bPos.y <= aPos.y) {
                            return b;
                        } else {
                            return a;
                        }
                    case 0:
                        return b;
                    case 1:
                        if (bPos.y >= aPos.y) {
                            return b;
                        } else {
                            return a;
                        }
                    default:
                        return null;
                }
            case 1:
                switch (a.direction.dy) {
                    case -1:
                        if (bPos.x >= aPos.x && bPos.y <= aPos.y) {
                            return b;
                        } else {
                            return a;
                        }
                    case 0:
                        if (bPos.x >= aPos.x) {
                            return b;
                        } else {
                            return a;
                        }
                    case 1:
                        if (bPos.x >= aPos.x && bPos.y >= aPos.y) {
                            return b;
                        } else {
                            return a;
                        }
                    default:
                        return null;
                }
            default:
                return null;
        }
    }

    @Override
    public String getTexturePath() {
        switch (message) {
            case DANGER:
                return "img/Objects/dangerPheromone.png";
            case FOOD:
                return "img/Objects/foodPheromone.png";
            case HOME:
                return "img/Objects/homePheromone.png";
            default:
                return null;
        }
    }

    public String toString() {
        return "{pos:" + getPosition() + " message:" + message + " direction:"
                + direction + "strength:" + strength + "}";
    }

}
