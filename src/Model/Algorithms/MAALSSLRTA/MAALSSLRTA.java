package Model.Algorithms.MAALSSLRTA;

import Model.Algorithms.ALSSLRTA.ALSSLRTA;

import Model.Agent;
import Model.Node;
import Model.Problem;
import javafx.util.Pair;

import java.util.*;

/**
 * This class represents the Multi Agent aLSS-LRTA*
 */
public class MAALSSLRTA extends ALSSLRTA {
    private Map<Integer,Map<Integer,Integer>> ocuupied_times;//Key - Node's id, value - dic
    //Key - Agent's id, int time
    private Agent currentAgent;//The current agent
    private IRules rules;//The rules for Real Time MAPF


    /**
     * The constructor
     *
     * @param problem - The given problem
     */
    public MAALSSLRTA(Problem problem) {
        super(problem);
        rules = new RuleBook(this);

        ocuupied_times = new HashMap<>();
        this.currentAgent = getAgent();



    }

    /**
     * This function will return the prefixes of the agents after an iteration
     * @param numOfNodesToDevelop - The number of nodes allowed to develop
     * @return - The prefixes of the agents after an iteration
     */
    public Map<Integer,List<Node>> getPrefixes(int numOfNodesToDevelop)
    {
        Map<Agent,Pair<Node,Node>> agent_goal_start = problem.getAgentsAndStartGoalNodes();
        Set<Agent> agents = agent_goal_start.keySet();
        Map<Integer,List<Node>> prefixes = new HashMap<>();

        for(Agent agent: agents)
        {
            if(agent.isDone())
            {
                List<Node> done = new ArrayList<>();
                done.add(agent.getGoal());
                prefixes.put(agent.getId(),done);

            }
            else {
                Node current = agent.getCurrent();

                List<Node> prefix = super.calculatePrefix(current, agent.getGoal(), numOfNodesToDevelop, agent);

                if (prefix == null) {
                    System.out.println("No Solution agent " + agent.getId());
                    return null;
                }

                prefixes.put(agent.getId(), prefix);
            }

        }



        return prefixes;
    }

    @Override
    protected void aStarPrecedure() {
        super.aStarPrecedure();
        // TODO: 8/27/2019 Complete this 
    }

    @Override
    protected void updateNode(int nodeId,int time) {


        if(!this.ocuupied_times.containsKey(nodeId))
        {
            this.ocuupied_times.put(nodeId,new HashMap<>());
        }
        Map<Integer, Integer> occupations = this.ocuupied_times.get(nodeId);
        occupations.put(getAgent().getId(),time);
    }


    /**
     * This function will clear the reserves
     */
    public void clear()
    {
        this.ocuupied_times.clear();
    }

    /**
     * This function will save a spot in a certain given time
     * @param time - The given time
     * @param nodeId - The given agent's id
     * @return - True IFF it was able to save a spot a the given time
     */
    public boolean saveSpot(int time,int nodeId)
    {
        if(canReserve(time,nodeId))
        {
            if(!this.ocuupied_times.containsKey(nodeId))
            {
                this.ocuupied_times.put(nodeId,new HashMap<>());
            }
            this.ocuupied_times.get(nodeId).put(this.currentAgent.getId(),time);
            return true;
        }
        return false;
    }


    /**
     * This function will check if it is possible to save a spot at the given time
     * @param time - The given time
     * @param nodeId - The node's id
     * @return - True IFF it is possible to save a spot at the given time
     */
    public boolean canReserve(int time,int nodeId)
    {
        return this.ocuupied_times.get(nodeId) !=null && !this.ocuupied_times.get(nodeId).containsKey(time);

    }

    /**
     * This function "inhabits" an agent in the node
     * It means that the agent has reached it's goal and now he is not moving from that spot
     * @param nodeId - The node's id
     */
    @Override
    protected void inhabitAgent(int nodeId)
    {
        this.ocuupied_times.put(nodeId,null);
    }

    /**
     * This function will return the agent that is occupying he node with the given node's id
     * at the given time
     * @param nodeId - The given node's id
     * @param time - The given time
     * @return - The id of the agent that will occupy the node at the given time
     */
    public int getAgent(int nodeId,int time)
    {
        return this.ocuupied_times.get(nodeId).get(time);
    }

}
