package fr.utbm.vi51.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import fr.utbm.vi51.configs.Consts;
import fr.utbm.vi51.environment.InsectBody;
import fr.utbm.vi51.environment.InsectBodyType;
import fr.utbm.vi51.environment.WorldObject;
import fr.utbm.vi51.util.ImageManager;
import fr.utbm.vi51.util.Point3D;

public class InsectTracker  extends JPanel{
	/**
	 * FINAL VARIABLES FOR TEXTS AND INFORMATIONS
	 */
	private static final long 		serialVersionUID 	= -5499177726478807431L;
	private static final String		BUTTON_CAPTION		= new String("X"); //$NON-NLS-1$
	private static final String		NO_INSECT_CAPTION	= new String("No insect is currently selected."); //$NON-NLS-1$
	
	private static final Point3D	IMG_POSITION 		= new Point3D(0,0,0);
	private static final Point3D	SIDE_POSITION 		= new Point3D(60,0,0);
	private static final Point3D	FUNCTION_POSITION   = new Point3D(60,25,0);
	private static final Point3D	COORD_POSITION   	= new Point3D(60,40,0);
	private static final Point3D	CARRIED_POSITION   	= new Point3D(60,60,0);
	private static final Point3D	LIFE_POSITION   	= new Point3D(0,50,0);
	private static final Point3D	HUNGER_POSITION   	= new Point3D(150,25,0);
	//private final Window	rParent;
	private JButton			buttonStopTrack = null;
    private Rectangle 		rView = null;
	
    private InsectBody		rInsect = null;
	private JProgressBar	lifeBar = null;
	private JProgressBar	hungerBar = null;
	private InsectBodyType	insectType = null;
    private List<Point3D>	listOldPosition = null;
	private Point3D			insectLastPos = null;
	
	
	
	public InsectTracker(/*Window parent,*/ Rectangle rView) {
		assert(rView != null);
//		this.rParent = parent;
		this.rView = rView;
		this.setLayout(null);
		addButton();
		addLifeBar();
		addHungerBar();
	}
	
	// Set the insect to track
	public void setInsect(InsectBody insect) {
		if(insect == null) {
			return;
		}
		unselectInsect();
		this.rInsect = insect;
		this.listOldPosition = new LinkedList<>();
		this.buttonStopTrack.setVisible(true);
		this.lifeBar.setMaximum(this.rInsect.getMaxHealth());
		this.lifeBar.setVisible(true);
		this.hungerBar.setVisible(true);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		// No insect selected
		if(this.rInsect == null) {
			g.drawString(InsectTracker.NO_INSECT_CAPTION, 0, 10);
		}
		else {
			paintInfos(g);
		}
	}
	
	public void paintInfos(Graphics g) {
		if(this.insectType == null) {
			this.insectType = this.rInsect.getType();
		}
	    // Draw Img
        g.drawImage(ImageManager.getInstance().getImage(this.rInsect.getTexturePath()),
        			IMG_POSITION.x,IMG_POSITION.y,50,50,this);
        
        // Draw side rectangle
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(this.rInsect.getSide().getDominantColor());
        g2.fill(new Rectangle2D.Double(SIDE_POSITION.x, SIDE_POSITION.y, this
                .getWidth() - SIDE_POSITION.x, 15));
        g2.setPaint(Color.black);
        g2.drawString(this.rInsect.getSide().toString(), SIDE_POSITION.x, SIDE_POSITION.y + 10);
        // Draw insect function
	    g.drawString(this.insectType.toString(), FUNCTION_POSITION.x, FUNCTION_POSITION.y);
        
	    // Insect location
	    g.drawString(this.rInsect.getPosition().toString(),COORD_POSITION.x,COORD_POSITION.y);
        
	    // Draw the object carried by the insect
        WorldObject obj = this.rInsect.getCarriedObject();
        if (obj != null) {
            g.drawString("Carrying :", CARRIED_POSITION.x, CARRIED_POSITION.y); //$NON-NLS-1$
            g.drawImage(
                    ImageManager.getInstance().getImage(obj.getTexturePath()),
                    CARRIED_POSITION.x + 60, CARRIED_POSITION.y - 12, 25, 25,
                    this);
        }
        // Update bars
        updatebars();
        // Update button position
		this.buttonStopTrack.setBounds(this.getWidth() - 60, this.getHeight() - 20 , 60, 20);
	}
	
	// Paint the road down by the insect on the current Map
	public void paintRoad(Graphics g,int tileWidth,int tileHeight,int levelPainted) {
		if(this.rInsect == null) {
			return;
		}
		if(!this.rInsect.isAlive()) {
			unselectInsect();
			return;
		}
		computeRoad();
		int listSize = this.listOldPosition.size();
		for(int i = 1; i < listSize ; i++) {
			paintVect(	g, 
						this.listOldPosition.get(i-1),
						this.listOldPosition.get(i),
						tileWidth,
						tileHeight,
						levelPainted);
		}
	}
	
	// Fonction computing the 10 old position of the insect
	private void computeRoad() {
		Point3D newPos = new Point3D(this.rInsect.getPosition());
		if(this.insectLastPos != null) {
			// Insect has not move
			if(newPos.equals(this.insectLastPos)) {
				return;	
			}
		}
		this.listOldPosition.add(newPos);
		this.insectLastPos = newPos;
		if(this.listOldPosition.size() > Consts.MAX_TRACKED_POSITION) {
			this.listOldPosition.remove(0);
		}
	}
	
	// Paint a segment 
	private void paintVect(Graphics g,Point3D pos1,Point3D pos2,int tileWidth,int tileHeight,int levelPainted) {
		if(pos1.z != levelPainted || pos2.z != levelPainted) {
			return;
		}
		
		if( !this.rView.contains(pos1.x, pos1.y) || !this.rView.contains(pos2.x, pos2.y)) {
			return;
		}
		Point3D posDep = new Point3D(pos1);
		Point3D posArr = new Point3D(pos2);
		
		posDep.x = (posDep.x - this.rView.x)*tileWidth + tileWidth/2;
		posDep.y = (posDep.y - this.rView.y)*tileHeight + tileHeight/2;
		posArr.x = (posArr.x - this.rView.x)*tileWidth + tileWidth/2;
		posArr.y = (posArr.y - this.rView.y)*tileHeight + tileHeight/2;
		
		g.drawLine(posDep.x, posDep.y, posArr.x, posArr.y);
	}
	
	// Unselect the insect
	public void unselectInsect() {
		this.rInsect = null;
		if(this.listOldPosition != null) {
			this.listOldPosition.clear();			
		}
		this.insectLastPos = null;
		this.buttonStopTrack.setVisible(false);
		this.lifeBar.setVisible(false);
		this.hungerBar.setVisible(false);
		this.insectType = null;
	}
	/**
	 * Function to initialize the button
	 */
	private void addButton() {
		this.buttonStopTrack = new JButton(InsectTracker.BUTTON_CAPTION);
		this.buttonStopTrack.setBounds(this.getWidth() - 60, this.getHeight() - 20 , 60, 20);
		ActionListener action = new ActionListener() {
				   @Override
				public void actionPerformed(ActionEvent e) {
					   InsectTracker.this.unselectInsect();
				   }
		};
		this.buttonStopTrack.addActionListener(action);
		this.buttonStopTrack.setFocusable(false);
		this.buttonStopTrack.setVisible(false);
		this.add(this.buttonStopTrack);
	}
	
	/**
	 * Function to initialize the life bar
	 */
	private void addLifeBar() {
		this.lifeBar = new JProgressBar();
		this.lifeBar.setBounds(LIFE_POSITION.x, LIFE_POSITION.y, 50, 15);
		this.lifeBar.setVisible(false);
		this.lifeBar.setBackground(new Color(255,0,0));
		this.lifeBar.setForeground(new Color(0,255,0));
		this.lifeBar.setStringPainted(true);
		this.add(this.lifeBar);
	}
	
	/**
	 * Function to initialize the hunger bar
	 */
	private void addHungerBar() {
		this.hungerBar = new JProgressBar();
		this.hungerBar.setBounds(HUNGER_POSITION.x, HUNGER_POSITION.y, this.getWidth() - HUNGER_POSITION.x, 20);
		this.hungerBar.setVisible(false);
		this.hungerBar.setBackground(new Color(156,90,60));
		this.hungerBar.setForeground(new Color(255,0,0));
		this.hungerBar.setMaximum(Consts.MAXHUNGER);
		this.hungerBar.setStringPainted(true);
		this.add(this.hungerBar);
	}
	
	/**
	 * Function to update bars at each draw
	 */
	private void updatebars() {
		// Update lifeBar value
        this.lifeBar.setValue(this.rInsect.getCurrentHealth());
        this.lifeBar.setString(this.rInsect.getCurrentHealth() + "/" + this.lifeBar.getMaximum()); //$NON-NLS-1$
        
        
        // Update hunger value
        this.hungerBar.setValue(this.rInsect.getHunger());
        // Update hunger bar bounds
        this.hungerBar.setBounds(HUNGER_POSITION.x, HUNGER_POSITION.y, this.getWidth() - HUNGER_POSITION.x, 15);
        
        String hungerString = new String();
        if(this.hungerBar.getWidth() > 200) {
        	hungerString += "Hunger:"; //$NON-NLS-1$
        }
        // Add Values
        hungerString += +this.rInsect.getHunger()/10 + "/" + this.hungerBar.getMaximum()/10; //$NON-NLS-1$
        this.hungerBar.setString(hungerString);
	}
}
