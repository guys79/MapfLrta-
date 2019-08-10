import javafx.util.Pair;

import java.util.*;

/**
 * This class represents the Real-Time search manager
 */
public class RealTimeSearchManager {

    private Problem problem;//The instance of the problem
    private Map<Agent, List<Node>> prefixesForAgents;//Key - agent, Value - The agent's prefix
    /**
     * The constructor of the class
     * @param problem - The given problem
     */
    public RealTimeSearchManager(Problem problem)
    {
        this.problem = problem;
        this.prefixesForAgents = new HashMap<>();
        Map<Agent,Pair<Node,Node>> agentsAndStartGoalNodes= problem.getAgentsAndStartGoalNodes();
        Set<Agent> agents = agentsAndStartGoalNodes.keySet();

        //Setting the agents in their start position
        for(Agent agent: agents)
        {
            agent.setCurrent(agentsAndStartGoalNodes.get(agent).getKey());
        }

    }

    /**
     * This function will calculate for each agent prefix
     */
    public void calculatePrefix()
    {

        HashSet<Agent> agents = new HashSet<>(problem.getAgentsAndStartGoalNodes().keySet());
        for(Agent agent : agents)
        {
            if(agent.isDone())
                continue;

            Node current = agent.getCurrent();
            IRealTimeSearchAlgorithm searchAlgorithm= new LRTA();
            List<Node> prefix = searchAlgorithm.calculatePrefix(current,problem.getAgentsAndStartGoalNodes().get(agent).getValue(),problem.getNumberOfNodeToDevelop(),agent);
            this.prefixesForAgents.put(agent,prefix);
        }
    }

    /**
     * This function will move the agents according to their calculated prefixes
     */
    public void move()
    {
        HashSet<Agent> agents = new HashSet<>(problem.getAgentsAndStartGoalNodes().keySet());
        for(Agent agent : agents)
        {
            // TODO: 8/10/2019 print progress 
            //System.out.println("Agent "+agent.getId()+" "+);
            List<Node> prefix = this.prefixesForAgents.get(agent);
            for(int i=0;i<prefix.size();i++)
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

    public void search()
    {
        while(isDone())
        {
            calculatePrefix();
            move();
        }
    }


}
