package fr.utbm.vi51.gui;

import javax.swing.JProgressBar;

import fr.utbm.vi51.configs.Consts;
import fr.utbm.vi51.environment.Pheromone;

/**
 * @author Top-K
 *
 */
public class PheromoneBar extends JProgressBar {
    public static final int HEIGHT = 20;
    public static final int WIDTH = 100;

    private static final long serialVersionUID = -6053057288381986971L;


    // Reference to the pheromone to track
    private Pheromone rPheromone;

    public PheromoneBar(Pheromone rPheromone) {
        assert rPheromone != null;
        this.rPheromone = rPheromone;
        setMaximum(100);
        setMinimum(0);
        setStringPainted(true);
        setString(rPheromone.getMessage().toString()
                + " " + this.rPheromone.getSide().getId()); //$NON-NLS-1$
        setBounds(0, 0, PheromoneBar.WIDTH, PheromoneBar.HEIGHT);
        setForeground(rPheromone.getMessage().color);
        setBackground(this.rPheromone.getSide().getDominantColor());
    }

    public boolean isEnded() {
        if (this.rPheromone == null) {
            return true;
        }
        return this.rPheromone.getStrength() < 0;
    }

    public void update(int index) {
        setBounds(200, 15 + index * PheromoneBar.HEIGHT, PheromoneBar.WIDTH,
                PheromoneBar.HEIGHT);
        if (this.rPheromone == null) {
            return;
        }
        // Compute a value between 0 and 100
        double percentRemaining = 1
                - (Consts.STARTINGPHEROMONEVALUE - (double) this.rPheromone
                        .getStrength()) / Consts.STARTINGPHEROMONEVALUE;
        int value = (int) Math.round(percentRemaining * 100);
        // Set value of the progress
        setValue(value);
    }

    public boolean isAttachedTo(Pheromone rPheromone1) {
        if (this.rPheromone == rPheromone1) {
            return true;
        }
        return false;
    }
}
