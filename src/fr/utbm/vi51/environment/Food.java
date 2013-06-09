package fr.utbm.vi51.environment;

import fr.utbm.vi51.util.Point3D;

/**
 * @author Top-K
 *
 */
public class Food extends WorldObject {
    public static final String IMGPATH = new String("img/Objects/food.png"); //$NON-NLS-1$

    public Food(Point3D position) {
        super(position, IMGPATH);
    }

}
