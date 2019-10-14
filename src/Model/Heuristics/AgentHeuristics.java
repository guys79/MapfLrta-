package Model.Heuristics;

import Model.Components.GridNode;
import Model.Heuristics.IAgentHeuristics;
import Model.Components.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * This class handles the agent's individual heuristic estimations
 */
public class AgentHeuristics implements IAgentHeuristics {


    private Map<Node,Double> localHeuristics;//Key - nodeId, value - heuristic value
    private Node goal;//The goal node

    /**
     * The constructor
     * @param goal - The goal node
     */
    public AgentHeuristics(Node goal)
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
    public double getHeuristics(Node node,Node goal) {

        if(this.goal!=null) {
            if (localHeuristics.containsKey(node))
                return getHeuristicsFromMemory(node);
            return getHeuristicsFromFunction(node);
        }
        return getHeuristicsForTwoFromFunction(node,goal);
    }



    @Override
    public void updateHeuristics(Node node,Node goal, double newVal) {
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
     * This function will calculate the heuristics using mathematical function (not memory)
     * @param n - The node n
     * @return - The  heuristic value of node n
     */
    private double getHeuristicsFromFunction(Node n)
    {
        //Manheten Distance
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
     * This function will calculate the heuristics using mathematical function (not memory)
     * considering the target node as the goal node.
     * @param origin - The origin node
     * @param target - The target node
     * @return - The heuristics using mathematical function (not memory)
     */
    private double getHeuristicsForTwoFromFunction(Node origin,Node target)
    {
        //Manheten Distance
        double value = 0;
        if(origin instanceof GridNode && target instanceof GridNode) {
            GridNode gd1 = (GridNode) origin;
            GridNode gd2 = (GridNode) target;
            value = Math.sqrt(Math.pow(gd1.getX()-gd2.getX(),2)+Math.pow(gd1.getY()-gd2.getY(),2));
        }
        else//In case the node is not GridNode, in our case it is unexpected
        {
            System.out.println("Fuck");
        }
        return value;
    }


    @Override
    public double getInitialHeuristicValue(Node n,Node goal) {

        return getHeuristicsForTwoFromFunction(n, goal);
    }
}

