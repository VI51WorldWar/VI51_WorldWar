package fr.utbm.vi51.environment;

import java.util.LinkedList;
import java.util.List;

public class Environment {

	private static Environment evt = null;

	private List<WorldObject> objects;
	private Square[][][] map;
	// width = x, height = y, depth = z
	private final int mapWidth, mapHeight, mapDepth;

	private Environment() {
		mapHeight = mapWidth = mapDepth = 500;
		map = new Square[mapWidth][mapHeight][mapDepth];
		objects = new LinkedList<WorldObject>();
	}

	public static Environment getInstance() {
		if (evt == null) {
			evt = new Environment();
		}

		return evt;
	}
}
