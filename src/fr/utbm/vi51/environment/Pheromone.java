package fr.utbm.vi51.environment;

import fr.utbm.vi51.util.Point3D;

/**
 * @author Top-K
 * 
 */
public class Pheromone extends WorldObject {
    private Message mess;
    private Direction direction;
    protected int strength; // Represents distance and amount of food/danger.
                            // Weakens with time

    public Pheromone(Point3D position, Message mess, Direction direction,
            int strength) {
        super(position, "img/Objects/pheromone.png");
        this.mess = mess;
        this.direction = direction;
        this.strength = strength;
    }

    public Message getMessage() {
        return mess;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getStrength() {
        return strength;
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
                        if (bPos.x < aPos.x && bPos.y <= aPos.y) {
                            return b;
                        } else {
                            return a;
                        }
                    case 0:
                        if (bPos.x < aPos.x) {
                            return b;
                        } else {
                            return a;
                        }
                    case 1:
                        if (bPos.x < aPos.x && bPos.y >= aPos.y) {
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
                        if (bPos.y < aPos.y) {
                            return b;
                        } else {
                            return a;
                        }
                    case 1:
                        if (bPos.y > aPos.y) {
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
                        if (bPos.x > aPos.x && bPos.y <= aPos.y) {
                            return b;
                        } else {
                            return a;
                        }
                    case 0:
                        if (bPos.x > aPos.x) {
                            return b;
                        } else {
                            return a;
                        }
                    case 1:
                        if (bPos.x > aPos.x && bPos.y >= aPos.y) {
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

}
