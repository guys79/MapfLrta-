package Model.Algorithms.LRTA;

import Model.Algorithms.ALSSLRTA.AlssLrtaAgentHeuristics;
import Model.Components.Node;
import Model.Algorithms.Dijkstra.ShortestPathGenerator;

/**
 * This class represents the heuristics of thew shortestPath
 */
public class ShortestPathAgentHeuristics extends AlssLrtaAgentHeuristics {
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
