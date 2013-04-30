package fr.utbm.vi51.environment;

import java.awt.Image;

import javax.vecmath.Point3d;

/**
 * @author Top-K
 *
 */
public class Pheromone extends WorldObject {
    private Message mess;
    private Direction dir;
    private int strength; // Represents distance and amount of food/danger.
                          // Weakens with time

    public Pheromone(Point3d position, Image texture) {
        super(position, texture);
    }

}
