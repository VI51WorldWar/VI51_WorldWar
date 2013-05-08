package fr.utbm.vi51.environment;

import fr.utbm.vi51.util.Point3D;

/**
 * @author Top-K
 *
 */
public class Pheromone extends WorldObject {
    private Message mess;
    private Direction dir;
    private int strength; // Represents distance and amount of food/danger.
                          // Weakens with time

    public Pheromone(Point3D position, String texture) {
        super(position, texture);
    }

}
