package Model.Algorithms.BudgetPolicy;

import Model.Components.Agent;
import Model.Components.Node;
import javafx.util.Pair;

import javax.xml.crypto.dom.DOMCryptoContext;
import java.util.*;

public class LongestPathBudgetPolicy implements IBudgetPolicy {
    @Override
    public Map<Agent, Integer> getBudgetMap(Map<Agent, Pair<Node, Node>> agentsAndStartGoalNodes, int totalBudget) {

        Map<Agent,Double> heuristics = new HashMap<>();
        Agent [] agents = new Agent[agentsAndStartGoalNodes.size()];
        int k =0;
        Agent agent;
        double sum = 0;
        double val;
        for(Map.Entry<Agent,Pair<Node,Node>> entry : agentsAndStartGoalNodes.entrySet())
        {
            agent = entry.getKey();
            val = agent.getHeuristicValue(entry.getValue().getKey());
            heuristics.put(agent,val);
            sum+=val;
            agents[k] = agent;
            k++;
        }

        double budget;
        PriorityQueue<Pair<Agent,Integer>> budgets = new PriorityQueue<>(new BudgetComperator());
        int check = 0;

        for(Map.Entry<Agent,Double> heuristic : heuristics.entrySet())
        {

            budget =(heuristic.getValue()/sum)*totalBudget;
            if(budget != (int)budget)
                budget = ((int)(budget)) + 1;
            check+= (int)budget;
            budgets.add(new Pair<>(heuristic.getKey(),(int)budget));
        }

        int delta = check - totalBudget;
        Map<Agent, Integer> budgetForAgent = new HashMap<>();
        Pair<Agent,Integer> agent_budget;
        int realBudget;
        check = 0;
        while(budgets.size()>0)
        {
            agent_budget = budgets.poll();
            if(delta>0)
            {
                delta--;
                realBudget = agent_budget.getValue() - 1;
            }
            else
                realBudget = agent_budget.getValue();
            check+=realBudget;
            budgetForAgent.put(agent_budget.getKey(),realBudget);
        }

        //for(int i=heuristics.size()-1;i>=0;i--)
        return budgetForAgent;
    }

    public class BudgetComperator implements Comparator<Pair<Agent,Integer>>
    {


        @Override
        public int compare(Pair<Agent, Integer> o1, Pair<Agent, Integer> o2) {
            return o1.getValue() - o2.getValue();
        }
    }
}