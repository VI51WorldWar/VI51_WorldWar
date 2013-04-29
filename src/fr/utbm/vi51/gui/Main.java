package fr.utbm.vi51.gui;

import java.util.LinkedList;
import java.util.List;

import fr.utbm.vi51.agent.Worker;
import fr.utbm.vi51.environment.Environment;
import fr.utbm.vi51.environment.WorldObject;

public class Main {

    public static void main(String[] args) {
        Environment env = Environment.getInstance();

        List<WorldObject> worldobjs = new LinkedList<WorldObject>();
        worldobjs.add(new Worker(""));
        env.setObjects(worldobjs);
        Window wind = new Window();
        wind.repaint();
    }
}
