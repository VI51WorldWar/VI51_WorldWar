package fr.utbm.vi51.agent;

import javax.vecmath.Point3d;

import org.janusproject.kernel.agent.Agent;

import fr.utbm.vi51.environment.Body;

/**
 * @author Top-K
 *
 */
public abstract class Insect extends Agent {
    private Body body;

    public Insect(String texture, Point3d position, int speed) {
        this.body = new Body(texture, position, speed);
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

}
