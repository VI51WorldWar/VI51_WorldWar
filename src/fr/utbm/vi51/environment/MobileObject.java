package fr.utbm.vi51.environment;

import java.util.logging.Logger;

import javax.vecmath.Point3d;

import fr.utbm.vi51.configs.Consts;

/**
 * @author Top-K
 *
 */
public abstract class MobileObject extends WorldObject {

    private int speed; // 100 means 1 to use only integers (faster)
    private int currentMove = 0;

    private Logger log = Logger.getLogger(MobileObject.class.getName());

    public MobileObject(Point3d position, String texture, int speed) {
        super(position, texture);
        this.speed = speed;

    }

    public void moveTo(Point3d target) {
        double xDist = target.getX() - this.getPosition().getX();
        double yDist = target.getY() - this.getPosition().getY();

        if (xDist >= 1 && yDist >= 1) {
            move(1, 1, 0);
        } else if (xDist <= -1 && yDist <= -1) {
            move(-1, -1, 0);
        } else if (xDist <= -1 && yDist >= 1) {
            move(-1, 1, 0);
        } else if (xDist >= 1 && yDist <= -1) {
            move(1, -1, 0);
        } else if (xDist >= 1) {
            move(1, 0, 0);
        } else if (xDist <= -1) {
            move(-1, 0, 0);
        } else if (yDist >= 1) {
            move(0, 1, 0);
        } else if (yDist <= -1) {
            move(0, -1, 0);
        }
    }
    private boolean move(int moveX, int moveY, int moveZ) {
        Point3d pos = this.getPosition();
        int moveXvalue = 0;
        int moveYvalue = 0;
        int moveZvalue = 0;
        if (moveX == 1) {
            moveXvalue = 1;
        } else if (moveX == -1) {
            moveXvalue = -1;
        }
        if (moveY == 1) {
            moveYvalue = 1;
        } else if (moveY == -1) {
            moveYvalue = -1;
        }
        if (moveZ == 1) {
            moveZvalue = 1;
        } else if (moveZ == -1) {
            moveZvalue = -1;
        }
        this.currentMove += this.speed;
        if (this.currentMove >= 500 / Consts.TURBO) {
            Environment env = Environment.getInstance();
            Square[][][] map = env.getMap();
            map[(int) pos.x][(int) pos.y][(int) pos.z].getObjects().remove(this);
            map[(int) pos.x + moveXvalue][(int) pos.y + moveYvalue][(int) pos.z + moveZvalue].getObjects().add(this);
            env.setMap(map);
            this.setPosition(new Point3d(pos.x + moveXvalue, pos.y + moveYvalue, pos.z + moveZvalue));
            this.currentMove = 0;
            log.info("Moving");
            return true;
        }
        return false;

    }

}
