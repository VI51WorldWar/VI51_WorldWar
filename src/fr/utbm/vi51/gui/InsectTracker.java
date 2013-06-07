package fr.utbm.vi51.gui;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import fr.utbm.vi51.configs.Consts;
import fr.utbm.vi51.environment.Body;
import fr.utbm.vi51.environment.Environment;
import fr.utbm.vi51.environment.Food;
import fr.utbm.vi51.environment.InsectBody;
import fr.utbm.vi51.environment.Pheromone;
import fr.utbm.vi51.environment.WorldObject;
import fr.utbm.vi51.util.Point3D;

public class InsectTracker  extends JPanel{
	// Represent the current view in terms of number of square
    private Rectangle rView = null;
	// Reference to the Insect to track
	InsectBody		rInsect = null;
	List<Point3D>	listOldPosition = null;
	Point3D			lastPos = null;
	Window			rParent = null;
	JButton			buttonStopTrack = null;
	
	public InsectTracker(Window parent, Rectangle rView) {
		assert(parent != null);
		assert(rView != null);
		this.rParent = parent;
		this.rView = rView;
		this.setLayout(null);
		buttonStopTrack = new JButton("Stop tacking.");
		this.buttonStopTrack.setBounds(0, 40, 100, 20);
		ActionListener action = new ActionListener() {
				   public void actionPerformed(ActionEvent e) {
					   rInsect =null;
				   }
		};
		this.buttonStopTrack.addActionListener(action);
		this.add(buttonStopTrack);
		this.buttonStopTrack.setVisible(false);
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
	
	public void paintComponent(Graphics g) {
		Point drawPosition = new Point(0,10);
		g.drawString("Insect tracker :", drawPosition.x, drawPosition.y);
		drawPosition.y += 20;
		// No insect selected
		if(this.rInsect == null) {
			this.buttonStopTrack.setVisible(false);
			g.drawString("No insect selected.", drawPosition.x, drawPosition.y);
		}
		else {
			paintInfos(g,drawPosition);
			this.buttonStopTrack.setVisible(true);
		}
	}
	
	public void paintInfos(Graphics g, Point drawPosition) {
		// Allocate a new Point
		Point drawPos = new Point(drawPosition);
		// Insect location
	    g.drawString(	"Coord : "+ this.rInsect.getPosition().toString()
	    				,drawPos.x
	    				,drawPos.y);
	}
	
	// Paint the road down by the insect on the current Map
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
}
