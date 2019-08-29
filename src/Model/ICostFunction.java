package Model;

/**
 * This interface represents a generic costFunction
 */
public interface ICostFunction {

    /**
     * This function will return the cost of the action to move from node 'origin' to node 'target'
     * @param origin - The origin node (start node)
     * @param target - The target node (goal node)
     * @return - The cost of moving from the ;origin' node to the 'target' node
     */
    public double getCost(Node origin,Node target);
}
