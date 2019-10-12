package Model.Algorithms.BudgetPolicy;

import Model.Components.Agent;
import Model.Components.Node;
import Model.Components.Problem;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a budget policy where every agent gets the same budget
 */
public class EqualBudget implements IBudgetPolicy {

    @Override
    public Map<Agent, Integer> getBudgetMap(Problem problem ) {

        Map<Agent,Integer> budget = new HashMap<>();
        Set<Agent> agents = problem.getAgentsAndStartGoalNodes().keySet();
        int totalBudget = agents.size()*problem.getNumberOfNodeToDevelop();
        int budgetVal = totalBudget/agents.size();
        for(Agent agent : agents)
        {
            budget.put(agent,budgetVal);
        }
        return budget;
    }
}
