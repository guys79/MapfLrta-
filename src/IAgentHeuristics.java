/**
 * This interface represents a generic heuristics for a single agent
 */
public interface IAgentHeuristics {

    /**
     * This function will return the heuristics of the node with the given id
     * @param id - The Node's id
     * @return - The heuristics of the node
     */
    public double getHeuristics(int id);


    /**
     * This function will update the heuristics of the node with the given id with the given value
     * @param id - The Node's id
     *
     */
    public void updateHeuristics(int id, double newVal);
}
