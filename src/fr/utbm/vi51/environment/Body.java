package fr.utbm.vi51.environment;

import fr.utbm.vi51.util.Point3D;

/**
 * @author Top-K
 * 
 */
public class Body extends MobileObject {
    private Action action;
    private WorldObject carriedObject;

    public Body(String texture, Point3D position, int speed) {
        super(position, texture, speed);
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Perception getPerception() {
        return new Perception(this);
    }

    public WorldObject getCarriedObject() {
        return carriedObject;
    }

    public void setCarriedObject(WorldObject carriedObject) {
        this.carriedObject = carriedObject;
    }
}
