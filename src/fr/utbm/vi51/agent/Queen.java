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
        this.lastTime = this.getTimeManager().getCurrentDate().getTime();
        return StatusFactory.ok(this);
    }

    @Override
    public Status live() {
        super.live();
        
        if (	this.getTimeManager().getCurrentDate().getTime() - this.lastTime < Consts.ANTACTIONDELAY
                && this.lastTime != 0) {
            return null;
        }
        
        if(this.getBody() == null || !this.getBody().isAlive()) {
        	return null;
        }
        
        countAmountOfFood();
        
        if(this.getBody().getAction() != null) {
        	return null;
        }
        
        // if queen is hungry
        if(this.getBody().isHungry()) {
            // We are sure that she has food on her square
        	this.getBody().setAction(new EatFood(this.getBody()));
            return null;
        }

        layIfCan();
        
        this.lastTime = this.getTimeManager().getCurrentDate().getTime();

        return null;
    }
    
    /**
     * The queen will lay an egg if the amount of
     * food of her Side is sufficient
     */
	private void layIfCan() {
		// If the amount of food is sufficient
        if(this.getBody().getSide().getFoodAmount() > 100){
        	// Lay an egg
            this.getBody().setAction(new Lay(this.getBody().getSide(), this.getBody().getPosition(), this.k));
        }
	}
	
	/**
	 * Count the amount of food on her square
	 * and set it as the amount of food for her side
	 */
	private void countAmountOfFood() {
		// Count the amount of food available on her square
		Point3D relativePos = this.getBody().getPerception().getPositionInPerceivedMap();
        Square sq = this.getBody().getPerception().getPerceivedMap()[relativePos.x][relativePos.y][relativePos.z];
        int foodAmount = 0;
        for(WorldObject ob :sq.getObjects()){
            if (ob instanceof Food){
                foodAmount++;
            }
        }
        this.getBody().getSide().setFoodAmount(foodAmount);
	}
}
