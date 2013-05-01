package fr.utbm.vi51.agent;

import javax.vecmath.Point3d;

/**
 * @author Top-K
 *
 */
public abstract class Ant extends Insect {
    public Ant(String texture, Point3d position, int speed) {
        super(texture, position, speed);
    }
}
