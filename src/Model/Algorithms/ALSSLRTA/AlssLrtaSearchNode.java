package Model.Algorithms.ALSSLRTA;

import Model.Node;

/**
 * This class represents a node that participates in the aLSS-LRTA* search
 */
public class AlssLrtaSearchNode {
    private Node node;//The actual node
    private double gValue;//The g value of the node
    private AlssLrtaSearchNode back;//The predecessor
    private boolean updated;//True IFF the node's heuristics were updated




    /**
     * The constructor of the class
     * @param node - The node that this class represents
     */
    public AlssLrtaSearchNode(Node node)
    {
        this.node = node;
        gValue = Double.MAX_VALUE;
        back = null;
        updated = false;

    }

    /**
     * This function will change the 'updated' status of the node
     * @param updated - The new status value
     */
    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    /**
     * This function will check if the node's heuristics have been updated
     * @return - True IFF the node's heuristics were updated
     */
    public boolean isUpdated() {
        return updated;
    }

    /**
     * This function will set the g value of the node
     * @param g - The new g value

     */
    public void setG(double g)
    {
        this.gValue = g;
    }

    /**
     * This function will return the g value of this node
     * @return - The gValue of the node
     */
    public double getG() {

        return gValue;
    }

    /**
     * This function will return the Node object that this class represents
     * @return - The node object
     */
    public Node getNode() {
        return node;
    }

    /**
     * This function will set the predecessor of the node in the solution path
     * @param back - The predecessor of the node
     */
    public void setBack(AlssLrtaSearchNode back) {
        this.back = back;
    }

    /**
     * This function will return the predecessor of the node in the solution path
     * @return - The predecessor of the node in the solution path
     */
    public AlssLrtaSearchNode getBack() {
        return back;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AlssLrtaSearchNode)
        {
            return ((AlssLrtaSearchNode)obj).node.equals(node);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return node.hashCode();
    }
    @Override
    public String toString() {
        return node.toString();
    }

}


