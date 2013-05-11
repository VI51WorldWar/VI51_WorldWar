package fr.utbm.vi51.environment;

import java.util.Random;

import fr.utbm.vi51.util.Point3D;

/**
 * 
 * @author Author: sgalland
 */
public enum Direction {
    /** North. */
    NORTH(0, -1) {
        @Override
        public Direction opposite() {
            return SOUTH;
        }
    },
    /** West. */
    WEST(-1, 0) {
        @Override
        public Direction opposite() {
            return EAST;
        }
    },
    /** South. */
    SOUTH(0, +1) {
        @Override
        public Direction opposite() {
            return NORTH;
        }
    },
    /** East. */
    EAST(+1, 0) {
        @Override
        public Direction opposite() {
            return WEST;
        }
    },
    /** NorthEast. */
    NORTHEAST(+1, -1) {
        @Override
        public Direction opposite() {
            return SOUTHWEST;
        }
    },
    /** NorthWest. */
    NORTHWEST(-1, -1) {
        @Override
        public Direction opposite() {
            return SOUTHEAST;
        }
    },
    /** NorthEast. */
    SOUTHWEST(-1, +1) {
        @Override
        public Direction opposite() {
            return NORTHEAST;
        }
    },
    /** SouthEast. */
    SOUTHEAST(-1, -1) {
        @Override
        public Direction opposite() {
            return NORTHWEST;
        }
    },
    /** None. */
    NONE(0, 0) {
        @Override
        public Direction opposite() {
            return NONE;
        }
    };

    /**
     * Relative coordinate of the direction.
     */
    public final int dx;

    /**
     * Relative coordinate of the direction.
     */
    public final int dy;

    /**
     * @param x
     * @param y
     */
    Direction(int x, int y) {
        this.dx = x;
        this.dy = y;
    }

    /**
     * Replies a random direction.
     * 
     * @return a random direction.
     */
    public static Direction random() {
        Random rnd = new Random();
        return values()[rnd.nextInt(values().length)];
    }

    /**
     * Replies the opposite direction.
     * 
     * @return the opposite direction.
     */
    public abstract Direction opposite();

    public static Direction toDirection(Point3D start, Point3D target) {
        if (target.x - start.x >= 1) {
            if (target.y - start.y <= -1) {
                return Direction.NORTHEAST;
            } else if (target.y - start.y == 0) {
                return Direction.EAST;
            } else {
                return Direction.SOUTHEAST;
            }
        }
        if (target.x - start.x <= -1) {
            if (target.y - start.y <= -1) {
                return Direction.NORTHWEST;
            } else if (target.y - start.y == 0) {
                return Direction.WEST;
            } else {
                return Direction.SOUTHWEST;
            }
        }
        if (target.y - start.y <= -1 && target.x - start.x == 0) {
            return Direction.NORTH;
        }
        if (target.y - start.y >= 1 && target.x - start.x == 0) {
            return Direction.SOUTH;
        }

        //This should happen only when start and target are the same point
        assert start.equals(target);
        return Direction.NONE;
    }
}
