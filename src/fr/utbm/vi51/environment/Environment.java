package fr.utbm.vi51.environment;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Top-K
 *
 */
public final class Environment {

    private static Environment evt;

    private List<WorldObject> objects;
    private Square[][][] map;
    // width = x, height = y, depth = z
    private final int mapWidth;
    private final int mapHeight;
    private final int mapDepth;

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
