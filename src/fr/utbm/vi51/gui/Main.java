package fr.utbm.vi51.gui;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.vecmath.Point3d;

import fr.utbm.vi51.environment.Body;
import fr.utbm.vi51.environment.Environment;
import fr.utbm.vi51.environment.LandType;
import fr.utbm.vi51.environment.Square;
import fr.utbm.vi51.environment.WorldObject;

/**
 * @author Top-K
 *
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        // Generate environment map
        generateMap1();

        Window wind = new Window();
        while (true) {
            wind.repaint();
            try {
                Thread.sleep(1000 / 30);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    // Functions for map génération
    private static boolean generateMap1() {
        // Getting the environment from the singleton
        Environment env = Environment.getInstance();
        // Getting environment map
        Square[][][] map = env.getMap();

        // Generation of top level full of grass
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j][0] = new Square(LandType.GRASS);
            }
        }
        // A lake in the middle of the map
        map[10][10][0] = new Square(LandType.WATER);
        map[11][10][0] = new Square(LandType.WATER);
        map[10][11][0] = new Square(LandType.WATER);
        map[11][11][0] = new Square(LandType.WATER);

        // Some walls around the map
        for (int i = 0; i < map.length; ++i) {
            map[i][0][0] = new Square(LandType.WALL);
            map[i][map[0].length - 1][0] = new Square(LandType.WALL);
        }
        for (int i = 0; i < map.length; ++i) {
            map[0][i][0] = new Square(LandType.WALL);
            map[map.length - 1][i][0] = new Square(LandType.WALL);
        }

        List<WorldObject> objs = env.getObjects();
        try {
            objs.add(new Body(ImageIO.read(new File("img/Ants/worker.png")),
                    new Point3d(5, 5, 0)));
            objs.add(new Body(ImageIO.read(new File("img/Ants/queen.png")),
                    new Point3d(5, 6, 0)));
            objs.add(new Body(ImageIO.read(new File("img/Ants/warrior.png")),
                    new Point3d(5, 7, 0)));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }
}
