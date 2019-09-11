package Model.Algorithms.LRTA;

import Model.Node;
import Model.ShortestPathGenerator;

/**
 * This class represents the heuristics of thew shortestPath
 */
public class ShortestPathAgentHeuristics extends AgentHeuristics {
    /**
     * The constructor
     *
     * @param goal - The goal node
     */
    public ShortestPathAgentHeuristics(Node goal) {
        super(goal);
    }

    @Override
    public double getHeuristicsFromFunction(Node n) {
        return ShortestPathGenerator.getInstance().getShortestPath(n,goal);
    }
}
