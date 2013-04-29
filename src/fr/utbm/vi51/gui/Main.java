package fr.utbm.vi51.gui;

import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Point3d;

import fr.utbm.vi51.environment.Body;
import fr.utbm.vi51.environment.Environment;
import fr.utbm.vi51.environment.WorldObject;

public class Main {

    public static void main(String[] args) {
        Environment env = Environment.getInstance();

        List<WorldObject> worldobjs = new LinkedList<WorldObject>();
        worldobjs.add(new Body("img/en_bug.png", new Point3d(10, 10, 1)));
        env.getMap();
        env.setObjects(worldobjs);
        Window wind = new Window();
        wind.repaint();
    }
}
