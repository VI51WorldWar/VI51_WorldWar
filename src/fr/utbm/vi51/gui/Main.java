package fr.utbm.vi51.gui;

import javax.vecmath.Point3d;

import org.janusproject.kernel.Kernel;
import org.janusproject.kernel.agent.Kernels;

import fr.utbm.vi51.agent.Queen;
import fr.utbm.vi51.agent.Warrior;
import fr.utbm.vi51.agent.Worker;
import fr.utbm.vi51.environment.Environment;
import fr.utbm.vi51.environment.LandType;
import fr.utbm.vi51.environment.Square;

/**
 * @author Top-K
 *
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) throws InterruptedException {
        // Generate environment map
        generateMap1();
        Environment env = Environment.getInstance();
        // SenderAgent b = new SenderAgent();
        Kernel k = Kernels.get();
        // k.launchLightAgent(b);
        Queen q = new Queen(new Point3d(5, 6, 0), 1);
        Warrior war = new Warrior(new Point3d(5, 7, 0), 10);
        for (int i = 0; i < 10; ++i) {
            k.launchLightAgent(new Worker(new Point3d(1, 1, 0), 15));
        }

        // Window wind = new Window();
        // while (true) {
        // List<WorldObject> objs = env.getObjects();
        // MobileObject enemy1 = (MobileObject) objs.get(0);
        // enemy1.moveTo(new Point3d(10, 14, 0));
        // wind.repaint();
        /*
         * try { Thread.sleep(1000 / 30); } catch (InterruptedException e) { //
         * TODO Auto-generated catch block e.printStackTrace(); }
         */
        // }
    }

    // Functions for map g�n�ration
    private static boolean generateMap1() throws InterruptedException {
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

        return true;
    }
}
