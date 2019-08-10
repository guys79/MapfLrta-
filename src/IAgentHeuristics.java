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
     *
     */
    public void updateHeuristics(Node node, double newVal);
}
