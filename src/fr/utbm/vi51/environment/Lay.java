package fr.utbm.vi51.environment;

import java.util.List;

import org.janusproject.kernel.Kernel;

import fr.utbm.vi51.agent.Warrior;
import fr.utbm.vi51.agent.Worker;
import fr.utbm.vi51.util.CustomRandom;
import fr.utbm.vi51.util.Point3D;

/**
 * @author Theo
 *
 */
public class Lay extends Action {
    private Side side;
    private Point3D pos;
    private Kernel k;

    public Lay(Side side, Point3D pos, Kernel k) {
        this.side = side;
        this.pos = pos;
        this.k = k;
    }

    @Override
    protected void doAction() {
        Worker wor;
        Warrior war;
        float chanceofWarrior= (this.side.getFoodAmount()-100);
        int rand = CustomRandom.getNextInt(100);
        if(rand > chanceofWarrior){
            wor = new Worker(new Point3D(this.pos), 10, this.side);
            this.k.launchLightAgent(wor);
        } else {
            war = new Warrior(new Point3D(this.pos), 10, this.side);
            this.k.launchLightAgent(war);

        }
        
        List<WorldObject> objects = Environment.getInstance().getMap()[this.pos.x][this.pos.y][this.pos.z]
                .getObjects();
        WorldObject[] toRemove = new WorldObject[10];
        int i = 0;
        for (WorldObject wo : objects) {
            if (wo instanceof Food) {
                toRemove[i] = wo;
                i++;
                if (i == 10) {
                    break;
                }
            }
        }
        for (int j = 0; j < i; j++) {
            objects.remove(toRemove[j]);
            Environment.getInstance().getObjects().remove(toRemove[j]);
        }
    }

    @Override
    protected boolean testAction() {
        return true;
    }

}
