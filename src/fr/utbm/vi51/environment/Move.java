package fr.utbm.vi51.environment;

import fr.utbm.vi51.util.Point3D;

/**
 * @author Top-K
 *
 */
public class Move implements Action {
    private Body body;
    private Direction direction;

    public Move(Body body, Direction direction) {
        this.direction = direction;
        this.body = body;
    }

    @Override
    public void doAction() {
        Point3D pos = body.getPosition();
        Point3D newPos = new Point3D(pos.x + direction.dx,
                pos.y + direction.dy, pos.z);
        Environment env = Environment.getInstance();
        Square[][][] map = env.getMap();
        map[pos.x][pos.y][pos.z].getObjects().remove(body);
        map[newPos.x][newPos.y][newPos.z].getObjects().add(body);
        env.setMap(map);
        body.setPosition(newPos);
        //Logger.getLogger(MobileObject.class.getName()).info("Moving");
    }

    @Override
    public boolean testAction() {
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
