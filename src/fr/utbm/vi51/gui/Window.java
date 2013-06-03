package fr.utbm.vi51.gui;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;

import javax.swing.JFrame;
import javax.vecmath.Point3d;

import fr.utbm.vi51.configs.Consts;
import fr.utbm.vi51.environment.InsectBody;
import fr.utbm.vi51.environment.Square;

/**
 * @author valentin
 */
public class Window extends JFrame {
    // Dimensions of the Window
	GameView 		view = null;
	MiniMap 		minimap = null;
	SquareInfos 	sqInfos = null;
	
	// Tracker
	InsectTracker	insectTracker = null;
	
    public Window() {
    	this.setTitle("Fenetre de base");
        this.setSize(Consts.WINWIDTH, Consts.WINHEIGHT);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout()); 
        addView();
        addMiniMap();
        addSquareInfos();
        this.setVisible(true);
        this.insectTracker = new InsectTracker(this,this.view.getViewReference());
    }
    
    public void setSquareForInfos(Square squareReference,Point3d squarePosition) {
    	if(this.sqInfos != null) {
    		this.sqInfos.setSquare(squareReference,squarePosition);
    	}
    }
    
    public void setInsectToTrack(InsectBody insectBody) {
    	if(this.insectTracker != null) {
    		this.insectTracker.setInsect(insectBody);
    	}
    }
    
    public void paintRoad(Graphics g,int tileWidth,int tileHeight,int levelPainted) {
    	if(this.insectTracker != null) {
    		this.insectTracker.paintRoad(g, tileWidth, tileHeight, levelPainted);
    	}
	}
    
    public void centerCurrentViewOn(Point squarePosition) {
    	if(this.view != null) {
    		this.view.centerViewOn(squarePosition);
    	}
    }
    
    private void addMiniMap() {
    	GridBagConstraints c = new GridBagConstraints();
    	this.minimap = new MiniMap(this,this.view.getViewReference());
    	c.fill = GridBagConstraints.BOTH;
    	c.gridx = 0;
    	c.gridy = 3;
    	c.gridheight = 2;
    	c.gridwidth = 1;
    	c.gridwidth = 1;
    	c.weighty = 0.1;
    	c.weightx = 0.1;
    	this.add(this.minimap, c);
    }
    
    private void addView() {
    	GridBagConstraints c = new GridBagConstraints();
    	this.view = new GameView(this);
    	c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 3;
        c.gridwidth = 3;
        c.weighty = 1.0;
        c.weightx = 1.0;
        this.add(this.view,c); 
    }
    
    private void addSquareInfos() {
    	GridBagConstraints c = new GridBagConstraints();
    	this.sqInfos = new SquareInfos(this);
    	c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = 3;
        c.gridheight = 2;
        c.gridwidth = 2;
        c.weighty = 0.1;
        c.weightx = 0.1;
        this.add(this.sqInfos, c);
    }
}
