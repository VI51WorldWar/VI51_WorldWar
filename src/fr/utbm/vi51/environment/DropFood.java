package fr.utbm.vi51.environment;

import fr.utbm.vi51.util.Point3D;

/**
 * @author Theo
 *
 */
public class DropFood extends Action {
    private InsectBody body;

    public DropFood(InsectBody body) {
        super();
        this.body = body;
    }

    @Override
    protected void doAction() {
        Point3D pos = body.getPosition();
        WorldObject carriedObject = body.getCarriedObject();
        Environment.getInstance().getMap()[pos.x][pos.y][pos.z].getObjects()
                .add(carriedObject);
        carriedObject.setPosition(new Point3D(pos));
        body.setCarriedObject(null);
    }

    @Override
    protected boolean testAction() {
        return true;
    }

}
