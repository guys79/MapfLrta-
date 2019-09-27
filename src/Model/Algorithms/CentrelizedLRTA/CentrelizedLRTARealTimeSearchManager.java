package Model.Algorithms.CentrelizedLRTA;

import Model.Components.AbstractRealTimeSearchManager;
import Model.Components.Agent;
import Model.Components.Node;
import Model.Components.Problem;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a centralized Lrta* manager
 */
public class CentrelizedLRTARealTimeSearchManager extends AbstractRealTimeSearchManager {
    CentrelizedLRTA centrelizedLRTA;//The algorithm
    /**
     * The constructor of the class
     *
     * @param problem - The given problem
     */
    public CentrelizedLRTARealTimeSearchManager(Problem problem) {
        super(problem);
        this.centrelizedLRTA = new CentrelizedLRTA();
    }

    @Override
    protected void calculatePrefix() {
        Map<Agent, Pair<Node,Node>> startGoal = problem.getAgentsAndStartGoalNodes();
        Node[] goal = new Node[startGoal.size()];
        Node[] start = new Node[startGoal.size()];
        Agent [] order = new Agent[startGoal.size()];
        int i=0;
        Map<Agent,List<Node>>prefixes = new HashMap<>();
        for(Agent agent: startGoal.keySet())
        {
            prefixes.put(agent,new ArrayList<>());
            Pair<Node,Node> vals = startGoal.get(agent);
            order[i] = agent;
            goal[i] =vals.getValue();
            start[i] =agent.getCurrent();
            i++;
        }
        CentrelizedLRTAState goalState = new CentrelizedLRTAState(goal,Integer.MAX_VALUE,null);
        CentrelizedLRTAState startState = new CentrelizedLRTAState(start,0,goalState);
        System.out.println(startState.getId());
        CentrelizedHeuristics.getInstance().setGoal(goalState);
        List<CentrelizedLRTAState> states = this.centrelizedLRTA.calculatePrefixes(startState,goalState,problem.getNumberOfNodeToDevelop()*startGoal.size());
        Agent agent;

        if(states.get(states.size()-1).equals(goalState))
        {
            for(Agent agent2: order)
                agent2.done();
        }
        for (int k=0;k<states.size();k++) {
            for (int j=0;j<startGoal.size();j++) {
                agent = order[j];
                prefixes.get(agent).add(states.get(k).getLocationAt(j));
       //         System.out.print(states.get(k).getLocationAt(j).getId()+",");
            }
       //     System.out.println(states.get(k).getId());
        }
        this.prefixesForAgents = prefixes;
        for (int j=0;j<order.length;j++) {
            agent = order[j];
            //Adding the prefix to the path
            List<Node> path = pathsForAgents.get(agent);
            path.remove(path.size()-1);
            path.addAll(prefixes.get(agent));
        }



    }
}
