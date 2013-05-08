package fr.utbm.vi51.agent;

import java.util.LinkedList;
import java.util.logging.Logger;

import org.janusproject.kernel.status.Status;
import org.janusproject.kernel.status.StatusFactory;

import fr.utbm.vi51.environment.Body;
import fr.utbm.vi51.environment.Environment;
import fr.utbm.vi51.environment.MobileObject;
import fr.utbm.vi51.util.PathFinder;
import fr.utbm.vi51.util.Point3D;

/**
 * @author Top-K
 *
 */
public class Worker extends Ant {
    private LinkedList<Point3D> movementPath;

    private Logger log = Logger.getLogger(MobileObject.class.getName());

    public Worker(Point3D position, int speed) {
        super("img/Ants/worker.png", position, speed);
    }

    @Override
    public Status activate(Object... params) {
        return StatusFactory.ok(this);
    }

    @Override
    public Status live() {
        Body bod = this.getBody();
        if (movementPath == null || movementPath.isEmpty()) {
            int pointX;
            int pointY;
            do {
                pointX = (int) Math.floor((Math.random() * 15) + 1);
                pointY = (int) Math.floor((Math.random() * 15) + 1);
            } while (!Environment.getInstance().getMap()[pointX][pointY][0]
                    .getLandType().isCrossable());
            movementPath = PathFinder.findPath(this.getBody().getPosition(),
                    new Point3D(pointX, pointY, 0), Environment.getInstance()
                            .getMap());
        }

        if (movementPath != null) {
            bod.moveTo(movementPath.removeFirst());
        }
        return null;
    }
}
