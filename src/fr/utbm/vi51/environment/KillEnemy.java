package fr.utbm.vi51.environment;

import java.util.Random;

import fr.utbm.vi51.util.Point3D;

/**
 * @author Top-K
 *
 */
public class KillEnemy extends Action {
    private InsectBody body;
    private Direction direction;

    public KillEnemy(InsectBody body, Direction direction) {
        super();
        this.body = body;
        this.direction = direction;
    }

    @Override
	protected void doAction() {
        Point3D pos = this.body.getPosition();
        for (WorldObject wo : Environment.getInstance().getMap()[pos.x+this.direction.dx][pos.y+this.direction.dy][pos.z].getObjects()) {
            if (wo instanceof InsectBody) {
                InsectBody ib = (InsectBody) wo;
                // If the sides are different
                if(!ib.getSide().equals(this.body.getSide())) {
                	// Hit the enemy
                    ib.hit(this.body.generateHitAmount());
                    return;
                }
            }
        }
    }

	@Override
	protected boolean testAction() {
        if(new Random().nextFloat() <= 0.3){
    		Point3D pos = this.body.getPosition();
    		for (WorldObject wo : Environment.getInstance().getMap()[pos.x+this.direction.dx][pos.y+this.direction.dy][pos.z].getObjects()) {
    			if (wo instanceof InsectBody) {
    			    InsectBody ib = (InsectBody) wo;
    			    if(!ib.getSide().equals(this.body.getSide())) {
    			        return true;
    			    }
    			}
    		}
		}
		return false;
	}
}
