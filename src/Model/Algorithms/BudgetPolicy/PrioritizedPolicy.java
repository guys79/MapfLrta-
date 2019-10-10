package Model.Algorithms.BudgetPolicy;

import Model.Algorithms.MAALSSLRTA.MAALSSLRTA;
import Model.Components.Agent;
import Model.Components.Node;
import javafx.util.Pair;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;

public class PrioritizedPolicy implements IBudgetPolicy  {
    @Override
    public Map<Agent, Integer> getBudgetMap(Map<Agent, Pair<Node, Node>> agentsAndStartGoalNodes, int totalBudget) {
        PriorityQueue<Agent> pAgents = new PriorityQueue<>(new MAALSSLRTA.CompareAgentsPriority());
        pAgents.addAll(agentsAndStartGoalNodes.keySet());
        return null;// TODO: 10/10/2019 Complete 
    }

    /**
     * This class will compare between agents by comparing their priority
     */
    public static class CompareAgentsPriority implements Comparator<Agent>
    {
        public CompareAgentsPriority()
        {

        }

        @Override
        public int compare(Agent o1, Agent o2) {
            double p1 = o1.getPriority();
            double p2 = o2.getPriority();
            if(p1>p2)
                return 1;
            if(p2>p1)
                return -1;
            return 0;
        }
    }
}
