package Model.Algorithms.Dijkstra;

import Model.Node;

/**
 * This class represents a node in the Dijkstra search
 */
public class DijkstraSearchNode {
    private Node node;//The node
    private double distance;//The cheapest known cost for a path from this node to the goal node
    private DijkstraSearchNode predecessor ;//The predecessor of the node

    /**
     * The constructor
     * @param node - The given node
     */
    public DijkstraSearchNode(Node node)
    {
        this.distance = Double.MAX_VALUE;
        this.node = node;
        this.predecessor = null;
    }

    /**
     * This function will return the node
     * @return - The node
     */
    public Node getNode() {
        return node;
    }

    /**
     * This function will return the distance
     * @return - the distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * This function will return the predecessor of the node in the search
     * @return - The predecessor
     */
    public DijkstraSearchNode getPredecessor() {
        return predecessor;
    }

    /**
     * This function will set the distance of the node
     * @param distance - The given distance
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * This function will set the predecessor of the node
     * @param predecessor - The predecessor of the node
     */
    public void setPredecessor(DijkstraSearchNode predecessor) {
        this.predecessor = predecessor;
    }
}
