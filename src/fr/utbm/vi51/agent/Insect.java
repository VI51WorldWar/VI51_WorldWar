package fr.utbm.vi51.agent;

import fr.utbm.vi51.environment.Body;

/**
 * @author Top-K
 *
 */
public abstract class Insect {
    private Body body;

    public Insect(Body bod) {
        this.body = bod;
    }

    public Body getBody() {
        return body;
    }

}
