package fr.utbm.vi51.environment;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.vecmath.Point2d;

/**
 * @author Top-K
 *
 */
public class Square {
	
    private  CopyOnWriteArrayList<WorldObject> objects;
    
    private LandType landType;

    public Square(LandType landType) {
        this.objects = new  CopyOnWriteArrayList<WorldObject>();
        this.landType = landType;
    }

    public void addObject(WorldObject obj) {
        this.objects.add(obj);
    }

    public boolean removeObject(WorldObject obj) {
        return this.objects.remove(obj);
    }

    public LandType getLandType() {
        return landType;
    }
    
    public void setLandType(LandType landtype) {
    	this.landType = landtype;
    }

    public List<WorldObject> getObjects() {
            return objects;
    }

    public void setObjects( CopyOnWriteArrayList<WorldObject> objects) {
        this.objects = objects;
    }

    public String toString() {
        return landType.toString();
    }
}
