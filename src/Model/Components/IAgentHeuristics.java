package Model.Components;

/**
 * This interface represents a generic heuristics for a single agent
 */
public interface IAgentHeuristics {

    /**
     * This function will return the heuristics of the node
     * @param node - The node
     * @return - The heuristics of the node
     */
    public double getHeuristics(Node node);


    /**
     * This function will update the heuristics of the node with the given value
     * @param node - The given node
     * @param newVal - The new heuristic value for the node
     */
    public void updateHeuristics(Node node, double newVal);

    /**
     * This function will return the heuristic value of a given node
     * From the heuristic function
     * @param n - The given node n
     * @return - The heuristic value of a given node
     */
    public double getHeuristicsFromFunction(Node n);
    /**
     * This function will return the heuristic value of the node from this agent's perspective.
     * @param n - The given node
     * @return - The heuristic value
     */
    public double getInitialHeuristicValue(Node n);
}
