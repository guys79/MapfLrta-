import javafx.util.Pair;

import java.util.Map;

/**
 * This class represents an abstract problem
 */
public abstract class AbstractProblem {

    private Map<Agent,Pair<Node,Node>> agentsAndStartGoalNodes;//A map from an agent to a pair of start/goal nodes

    /**
     * The constructor of the class
     * @param pathTOFIle - the given path to the problem instance
     * @param agentsAndStartGoalNodes - A map from an agent to a pair of start/goal nodes
     */
    public AbstractProblem(String pathTOFIle, Map<Agent,Pair<Node,Node>> agentsAndStartGoalNodes)
    {
        this.agentsAndStartGoalNodes = agentsAndStartGoalNodes;
        initializeMap(pathTOFIle);
    }
    /**
     * This function will initialize the graph using a path to ta file
     * @param pathTOFIle - the given path
     */
    private void initializeMap(String pathTOFIle)
    {

    }

    /**
     * This function will create a directed edge from the origin to the target wighting the given wight
     * @param origin - The origin node
     * @param target - The target node
     * @param weight - The weight of the edge that connects the two nodes
     */
    private void createDirectedEdge(Node origin, Node target, double weight)
    {
        origin.addNeighbor(target,weight);
    }

    /**
     * This function will create an undirected edge from the origin to the target wighting the given wight (both ways)
     * @param origin - The origin node
     * @param target - The target node
     * @param weight - The weight of the edge that connects the two nodes
     */
    private void createUndirectedEdge(Node origin, Node target, double weight)
    {
        origin.addNeighbor(target,weight);
        target.addNeighbor(origin,weight);
    }





}
