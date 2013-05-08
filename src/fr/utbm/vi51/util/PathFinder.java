package fr.utbm.vi51.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import fr.utbm.vi51.environment.Square;

/**
 * @author Top-K
 * 
 */
public final class PathFinder {
    private PathFinder() {

    }

    /**
     * @author Top-K
     * 
     */
    private static class Node {
        private Point3D position;
        private int currentCost;
        private int heuristicCost;
        private Node previousNode;

        public Node(Point3D position, int currentCost, int heuristicCost,
                Node previousNode) {
            super();
            this.position = position;
            this.currentCost = currentCost;
            this.heuristicCost = heuristicCost;
            this.previousNode = previousNode;
        }

        public Node getPreviousNode() {
            return previousNode;
        }

        public Point3D getPosition() {
            return position;
        }

        public int getCurrentCost() {
            return currentCost;
        }

        public int getHeuristicCost() {
            return heuristicCost;
        }

        public boolean equals(Node n) {
            return position.equals(n.getPosition());
        }

        public int hashCode() {
            return position.x * 1000 + position.y;
        }

        public String toString() {
            return "{" + position.toString() + "," + "c:" + currentCost + "h:"
                    + heuristicCost + "}";
        }
    }

    /**
     * @author Top-K
     *
     */
    private static class NodeComparator implements Comparator<Node> {

        @Override
        public int compare(Node a, Node b) {
            return Integer.compare(a.getCurrentCost() + a.getHeuristicCost(),
                    b.getCurrentCost() + b.getHeuristicCost());
        }

    }

    public static LinkedList<Point3D> findPath(Point3D start, Point3D goal,
            Square[][][] map) {
        ArrayList<Node> closedList = new ArrayList<Node>();
        ArrayList<Node> openList = new ArrayList<Node>();
        openList.add(new Node(start, 0, computeHeuristicCost(start, goal), null));

        NodeComparator comparator = new NodeComparator();

        Node current;
        while (!openList.isEmpty()) {
            Collections.sort(openList, comparator);
            current = openList.get(0);

            if (current.getPosition().equals(goal)) {
                return buildPath(current);
            }

            for (Node n : getNeighboors(current, goal, map)) {
                Node olderNode;
                if ((olderNode = getNodeWithSamePosition(closedList, n)) != null) {
                    if (n.getCurrentCost() >= olderNode.getCurrentCost()) {
                        continue;
                    }
                    closedList.remove(olderNode);
                } else  if ((olderNode = getNodeWithSamePosition(openList, n)) != null) {
                    olderNode = getNodeWithSamePosition(openList, n);
                    if (n.getCurrentCost() >= olderNode.getCurrentCost()) {
                        continue;
                    }
                } else {
                    openList.add(n);
                }
            }

            openList.remove(current);
            closedList.add(current);
        }
        return null;

    }

    private static List<Node> getNeighboors(Node origin, Point3D goal,
            Square[][][] map) {
        LinkedList<Node> neighboors = new LinkedList<Node>();
        if (origin.getPosition().x > 0) {
            Point3D newPos = new Point3D(origin.getPosition().x - 1,
                    origin.getPosition().y, origin.getPosition().z);
            if (map[newPos.x][newPos.y][newPos.z].getLandType().isCrossable()) {
                neighboors.add(new Node(newPos, origin.getCurrentCost()
                        + map[newPos.x][newPos.y][newPos.z].getLandType()
                                .getCost(), computeHeuristicCost(newPos, goal),
                        origin));
            }
        }

        if (origin.getPosition().x < map.length - 2) {
            Point3D newPos = new Point3D(origin.getPosition().x + 1,
                    origin.getPosition().y, origin.getPosition().z);
            if (map[newPos.x][newPos.y][newPos.z].getLandType().isCrossable()) {
                neighboors.add(new Node(newPos, origin.getCurrentCost()
                        + map[newPos.x][newPos.y][newPos.z].getLandType()
                                .getCost(), computeHeuristicCost(newPos, goal),
                        origin));
            }
        }
        if (origin.getPosition().y > 0) {
            Point3D newPos = new Point3D(origin.getPosition().x,
                    origin.getPosition().y - 1, origin.getPosition().z);
            if (map[newPos.x][newPos.y][newPos.z].getLandType().isCrossable()) {
                neighboors.add(new Node(newPos, origin.getCurrentCost()
                        + map[newPos.x][newPos.y][newPos.z].getLandType()
                                .getCost(), computeHeuristicCost(newPos, goal),
                        origin));
            }
        }

        if (origin.getPosition().y < map[0].length - 2) {
            Point3D newPos = new Point3D(origin.getPosition().x,
                    origin.getPosition().y + 1, origin.getPosition().z);
            if (map[newPos.x][newPos.y][newPos.z].getLandType().isCrossable()) {
                neighboors.add(new Node(newPos, origin.getCurrentCost()
                        + map[newPos.x][newPos.y][newPos.z].getLandType()
                                .getCost(), computeHeuristicCost(newPos, goal),
                        origin));
            }
        }
        return neighboors;
    }

    private static int computeHeuristicCost(Point3D position, Point3D goal) {
        return (int) Math.floor(Math.sqrt(Math.pow(goal.x - position.x, 2)
                + Math.pow(goal.y - position.y, 2)));
    }

    private static LinkedList<Point3D> buildPath(Node goal) {
        LinkedList<Point3D> path = new LinkedList<Point3D>();
        Node current = goal;
        while (current != null) {
            path.addFirst(current.getPosition());
            current = current.getPreviousNode();
        }
        return path;
    }

    private static Node getNodeWithSamePosition(Collection<Node> c, Node a) {
        for (Node b : c) {
            if (b.getPosition().equals(a.getPosition())) {
                return b;
            }
        }
        return null;
    }
    
}
