package fr.utbm.vi51.environment;

import java.util.List;

import org.janusproject.kernel.Kernel;

import fr.utbm.vi51.agent.Worker;
import fr.utbm.vi51.util.Point3D;

public class Lay implements Action{
    private Side side;
    private Point3D pos;
    private Kernel k;
    @Override
    public void doAction() {
        Worker w = new Worker(new Point3D(this.pos),10, this.side);
        k.launchLightAgent(w);
        List<WorldObject> objects = Environment.getInstance().getMap()[pos.x][pos.y][pos.z]
                .getObjects();
        WorldObject[] toRemove = new WorldObject[10];
        int i = 0;
        for (WorldObject wo : objects) {
            if (wo instanceof Food) {
                toRemove[i] = wo;
                i++;
                if(i == 10){
                    break;
                }
            }
        }
        for(int j = 0; j<i;j++){
            objects.remove(toRemove[j]);
            Environment.getInstance().getObjects().remove(toRemove[j]);
        }
        this.side.setFoodAmount(this.side.getFoodAmount()-10);
    }

    public Lay(Side side, Point3D pos, Kernel k) {
        this.side = side;
        this.pos = pos;
        this.k = k;
    }

    @Override
    public boolean testAction() {
        return true;
    }

}
