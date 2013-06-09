package fr.utbm.vi51.environment;

import java.util.logging.Logger;

import fr.utbm.vi51.util.Point3D;

/**
 * @author Top-K
 *
 */
public abstract class MobileObject extends WorldObject {

    @SuppressWarnings("unused")
    private Logger log = Logger.getLogger(MobileObject.class.getName());

    public MobileObject(Point3D position, String texture, int speed) {
        super(position, texture);

    }

}
