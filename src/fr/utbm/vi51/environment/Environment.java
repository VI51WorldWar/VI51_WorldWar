package fr.utbm.vi51.environment;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.janusproject.kernel.agent.Agent;
import org.janusproject.kernel.status.Status;
import org.janusproject.kernel.status.StatusFactory;

/**
 * @author Top-K
 *
 */
public final class Environment extends Agent {

    private static Environment evt;

    private List<WorldObject> objects;
    private Square[][][] map;
    // width = x, height = y, depth = z
    private final int mapWidth;
    private final int mapHeight;
    private final int mapDepth;
    private Logger log = Logger.getLogger(Environment.class.getName());

    private Environment() {
        mapHeight = 500;
        mapWidth = 500;
        mapDepth = 5;
        map = new Square[mapWidth][mapHeight][mapDepth];
        objects = new LinkedList<WorldObject>();
    }

    public static Environment getInstance() {
        if (evt == null) {
            evt = new Environment();
        }

        return evt;
    }

    @Override
    public Status activate(Object... params) {
        return StatusFactory.ok(this);
    }

    @Override
    public Status live() {
        log.severe("live env");
        return null;
    }

    public List<WorldObject> getObjects() {
        return objects;
    }

    public void setObjects(List<WorldObject> objects) {
        this.objects = objects;
    }

    public Square[][][] getMap() {
        return map;
    }

    public void setMap(Square[][][] map) {
        this.map = map;
    }

    public void addWorldObject(WorldObject wo) {
        objects.add(wo);
        map[(int) wo.getPosition().x][(int) wo.getPosition().y][(int) wo.getPosition().z].getObjects().add(wo);
    }

}
