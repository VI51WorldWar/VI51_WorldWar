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

}
