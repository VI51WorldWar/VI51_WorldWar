package fr.utbm.vi51.environment;

public class Pheromone extends WorldObject {
	private Message mess;
	private Direction dir;
	private int strength; //Represents distance and amount of food/danger. Weakens with time
}
