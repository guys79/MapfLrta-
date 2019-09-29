package Model.Algorithms.BudgetPolicy;

import Model.Components.Agent;
import Model.Components.Node;
import javafx.util.Pair;

import java.util.Map;
import java.util.Set;

public interface IBudgetPolicy {

    public Map<Agent,Integer> getBudgetMap(Map<Agent,Pair<Node,Node>> agentsAndStartGoalNodes,int totalBudget);
}
