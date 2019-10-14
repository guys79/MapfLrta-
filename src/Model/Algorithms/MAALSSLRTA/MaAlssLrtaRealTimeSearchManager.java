package Model.Algorithms.MAALSSLRTA;

import Model.Algorithms.BudgetOrientedMALRTA.BudgetOrientedMALRTA;
import Model.Components.AbstractRealTimeSearchManager;
import Model.Components.Agent;
import Model.Components.Node;
import Model.Components.Problem;
import javafx.util.Pair;

import java.util.*;

/**
 * This class represents a Multi Agent aLSS-LRTA*
 */
public class MaAlssLrtaRealTimeSearchManager extends AbstractRealTimeSearchManager {
    private int maxLength;//The maximum kength of prefix in a certain iteration
    public static int test =0;

    /**
     * The constructor of the class
     *
     * @param problem - The given problem
     */
    public MaAlssLrtaRealTimeSearchManager(Problem problem) {
        super(problem);


    }


    @Override
    protected void getPriorities()
    {
        Collection<Agent> agents = problem.getAgentsAndStartGoalNodes().keySet();
        for(Agent agent: agents)
        {
            agent.setPriority(agent.getHeuristicValue(agent.getCurrent()));
        }
    }

    @Override
    protected void calculatePrefix()
    {
        test++;
        prev = new HashMap<>();

        getPriorities();
        //Calculate the prefixes for all agents
        for(Agent agent:this.prefixesForAgents.keySet())
        {
                prev.put(agent,agent.getCurrent());
        }


        //Get prefixes
        Map<Agent,Pair<Node,Node>> agent_goal_start = problem.getAgentsAndStartGoalNodes();
        Collection<Agent> agents = agent_goal_start.keySet();
        //MAALSSLRTA maalsslrta = new MAALSSLRTA(problem);
        MAALSSLRTA maalsslrta = new BudgetOrientedMALRTA(problem);
        this.prev.clear();

        Map<Integer,List<Node>> prefixes = maalsslrta.getPrefixes(this.budgetMap);
        /*for(HashMap.Entry<Integer,List<Node>> prefix : prefixes.entrySet())
        {
            System.out.println("Agent "+prefix.getKey()+" prefix "+prefix.getValue());
        }*/

        //If there is no solution
        if(prefixes.containsValue(null))
        {
            List<Node> prefix;
            for(Agent agent: agents)
            {
                prefix = prefixes.get(agent);
                if(prefix == null) {
                    prefixesForAgents.put(agent, null);
                    List<Node> fail = new ArrayList<>();
                    fail.add(agent.getCurrent());
                    prefix = fail;
                }


                //Adding the prefix to the path
                List<Node>path = pathsForAgents.get(agent);
                path.remove(path.size()-1);
                path.addAll(prefix);

            }
            return;
        }

        //Calculate the maximum prefix size
        maxLength = -1;
        for (Agent agent : agents) {
            this.prefixesForAgents.put(agent,prefixes.get(agent.getId()));
            List<Node> prefix = this.prefixesForAgents.get(agent);
            if (prefix == null) {
                return;
            }
            if (maxLength < prefix.size())
                maxLength = prefix.size();
        }
        //Converting the prefixes to be with the same length
        for (Agent agent : agents) {
            List<Node> prefix = this.prefixesForAgents.get(agent);

            int length = prefix.size();
            Node last = prefix.get(length-1);
            if(last.getId()!= agent.getGoal().getId())
            {
                for(int i=length;i<maxLength;i++)
                {
                    prefix.add(last);
                }
            }
            //Adding the prefix to the path
            List<Node> path = pathsForAgents.get(agent);
            path.remove(path.size()-1);
            path.addAll(prefixes.get(agent.getId()));
        }


    }

    @Override
    public boolean isDone() {

        if( super.isDone())
            return true;

        //Check if there was a change
        Set<Agent> agents = prev.keySet();
        if(prev.size() ==0)
            return false;
        int count = 0;
        for(Agent agent : agents)
        {
            if(agent.isDone())
            {
                count++;
                if(count>numOfFinish)
                    return false;

            }
            else
            {
                if(this.prev.get(agent).getId() != agent.getCurrent().getId())
                    return false;
            }


        }
        return true;
    }

    @Override
    public void move() {
        PriorityQueue<Agent> agents = new PriorityQueue<>(new MAALSSLRTA.CompareAgentsHeurstics());
        agents.addAll(problem.getAgentsAndStartGoalNodes().keySet());

        //In each time step:
        Agent agent;
        for (int i = 0; i < maxLength; i++) {

            //For every agent
            while(agents.size()>0)
            {
                agent = agents.poll();

                //If there is a solution
                List<Node> prefix = this.prefixesForAgents.get(agent);
                if (prefix == null)
                    return;

                //Try to move the agent
                if (i <= prefix.size() - 1) {
                    if (!agent.moveAgent(prefix.get(i))) {
                        System.out.println("Collision between agent " + agent.getId() + " and agent " + prefix.get(i).getOccupationId() + " in " + prefix.get(i) +" id = "+prefix.get(i).getId());
                        prefixesForAgents.put(agent, null);
                        return;
                    }
                    else
                    {
                        if(prefix.size()-1 == i && problem.getAgentsAndStartGoalNodes().get(agent).getValue().getId() == prefix.get(i).getId())
                            prefix.get(i).inhabitate(agent.getId());
                    }
                }
            }

            agents = new PriorityQueue<>(new MAALSSLRTA.CompareAgentsHeurstics());
            agents.addAll(problem.getAgentsAndStartGoalNodes().keySet());

            //Don't clock way
            for (Agent agent2 : agents) {

                List<Node> prefix = this.prefixesForAgents.get(agent2);

                if (i <= prefix.size() - 1) {
                    int index = i;
                    Node node = prefix.get(index);
                    node.moveOut();
                }

            }
        }

    }


}
