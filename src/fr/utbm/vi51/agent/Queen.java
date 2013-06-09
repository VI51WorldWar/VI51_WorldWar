package fr.utbm.vi51.agent;

import org.janusproject.kernel.Kernel;
import org.janusproject.kernel.status.Status;
import org.janusproject.kernel.status.StatusFactory;

import fr.utbm.vi51.configs.Consts;
import fr.utbm.vi51.environment.EatFood;
import fr.utbm.vi51.environment.Food;
import fr.utbm.vi51.environment.Lay;
import fr.utbm.vi51.environment.Side;
import fr.utbm.vi51.environment.Square;
import fr.utbm.vi51.environment.WorldObject;
import fr.utbm.vi51.util.Point3D;

/**
 * @author Top-K
 *
 */
public class Queen extends Ant {
    /**
     *
     */
    private static final long serialVersionUID = -6211112771194869755L;

    private Kernel k;
    public Queen(Point3D position, int speed, Side side, Kernel k) {
        super(side.getQueenTexture(), position, speed, side);
        this.k = k;
    }
    @Override
    public Status activate(Object... params) {
        lastTime = this.getTimeManager().getCurrentDate().getTime();
        return StatusFactory.ok(this);
    }

    @Override
    public Status live() {
        super.live();
        if (this.getBody() == null || !this.getBody().isAlive()) {
            return null;
        }

        if (this.getTimeManager().getCurrentDate().getTime() - lastTime < Consts.ANTACTIONDELAY
                && lastTime != 0) {
            return null;
        }

        if (this.getBody().isHungry() && this.getBody().getAction() == null) {
            this.getBody().setAction(new EatFood(this.getBody()));
        }

        if (this.getBody().getAction() != null) {
            return null;
        }

        Point3D relativePos = this.getBody().getPerception().getPositionInPerceivedMap();
        Square sq = this.getBody().getPerception().getPerceivedMap()[relativePos.x][relativePos.y][relativePos.z];
        int foodAmount = 0;
        for (WorldObject ob :sq.getObjects()) {
            if (ob instanceof Food) {
                foodAmount++;
            }
        }
        this.getBody().getSide().setFoodAmount(foodAmount);
        if (foodAmount > 100) {
            Lay lay = new Lay(this.getBody().getSide(), this.getBody().getPosition(), this.k);
            this.getBody().setAction(lay);
        }

        lastTime = this.getTimeManager().getCurrentDate().getTime();

        return null;
    }
}
