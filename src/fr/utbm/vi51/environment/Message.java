package fr.utbm.vi51.environment;

import java.awt.Color;

/**
 * @author Theo
 *
 */
public enum Message {
    DANGER(Color.red), 
    FOOD(Color.yellow), 
    HOME(Color.blue);

    /**
     * Color to use for the pheromone message
     */
    public final Color color;

    /**
     * @param color
     */
    Message(Color color) {
        this.color = color;
    }
}
