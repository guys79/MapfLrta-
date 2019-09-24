package Model.Algorithms.Dijkstra;

import Model.Components.Node;

/**
 * This class represents a node in the Dijkstra search
 */
public class DijkstraSearchNode {
    private Node node;//The node
    private double distance;//The cheapest known cost for a path from this node to the goal node


    /**
     * The constructor
     * @param node - The given node
     */
    public DijkstraSearchNode(Node node)
    {
        this.distance = Double.MAX_VALUE;
        this.node = node;

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
     * This function will set the distance of the node
     * @param distance - The given distance
     */
    public void setDistance(Double distance) {
        if(distance == null)
        {
            this.distance = Double.MAX_VALUE;
            return;
        }
        this.distance = distance;
    }



    @Override
    public boolean equals(Object obj) {

        if(!(obj instanceof DijkstraSearchNode))
            return false;
        DijkstraSearchNode dijkstraSearchNode = (DijkstraSearchNode)obj;
        return dijkstraSearchNode.getNode().getId() == this.node.getId();
    }

    @Override
    public int hashCode() {
        return node.getId();
    }

}
