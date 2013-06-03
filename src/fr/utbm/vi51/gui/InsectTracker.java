package fr.utbm.vi51.gui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

import fr.utbm.vi51.configs.Consts;
import fr.utbm.vi51.environment.InsectBody;
import fr.utbm.vi51.util.Point3D;

public class InsectTracker {
	// Represent the current view in terms of number of square
    private Rectangle rView = null;
	// Reference to the Insect to track
	InsectBody		rInsect = null;
	List<Point3D>	listOldPosition = null;
	Point3D			lastPos = null;
	
	Window			rParent = null;
	
	public InsectTracker(Window parent, Rectangle rView) {
		assert(parent != null);
		assert(rView != null);
		this.rParent = parent;
		this.rView = rView;
	}
	// Set the insect to track
	public void setInsect(InsectBody insect) {
		if(insect == null) {
			return;
		}
		this.rInsect = insect;
		this.listOldPosition = new LinkedList<Point3D>();
		System.out.println("Insect setted");
	}
	
	public void paintRoad(Graphics g,int tileWidth,int tileHeight,int levelPainted) {
		if(this.rInsect == null) {
			return;
		}
		if(!this.rInsect.isAlive()) {
			this.rInsect = null;
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
		Point3D newPos = new Point3D(rInsect.getPosition());
		if(lastPos != null) {
			if(newPos.equals(lastPos)) {
				return;	
			}
		}
		this.listOldPosition.add(newPos);
		this.lastPos = newPos;
		if(this.listOldPosition.size() > Consts.MAX_TRACKED_POSITION) {
			this.listOldPosition.remove(0);
		}
	}
	
	private void paintVect(Graphics g,Point3D pos1,Point3D pos2,int tileWidth,int tileHeight,int levelPainted) {
		if(pos1.z != levelPainted || pos2.z != levelPainted) {
			return;
		}
		
		if( !this.rView.contains(pos1.x, pos1.y) || !this.rView.contains(pos2.x, pos2.y)) {
			return;
		}
		Point3D posDep = new Point3D(pos1);
		Point3D posArr = new Point3D(pos2);
		

		// Move in view
		posDep.x -= this.rView.x;
		posDep.y -= this.rView.y;
		posArr.x -= this.rView.x;
		posArr.y -= this.rView.y;
		
		// Scaled
		posDep.x *= tileWidth;
		posDep.y *= tileHeight;
		posArr.x *= tileWidth;
		posArr.y *= tileHeight;
		
		// Centered
		posDep.x += tileWidth/2;
		posDep.y += tileHeight/2;
		posArr.x += tileWidth/2;
		posArr.y += tileHeight/2;
		
		g.drawLine(posDep.x, posDep.y, posArr.x, posArr.y);
	}
}
