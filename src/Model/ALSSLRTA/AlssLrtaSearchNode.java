package Model.ALSSLRTA;

import Model.Node;

/**
 * This class represents a node that participates in the aLSS-LRTA* search
 */
public class AlssLrtaSearchNode {
    private Node node;
    private double gValue;

    public AlssLrtaSearchNode(Node node)
    {
        this.node = node;
        gValue = Double.MAX_VALUE;
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

    public void setG(double g)
    {
        this.gValue = g;
    }
}


