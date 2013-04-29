package fr.utbm.vi51.environment;

import javax.vecmath.Point3d;

public abstract class MobileObject extends WorldObject {

    private int speed; // 100 means 1 to use only integers (faster)

    public MobileObject(Point3d position, String texture) {
        super(position, texture);
    }

}
