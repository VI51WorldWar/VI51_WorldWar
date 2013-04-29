package fr.utbm.vi51.gui;

import javax.vecmath.Point3d;

import fr.utbm.vi51.environment.Body;
import fr.utbm.vi51.environment.Environment;
import fr.utbm.vi51.environment.LandType;
import fr.utbm.vi51.environment.Square;

public class Main {

    public static void main(String[] args) {
        Environment env = Environment.getInstance();

        //List<WorldObject> worldobjs = new LinkedList<WorldObject>();
        //worldobjs.add(new Body("img/en_bug.png", new Point3d(10, 10, 1)));
        Square[][][] map = env.getMap();
        //env.setObjects(worldobjs);
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                map[i][j][0] = new Square(LandType.GRASS);
                if (i == 10 && j == 10) {
                    map[i][j][0].addObject(new Body("img/en_bug.png",
                            new Point3d(10, 10, 1)));
                }

            }
        }
        Window wind = new Window();
        wind.repaint();
    }
}
