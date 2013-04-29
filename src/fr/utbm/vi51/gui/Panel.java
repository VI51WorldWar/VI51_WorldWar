package fr.utbm.vi51.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import fr.utbm.vi51.environment.Environment;
import fr.utbm.vi51.environment.Square;
import fr.utbm.vi51.environment.WorldObject;

public class Panel extends JPanel {
	private final int displayedTilesX = 20;
	private final int displayedTilesY = 20;
	private final int originX = 0;
	private final int originY = 0;
	
	
    public void paintComponent(Graphics g) {
    	int tileSizeX = this.getWidth() / displayedTilesX;
    	int tileSizeY = this.getHeight() / displayedTilesY;
    	
        Environment env = Environment.getInstance();
		Square[][][] map = env.getMap();
		for (int i = 0; i < displayedTilesX; i++) {
		    for (int j = 0; j < displayedTilesY; j++) {
		    	// Draw land
		    	g.drawImage(map[i+originX][j+originY][0].getLandType().getTexture(), tileSizeX * i, tileSizeY * j, tileSizeX, tileSizeY, this);
		    	// Draw World's objects on the square
		        for (WorldObject obj : map[i+originX][j+originY][0].getObjects()) {
		            g.drawImage(obj.getTexture(), (int) (tileSizeX * obj.getPosition().getX()), (int) (tileSizeY * obj.getPosition().getY()), tileSizeX, tileSizeY, this);
		        }
		    }
		}
    }
}
