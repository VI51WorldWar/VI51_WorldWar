package fr.utbm.vi51.environment;

import java.util.logging.Logger;

import fr.utbm.vi51.util.Point3D;

/**
 * @author Top-K
 *
 */
public abstract class MobileObject extends WorldObject {

    private int speed; // 100 means 1 to use only integers (faster)
    private int currentMove;

    private Logger log = Logger.getLogger(MobileObject.class.getName());

    public MobileObject(Point3D position, String texture, int speed) {
        super(position, texture);
        this.speed = speed;

    }

    public void moveTo(Point3D target) {
        int xDist = target.x - this.getPosition().x;
        int yDist = target.y - this.getPosition().y;
        xDist /= Math.max(Math.abs(xDist), 1);
        yDist /= Math.max(Math.abs(yDist), 1);
        move(xDist, yDist, 0);
        /*
         * if (xDist >= 1 && yDist >= 1) { move(1, 1, 0); } else if (xDist <= -1
         * && yDist <= -1) { move(-1, -1, 0); } else if (xDist <= -1 && yDist >=
         * 1) { move(-1, 1, 0); } else if (xDist >= 1 && yDist <= -1) { move(1,
         * -1, 0); } else if (xDist >= 1) { move(1, 0, 0); } else if (xDist <=
         * -1) { move(-1, 0, 0); } else if (yDist >= 1) { move(0, 1, 0); } else
         * if (yDist <= -1) { move(0, -1, 0); }
         */
    }

    private boolean move(int moveX, int moveY, int moveZ) {
        Point3D pos = this.getPosition();

        Environment env = Environment.getInstance();
        Square[][][] map = env.getMap();
        map[pos.x][pos.y][pos.z].getObjects().remove(this);
        map[pos.x + moveX][pos.y + moveY][pos.z + moveZ].getObjects().add(this);
        env.setMap(map);
        this.setPosition(new Point3D(pos.x + moveX, pos.y + moveY, pos.z
                + moveZ));
        log.info("Moving");
        return true;

    }

}
