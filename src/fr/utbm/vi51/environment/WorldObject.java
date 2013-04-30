package fr.utbm.vi51.environment;

import javax.vecmath.Point3d;

/**
 * @author Top-K
 *
 */
public abstract class WorldObject {

    private Point3d position;
    private String texturePath;

    public WorldObject(Point3d position) {
        super();
        this.position = position;
    }

    public WorldObject(Point3d position, String texturePath) {
        super();
        this.position = position;
        this.texturePath = texturePath;
        Environment.getInstance().getMap()[(int) position.x][(int) position.y][(int) position.z].addObject(this);
    }
    public String getTexturePath() {
        return texturePath;
    }
    public void setTexturePath(String texturePath) {
        this.texturePath = texturePath;
    }
    public Point3d getPosition() {
        return position;
    }
    public void setPosition(Point3d position) {
        this.position = position;
    }

}
