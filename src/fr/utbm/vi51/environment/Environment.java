package fr.utbm.vi51.environment;

import java.util.LinkedList;
import java.util.List;

public class Environment {

    private static Environment evt = null;

    private List<WorldObject> objects;
    private Square[][][] map;
    // width = x, height = y, depth = z
    private final int mapWidth, mapHeight, mapDepth;

    private Environment() {
        mapHeight = 500;
        mapWidth = 500;
        mapDepth = 500;
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

}
