package Model.Heuristics;

import Model.Components.Node;

import java.util.HashMap;
import java.util.Map;

public class CollectiveHeuristicsManager implements IAgentHeuristics {

    private IAgentHeuristics heuristics;
    private Map<Integer,Map<Integer,Double>> costs;
    private static CollectiveHeuristicsManager collectiveHeuristicsManeger;


    private CollectiveHeuristicsManager()
    {
        System.out.println("Collective");
        heuristics =HeuristicFactory.getInstance().getAgentHeuristics(1,null);
        costs = new HashMap<>();
    }
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
