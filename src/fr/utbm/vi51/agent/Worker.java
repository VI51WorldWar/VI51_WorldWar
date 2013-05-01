package fr.utbm.vi51.agent;

import java.util.logging.Logger;

import javax.vecmath.Point3d;

import org.janusproject.kernel.status.Status;
import org.janusproject.kernel.status.StatusFactory;

import fr.utbm.vi51.environment.Body;
import fr.utbm.vi51.environment.MobileObject;
import fr.utbm.vi51.gui.Window;

/**
 * @author Top-K
 *
 */
public class Worker extends Ant {
    private Point3d lastPoint;
    private Window wind;
    private Logger log = Logger.getLogger(MobileObject.class.getName());

    public Worker(Body bod) {
        super(bod);
    }

    @Override
    public Status activate(Object... params) {
        //this.setBody((Body) params[0]);
        wind = new Window();

        return StatusFactory.ok(this);
    }

    @Override
    public Status live() {
        Body bod = this.getBody();
        if (lastPoint == null || lastPoint.equals(bod.getPosition())) {
            double pointX = Math.floor((Math.random() * 10) + 1);
            double pointY = Math.floor((Math.random() * 10) + 1);
            lastPoint = new Point3d(pointX, pointY, 0);
            log.info("new target = " + lastPoint);
        }

        bod.moveTo(lastPoint);
        wind.repaint();
        try {
            Thread.sleep(1000 / 30);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
