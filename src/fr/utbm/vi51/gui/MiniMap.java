package fr.utbm.vi51.gui;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.vecmath.Point2d;

import fr.utbm.vi51.environment.Environment;
import fr.utbm.vi51.environment.Square;
import fr.utbm.vi51.environment.WorldObject;
import fr.utbm.vi51.util.ImageManager;

/*
 * This class represents the mini map in the user interface
 */
public class MiniMap extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4627169120531941096L;
	// Reference to the current View 
	// !!!!! DO NOT MODIFY HERE !!!!!!!!
	private Rectangle 	rCurrentView = null;
	// Reference to the parent Window
	Window 		rParent = null;
	
	private JButton		butLevelUp = null;
	private JButton		butLevelDown = null;
	
	private Rectangle 	mapView = null;
	private int			currentLevel = 0;
	private int 		currentZoom = 0;
	private int 		currentTileWidth = 0;
	private int 		currentTileHeight = 0;
	
	public MiniMap(Window rParent,Rectangle currentViewReference) {
		//assert(rCurrentView != null);
		assert(rParent != null);
		Environment env = Environment.getInstance();
		this.mapView = new Rectangle(0,0,0,0);
		this.mapView.height = env.getMapHeight();
		this.mapView.width = env.getMapWidth();
		this.rCurrentView = currentViewReference;
		this.rParent = rParent;
		this.setLayout(null);
		this.addMouseListener(new MouseListener(){
			@Override
			public void mouseReleased(MouseEvent e) {
				// NOTHING TO DO
			}
			@Override
			public void mousePressed(MouseEvent e) 	{
				// NOTHING TO DO
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				// Move the view to center it on the mouse
				moveView(new Point(e.getPoint()));
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				// NOTHING TO DO
			}
			@Override
			public void mouseExited(MouseEvent e) {
				// NOTHING TO DO
			}
		});
		initbutLevel();
		this.setFocusable(false);
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		// Get the better zoom to paint
		updateZoom();
        // Draw the minimap first
        drawMiniMap(g);
        // Then the rectangle to indicate the current view
        drawViewRect(g);
	}
	
	// Update the zoom to have the better view
	private void updateZoom() {
		this.currentZoom = Math.min(	this.getHeight() / Environment.getInstance().getMapHeight(),
										this.getWidth() / Environment.getInstance().getMapWidth()
									);
		// Update the Width and height of each tile
		this.currentTileWidth = this.mapView.width / Environment.getInstance().getMapWidth() * this.currentZoom;
        this.currentTileHeight = this.mapView.height / Environment.getInstance().getMapHeight() * this.currentZoom;
	}
	
	/*
	 * Function to move the current view
	 * The aim is to have the point coordViewCenter centered in the view
	 */
	void moveView(Point coordViewCenter) {
		// Determine which square have to be in the middle
 		Point squareCoord = new Point( coordViewCenter.x / this.currentTileWidth,coordViewCenter.y / this.currentTileHeight );
		// Ask to the parent to move view
 		if(this.rParent != null) {
 			this.rParent.centerCurrentViewOn(squareCoord);
 		}
	}
	
	// Function to draw the view Rectangle on the minimap
	private void drawViewRect(Graphics g) {
        g.drawRect( (this.mapView.x + this.rCurrentView.x)*this.currentTileWidth,
        			(this.mapView.y + this.rCurrentView.y)*this.currentTileHeight,
        			(this.rCurrentView.width)*this.currentTileWidth,
        			(this.rCurrentView.height)*this.currentTileHeight);
	}
	
	// Function to draw the entire minimap
	private void drawMiniMap(Graphics g) {
		this.currentLevel = this.rParent.getLevelToPaint();
		Square[][][] map = Environment.getInstance().getMap();
		assert(map != null);
        Point2d drawPosition = new Point2d(0,0);
        int mapWidth = Environment.getInstance().getMapWidth();
        int mapHeight = Environment.getInstance().getMapHeight();
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
            	// Get a square
            	Square currentSquare = map[i][j][this.currentLevel];
            	drawPosition.x = this.mapView.x + this.currentTileWidth * i;
            	drawPosition.y = this.mapView.y + this.currentTileHeight * j;
            	// Draw land
                g.drawImage(	ImageManager.getInstance().getImage(currentSquare.getLandType().getTexturePath()), 
                				(int) drawPosition.x,
                				(int) drawPosition.y, 
                				this.currentTileWidth, 
                				this.currentTileHeight, 
                                this);
                List<WorldObject> objs = currentSquare.getObjects();
                assert(objs != null);
                // if the square contains at least one worldObject
                if(!objs.isEmpty()) {
                	// Draw object
                    WorldObject obj = objs.get(0);
                    g.drawImage(ImageManager.getInstance().getImage(obj.getTexturePath()),
                    			(int) drawPosition.x,
	                    		(int) drawPosition.y, 
	                    		this.currentTileWidth,
	                    		this.currentTileHeight,
	                            this);
                }
            }
        }
        int x = this.mapView.x + this.currentTileWidth * this.mapView.width;
		this.butLevelUp.setBounds(x + 10, 0, 50, 20);
		this.butLevelDown.setBounds(x + 10, 20, 50, 20);
	}
	
	// CHANGING Level
	private void initbutLevel() {
		// Only one initialization
		if(this.butLevelDown != null && this.butLevelUp != null) {
			assert(false);
			return;
		}
		this.butLevelDown = new JButton("-"); //$NON-NLS-1$
		this.butLevelUp = new JButton("+"); //$NON-NLS-1$
		
		ActionListener action = new ActionListener() {
				   @Override
				public void actionPerformed(ActionEvent e) {
					   MiniMap.this.rParent.changeLevelDown();
				   }
		};
		
		this.butLevelDown.addActionListener(action);
		this.add(this.butLevelDown);
		
		ActionListener action2 = new ActionListener() {
			   @Override
			public void actionPerformed(ActionEvent e) {
				   MiniMap.this.rParent.changeLevelUp();
			   }
		};
		
		this.butLevelUp.addActionListener(action2);
		this.add(this.butLevelUp);
		
		this.butLevelDown.setFocusable(false);
		this.butLevelUp.setFocusable(false);
	}
}
