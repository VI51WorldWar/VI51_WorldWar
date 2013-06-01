package fr.utbm.vi51.environment;

import java.util.List;

import fr.utbm.vi51.util.Point3D;

/**
 * @author Top-K
 *
 */
public class TakeFood implements Action {
    private InsectBody body;

    public TakeFood(InsectBody body) {
        super();
        this.body = body;
    }

    @Override
    public void doAction() {
        Point3D pos = body.getPosition();
        List<WorldObject> objects = Environment.getInstance().getMap()[pos.x][pos.y][pos.z]
                .getObjects();
        WorldObject toRemove = null;
        for (WorldObject wo : objects) {
            if (wo instanceof Food) {
                toRemove = wo;
                body.setCarriedObject(wo);
                wo.setPosition(body.getPosition());
                break;
            }
        }
        objects.remove(toRemove);
    }

    @Override
    public boolean testAction() {
        Point3D pos = body.getPosition();
        for (WorldObject wo : Environment.getInstance().getMap()[pos.x][pos.y][pos.z]
                .getObjects()) {
            if (wo instanceof Food) {
                return true;
            }
        }
        return false;
    }

}
