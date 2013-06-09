package fr.utbm.vi51.gui;

import javax.swing.JProgressBar;

import fr.utbm.vi51.configs.Consts;
import fr.utbm.vi51.environment.Pheromone;

public class PheromoneBar extends JProgressBar{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6053057288381986971L;
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
		setString(rPheromone.getMessage().toString() + " " + this.rPheromone.getSide().getId()); //$NON-NLS-1$
		setBounds(0, 0, PheromoneBar.width,PheromoneBar.height);
		setForeground(rPheromone.getMessage().color);
		setBackground(this.rPheromone.getSide().getDominantColor());
	}
	
	public boolean isEnded() {
		if(this.rPheromone == null) {
			return true;
		}
		return (this.rPheromone.getStrength() < 0);
	}
	
	public void update(int index) {
		setBounds(200,15 + index*PheromoneBar.height, PheromoneBar.width,PheromoneBar.height);
		if(this.rPheromone == null) {
			return;
		}
		// Compute a value between 0 and 100
		double percentRemaining = 1 -  (Consts.STARTINGPHEROMONEVALUE - (double) this.rPheromone.getStrength() ) / Consts.STARTINGPHEROMONEVALUE ;
		int value = (int)Math.round(percentRemaining*100);
		// Set value of the progress
		setValue(value);
	}
	
	public boolean isAttachedTo(Pheromone rPheromone1) {
		if(this.rPheromone == rPheromone1) {
			return true;
		}
		return false;
	}
}
