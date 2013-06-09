package fr.utbm.vi51.environment;

import java.util.logging.Logger;

import fr.utbm.vi51.util.Point3D;

/**
 * @author Top-K
 *
 */
public abstract class WorldObject {

    private Point3D position;
    private String texturePath;
    @SuppressWarnings("unused")
    private Logger log = Logger.getLogger(MobileObject.class.getName());

    public WorldObject(Point3D position) {
        super();
        this.position = position;
    }

    public WorldObject(Point3D position, String texturePath) {
        super();
        this.position = position;
        this.texturePath = texturePath;
        Environment.getInstance().addWorldObject(this);
    }
    public String getTexturePath() {
        return texturePath;
    }
    public void setTexturePath(String texturePath) {
        this.texturePath = texturePath;
    }
    public Point3D getPosition() {
        return position;
    }
    public void setPosition(Point3D position) {
        this.position = position;
    }

}
