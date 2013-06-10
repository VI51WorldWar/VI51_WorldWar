package fr.utbm.vi51.environment;

import fr.utbm.vi51.configs.Consts;
import fr.utbm.vi51.util.Point3D;

public class DropPheromone extends Action {

	private final Side 			side;
	private final Point3D		position;
	private final Message		message;
	private final Direction		direction;
	
	public DropPheromone(Side side,Point3D pt,Message msg,Direction dir) {
		this.side = side;
		this.position = new Point3D(pt);
		this.message = msg;
		this.direction = dir;
	}
	
	@Override
	protected void doAction() {
		Square square = Environment.getInstance().getSquare(this.position);
		// Look on the square is a same pheromone is available
		for(WorldObject obj : square.getObjects()) {
			if(obj instanceof Pheromone) {
				Pheromone ph = (Pheromone) obj;
				if(ph.isEqual(this.message,this.direction,this.side)) {
					// Refresh the strenght of the old pheromone
					ph.refresh();
					return;
				}
			}
		}
		// Create a pheromone
		new Pheromone(	this.position,
						this.message,
						this.direction,
						(int) Consts.STARTINGPHEROMONEVALUE, 
        				this.side);
	}

	@Override
	protected boolean testAction() {
        return true;
	}

}
