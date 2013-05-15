package fr.utbm.vi51.environment;

import fr.utbm.vi51.configs.Consts;
import fr.utbm.vi51.util.Point3D;

public class InsectBody extends Body {

    private boolean isAlive;
    private int hunger;

    public InsectBody(String texture, Point3D position, int speed) {
        super(texture, position, speed);
        hunger = 0;
        isAlive = true;
    }

    public boolean isAlive() {
        return isAlive;
    }

    protected void die() {
        this.isAlive = false;
    }

    public int getHunger() {
        return hunger;
    }

    protected void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public boolean isHungry() {
        return this.hunger > Consts.MAXHUNGER * 0.8;
    }

}
