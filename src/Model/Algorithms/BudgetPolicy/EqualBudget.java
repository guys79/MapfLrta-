package Model.Algorithms.BudgetPolicy;

import Model.Components.Agent;
import Model.Components.Node;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a budget policy where every agent gets the same budget
 */
public class EqualBudget implements IBudgetPolicy {
    @Override
    public Map<Agent, Integer> getBudgetMap(Map<Agent, Pair<Node, Node>> agentsAndStartGoalNodes, int totalBudget) {
        Map<Agent,Integer> budget = new HashMap<>();
        Set<Agent> agents = agentsAndStartGoalNodes.keySet();
        int budgetVal = totalBudget/agents.size();
        for(Agent agent : agents)
        {
            budget.put(agent,budgetVal);
        }
        return budget;

    }
}
