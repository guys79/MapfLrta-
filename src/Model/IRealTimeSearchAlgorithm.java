package Model;

import java.util.List;

/**
 * This interface represents a RTS algorithm
 */
public interface IRealTimeSearchAlgorithm {

    /**
     * This function will calculate a prefix from the start node to the goal node path
     * @param start - The start node
     * @param goal - The goal node
     * @param numOfNodesToDevelop - The number of nodes to develop in the search
     * @param agent - The agent
     * @return - A prefix from the start node to the goal node path
     */
    public List<Node> calculatePrefix(Node start,Node goal, int numOfNodesToDevelop, Agent agent);
}
