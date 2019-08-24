package Model.ALSSLRTA;

import Model.Node;

/**
 * This class represents a node that participates in the aLSS-LRTA* search
 */
public class AlssLrtaSearchNode {
    private Node node;
    private double gValue;
    private AlssLrtaSearchNode back;
    private boolean updated;

    public AlssLrtaSearchNode(Node node)
    {
        this.node = node;
        gValue = Double.MAX_VALUE;
        back = null;
        updated = false;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AlssLrtaSearchNode)
        {
            return ((AlssLrtaSearchNode)obj).node.equals(node);
        }
        return false;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public boolean isUpdated() {
        return updated;
    }

    @Override
    public int hashCode() {
        return node.hashCode();
    }

    public void setG(double g)
    {
        this.gValue = g;
    }

    public double getG() {
        return gValue;
    }

    public Node getNode() {
        return node;
    }

    public void setBack(AlssLrtaSearchNode back) {
        this.back = back;
    }

    public AlssLrtaSearchNode getBack() {
        return back;
    }

    @Override
    public String toString() {
        return node.toString();
    }

}


