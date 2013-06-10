package fr.utbm.vi51.environment;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.status.Status;
import org.janusproject.kernel.status.StatusFactory;

import fr.utbm.vi51.configs.Consts;
import fr.utbm.vi51.util.Point3D;

/**
 * @author Top-K
 *
 */
public final class Environment extends Agent {

    /**
     *
     */
    private static final long serialVersionUID = 8497722280590030106L;

	private static Environment evt;

    private CopyOnWriteArrayList<WorldObject> objects;
    private Square[][][] map;
    // width = x, height = y, depth = z
    private final int mapWidth;
    private final int mapHeight;
    private final int mapDepth;
    @SuppressWarnings("unused")
    private Logger log = Logger.getLogger(Environment.class.getName());
    private long lastTime;

    private Environment() {
        this.mapHeight = 40;
        this.mapWidth = 40;
        this.mapDepth = 5;
        this.map = new Square[this.mapWidth][this.mapHeight][this.mapDepth];
        this.objects = new CopyOnWriteArrayList<WorldObject>();
    }

    public static Environment getInstance() {
        if (evt == null) {
            evt = new Environment();
        }

        return evt;
    }

    public int getMapWidth() {
        return this.mapWidth;
    }

    public int getMapHeight() {
        return this.mapHeight;
    }

    public int getMapDepth() {
        return this.mapDepth;
    }

    @Override
    public Status activate(Object... params) {
        return StatusFactory.ok(this);
    }

    public List<WorldObject> getObjects() {
        return this.objects;
    }

    public void setObjects(CopyOnWriteArrayList<WorldObject> objects) {
        this.objects = objects;
    }

    public Square[][][] getMap() {
        return this.map;
    }
    
    public Square getSquare(int x, int y, int z) {
    	if(	x >= this.mapWidth || x < 0) {
    		return null;
    	}
    	if(y >= this.mapHeight || y < 0) {
    		return null;
    	}
    	if(z >= this.mapDepth || z < 0 ) {
    		return null;
    	}
    	return this.map[x][y][z];
    }
    
    public Square getSquare(Point3D pt) {
    	return this.getSquare(pt.x,pt.y,pt.z);
    }

    public void setMap(Square[][][] map) {
        this.map = map;
    }

    public void addWorldObject(WorldObject wo) {
        this.objects.add(wo);
        this.map[wo.getPosition().x][wo.getPosition().y][wo
                .getPosition().z].getObjects().add(wo);
    }

    @Override
    public Status live() {

        int diffTime = (int) (this.getTimeManager().getCurrentDate().getTime() - this.lastTime);
        if (diffTime > 100000) {
            diffTime = 30;
        }
        LinkedList<WorldObject> toRemove = new LinkedList<WorldObject>();
        synchronized (this.objects) {
            for (WorldObject o : this.objects) {
                if (o instanceof Body) {
                    Body b = (Body) o;
                    Action a = b.getAction();
                    if (a != null && a.testAction()) {
                        a.doAction();
                        b.setAction(null);
                    }
                    if (a != null && !a.testAction()) {
                        b.setAction(null);
                    }
                    if (b instanceof InsectBody) {
                        InsectBody ib = (InsectBody) b;
                        ib.setHunger(ib.getHunger() + diffTime);
                        if (ib.getHunger() > Consts.MAXHUNGER) {

                            ib.die();
                        }
                        if (!ib.isAlive()) {
                            toRemove.add(ib);
                        }
                    }
                }
                if (o instanceof Pheromone) {
                    ((Pheromone) o).strength -= diffTime;
                    if (((Pheromone) o).strength < 0) {
                        toRemove.add(o);
                    }
                }
            }

        }
        for (WorldObject wo : toRemove) {
            this.objects.remove(wo);
            this.map[wo.getPosition().x][wo.getPosition().y][wo.getPosition().z]
                    .getObjects().remove(wo);
        }
        this.lastTime = this.getTimeManager().getCurrentDate().getTime();
        diffTime = (int) (this.getTimeManager().getCurrentDate().getTime() - this.lastTime);
        try {
            if (30 - diffTime > 0) {
                Thread.sleep(30 - diffTime);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
