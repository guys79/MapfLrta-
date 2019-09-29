package Model.Algorithms.BudgetPolicy;

import Model.Components.Agent;
import Model.Components.Node;
import javafx.util.Pair;

import java.util.Map;
import java.util.Set;

/**
 * This interface represents a generic budgetPolicy
 */
public interface IBudgetPolicy {

    /**
     * This function will return a budget for agent
     * @param agentsAndStartGoalNodes - The agents starts and goals
     * @param totalBudget - The total budget
     * @return - A dictionary describing budget per agent
     */
    public Map<Agent,Integer> getBudgetMap(Map<Agent,Pair<Node,Node>> agentsAndStartGoalNodes,int totalBudget);
}
