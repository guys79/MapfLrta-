package Model;

import Model.LRTA.LRTA;
import javafx.util.Pair;

import java.util.*;

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

    @Override
    /**
     * This function will search for all the agents their paths using Real-Time Search
     * @return - Key - agent, Value - The agent's path
     */
    public Map<Agent,List<Node>> search()
    {
        while(!isDone())
        {
            calculatePrefix();
            move();
        }
        return this.pathsForAgents;
    }

}
