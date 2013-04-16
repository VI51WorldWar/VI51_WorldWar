package fr.utbm.vi51.environment;

import java.util.LinkedList;
import java.util.List;

public class Square {
	private List<WorldObject> objects;
	private final LandType landType;

	public Square(LandType landType) {
		this.objects = new LinkedList<WorldObject>();
		this.landType = landType;
	}
	
	public void addObject(WorldObject obj) {
		this.objects.add(obj);
	}
	
	public boolean removeObject(WorldObject obj) {
		return this.objects.remove(obj);
	}
}
