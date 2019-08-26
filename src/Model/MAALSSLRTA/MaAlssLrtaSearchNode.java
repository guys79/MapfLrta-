package Model.MAALSSLRTA;

import Model.ALSSLRTA.AlssLrtaSearchNode;
import Model.Node;

import java.util.HashMap;
import java.util.HashSet;

public class MaAlssLrtaSearchNode  extends AlssLrtaSearchNode {

    private HashMap<Integer,Integer> ocuupied_times;
    /**
     * The constructor of the class
     *
     * @param node - The node that this class represents
     */
    public MaAlssLrtaSearchNode(Node node) {
        super(node);
        this.ocuupied_times = new HashMap<>();
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
     * @param id - The given agent's id
     * @return - True IFF it was able to save a spot a the given time
     */
    public boolean saveSpot(int time,int id)
    {
        if(canReserve(time))
        {
            this.ocuupied_times.put(time,id);
            return true;
        }
        return false;
    }

    /**
     * This function will return the agent's id that occupies the node at the given time
     * @param time - The given time
     */
    public int getAgent(int time)
    {
        if(this.ocuupied_times == null)
            return -1;
        Integer id = this.ocuupied_times.get(time);
        if(id == null)
            return -1;
        return id;
    }
    /**
     * This function will check if it is possible to save a spot at the given time
     * @param time - The given time
     * @return - True IFF it is possible to save a spot at the given time
     */
    public boolean canReserve(int time)
    {
        return this.ocuupied_times !=null && !this.ocuupied_times.containsKey(time);
    }

    /**
     * This function "inhabits" an agent in the node
     * It means that the agent has reached it's goal and now he is not moving from that spot
     */
    public void inhabitAgent()
    {
        this.ocuupied_times = null;
    }


}
