package fr.utbm.vi51.agent;

import fr.utbm.vi51.environment.Side;
import fr.utbm.vi51.util.Point3D;

/**
 * @author Top-K
 *
 */
public abstract class Ant extends Insect {

    private static final long serialVersionUID = -451073549367338413L;

    public Ant(String texture, Point3D position, int speed, Side side) {
        super(texture, position, speed, side);
    }
}
