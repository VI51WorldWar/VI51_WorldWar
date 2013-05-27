package fr.utbm.vi51.gui;

import java.awt.Graphics;
import java.awt.Point;
import java.util.List;

import javax.swing.JPanel;

import fr.utbm.vi51.environment.Body;
import fr.utbm.vi51.environment.Food;
import fr.utbm.vi51.environment.Pheromone;
import fr.utbm.vi51.environment.Square;
import fr.utbm.vi51.environment.WorldObject;

public class SquareInfos extends JPanel{
	Square rSquareReference = null;
	
	public SquareInfos() {
		this.rSquareReference = null;
	}
	
	public void paintComponent(Graphics g) {
		Point drawPosition = new Point(0,10);
		if(this.rSquareReference == null) {
			g.drawString("No case selected.", drawPosition.x, drawPosition.y);
		}
		else {
			drawSquareInfos(g,drawPosition);
		}
	}
	
	public void setSquare(Square squareReference) {
		this.rSquareReference = squareReference;
	}
	
	private void drawSquareInfos(Graphics g,Point drawPosition) {
		// Allocate a new Point
		Point drawPos = new Point(drawPosition);
		
		// Type of Land
		g.drawString("Type of land : " + this.rSquareReference.getLandType().getName(), drawPos.x, drawPos.y);
		
		// Analyze the World objects on the square
		int nbAnts = 0;
		int nbPheromone = 0;
		int nbFood = 0;
		List<WorldObject> objs = this.rSquareReference.getObjects();
	    for (int k = 0; k < objs.size(); ++k) {
	    	WorldObject obj = objs.get(k);
	        if (obj instanceof Pheromone) {
	        	nbPheromone++;
	        }
	        else if (obj instanceof Body) {
	        	nbAnts++;
	        }
	        else if (obj instanceof Food) {
	        	nbFood++;
	        }
	    }
		// Number of Ants
		drawPos.y += 20;
		g.drawString("Ants : " + nbAnts, drawPos.x, drawPos.y);
			
		// Pheromones
		drawPos.y += 20;
		g.drawString("Pheromones : " + nbPheromone, drawPos.x, drawPos.y);
		
		// Foods
		drawPos.y += 20;
		g.drawString("Foods : " + nbFood, drawPos.x, drawPos.y);
	}
}
