package fr.utbm.vi51.agent;

import org.janusproject.kernel.agent.Agent;

import fr.utbm.vi51.environment.Body;
import fr.utbm.vi51.util.Point3D;

/**
 * @author Top-K
 *
 */
public abstract class Insect extends Agent {
    private Body body;

    public Insect(String texture, Point3D position, int speed) {
        this.body = new Body(texture, position, speed);
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

}
