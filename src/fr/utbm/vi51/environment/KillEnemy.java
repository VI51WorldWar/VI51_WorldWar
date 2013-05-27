package fr.utbm.vi51.environment;

import java.util.List;

import fr.utbm.vi51.util.Point3D;

public class KillEnemy implements Action {
	private InsectBody body;
	
	public KillEnemy (InsectBody body) {
		super();
		this.body = body;
	}

	@Override
	public void doAction() {
		Point3D pos = body.getPosition();
		List<WorldObject> objects = Environment.getInstance().getMap()[pos.x][pos.y][pos.z].getObjects();
		
		for (WorldObject wo : objects) {
			if (wo.getTexturePath().equals("img/Ants/warrior.png") && ((InsectBody) wo).getSide() != body.getSide()) {
				InsectBody enemy = ((InsectBody) wo);
				if (enemy.getHealthPoints() <= 0) {
					enemy.die();
					break;
				}
			}
		}
		
	}

	@Override
	public boolean testAction() {
		Point3D pos = body.getPosition();
		for (WorldObject wo : Environment.getInstance().getMap()[pos.x][pos.y][pos.z].getObjects()) {
			if (wo.getTexturePath().equals("img/Ants/warrior.png") && ((InsectBody) wo).getSide() != body.getSide()) {
				return true;
			}
		}
		return false;
	}

}
