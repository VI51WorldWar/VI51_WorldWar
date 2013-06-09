package fr.utbm.vi51.agent;

import java.util.LinkedList;

import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.status.Status;

import fr.utbm.vi51.environment.Direction;
import fr.utbm.vi51.environment.InsectBody;
import fr.utbm.vi51.environment.Side;
import fr.utbm.vi51.util.Point3D;

/**
 * @author Top-K
 *
 */
public abstract class Insect extends Agent {
    /**
	 * 
	 */
	private static final long serialVersionUID = -706580571446108492L;
	protected long lastTime;
    //protected Perception currentPerception;
    protected LinkedList<Direction> movementPath;
    private InsectBody body;
    
    public Insect(String texture, Point3D position, int speed, Side side) {
        this.body = new InsectBody(texture, position, speed, side);
    }

    public InsectBody getBody() {
        return this.body;
    }

    public void setBody(InsectBody body) {
        this.body = body;
    }

    @Override
    public Status live() {
        if(!this.body.isAlive()) {
            this.body = null;
            this.killMe();
        }
        return null;
    }
    
    protected void generateWanderMovement() {
    	// If an action is planed
    	if (this.body.getAction() != null) {
    		// Do nothing
    		return;
    	}
    	// If a movement is already defined
    	if(this.movementPath != null) {
    		// Erase it
    		this.movementPath.clear();
    		this.movementPath = null;
    	}
    	this.movementPath = new  LinkedList<>();
    	this.movementPath.add(Direction.random());
    }
    
    
}
