package fr.utbm.vi51.gui;

import fr.utbm.vi51.environment.Environment;
import fr.utbm.vi51.environment.LandType;
import fr.utbm.vi51.environment.Square;

//Comment to test git
public class Main {

    public static void main(String[] args) {
    	// Generate environment map
    	generateMap1();
    	
        Window wind = new Window();
        wind.repaint();
    }
    // Functions for map génération
    static private boolean generateMap1() {
    	// Getting the environment from the singleton
    	Environment env = Environment.getInstance();
    	// Getting environment map
    	Square[][][] map = env.getMap();
        
    	// Generation of top level full of grass
    	for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                map[i][j][0] = new Square(LandType.GRASS);
            }
        }
        // A lake in the middle of the map
        map[10][10][0] = new Square(LandType.WATER);
        map[11][10][0] = new Square(LandType.WATER);
        map[10][11][0] = new Square(LandType.WATER);
        map[11][11][0] = new Square(LandType.WATER);
    	return true;
    }
}
