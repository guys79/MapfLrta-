package Model.Heuristics;

import Model.Components.Node;

/**
 * This interface represents a generic heuristics for a single agent
 */
public interface IAgentHeuristics {

    /**
     * This function will return the heuristics of the node
     * @param node - The node
     * @return - The heuristics of the node
     */
    public double getHeuristics(Node node,Node target);

    /**
     * This function will update the heuristics of the node with the given value
     * @param origin  - The origin node
     * @param goal - The goal node
     * @param newVal - The new heuristic value for the node
     */
    public void updateHeuristics(Node origin, Node goal,double newVal);


    /**
     * This function will return the heuristic value of the node from this agent's perspective.
     * @param n - The given node
     * @return - The heuristic value
     */
    public double getInitialHeuristicValue(Node n, Node goal);
}
