package fr.utbm.vi51.environment;

import java.util.List;

import fr.utbm.vi51.util.Point3D;

/**
 * @author Theo
 *
 */
public class TakeFood extends Action {
    private InsectBody body;

    public TakeFood(InsectBody body) {
        super();
        this.body = body;
    }

    @Override
    protected void doAction() {
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
    protected boolean testAction() {
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
