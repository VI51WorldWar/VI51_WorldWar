package fr.utbm.vi51.environment;

import fr.utbm.vi51.util.Point3D;

/**
 * @author Top-K
 *
 */
public class Move extends Action {
    private Body body;
    private Direction direction;

    public Move(Body body, Direction direction) {
        this.direction = direction;
        this.body = body;
    }

    @Override
    protected void doAction() {
        //System.out.println("Moving to direction : " + direction);
        Environment env = Environment.getInstance();
        Square[][][] map = env.getMap();
        //System.out.println("Moving to direction : " + direction);
        Point3D pos = body.getPosition();
        Point3D newPos = new Point3D(pos.x + direction.dx,
                pos.y + direction.dy, pos.z);

        // Cas d'une cave
        if (map[newPos.x][newPos.y][newPos.z].getLandType() == LandType.CAVE) {
            if (pos.z == env.getMapDepth() - 1) {
                System.out.println("Erreur de placement d'une cave");
            } else {
                // On descend d'un niveau
                newPos.z += 1;
                newPos.z = Math.min(newPos.z, env.getMapDepth() - 1);
            }
        }
        // Cas d'une remontée
        else if (map[newPos.x][newPos.y][newPos.z].getLandType() == LandType.STAIR) {
            if (pos.z == 0) {
                System.out.println("Erreur de placement d'une remontée");
            } else {
                // On remonte d'un niveau
                newPos.z -= 1;
                newPos.z = Math.max(newPos.z, 0);
            }
        }
        //System.out.println(newPos);
        map[pos.x][pos.y][pos.z].getObjects().remove(body);
        map[newPos.x][newPos.y][newPos.z].getObjects().add(body);
        body.setPosition(newPos);
        //Logger.getLogger(MobileObject.class.getName()).info("Moving");
    }

    @Override
    protected boolean testAction() {
        Square[][][] map = Environment.getInstance().getMap();
        Point3D pos = body.getPosition();

        Point3D newPos = new Point3D(pos.x + direction.dx,
                pos.y + direction.dy, pos.z);
        if (newPos.x < 0 || newPos.x >= map.length || newPos.y < 0
                || newPos.y >= map[0].length) {
            // System.out.println("False test");
            return false;
        } else {
            return map[newPos.x][newPos.y][newPos.z].getLandType()
                    .isCrossable();
        }
    }

    public Direction getDirection() {
        return direction;
    }
}
