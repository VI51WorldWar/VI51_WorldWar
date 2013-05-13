package fr.utbm.vi51.agent;

import java.util.logging.Logger;

import javax.vecmath.Point3d;

import org.janusproject.kernel.status.Status;
import org.janusproject.kernel.status.StatusFactory;

import fr.utbm.vi51.environment.Body;

/**
 * @author Top-K
 *
 */
public class Worker extends Ant {
    private Point3d lastPoint;

    private Logger log = Logger.getLogger(Worker.class.getName());

    public Worker(Point3d position, int speed) {
        super("img/Ants/worker.png", position, speed);
    }

    @Override
    public Status activate(Object... params) {
        return StatusFactory.ok(this);
    }

    @Override
    public Status live() {
        Body bod = this.getBody();
        if (lastPoint == null || lastPoint.equals(bod.getPosition())) {
            double pointX = Math.floor((Math.random() * 10) + 1);
            double pointY = Math.floor((Math.random() * 10) + 1);
            lastPoint = new Point3d(pointX, pointY, 0);
            log.info("live");
        }

        bod.moveTo(lastPoint);
        return null;
    }
}
