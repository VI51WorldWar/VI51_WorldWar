package fr.utbm.vi51.agent;

import org.janusproject.kernel.status.Status;
import org.janusproject.kernel.status.StatusFactory;

import fr.utbm.vi51.configs.Consts;
import fr.utbm.vi51.environment.Direction;
import fr.utbm.vi51.environment.Square;
import fr.utbm.vi51.environment.WorldObject;
import fr.utbm.vi51.util.Point3D;

/**
 * @author Top-K
 *
 */
public class Warrior extends Ant {
    private WorkerBehaviour currentBehaviour;
    private Point3D lastPosition;
    private Point3D relativeStartingPointPosition; // Remembers the position of

    public Warrior(Point3D position, int speed) {
        super("img/Ants/warrior.png", position, speed);
    }
    @Override
    public Status activate(Object... params) {
        lastTime = this.getTimeManager().getCurrentDate().getTime();
        return StatusFactory.ok(this);
    }

    @Override
    public Status live() {
        if (this.getTimeManager().getCurrentDate().getTime() - lastTime < Consts.ANTACTIONDELAY
                && lastTime != 0) {
            return null;
        }
        currentPerception = this.getBody().getPerception();
        Square[][][] perceivedMap = currentPerception.getPerceivedMap();
        Point3D positionInPerceivedMap = currentPerception.getPositionInPerceivedMap();
        
        if (relativeStartingPointPosition != null) {
            relativeStartingPointPosition.x -= this.getBody().getPosition().x
                    - lastPosition.x;
            relativeStartingPointPosition.y -= this.getBody().getPosition().y
                    - lastPosition.y;
        }
        lastPosition = new Point3D(this.getBody().getPosition());
        for (WorldObject wo : perceivedMap[positionInPerceivedMap.x][positionInPerceivedMap.y][0]
                .getObjects()) {
            if (wo.getTexturePath().equals("img/Ants/queen.png")) {
                relativeStartingPointPosition = new Point3D(0, 0, 0);
                break;
            }

        }
        Move m;
        if(relativeStartingPointPosition != null && Point3D.euclidianDistance(this.getBody().getPosition(), relativeStartingPointPosition) < 10){
            m = new Move(this.getBody(),Direction.random());
        } else {
            m = new Move(this.getBody(),Direction.random());
        }
        float difftime = this.getTimeManager().getCurrentDate().getTime()-lastTime; 
            
        this.getBody().setAction(m);

        lastTime = this.getTimeManager().getCurrentDate().getTime();

        
        return null;
    }
    
}
