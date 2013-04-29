package fr.utbm.vi51.environment;

import java.awt.Image;

import javax.vecmath.Point3d;

public abstract class MobileObject extends WorldObject {

    private int speed; // 100 means 1 to use only integers (faster)

    public MobileObject(Point3d position, Image texture) {
        super(position, texture);
    }

}
