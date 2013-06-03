package fr.utbm.vi51.environment;

import java.util.List;

import fr.utbm.vi51.util.Point3D;

public class EatFood extends Action {
    private InsectBody body;

    public EatFood(InsectBody body) {
        super();
        this.body = body;
    }

    @Override
    protected void doAction() {
        if(body.getCarriedObject() instanceof Food) {
            body.setCarriedObject(null);
            body.setHunger(0);
            return;
        }
        
        Point3D pos = body.getPosition();
        List<WorldObject> objects = Environment.getInstance().getMap()[pos.x][pos.y][pos.z]
                .getObjects();
        WorldObject toRemove = null;
        for (WorldObject wo : objects) {
            if (wo instanceof Food) {
                toRemove = wo;   
                break;
            }
        }
        body.setHunger(0);
        objects.remove(toRemove);
        Environment.getInstance().getObjects().remove(toRemove);
    }

    @Override
    protected boolean testAction() {
        //If the body is carrying food, it can eat it
        if (body.getCarriedObject() instanceof Food) {
            return true;
        }
        
        //Else we must check if there is food on the same square
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
