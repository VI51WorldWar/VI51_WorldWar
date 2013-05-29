package fr.utbm.vi51.gui;

import java.awt.Color;

import javax.swing.JProgressBar;

import fr.utbm.vi51.configs.Consts;
import fr.utbm.vi51.environment.Pheromone;

public class PheromoneBar extends JProgressBar{
	static final public int height = 20;
	static final public int width = 100;
	
	// Reference to the pheromone to track
	private Pheromone 		rPheromone = null;
	
	public PheromoneBar(Pheromone rPheromone) {
		assert(rPheromone != null);
		this.rPheromone = rPheromone;
		setMaximum(100);
		setMinimum(0);
		setStringPainted(true);
		setString(rPheromone.getMessage().toString() + " " + this.rPheromone.getSide().getId());
		setBounds(0, 0, PheromoneBar.width,PheromoneBar.height);
		Color color = null;
		switch(rPheromone.getMessage()) {
			case DANGER : 	color = new Color(255,0,0); break;
			case FOOD :		color = new Color(0,255,0); break;
			case HOME :		color = new Color(0,0,255); break;
		}
		setBackground(color);
	}
	
	public boolean isEnded() {
		if(rPheromone == null) {
			return true;
		}
		return (rPheromone.getStrength() < 0);
	}
	
	public void update(int index) {
		setBounds(200,15 + index*PheromoneBar.height, PheromoneBar.width,PheromoneBar.height);
		if(rPheromone == null) {
			System.out.println("Erreur");
		}
		// Compute a value between 0 and 100
		double percentRemaining = 1 -  (Consts.STARTINGPHEROMONEVALUE - (double) this.rPheromone.getStrength() ) / Consts.STARTINGPHEROMONEVALUE ;
		int value = (int)Math.round(percentRemaining*100);
		// Set value of the progress
		setValue(value);
	}
	
	public boolean isAttachedTo(Pheromone rPheromone) {
		if(this.rPheromone == rPheromone) {
			return true;
		}
		return false;
	}
}
