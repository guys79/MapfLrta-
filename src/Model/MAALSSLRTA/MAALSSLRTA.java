package Model.MAALSSLRTA;

import Model.ALSSLRTA.ALSSLRTA;

import Model.ALSSLRTA.AlssLrtaSearchNode;
import Model.Agent;
import Model.Node;
import Model.Problem;

import java.util.*;

/**
 * This class represents the Multi Agent aLSS-LRTA*
 */
public class MAALSSLRTA extends ALSSLRTA {
    private Map<Integer,Map<Integer,Integer>> ocuupied_times;//Key - Node's id, value - dic
    //Key - Agent's id, int time
    private Set<Agent> agents;//The agents
    private Agent currentAgent;
    //private Agent currentAgent;//The current agent
    private Map<Integer,Map<Integer,AlssLrtaSearchNode>> closed;//The cosed list fr each agent
    private Map<Integer,PriorityQueue<AlssLrtaSearchNode>> open;//The open list for each agent
    private Map<Integer,PriorityQueue<AlssLrtaSearchNode>> open_min;//The open list for each agent
    private Map<Integer,PriorityQueue<AlssLrtaSearchNode>> open_min_update;//The open list for each agent
    private Map<Integer,Map<Integer,AlssLrtaSearchNode>> open_id;//key -Agent's id, value  - dic
    //Key - node's id, Value - the search node itself
    private IRules rules;//The rules for Real Time MAPF


    /**
     * The constructor
     *
     * @param problem - The given problem
     */
    public MAALSSLRTA(Problem problem) {
        super(problem);
        closed = new HashMap<>();
        rules = new RuleBook(this);
        open =new HashMap<>();
        open_min = new HashMap<>();
        open_min_update = new HashMap<>();
        open_id = new HashMap<>();
        ocuupied_times = new HashMap<>();
        agents = problem.getAgentsAndStartGoalNodes().keySet();
        int id;
        for(Agent agent : agents)
        {
            id =agent.getId();
            open.put(id,new PriorityQueue<>(new CompareAlssNode()));
            open_min.put(id, new PriorityQueue<>(new CompareHeuristicAlssNode()));
            open_min_update.put(id, new PriorityQueue<>(new CompareHeuristicUpdateAlssNode()));
            open_id.put(id, new HashMap<>());
            closed.put(id,new HashMap<>());
        }

    }

    /**
     * This function will return the prefixes of the agents after an iteration
     * @param numOfNodesToDevelop - The number of nodes allowed to develop
     * @return - The prefixes of the agents after an iteration
     */
    public Map<Integer,List<Node>> getPrefixes(int numOfNodesToDevelop)
    {
        //Maybe prioritize by heuristic of goal (the smaller the better)
        int id;
        Map<Integer,List<Node>> prefixes = new HashMap<>();
        for(Agent currentAgent: agents)
        {
            this.currentAgent = currentAgent;
            id = currentAgent.getId();
            PriorityQueue<AlssLrtaSearchNode> open_curr = open.get(id);
            PriorityQueue<AlssLrtaSearchNode> open_min_curr = open_min.get(id);
            PriorityQueue<AlssLrtaSearchNode> open_min_update_curr= open_min_update.get(id);
            Map<Integer,AlssLrtaSearchNode> closed_curr = closed.get(id);
            List<Node> prefix = super.calculatePrefix(currentAgent.getCurrent(),currentAgent.getGoal(),numOfNodesToDevelop,currentAgent,open_curr,open_min_curr,open_min_update_curr,closed_curr);
            if(prefix == null)
                return null;
            prefixes.put(id,prefix);
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
        occupations.put(currentAgent.getId(),time);
    }

    @Override
    protected List<Node> calculatePrefix(Node start, Node goal, int numOfNodesToDevelop, Agent agent, PriorityQueue<AlssLrtaSearchNode> open,PriorityQueue<AlssLrtaSearchNode> open_min,PriorityQueue<AlssLrtaSearchNode> open_min_update,Map<Integer,AlssLrtaSearchNode> closed) {
        return super.calculatePrefix(start,goal,numOfNodesToDevelop,agent,open,open_min,open_min_update,closed);
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
     * @return
     */
    public int getAgent(int nodeId,int time)
    {
        return this.ocuupied_times.get(nodeId).get(time);
    }
}
