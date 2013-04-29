package fr.utbm.vi51.environment;

import java.awt.Image;

import javax.vecmath.Point3d;

public class Body extends MobileObject {

    public Body(Image texture, Point3d position) {
        super(position, texture);
    }
}
