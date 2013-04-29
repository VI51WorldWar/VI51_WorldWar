package fr.utbm.vi51.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import fr.utbm.vi51.environment.Environment;
import fr.utbm.vi51.environment.LandType;
import fr.utbm.vi51.environment.Square;
import fr.utbm.vi51.environment.WorldObject;

public class Panel extends JPanel {
    public void paintComponent(Graphics g) {
    	// Loading of the differents textures
    	Image grassTexture = null;
    	Image waterTexture = null;
    	Image wallTexture = null;
    	try {
	    	grassTexture = ImageIO.read(new File(LandType.GRASS.getTexturePath()));
	    	waterTexture = ImageIO.read(new File(LandType.WATER.getTexturePath()));
	    	wallTexture = ImageIO.read(new File(LandType.WALL.getTexturePath()));
    	} catch (IOException e) {
            e.printStackTrace();
        }
    	
        try {
            Environment env = Environment.getInstance();
            Square[][][] map = env.getMap();
            for (int i = 0; i < 800 / 50; i++) {
                for (int j = 0; j < 600 / 44; j++) {
                    // Select the appropriate texture
                	Image landTexture = null;
                	// Depending of the square landtype
                	switch(map[i][j][0].getLandType()) {
	                	case GRASS : 	landTexture = grassTexture; 	break;
	                	case WATER : 	landTexture = waterTexture;		break;
	                	case WALL :		landTexture = wallTexture;		break;
                	}
                	// Draw land
                	g.drawImage(landTexture, 50 * i, 44 * j, 50, 50, this);
                	// Draw World's objects on the square
                    for (WorldObject obj : map[i][j][0].getObjects()) {
                        Image img1 = ImageIO.read(new File(obj.getTexture()));
                        g.drawImage(img1, (int) (50 * obj.getPosition().getX()), (int) (44 * obj.getPosition().getY()), 20, 20, this);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
