package fr.utbm.vi51.environment;

import java.util.Random;

import fr.utbm.vi51.util.Point3D;

/**
 * @author Top-K
 *
 */
public class KillEnemy extends Action {
    private InsectBody body;
    private Direction direction;

    public KillEnemy(InsectBody body, Direction direction) {
        super();
        this.body = body;
        this.direction = direction;
    }

    @Override
    protected void doAction() {
        Point3D pos = body.getPosition();
        for (WorldObject wo : Environment.getInstance().getMap()[pos.x
                + direction.dx][pos.y + direction.dy][pos.z].getObjects()) {
            if (wo instanceof InsectBody) {
                InsectBody ib = (InsectBody) wo;
                if (!ib.getSide().equals(body.getSide())) {
                    ib.die();
                    for (int i = 0; i < 10; ++i) {
                        new Food(ib.getPosition());
                    }
                    return;
                }
            }
        }
    }

    @Override
    protected boolean testAction() {
        if (new Random().nextFloat() <= 0.3) {
            Point3D pos = body.getPosition();
            for (WorldObject wo : Environment.getInstance().getMap()[pos.x
                    + direction.dx][pos.y + direction.dy][pos.z].getObjects()) {
                if (wo instanceof InsectBody) {
                    InsectBody ib = (InsectBody) wo;
                    if (!ib.getSide().equals(body.getSide())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
