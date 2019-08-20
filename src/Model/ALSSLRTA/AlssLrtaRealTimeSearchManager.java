package Model.ALSSLRTA;

import Model.Agent;
import Model.IRealTimeSearchManager;
import Model.Node;
import Model.Problem;
import javafx.util.Pair;

import java.util.*;

/**
 * This class represents a manager for the aLSS-LRTA* algorithm
 */
public class AlssLrtaRealTimeSearchManager implements IRealTimeSearchManager {
    private Problem problem;//The problem
    private Map<Agent, List<Node>> pathsForAgents;//Key - agent, Value - The agent's prefix
    private Map<Agent, List<Node>> prefixesForAgents;//Key - agent, Value - The agent's prefix

    public AlssLrtaRealTimeSearchManager(Problem problem)
    {
        this.problem = problem;
        this.pathsForAgents = new HashMap<>();
        this.prefixesForAgents = new HashMap<>();

        Map<Agent,Pair<Node,Node>> agentsAndStartGoalNodes= problem.getAgentsAndStartGoalNodes();
        Set<Agent> agents = agentsAndStartGoalNodes.keySet();

        for(Agent agent: agents)
        {
            Node start = agentsAndStartGoalNodes.get(agent).getKey();
            agent.setCurrent(start);
            List<Node> path = new ArrayList<>();
            path.add(start);
            pathsForAgents.put(agent,path);
        }

    }
    private void calculatePrefix()
    {
        Map<Agent,Pair<Node,Node>> agent_goal_start = problem.getAgentsAndStartGoalNodes();
        Collection<Agent> agents = agent_goal_start.keySet();

        for(Agent agent: agents)
        {
            ALSSLRTA alsslrta = new ALSSLRTA(problem);
            List<Node> prefix = alsslrta.calculatePrefix(agent_goal_start.get(agent).getKey(),agent_goal_start.get(agent).getValue(),problem.getNumberOfNodeToDevelop(),agent);
            this.prefixesForAgents.put(agent,prefix);
            List<Node> path = pathsForAgents.get(agent);
            path.remove(path.size()-1);
            path.addAll(prefix);
        }
    }
    @Override
    public Map<Agent,List<Node>> search()
    {
        while(!isDone())
        {
            calculatePrefix();
            move();
        }
        return this.pathsForAgents;
    }

    /**
     * This function will move the agents according to their calculated prefixes
     */
    public void move()
    {
        HashSet<Agent> agents = new HashSet<>(problem.getAgentsAndStartGoalNodes().keySet());
        for(Agent agent : agents)
        {

            List<Node> prefix = this.prefixesForAgents.get(agent);
            System.out.println("Model.Agent "+agent.getId()+"'s prefix is : "+ prefix);

            for(int i=1;i<prefix.size();i++)
            {
                if(!agent.moveAgent(prefix.get(i)))
                    System.out.println("Collision");
            }
        }
    }

    /**
     * This function will determine if the problem is solved
     * @return - True IFF the problem id solved
     */
    public boolean isDone()
    {

        HashSet<Agent> agents = new HashSet<>(problem.getAgentsAndStartGoalNodes().keySet());
        for(Agent agent : agents)
        {
            if(!agent.isDone())
                return  false;
        }
        return true;
    }
}
