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

public class CentrelizedLRTARealTimeSearchManager extends AbstractRealTimeSearchManager {
    CentrelizedLRTA centrelizedLRTA;
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
            goal[i] =vals.getValue();
            start[i] =agent.getCurrent();
            i++;
        }
        CentrelizedLRTAState goalState = new CentrelizedLRTAState(goal);
        CentrelizedLRTAState startState = new CentrelizedLRTAState(start);
        CentrelizedHeuristics.getInstance().setGoal(goalState);
        List<CentrelizedLRTAState> states = this.centrelizedLRTA.calculatePrefixes(startState,goalState,problem.getNumberOfNodeToDevelop());
        Agent agent;


        for (int k=0;k<states.size();k++) {
            for (int j=0;j<startGoal.size();j++) {
                agent = order[j];
                prefixes.get(agent).add(states.get(k).getLocationAt(j));
            }
        }
        this.prefixesForAgents = prefixes;

    }
}
