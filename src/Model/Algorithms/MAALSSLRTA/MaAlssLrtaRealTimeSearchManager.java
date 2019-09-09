package Model.Algorithms.MAALSSLRTA;

import Model.AbstractRealTimeSearchManager;
import Model.Agent;
import Model.Algorithms.ALSSLRTA.AlssLrtaSearchNode;
import Model.Node;
import Model.Problem;
import javafx.util.Pair;

import java.util.*;

/**
 * This class represents a Multi Agent aLSS-LRTA*
 */
public class MaAlssLrtaRealTimeSearchManager extends AbstractRealTimeSearchManager {
    private int maxLength;//The maximum kength of prefix in a certain iteration

    /**
     * The constructor of the class
     *
     * @param problem - The given problem
     */
    public MaAlssLrtaRealTimeSearchManager(Problem problem) {
        super(problem);

    }


    @Override
    protected void calculatePrefix()
    {
        prev = new HashMap<>();
        for(Agent agent:this.prefixesForAgents.keySet())
        {
            if(!agent.isDone())
                prev.put(agent,agent.getCurrent());
        }

        Map<Agent,Pair<Node,Node>> agent_goal_start = problem.getAgentsAndStartGoalNodes();
        Collection<Agent> agents = agent_goal_start.keySet();
        MAALSSLRTA maalsslrta = new MAALSSLRTA(problem);
        this.prev.clear();
        Map<Integer,List<Node>> prefixes = maalsslrta.getPrefixes(problem.getNumberOfNodeToDevelop());
        if(prefixes==null)
        {
            for(Agent agent: agents)
            {
                //System.out.println("No Solution");
                prefixesForAgents.put(agent,null);
                List<Node> fail = new ArrayList<>();
                fail.add(agent_goal_start.get(agent).getKey());
                pathsForAgents.put(agent,fail);
            }
            return;
        }

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

            List<Node> path = pathsForAgents.get(agent);
            path.remove(path.size()-1);
            path.addAll(prefixes.get(agent.getId()));
        }


    }

    @Override
    public boolean isDone() {
        if( super.isDone())
            return true;
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
      //  PriorityQueue<Agent> prev = new PriorityQueue<>(new MAALSSLRTA.CompareAgentsHeurstics());
        agents.addAll(problem.getAgentsAndStartGoalNodes().keySet());

        Agent agent;
        for (int i = 0; i < maxLength; i++) {

            while(agents.size()>0)
            {
                agent = agents.poll();

                List<Node> prefix = this.prefixesForAgents.get(agent);
                if (prefix == null)
                    return;
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
