package Model.Algorithms.BudgetPolicy;

import Model.Components.Agent;
import Model.Components.Node;
import Model.Components.Problem;
import javafx.util.Pair;

import java.util.Map;
import java.util.Set;

/**
 * This interface represents a generic budgetPolicy
 */
public interface IBudgetPolicy {

    /**
     * This function will return a budget for agent
     * @param problem - the given problem
     * @return - A dictionary describing budget per agent
     */
    public Map<Agent,Integer> getBudgetMap(Problem problem);
}
