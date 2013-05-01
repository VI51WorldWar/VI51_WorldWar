package fr.utbm.vi51.agent;

import org.janusproject.kernel.agent.Agent;

import fr.utbm.vi51.environment.Body;

/**
 * @author Top-K
 *
 */
public abstract class Insect extends Agent {
    private Body body;

    public Insect(Body bod) {
        this.body = bod;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

}
