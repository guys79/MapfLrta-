package Model.Algorithms.CentrelizedLRTA;

import Model.Components.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a state in the Centralized-Lrta*
 */
public class CentrelizedLRTAState {

    private Node [] locations;//The locations of the agents
    private String id;
    /**
     * The constructor
     * @param locations - The locations of the agents
     */
    public CentrelizedLRTAState(Node [] locations)
    {
        this.locations = locations;
        this.id = "";
        for(int i=0;i<locations.length;i++)
        {
            id += locations[i].getId()+",";
        }
        id = id.substring(0,id.length()-1);

    }

    public Node getLocationAt(int index)
    {
        return this.locations[index];
    }
    public int getNumOfAgets()
    {
        return this.locations.length;
    }
    public double getCost(CentrelizedLRTAState state)
    {
        double sum =0 ;
        Node [] stateLocs = state.locations;

        for(int i=0;i<stateLocs.length;i++)
        {
            sum+=stateLocs[i].getWeight(this.locations[i]);
        }
        return sum;
    }
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof CentrelizedLRTAState))
            return false;
        return ((CentrelizedLRTAState)obj).id .equals( id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * This function will return the legal states that can be achieves by preforming a single action
     * @return - The set of legal states
     */
    public Set<CentrelizedLRTAState> getLegalStates()
    {
        int index = 0;

        Set<CentrelizedLRTAState> res = getLegalStates(index,true,null, new HashSet<>());
        return res;
    }

    /**
     * This function is a recursive helper function to find a set of legal states
     * @param index - The index in the array
     * @param isSame - True IFF the state creating is the same a this one
     * @param locs - the states content
     * @param res - Te result
     * @return - the set of legal states
     */
    private Set<CentrelizedLRTAState> getLegalStates(int index,boolean isSame,Node [] locs,Set<CentrelizedLRTAState> res)
    {
        //Stop
        if(index == this.locations.length)
        {
            if(!isSame) {
                CentrelizedLRTAState centrelizedLRTAState = new CentrelizedLRTAState(locs);
                res.add(centrelizedLRTAState);
            }
            return null;
        }

        //Fetch the neighbors of te node
        Set<Node> nodes = this.locations[index].getNeighbors().keySet();
        nodes.add(this.locations[index]);
        if(index ==0) {
            Node [] init;
            for (Node neigh : nodes) {
                init = new Node[this.locations.length];
                init[index] = neigh;
                getLegalStates(index+1,isSame&&this.locations[index].getId() == neigh.getId(),init,res);
            }

            return res;
        }
        else
        {
            for (Node neigh : nodes) {
                locs[index] = neigh;
                if(allowed(locs,index,neigh)) {
                    getLegalStates(index + 1, isSame && this.locations[index].getId() == neigh.getId(), locs, res);
                }
            }
            return null;
        }



    }

    /**
     * This function will check id the state is legal
     * @param locs - the locations
     * @param index - the index in the array
     * @param neigh - the given neighbor
     * @return - True IFF the state is legal
     */
    private boolean allowed(Node [] locs,int index,Node neigh)
    {
        Node prev = this.locations[index];
        Node otherPrev;
        for(int i=0;i<index;i++)
        {
            if(locs[i].getId() == neigh.getId())
                return false;

            if(locs[i].getId() == prev.getId())
            {
                otherPrev = this.locations[i];
                if(otherPrev.getId() == neigh.getId())
                    return false;
            }
        }
        return true;
    }
}
