package fr.utbm.vi51.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import fr.utbm.vi51.environment.Direction;
import fr.utbm.vi51.environment.Square;

/**
 * @author Top-K Static class using A* to find paths
 */
public final class PathFinder {
    private PathFinder() {

    }

    /**
     * @author Top-K Defines a node as used in A*
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
     * @author Top-K Used to sort nodes using their total estimated cost
     */
    private static class NodeComparator implements Comparator<Node> {

        @Override
        public int compare(Node a, Node b) {
            return Integer.compare(a.getCurrentCost() + a.getHeuristicCost(),
                    b.getCurrentCost() + b.getHeuristicCost());
        }

    }

    /**
     * @param start
     *            Starting point of the path
     * @param goal
     *            Point you want to reach
     * @param map
     *            3D map, currently only 2 dimensions are used
     * @return A linked list of directions, moving along this list will get the
     *         object to the goal
     */
    public static LinkedList<Direction> findPath(Point3D start, Point3D goal,
            Square[][][] map) {
        ArrayList<Node> closedList = new ArrayList<Node>();
        ArrayList<Node> openList = new ArrayList<Node>();
        openList.add(new Node(start, 0, computeHeuristicCost(start, goal), null));

        NodeComparator comparator = new NodeComparator();

        Node current;
        while (!openList.isEmpty()) {
            Collections.sort(openList, comparator);
            current = openList.get(0); //Gets the best element of the open list

            //If the current node is the goal, path is found
            if (current.getPosition().equals(goal)) {
                return buildPath(current);
            }

            for (Node n : getNeighbors(current, goal, map)) {
                Node olderNode;
                if ((olderNode = getNodeWithSamePosition(closedList, n)) != null) {
                    if (n.getCurrentCost() >= olderNode.getCurrentCost()) {
                        continue;
                    }
                    closedList.remove(olderNode);
                } else if ((olderNode = getNodeWithSamePosition(openList, n)) != null) {
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

    /**
     * . Returns all 4 direct neighbors of the origin node
     * 
     * @param origin
     *            Point around which the neighbors will be searched
     * @param goal
     *            Goal of the A* algorithm, used to compute the heuristic costs
     * @param map
     *            3D map, currently only 2 dimensions are used
     * @return A list of 4 neighbors as nodes
     */
    private static List<Node> getNeighbors(Node origin, Point3D goal,
            Square[][][] map) {
        LinkedList<Node> neighboors = new LinkedList<Node>();

        //West neighbor
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

        //East neighbor
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

        //North neighbor
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

        //South neighbor
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

    /**
     * Estimate the movement cost between position and goal (currently euclidian
     * distance)
     * @param position
     * @param goal
     * @return The estimate cost
     */
    private static int computeHeuristicCost(Point3D position, Point3D goal) {
        return (int) Math.floor(Math.sqrt(Math.pow(goal.x - position.x, 2)
                + Math.pow(goal.y - position.y, 2)));
    }

    /**
     * Once A* is completed, gives the successive directions to follow to go
     * from start to goal
     * @param goal
     *            The node representing the goal position.
     * @return An ordered list of directions
     */
    private static LinkedList<Direction> buildPath(Node goal) {
        LinkedList<Direction> path = new LinkedList<Direction>();
        Node current = goal;
        while (current.getPreviousNode() != null) {
            path.addFirst(Direction.toDirection(current.getPreviousNode()
                    .getPosition(), current.getPosition()));
            current = current.getPreviousNode();
        }
        return path;
    }

    /**
     * Returns the first node in c that has the same postion as a
     * @param c
     *            The collection to search
     * @param a
     *            The reference node
     * @return The first found node
     */
    private static Node getNodeWithSamePosition(Collection<Node> c, Node a) {
        for (Node b : c) {
            if (b.getPosition().equals(a.getPosition())) {
                return b;
            }
        }
        return null;
    }

}
