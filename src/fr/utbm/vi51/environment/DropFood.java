package fr.utbm.vi51.environment;

import fr.utbm.vi51.util.Point3D;

/**
 * @author Top-K
 *
 */
public class DropFood implements Action {
    private Body body;

    public DropFood(Body body) {
        super();
        this.body = body;
    }

    @Override
    public void doAction() {
        Point3D pos = body.getPosition();
        WorldObject carriedObject = body.getCarriedObject();
        Environment.getInstance().getMap()[pos.x][pos.y][pos.z].getObjects()
                .add(carriedObject);
        carriedObject.setPosition(new Point3D(pos));
        body.setCarriedObject(null);
    }

    @Override
    public boolean testAction() {
        return true;
    }

}
