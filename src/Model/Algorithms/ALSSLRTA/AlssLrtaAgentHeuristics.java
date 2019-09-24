package Model.Algorithms.ALSSLRTA;

import Model.Components.GridNode;
import Model.Components.IAgentHeuristics;
import Model.Components.Node;
import java.util.HashMap;
import java.util.Map;

/**
 * This class describes a node that participates in the
 */
public class AlssLrtaAgentHeuristics implements IAgentHeuristics {
    private Map<Integer,Double> localHeuristics;//Key - nodeId, value - heuristic value
    protected Node goal;//The goal node

    /**
     * The constructor
     * @param goal - The goal node
     */
    public AlssLrtaAgentHeuristics(Node goal)
    {
        localHeuristics = new HashMap<>();
        this.goal = goal;
        localHeuristics.put(goal.getId(),0d);
    }

    /**
     * This function will return the heuristics of the node
     * @param node - The node
     * @return - The heuristics of the node
     */
    @Override
    public double getHeuristics(Node node) {

        if(localHeuristics.containsKey(node.getId()))
            return getHeuristicsFromMemory(node);
        return getHeuristicsFromFunction(node);
    }


    /**
     * This function will update the heuristics of the node with the given value
     * @param node - The given node
     * @param newVal - The new heuristic value for the node
     */
    @Override
    public void updateHeuristics(Node node, double newVal) {
        this.localHeuristics.put(node.getId(),newVal);
    }

    /**
     * This function will return the heuristic value of a given node
     * From the local memory of the agent
     * @param n - The given node n
     * @return - The heuristic value of a given node
     */
    private double getHeuristicsFromMemory(Node n)
    {
        return this.localHeuristics.get(n.getId());
    }


    /**
     * This function will return the heuristic value of a given node
     * From the heuristic function (in this case, Manheten Distance)
     * @param n - The given node n
     * @return - The heuristic value of a given node
     */
    public double getHeuristicsFromFunction(Node n)
    {

        double value = 0;
        if(n instanceof GridNode && goal instanceof GridNode) {
            GridNode gd1 = (GridNode) n;
            GridNode gd2 = (GridNode) goal;
            double D=1;
            double D2 = Math.sqrt(2);
            double dx = Math.abs(gd1.getX() - gd2.getX());
            double dy = Math.abs(gd1.getY() - gd2.getY());

            value = D * (dx + dy) + (D2 - 2 * D) * Math.min(dx, dy);

        }
        else//In case the node is not GridNode, in our case it is unexpected
        {
            System.out.println("Fuck");
        }
        return value;
    }

    @Override
    public double getInitialHeuristicValue(Node n) {
        return getHeuristicsFromFunction(n);
    }

    /**
     * This function will check whether the given node's heuristic value has been updated
     * @param node - The given node
     * @return - True IFF the node's heuristic value has been updated
     */
    public boolean checkIfUpdated(Node node)
    {
        return localHeuristics.containsKey(node);
    }

    /**
     * This function will return the goal state of the agent
     * @return - The goal state
     */
    public Node getGoal() {
        return goal;
    }
}
