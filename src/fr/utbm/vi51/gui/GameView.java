package fr.utbm.vi51.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JPanel;
import javax.vecmath.Point3d;

import fr.utbm.vi51.configs.Consts;
import fr.utbm.vi51.environment.Environment;
import fr.utbm.vi51.environment.Pheromone;
import fr.utbm.vi51.environment.Square;
import fr.utbm.vi51.environment.WorldObject;
import fr.utbm.vi51.util.ImageManager;

/**
 * @author Top-K
 * 
 */
public class GameView extends JPanel {
    // STATICS VARS
    static int s_displayedTilesX = 40;
    static int s_displayedTilesY = 40;

    // Represent the current view in terms of number of square
    private Rectangle view = null;
    private int currentTileWidth = new Integer(0);
    private int currentTileHeight = new Integer(0);
    private int levelToPaint = new Integer(0);
    
    private Window parent = null;

    /*
     * Constructor for the GUI
     */
    public GameView(Window parent) {
        this.view = new Rectangle(0, 0, GameView.s_displayedTilesY,
                GameView.s_displayedTilesX);
        this.parent = parent;
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_UP) {
                    moveView("UP");
                }
                if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
                    moveView("DOWN");
                }
                if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
                    moveView("LEFT");
                }
                if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
                    moveView("RIGHT");
                }
                if (ke.getKeyCode() == KeyEvent.VK_PAGE_UP) {
                    zoomIn();
                }
                if (ke.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
                    zoomOut();
                }
                if (ke.getKeyCode() == KeyEvent.VK_O) {
                    zoomInX();
                }
                if (ke.getKeyCode() == KeyEvent.VK_L) {
                    zoomInY();
                }
                if (ke.getKeyCode() == KeyEvent.VK_M) {
                    zoomOutY();
                }
                if (ke.getKeyCode() == KeyEvent.VK_P) {
                    zoomOutX();
                }
                if (ke.getKeyCode() == KeyEvent.VK_Q) {
                    changeLevelUp();
                }
                if (ke.getKeyCode() == KeyEvent.VK_W) {
                    changeLevelDown();
                }
                if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            }
        });

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent arg0) {
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                clicAt(e.getPoint());
            }
        });

    }

    public void paintComponent(Graphics g) {
        // Print the view on the screen
        printView(g);
    }

    public Rectangle getViewReference() {
        return this.view;
    }

    public void moveView(String direction) {
        if (direction == "UP") {
            // Move UP
            this.view.y--;
            this.view.y = Math.max(0, this.view.y);
        } else if (direction == "DOWN") {
            // Move DOWN 
            this.view.y++;
            this.view.y = Math.min(Environment.getInstance().getMap()[0].length
                    - this.view.height, this.view.y);
        } else if (direction == "RIGHT") {
            // Move RIGHT
            this.view.x++;
            this.view.x = Math.min(Environment.getInstance().getMap().length
                    - this.view.width, this.view.x);
        } else if (direction == "LEFT") {
        	// Move LEFT
            this.view.x--;
            this.view.x = Math.max(0, this.view.x);
        }
    }

    public void zoomIn() {
        zoomInX();
        zoomInY();
    }
    
    public void zoomInX() {
        view.width++;
        view.width = Math.min(this.view.width, Environment.getInstance()
                .getMap().length);
    }
    
    public void zoomInY() {
        view.height++;
        view.height = Math.min(this.view.height, Environment.getInstance()
                .getMap().length);
    }

    public void zoomOut() {
        zoomOutX();
        zoomOutY();
    }
    
    public void zoomOutX() {
        view.width--;
        view.width = Math.max(this.view.width, 1);
    }
    
    public void zoomOutY() {
        view.height--;
        view.height = Math.max(this.view.height, 1);
    }
    
    // FUNCTIONS FOR CHANGE AND GET THE LEVEL TO PAINT
    
    public void changeLevelUp() {
    	this.levelToPaint --;
    	this.levelToPaint = Math.max(this.levelToPaint, 0);
    }
    
    public void changeLevelDown() {
    	Environment env = Environment.getInstance();
    	this.levelToPaint ++;
    	this.levelToPaint = Math.min(this.levelToPaint, env.getMapDepth() - 1);
    }
    
    public void setLevelToPaint(int level) {
    	Environment env = Environment.getInstance();
    	this.levelToPaint  = level;
    	this.levelToPaint = Math.min(this.levelToPaint, env.getMapDepth() - 1);
    	this.levelToPaint = Math.max(this.levelToPaint, 0);
    }
    
    public int getLevelToPaint() {
    	return this.levelToPaint;
    }

    protected void printView(Graphics g) {
	    	
	    	 this.currentTileWidth = this.getWidth() / this.view.width;
	         this.currentTileHeight = this.getHeight() / this.view.height;
	         int min = Math.min(currentTileHeight, currentTileWidth);
	         this.currentTileHeight = min;
	         this.currentTileWidth = min;

	         Environment env = Environment.getInstance();
	         ImageManager imgMgr = ImageManager.getInstance();
	         Square[][][] map = env.getMap();
	         for (int i = 0; i < this.view.width; i++) {
	             for (int j = 0; j < this.view.height; j++) { 
	             	// Reference to the current square to print
	             	Square currentSquare = map[i + this.view.x][j + this.view.y][this.levelToPaint];
	                 // Draw land
	                 g.drawImage(imgMgr.getImage(	currentSquare.getLandType().getTexturePath()), 
	                		 						this.currentTileWidth * i,
	                		 						this.currentTileHeight * j, 
		         									this.currentTileWidth, 
		         									this.currentTileHeight, 
		         									this);
	                 // Draw World's objects on the square
	                 List<WorldObject> objs = currentSquare.getObjects();
	                 for (int k = 0; k < objs.size(); ++k) {
	                     WorldObject obj = objs.get(k);
	                     Composite oldComposite = ((Graphics2D) g).getComposite();
	                     if (obj instanceof Pheromone) {
	                    	//Pheromones are printed in the corner corresponding to their direction
	                         if (obj instanceof Pheromone) {
	                             Pheromone p = (Pheromone) obj;
	                             ((Graphics2D) g)
	                                     .setComposite(AlphaComposite.getInstance(
	                                             AlphaComposite.SRC_OVER,
	                                             Math.max(
	                                                     p.getStrength()
	                                                             / Consts.STARTINGPHEROMONEVALUE,
	                                                     0)));
	                             int imagePositionX;
	                             switch (p.getDirection().dx) {
	                                 case -1:
	                                     imagePositionX = this.currentTileWidth
	                                             * (obj.getPosition().x - this.view.x);
	                                     break;
	                                 case 1:
	                                     imagePositionX = this.currentTileWidth
	                                             * (obj.getPosition().x - this.view.x)
	                                             + this.currentTileWidth * 2 / 3;
	                                     break;
	                                 case 0:
	                                 default:
	                                     imagePositionX = this.currentTileWidth
	                                             * (obj.getPosition().x - this.view.x)
	                                             + this.currentTileWidth / 2 - (this.currentTileWidth/3)/2;
	                                     break;
	                             }
	                             int imagePositionY;
	                             switch (p.getDirection().dy) {
	                                 case -1:
	                                     imagePositionY = this.currentTileHeight
	                                             * (obj.getPosition().y - this.view.y);
	                                     break;
	                                 case 1:
	                                     imagePositionY = this.currentTileHeight
	                                             * (obj.getPosition().y - this.view.y)
	                                             + this.currentTileHeight * 2 / 3;
	                                     break;
	                                 case 0:
	                                 default:
	                                     imagePositionY = this.currentTileHeight
	                                             * (obj.getPosition().y - this.view.y)
	                                             + this.currentTileHeight / 2 - (this.currentTileHeight/3)/2;
	                                     break;
	                             }
	                             g.drawImage(
	                                     ImageManager.getInstance().getImage(
	                                             p.getTexturePath()), imagePositionX,
	                                     imagePositionY, this.currentTileWidth / 3, this.currentTileHeight / 3,
	                                     this);
	                             ((Graphics2D) g).setComposite(AlphaComposite
	                                     .getInstance(AlphaComposite.SRC_OVER, 1));
	                             continue;
	                         }
	                     } else {
	                         g.setColor(new Color(255, 255, 255));
	                     }
	                     g.drawImage(
	                             ImageManager.getInstance().getImage(
	                                     obj.getTexturePath()),
	                                     this.currentTileWidth * (obj.getPosition().x - this.view.x) + k
	                                     * this.currentTileWidth / objs.size(),
	                                     this.currentTileHeight * (obj.getPosition().y - this.view.y),
	                             this.currentTileWidth / 3, this.currentTileHeight / 3, this);
	                     ((Graphics2D) g).setComposite(oldComposite);

	                 }             
	                 }
	         }
	         parent.paintRoad(g, this.currentTileWidth, this.currentTileHeight, this.levelToPaint);
	    }

    /*
     * Function to execute on a clic at the position pointPosition
     */
    public void clicAt(Point pointPosition) {
        if (parent != null) {
            parent.setSquareForInfos(getPointedSquare(pointPosition),
                    getSquareCoord(pointPosition));
        }
    }

    /*
     * Function to call to move the currentView to be centered on 
     * the wanted square
     */
    public void centerViewOn(Point squarePosition) {
        // Determine the (x,y) origin of the view
        Point originCoord = new Point(Math.max(
                0,
                Math.min((int) Environment.getInstance().getMapWidth()
                        - this.view.width, (int) squarePosition.x
                        - this.view.width / 2)), Math.max(
                0,
                Math.min((int) Environment.getInstance().getMapHeight()
                        - this.view.height, (int) squarePosition.y
                        - this.view.height / 2)));
        // Set the view new coord
        this.view.x = originCoord.x;
        this.view.y = originCoord.y;
    }

    private Square getPointedSquare(Point pointPosition) {
        Point3d squareCoord = getSquareCoord(pointPosition);
        return Environment.getInstance().getMap()[(int) squareCoord.x][(int) squareCoord.y][(int) squareCoord.z];
    }

    private Point3d getSquareCoord(Point pointPosition) {
        // Determine which square is at pointPosition
        Point3d squareCoord = new Point3d(this.view.x + (int) pointPosition.x
                / this.currentTileWidth, this.view.y + (int) pointPosition.y
                / this.currentTileHeight, this.levelToPaint);
        assert (squareCoord.x >= 0 && squareCoord.x <= Environment
                .getInstance().getMapWidth());
        assert (squareCoord.y >= 0 && squareCoord.y <= Environment
                .getInstance().getMapHeight());
        assert (squareCoord.z >= 0 && squareCoord.z <= Environment
                .getInstance().getMapDepth());

        return squareCoord;
    }
}
