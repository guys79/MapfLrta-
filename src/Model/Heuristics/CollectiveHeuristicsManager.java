package Model.Heuristics;

import Model.Components.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the collective heuristics manager
 * This class implements the Singleton DP
 */
public class CollectiveHeuristicsManager implements IAgentHeuristics {

    private IAgentHeuristics heuristics;//The heuristics method for one agent
    private Map<Integer,Map<Integer,Double>> costs;//The database
    private static CollectiveHeuristicsManager collectiveHeuristicsManeger;//The singleton

    /**
     * The constructor
     */
    private CollectiveHeuristicsManager()
    {
        System.out.println("Collective");
        heuristics =HeuristicFactory.getInstance().getAgentHeuristics(1,null);
        costs = new HashMap<>();
    }

    /**
     * This function will return the instance (the singleton) of the class
     * @return - The instance of this class
     */
    public static CollectiveHeuristicsManager getInstance()
    {
        if(collectiveHeuristicsManeger == null)
            collectiveHeuristicsManeger = new CollectiveHeuristicsManager();
        return collectiveHeuristicsManeger;
    }


    @Override
    public double getHeuristics(Node node,Node goal) {
        double res;
        //check if in memory
        if(this.costs.containsKey(node.getId()))
        {
            Map<Integer,Double> map = costs.get(node.getId());
            if(map.containsKey(goal.getId()))
            {
                res = map.get(goal.getId());
          //      System.out.println(res);
                return res;
            }
        }

        //If not in memory
        res = getHeuristicsForTwoNodes(node,goal);
        //System.out.println(res);
        return res;
    }


    /**
     * This function will calculate the heuristics using mathematical function (not memory)
     * considering the target node as the goal node.
     * @param origin - The origin node
     * @param target - The target node
     * @return - The heuristics using mathematical function (not memory)
     */
    private double getHeuristicsForTwoNodes(Node origin, Node target) {

        return heuristics.getHeuristics(origin, target);
    }

    @Override
    public void updateHeuristics(Node node,Node goal, double newVal){
        Map<Integer,Double> map;
        if(!this.costs.containsKey(node.getId()))
        {
            map = new HashMap<>();
            this.costs.put(node.getId(),map);
        }
        map = costs.get(node.getId());
        map.put(goal.getId(),newVal);
    }


    @Override
    public double getInitialHeuristicValue(Node n,Node goal) {
        return heuristics.getInitialHeuristicValue(n,goal);
    }
}
