package fr.utbm.vi51.environment;

import fr.utbm.vi51.util.CustomRandom;
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

        @Override
        public Direction west() {
            return NORTHWEST;
        }

        @Override
        public Direction east() {
            return NORTHEAST;
        }
    },
    /** West. */
    WEST(-1, 0) {
        @Override
        public Direction opposite() {
            return EAST;
        }

        @Override
        public Direction west() {
            return SOUTHWEST;
        }

        @Override
        public Direction east() {
            return NORTHWEST;
        }
    },
    /** South. */
    SOUTH(0, +1) {
        @Override
        public Direction opposite() {
            return NORTH;
        }

        @Override
        public Direction west() {
            return SOUTHWEST;
        }

        @Override
        public Direction east() {
            return SOUTHEAST;
        }
    },
    /** East. */
    EAST(+1, 0) {
        @Override
        public Direction opposite() {
            return WEST;
        }

        @Override
        public Direction west() {
            return SOUTHEAST;
        }

        @Override
        public Direction east() {
            return NORTHEAST;
        }
    },
    /** NorthEast. */
    NORTHEAST(+1, -1) {
        @Override
        public Direction opposite() {
            return SOUTHWEST;
        }

        @Override
        public Direction west() {
            return EAST;
        }

        @Override
        public Direction east() {
            return NORTH;
        }
    },
    /** NorthWest. */
    NORTHWEST(-1, -1) {
        @Override
        public Direction opposite() {
            return SOUTHEAST;
        }

        @Override
        public Direction west() {
            return WEST;
        }

        @Override
        public Direction east() {
            return NORTH;
        }
    },
    /** NorthEast. */
    SOUTHWEST(-1, +1) {
        @Override
        public Direction opposite() {
            return NORTHEAST;
        }

        @Override
        public Direction west() {
            return SOUTH;
        }

        @Override
        public Direction east() {
            return WEST;
        }
    },
    /** SouthEast. */
    SOUTHEAST(+1, +1) {
        @Override
        public Direction opposite() {
            return NORTHWEST;
        }

        @Override
        public Direction west() {
            return EAST;
        }

        @Override
        public Direction east() {
            return SOUTH;
        }
    },
    /** None. */
    NONE(0, 0) {
        @Override
        public Direction opposite() {
            return NONE;
        }

        @Override
        public Direction west() {
            return NONE;
        }

        @Override
        public Direction east() {
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
        return values()[CustomRandom.getNextInt(values().length)];
    }

    /**
     * Replies the opposite direction.
     *
     * @return the opposite direction.
     */
    public abstract Direction opposite();

    public abstract Direction west();

    public abstract Direction east();

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
