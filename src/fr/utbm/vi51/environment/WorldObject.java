package fr.utbm.vi51.environment;

import javax.vecmath.Point3d;

public abstract class WorldObject {

    private Point3d position;
    private String texture;

    public WorldObject(Point3d position, String texture) {
        super();
        this.position = position;
        this.texture = texture;
    }
    public String getTexture() {
        return texture;
    }
    public void setTexture(String texture) {
        this.texture = texture;
    }
    public Point3d getPosition() {
        return position;
    }
    public void setPosition(Point3d position) {
        this.position = position;
    }

}
