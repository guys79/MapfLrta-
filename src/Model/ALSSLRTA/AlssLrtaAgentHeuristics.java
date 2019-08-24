package Model.ALSSLRTA;

import Model.GridNode;
import Model.IAgentHeuristics;
import Model.Node;
import java.util.HashMap;
import java.util.Map;

/**
 * This class describes a node that participates in the
 */
public class AlssLrtaAgentHeuristics implements IAgentHeuristics {
    private Map<Node,Double> localHeuristics;//Key - nodeId, value - heuristic value
    private Node goal;//The goal node

    /**
     * The constructor
     * @param goal - The goal node
     */
    public AlssLrtaAgentHeuristics(Node goal)
    {
        localHeuristics = new HashMap<>();
        this.goal = goal;
        localHeuristics.put(goal,0d);
    }

    /**
     * This function will return the heuristics of the node
     * @param node - The node
     * @return - The heuristics of the node
     */
    @Override
    public double getHeuristics(Node node) {

        if(localHeuristics.containsKey(node))
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
        this.localHeuristics.put(node,newVal);
    }

    /**
     * This function will return the heuristic value of a given node
     * From the local memory of the agent
     * @param n - The given node n
     * @return - The heuristic value of a given node
     */
    private double getHeuristicsFromMemory(Node n)
    {
        return this.localHeuristics.get(n);
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
            value = Math.sqrt(Math.pow(gd1.getX()-gd2.getX(),2)+Math.pow(gd1.getY()-gd2.getY(),2));
        }
        else//In case the node is not GridNode, in our case it is unexpected
        {
            System.out.println("Fuck");
        }
        return value;
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

    public Node getGoal() {
        return goal;
    }
}
