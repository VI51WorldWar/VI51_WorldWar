package fr.utbm.vi51.environment;

import fr.utbm.vi51.configs.Consts;
import fr.utbm.vi51.util.Point3D;

/**
 * @author Top-K
 *
 */
public class InsectBody extends Body {

    private boolean isAlive;
    private int hunger;
    private Side side;
    private WorldObject carriedObject;

    public InsectBody(String texture, Point3D position, int speed, Side side) {
        super(texture, position, speed);
        this.side = side;
        this.hunger = 0;
        this.isAlive = true;
    }

    public Side getSide() {
        return this.side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public boolean isAlive() {
        return this.isAlive;
    }

    protected void die() {
        this.isAlive = false;
    }

    public int getHunger() {
        return this.hunger;
    }

    protected void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public boolean isHungry() {
        return this.hunger > Consts.MAXHUNGER * 0.8;
    }

    public WorldObject getCarriedObject() {
        return this.carriedObject;
    }

    protected void setCarriedObject(WorldObject carriedObject) {
        this.carriedObject = carriedObject;
    }

}
