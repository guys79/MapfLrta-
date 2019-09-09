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
    protected Map<Agent, Node> prev;//Key - agent, Value - The agent's prefix
    protected int numOfFinish;
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
        this.prev = new HashMap<>();
        //Setting the agents in their start position
        for(Agent agent: agents)
        {
            Node start = agentsAndStartGoalNodes.get(agent).getKey();
            agent.setCurrent(start);
            List<Node> path = new ArrayList<>();
            path.add(start);
            pathsForAgents.put(agent,path);
        }
        numOfFinish = 0;



    }

    /**
     * This function will calculate for each agent prefix
     */
    protected abstract void calculatePrefix();


    /**
     * This function will move the agents according to their calculated prefixes
     */
    public void move() {
        Collection<Agent> agents = new HashSet<>(problem.getAgentsAndStartGoalNodes().keySet());
        int maxLength = -1;
        this.numOfFinish = 0;
        for (Agent agent : agents) {
            List<Node> prefix = this.prefixesForAgents.get(agent);
            if(agent.isDone())
                numOfFinish++;
          //  System.out.println("Agent "+agent.getId()+" prefix is "+prefix);
            if (prefix == null) {
                return;
            }
            if (maxLength < prefix.size())
                maxLength = prefix.size();
        }

        for (int i = 0; i < maxLength; i++) {
            for (Agent agent : agents) {

                List<Node> prefix = this.prefixesForAgents.get(agent);
                if (prefix == null)
                    return;

                if (i <= prefix.size() - 1) {
                    if (!agent.moveAgent(prefix.get(i))) {
                        System.out.println("Collision between agent " + agent.getId() + " and agent " + prefix.get(i).getOccupationId() + " in " + prefix.get(i));
                        prefixesForAgents.put(agent, null);
                        return;
                    }
                }
            }
        }
        for (Agent agent : agents) {

            List<Node> prefix = this.prefixesForAgents.get(agent);
            int index = prefix.size() - 1;
            Node node=prefix.get(index);
            node.moveOut();

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
        clear();
        return this.pathsForAgents;
    }
    private void clear()
    {
        Set<Agent> agents = problem.getAgentsAndStartGoalNodes().keySet();
        for(Agent agent: agents)
        {
            this.problem.getAgentsAndStartGoalNodes().get(agent).getValue().unInhabit();
        }
    }


}

