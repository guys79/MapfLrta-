package Model;


import Model.Algorithms.Dijkstra.ShortestPathGenerator;
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
    protected int numOfFinish;//Number of finished problems
    private int iteration;//Number of iterations
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
                        System.out.println("Collision between agent " + agent.getId() + " and agent " + prefix.get(i).getOccupationId() + " in " + prefix.get(i) +" at iteration "+iteration);
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
            TestPreformer.getInstance().updateNonComplete();
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
        iteration=0;
        long start,end;
        start =  System.currentTimeMillis();
        long sum =0l;
        while(!isDone() && iteration<10000)
        {

            //System.out.println("Iteration number "+(i+1));
            calculatePrefix();
            move();
            end = System.currentTimeMillis();
            sum+= (end-start);

            iteration++;
            start =  System.currentTimeMillis();
        }
        TestPreformer.getInstance().updateAverageIteration((sum*1.0)/iteration);
        if(iteration>=10000)
        {
            TestPreformer.getInstance().updateNonComplete();
        }
        System.out.println("Number of iterations "+iteration);
        clear();
        return this.pathsForAgents;
    }

    /**
     * This function will clear the inhabitants
     */
    private void clear()
    {
        Set<Agent> agents = problem.getAgentsAndStartGoalNodes().keySet();
        for(Agent agent: agents)
        {
            this.problem.getAgentsAndStartGoalNodes().get(agent).getValue().unInhabit();
        }

    }


}

