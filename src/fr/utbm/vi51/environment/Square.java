package fr.utbm.vi51.environment;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Theo
 *
 */
public class Square {
    private  CopyOnWriteArrayList<WorldObject> objects;

    private LandType landType;

    public Square(LandType landType) {
        this.objects = new  CopyOnWriteArrayList<>();
        this.landType = landType;
    }

    public void addObject(WorldObject obj) {
        this.objects.add(obj);
    }

    public boolean removeObject(WorldObject obj) {
        return this.objects.remove(obj);
    }

    public LandType getLandType() {
        return this.landType;
    }

    public void setLandType(LandType landtype) {
        this.landType = landtype;
    }

    public List<WorldObject> getObjects() {
            return this.objects;
    }

    public void setObjects(CopyOnWriteArrayList<WorldObject> objects) {
        this.objects = objects;
    }

    @Override
	public String toString() {
        return this.landType.toString();
    }
}
