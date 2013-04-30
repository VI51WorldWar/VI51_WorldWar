package fr.utbm.vi51.environment;

import java.awt.Image;

import javax.vecmath.Point3d;

/**
 * @author Top-K
 *
 */
public abstract class WorldObject {

    private Point3d position;
    private Image texture;

    public WorldObject(Point3d position) {
        super();
        this.position = position;
    }

    public WorldObject(Point3d position, Image texture) {
        super();
        this.position = position;
        this.texture = texture;
        Environment.getInstance().getMap()[(int) position.x][(int) position.y][(int) position.z].addObject(this);
    }
    public Image getTexture() {
        return texture;
    }
    public void setTexture(Image texture) {
        this.texture = texture;
    }
    public Point3d getPosition() {
        return position;
    }
    public void setPosition(Point3d position) {
        this.position = position;
    }

}
