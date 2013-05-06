package fr.utbm.vi51.gui.minimap;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.JPanel;
import javax.vecmath.Point2d;

import fr.utbm.vi51.environment.Environment;
import fr.utbm.vi51.environment.Square;
import fr.utbm.vi51.environment.WorldObject;
import fr.utbm.vi51.util.ImageManager;

/*
 * This class represents the mini map in the user interface
 * Use the moveTo function to move the mini map
 * Use the setDimensions function to set minimap's height and width
 * 		!!!! WARNING !!!! A dimension cannot be smaller than the number of square in this dimension
 * Use the paint function to paint the mini map on a Graphics
 */
public class MiniMap {
	Point2d	position = new Point2d(0,300);
	int		height = 0;
	int		width = 0;
	
	
	public MiniMap() {
		Environment env = Environment.getInstance();
		this.height = env.getMapHeight();
		this.width = env.getMapWidth();
	}
	
	
	public void paint(Graphics g, JPanel panel,Rectangle viewRect) {
		assert(g == null && panel != null && viewRect != null);
		
		Environment env = Environment.getInstance();
		assert(env != null);
		Square[][][] map = env.getMap();
		assert(map != null);
		int displayedTilesX = env.getMapWidth();
		int displayedTilesY = env.getMapHeight();
		int tileWidth = this.width / displayedTilesX;
        int tileHeight = this.height / displayedTilesY;
        
        Point2d drawPosition = new Point2d(0,0);
        for (int i = 0; i < displayedTilesX; i++) {
            for (int j = 0; j < displayedTilesY; j++) {
            	// Get a square
            	Square currentSquare = map[i][j][0];
            	drawPosition.x = position.x + tileWidth * i;
            	drawPosition.y = position.y + tileHeight * j;
                List<WorldObject> objs = currentSquare.getObjects();
                // if the square contains at least one worldObject
                if(!objs.isEmpty()) {
                	// Draw object
                    WorldObject obj = objs.get(0);
                    g.drawImage(ImageManager.getInstance().getImage(obj.getTexturePath()),
                    			(int) drawPosition.x,
	                    		(int) drawPosition.y, 
	                            tileWidth,
	                            tileHeight,
	                            panel);
                }
                else {
                	// Draw land
                    g.drawImage(	ImageManager.getInstance().getImage(currentSquare.getLandType().getTexturePath()), 
                    				(int) drawPosition.x,
                    				(int) drawPosition.y, 
                                    tileWidth, 
                                    tileHeight, 
                                    panel);
                }
            }
        }
        // Draw viewRect
        g.drawRect( (int) position.x + viewRect.x,
        			(int) position.y + viewRect.y,
        			viewRect.width*tileWidth,
        			viewRect.height*tileHeight);
	}
	
	public void moveTo(int posX,int posY) {
		this.position.x = posX;
		this.position.y = posY;
	}
	
	public boolean setDimensions(int width,int height){
		Environment env = Environment.getInstance();
		// The minimap need at least one pixel per square of the map
		if(width < env.getMapWidth() || height < env.getMapHeight()) {
			return false;
		}
		this.width = width;
		this.height = height;
		return true;
	}
}
