package fr.utbm.vi51.gui;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.vecmath.Point3d;

import fr.utbm.vi51.environment.Body;
import fr.utbm.vi51.environment.Environment;
import fr.utbm.vi51.environment.Food;
import fr.utbm.vi51.environment.LandType;
import fr.utbm.vi51.environment.Pheromone;
import fr.utbm.vi51.environment.Square;
import fr.utbm.vi51.environment.WorldObject;
import fr.utbm.vi51.util.Point3D;

public class SquareInfos extends JPanel{
	Square 				rSquareReference = null;
	Point3d				squarePosition = null;
	
	JButton 			buttonAddFood = null;
	JComboBox			comLandType = null;
	List<PheromoneBar> 	listBars = null;
	
	public SquareInfos() {
		this.rSquareReference = null;
		this.listBars = new LinkedList<PheromoneBar>();
		initLandTypeSelection();
		initAddFoodButton();
		this.setLayout(null);
		this.buttonAddFood.setVisible(false);
		this.comLandType.setVisible(false);
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
	
	public void addPheromoneBar(Pheromone rPheromone) {
		assert(rPheromone != null);
		// Check if the pheromone is not already referenced
		for(PheromoneBar bar : this.listBars) {
			if(bar.isAttachedTo(rPheromone)) {
				return;
			}
		}
		PheromoneBar newPheromoneBar = new PheromoneBar(rPheromone);
		this.listBars.add(newPheromoneBar);
		this.add(newPheromoneBar);
	}
	
	public void setSquare(Square squareReference,Point3d squarePosition) {
		// Set the reference
		this.rSquareReference = squareReference;
		this.squarePosition = new Point3d(squarePosition);
		// Update visibility of the differents widgets
		if(squareReference == null) {
			this.buttonAddFood.setVisible(false);
			this.comLandType.setVisible(false);
		}
		else {
			this.buttonAddFood.setVisible(true);
			this.comLandType.setVisible(true);
			// Set the current landtype as default value of the combo box
			this.comLandType.setSelectedItem(rSquareReference.getLandType());
		}
		for(PheromoneBar bar : this.listBars) {
			this.remove(bar);
		}
		this.listBars.clear();
	}
	
	private void drawSquareInfos(Graphics g,Point drawPosition) {
		// Allocate a new Point
		Point drawPos = new Point(drawPosition);
		
		// Type of Land
		g.drawString("Type of land : ", drawPos.x, drawPos.y);
		// Analyze the World objects on the square
		int nbAnts = 0;
		int nbFood = 0;
		List<WorldObject> objs = this.rSquareReference.getObjects();
	    for (int k = 0; k < objs.size(); ++k) {
	    	WorldObject obj = objs.get(k);
	        if (obj instanceof Pheromone) {
	        	addPheromoneBar((Pheromone) obj);
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
		
		// Draw Pheromones bars
		drawBars(g);
		
		// Foods
		drawPos.x += 80;
		g.drawString("Foods : " + nbFood, drawPos.x, drawPos.y);
	}
	
	public void drawBars(Graphics g) {
		PheromoneBar bar = null;
		int i = 0;
		g.drawString("Pheromones : ",200,10);
		while( i < this.listBars.size()) {
			bar = this.listBars.get(i);
			if(bar.isEnded()) {
				this.listBars.remove(bar);
				this.remove(bar);
			}
			else {
				bar.update(i);
				i++;
			}
		}
	}
	
	// ADDING FOOD
	private void initAddFoodButton() {
		// Only one initialization
		if(buttonAddFood != null) {
			assert(false);
			return;
		}
		buttonAddFood = new JButton("Add food.");
		this.buttonAddFood.setBounds(80, 40, 100, 20);
		ActionListener action = new ActionListener() {
				   public void actionPerformed(ActionEvent e) {
					   Environment.getInstance().addWorldObject(new Food(new Point3D(squarePosition)));
				   }
		};
		this.buttonAddFood.addActionListener(action);
		this.add(buttonAddFood);
	}
	
	// CHANGING LANDTYPE
	private void initLandTypeSelection() {
		// Only one initialization
		if(this.comLandType != null) {
			assert(false);
			return;
		}	   
		// Add a combo box with all values of the LandType enumeration
	    this.comLandType = new JComboBox(LandType.values());
	    this.comLandType.setBounds(80,0,70,20);
		ActionListener action = new ActionListener() {
			   public void actionPerformed(ActionEvent e) {
				   // Execute the private function
				   changeLandType();					   
			   }
			};

		this.comLandType.addActionListener(action);
	    this.add(this.comLandType);
	}
	
	private void changeLandType() {
		// Square must be referenced
		if(rSquareReference == null) {
			return;
		}
		// Change to selected type
		rSquareReference.setLandType((LandType)this.comLandType.getSelectedItem());
	}
}