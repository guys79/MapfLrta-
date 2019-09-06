package Model;

import javafx.util.Pair;

import java.util.*;
/**
 * This class represents an abstract Real Time Search Manager
 */
public abstract class AbstractRealTimeSearchManager implements IRealTimeSearchManager{
    protected Problem problem;//The instance of the problem
    protected Map<Agent, List<Node>> prefixesForAgents;//Key - agent, Value - The agent's prefix
    protected Map<Agent, List<Node>> pathsForAgents;//Key - agent, Value - The agent's prefix
    /**
     * The constructor of the class
     * @param problem - The given problem
     */
    public AbstractRealTimeSearchManager(Problem problem)
    {
        this.problem = problem;
        this.prefixesForAgents = new HashMap<>();
        Map<Agent,Pair<Node,Node>> agentsAndStartGoalNodes= problem.getAgentsAndStartGoalNodes();
        pathsForAgents = new HashMap<>();
        Set<Agent> agents = agentsAndStartGoalNodes.keySet();

        //Setting the agents in their start position
        for(Agent agent: agents)
        {
            Node start = agentsAndStartGoalNodes.get(agent).getKey();
            agent.setCurrent(start);
            List<Node> path = new ArrayList<>();
            path.add(start);
            pathsForAgents.put(agent,path);
        }



    }

    /**
     * This function will calculate for each agent prefix
     */
    protected abstract void calculatePrefix();


    /**
     * This function will move the agents according to their calculated prefixes
     */
    public void move()
    {
        HashSet<Agent> agents = new HashSet<>(problem.getAgentsAndStartGoalNodes().keySet());
        for(Agent agent : agents)
        {

            List<Node> prefix = this.prefixesForAgents.get(agent);
            if(prefix == null)
            {
                return;
            }
            System.out.println("Agent "+agent.getId()+"'s prefix is : "+ prefix);

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
        if(this.prefixesForAgents.values().contains(null))
        {
            return true;
        }
        for(Agent agent : agents)
        {
            if(!agent.isDone())
                return  false;
        }
        return true;
    }

    @Override
    /**
     * This function will search for all the agents their paths using Real-Time Search
     * @return - Key - agent, Value - The agent's path
     */
    public Map<Agent,List<Node>> search()
    {
        int i=0;
        while(!isDone())
        {
            //System.out.println("Iteration number "+(i+1));
            calculatePrefix();
            move();
            i++;
        }
        System.out.println("Number of iterations "+i);
        return this.pathsForAgents;
    }


}

