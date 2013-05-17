package fr.utbm.vi51.agent;

import java.util.List;

import javax.vecmath.Point3d;

import org.janusproject.kernel.status.Status;
import org.janusproject.kernel.status.StatusFactory;

import fr.utbm.vi51.configs.Consts;
import fr.utbm.vi51.environment.Direction;
import fr.utbm.vi51.environment.EatFood;
import fr.utbm.vi51.environment.Food;
import fr.utbm.vi51.environment.InsectBody;
import fr.utbm.vi51.environment.Message;
import fr.utbm.vi51.environment.Move;
import fr.utbm.vi51.environment.Pheromone;
import fr.utbm.vi51.environment.Side;
import fr.utbm.vi51.environment.Square;
import fr.utbm.vi51.environment.WorldObject;
import fr.utbm.vi51.util.PathFinder;
import fr.utbm.vi51.util.Point3D;

/**
 * @author Top-K
 *
 */
public class Queen extends Ant {

    public Queen(Point3D position, int speed, Side side) {
        super("img/Ants/queen.png", position, speed, side);
    }
    @Override
    public Status activate(Object... params) {
        lastTime = this.getTimeManager().getCurrentDate().getTime();
        return StatusFactory.ok(this);
    }

    @Override
    public Status live() {
        super.live();
        if (this.getTimeManager().getCurrentDate().getTime() - lastTime < Consts.ANTACTIONDELAY
                && lastTime != 0) {
            return null;
        }
        Point3D relativePos = this.getBody().getPerception().getPositionInPerceivedMap();
        Square sq = this.getBody().getPerception().getPerceivedMap()[relativePos.x][relativePos.y][relativePos.z];
        int foodAmount = 0;
        for(WorldObject ob :sq.getObjects()){
            if (ob instanceof Food){
                foodAmount++;
            }
        }
        this.getBody().getSide().setFoodAmount(foodAmount);
        lastTime = this.getTimeManager().getCurrentDate().getTime();

        return null;
    }
}
