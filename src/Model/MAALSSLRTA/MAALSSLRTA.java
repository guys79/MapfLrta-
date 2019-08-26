package Model.MAALSSLRTA;

import Model.ALSSLRTA.ALSSLRTA;

import Model.Agent;
import Model.IRealTimeSearchAlgorithm;
import Model.Node;
import Model.Problem;

import java.util.*;

/**
 * This class represents the Multi Agent aLSS-LRTA*
 */
public class MAALSSLRTA implements IRealTimeSearchAlgorithm {

    private Map<Integer,Map<Integer,Integer>> ocuupied_times;//Key - Node's id, value - dic
                                                             //Key - Agent's id, int time
    private Agent currentAgent;//The current agent
    private IRules rules;//The rules defining the RT-MAPF
    private Problem problem;//Th eproblem
    /**
     * The constructor
     *
     * @param problem - The given problem
     *
     */
    public MAALSSLRTA(Problem problem) {
        ocuupied_times = new HashMap<>();
        this.rules = new RuleBook(this);
        this.problem = problem;
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
            this.ocuupied_times.get(nodeId).put(time,currentAgent.getId());
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
    public void inhabitAgent(int nodeId)
    {
        this.ocuupied_times.put(nodeId,null);
    }

    @Override
    public List<Node> calculatePrefix(Node start, Node goal, int numOfNodesToDevelop, Agent agent) {
        return null;
    }
    public int getAgent(int nodeId,int time)
    {
        return this.ocuupied_times.get(nodeId).get(time);
    }
}
