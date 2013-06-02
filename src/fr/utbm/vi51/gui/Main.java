package fr.utbm.vi51.gui;

import org.janusproject.kernel.Kernel;
import org.janusproject.kernel.agent.Kernels;

import fr.utbm.vi51.agent.Queen;
import fr.utbm.vi51.agent.Warrior;
import fr.utbm.vi51.agent.Worker;
import fr.utbm.vi51.environment.Environment;
import fr.utbm.vi51.environment.Food;
import fr.utbm.vi51.environment.LandType;
import fr.utbm.vi51.environment.Side;
import fr.utbm.vi51.environment.Square;
import fr.utbm.vi51.util.AgentScheduler;
import fr.utbm.vi51.util.Point3D;

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
        Square[][][] map = Environment.getInstance().getMap();
        // SenderAgent b = new SenderAgent();
        //KernelTimeManager tm = new VMKernelTimeManager();
        AgentScheduler as = new AgentScheduler();
        Kernel k = Kernels.create(as);
        k.launchLightAgent(env);
        // k.launchLightAgent(b);
        Side a = new Side(1, "img/Ants/worker1.png", "img/Ants/warrior1.png",
                "img/Ants/queen1.png");
        Side b = new Side(2, "img/Ants/worker2.png", "img/Ants/warrior2.png",
                "img/Ants/queen2.png");
        Side c = new Side(3, "img/Ants/worker3.png", "img/Ants/warrior3.png",
                "img/Ants/queen3.png");

        Queen q1 = new Queen(new Point3D(5, 35, 0), 1, a, k);
        Queen q2 = new Queen(new Point3D(35, 35, 0), 1, b, k);
        Queen q3 = new Queen(new Point3D(20, 5, 0), 1, c, k);
        k.launchLightAgent(q1);
        k.launchLightAgent(q2);
        k.launchLightAgent(q3);

        for (int i = 0; i < 25; ++i) {
            k.launchLightAgent(new Warrior(new Point3D(5, 35, 0), 15, a));
            k.launchLightAgent(new Warrior(new Point3D(35, 35, 0), 15, b));
            k.launchLightAgent(new Warrior(new Point3D(20, 5, 0), 15, c));
        }

        for (int i = 0; i < 75; ++i) {
            k.launchLightAgent(new Worker(new Point3D(5, 35, 0), 15, a));
            k.launchLightAgent(new Worker(new Point3D(35, 35, 0), 15, b));
            k.launchLightAgent(new Worker(new Point3D(20, 5, 0), 15, c));
        }

        k.launchHeavyAgent(new WindowsContainer());

        //Food around camps
        for (int i = 0; i < 25; ++i) {
            //Bottom left
            new Food(new Point3D(1, 37, 0));
            new Food(new Point3D(1, 38, 0));
            new Food(new Point3D(2, 37, 0));
            new Food(new Point3D(2, 38, 0));

            //Bottom right
            new Food(new Point3D(37, 37, 0));
            new Food(new Point3D(37, 38, 0));
            new Food(new Point3D(38, 37, 0));
            new Food(new Point3D(38, 38, 0));

            //Top middle
            new Food(new Point3D(19, 1, 0));
            new Food(new Point3D(20, 1, 0));
            new Food(new Point3D(19, 2, 0));
            new Food(new Point3D(20, 2, 0));
        }

        //Food in the middle
        for (int i = 0; i < 100; ++i) {
            new Food(new Point3D(19, 19, 0));
            new Food(new Point3D(19, 21, 0));
            new Food(new Point3D(21, 19, 0));
            new Food(new Point3D(21, 21, 0));
        }
        
        //Food on sides
        for (int i = 0; i < 40; ++i) {
            new Food(new Point3D(12, 18, 0));
            new Food(new Point3D(map.length-12, 18, 0));
            new Food(new Point3D(20, map[0].length-8, 0));
        }
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

        // Some walls around the map
        for (int i = 0; i < map.length; ++i) {
            map[i][0][0] = new Square(LandType.WALL);
            map[i][map[0].length - 1][0] = new Square(LandType.WALL);
        }
        for (int i = 0; i < map.length; ++i) {
            map[0][i][0] = new Square(LandType.WALL);
            map[map.length - 1][i][0] = new Square(LandType.WALL);
        }

        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8 - i; ++j) {
                map[i][j][0] = new Square(LandType.WALL);
            }
        }

        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8 - i; ++j) {
                map[map.length - i - 1][j][0] = new Square(LandType.WALL);
            }
        }

        for (int i = 8; i < 16; ++i) {
            for (int j = 30; j < 34; ++j) {
                map[i][j][0] = new Square(LandType.WALL);
                map[i + 16][j][0] = new Square(LandType.WALL);
            }
        }

        for (int i = 4; i < 10; ++i) {
            for (int j = 35; j > 31; --j) {
                map[i][j - i - 4][0] = new Square(LandType.WALL);
                map[i + 8][j - i - 15][0] = new Square(LandType.WALL);
            }
        }

        for (int i = 4; i < 10; ++i) {
            for (int j = 35; j > 31; --j) {
                map[map.length - (i) - 1][j - i - 4][0] = new Square(
                        LandType.WALL);
                map[map.length - (i + 8) - 1][j - i - 15][0] = new Square(
                        LandType.WALL);
            }
        }

        for (int i = 17; i < 24; ++i) {
            map[i][17][0] = new Square(LandType.WATER);
            map[i][18][0] = new Square(LandType.WATER);
            map[i][22][0] = new Square(LandType.WATER);
            map[i][23][0] = new Square(LandType.WATER);
        }

        for (int j = 19; j < 22; ++j) {
            map[17][j][0] = new Square(LandType.WATER);
            map[18][j][0] = new Square(LandType.WATER);
            map[22][j][0] = new Square(LandType.WATER);
            map[23][j][0] = new Square(LandType.WATER);
        }
        return true;
    }
}
