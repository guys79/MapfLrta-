package Model.Algorithms.CentrelizedLRTA;

import Model.Components.GridNode;
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
    private int time;
    private int numInGoal;
    private CentrelizedLRTAState goal;
    private int chengedAgents;//The state where the agent the numInODth index has moved
    private boolean changed;


    public int getNumInGoal() {
        return numInGoal;
    }


    public boolean isPredecessor(CentrelizedLRTAState state)
    {
        if(state.time != time - 1)
            return false;
        for(int i=0;i<this.locations.length;i++)
        {
            if(!this.locations[i].isNeighbor(state.locations[i]))
                return false;
        }
        return true;
    }
    public int numOfMovingAgents(CentrelizedLRTAState state)
    {
        int num =0;
        for(int i=0;i<this.locations.length;i++)
        {
            if(this.locations[i].getId() != state.locations[i].getId())
                num++;
        }
        return num;
    }

    public boolean isRealState()
    {
        return  this.chengedAgents== 0;
    }
    /**
     * The constructor
     * @param locations - The locations of the agents
     */
    public CentrelizedLRTAState(Node [] locations,int time,CentrelizedLRTAState goal,int numInOD,boolean changed)
    {

        this.chengedAgents= numInOD;
        this.changed = changed;

        if(numInOD == locations.length)
            this.chengedAgents =0;//Real

        this.locations = new Node[locations.length];
        this.time = time;
        Node [] goalLocations;
        if(goal != null)
            goalLocations = goal.locations;
        else
            goalLocations = this.locations;

        this.id = "";
        this.numInGoal = 0;
       // System.out.println();

        for(int i=0;i<this.locations.length;i++)
        {
            this.locations[i] = locations[i];
            if(goalLocations[i] == null)
                System.out.println();
            if(this.locations[i].getId() ==goalLocations[i].getId())
                numInGoal++;
            id += this.locations[i].getId()+",";
         //   System.out.print(this.locations[i].getId()+",");
        }
        //id +=time;
        id = id.substring(0,id.length()-1);

    }

   /* public boolean check()
    {
        String id="";
        for(int i=0;i<this.locations.length;i++)
        {
            id += this.locations[i].getId()+",";
            //   System.out.print(this.locations[i].getId()+",");
        }
        id = id.substring(0,id.length()-1);
        if(!(id.equals(this.id)))
            throw new UnsupportedOperationException("fuckkk");
        return id.equals(this.id);
    }

    */

    public int getTime() {
        return time;
    }

    /**
     * This function will return the node of the indexTh agent
     * @param index - The index
     * @return- The location of the agent
     */
    public Node getLocationAt(int index)
    {


        return this.locations[index];
    }

    /**
     * This function will return the number of agents
     * @return - The number of agents
     */
    public int getNumOfAgets()
    {


        return this.locations.length;
    }

    /**
     * This function will return the cost of a transaction between this state to the given one
     * @param state - The given state
     * @return - The cost of the transaction between this state to the given one
     */
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

    /**
     * this function will return the if od the state
     * @return - The id
     */
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
        //count++;
       // System.out.println(count);
        //Stop
        if(index == this.locations.length)
        {
            if(!isSame) {
                CentrelizedLRTAState centrelizedLRTAState = new CentrelizedLRTAState(locs,time+1,goal,chengedAgents+1,true);
                res.add(centrelizedLRTAState);
            }
            return null;
        }

        //Fetch the neighbors of te node
        Set<Node> nodes = new HashSet<>(this.locations[index].getNeighbors().keySet());
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
     * This function will return the legal states that can be achieves by preforming a single action
     * @return - The set of legal states
     */
    public Set<CentrelizedLRTAState> getLegalStatesOD()
    {
        Set<CentrelizedLRTAState> neigh = new HashSet<>();
        int index = chengedAgents;

            Node[] loc;
            Set<Node> neighbors = this.locations[index].getNeighbors().keySet();
            int i=0;
            for(Node node : neighbors) {
                loc = new Node[this.locations.length];
                for (int j = 0; j < this.locations.length; j++) {
                    loc[j] = this.locations[j];
                }
                loc[index] = node;
                if (allowed(loc, index, node) && !(index==this.locations.length-1 && !changed)) {
                    neigh.add(new CentrelizedLRTAState(loc, time + 1, goal, index+1,changed || this.locations[index].getId()!= node.getId()));
                }
                i++;
            }

        return neigh;
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
            if(locs[i].getId() == neigh.getId()) {
                return false;
            }

            if(locs[i].getId() == prev.getId())
            {
                otherPrev = this.locations[i];
                if(otherPrev.getId() == neigh.getId()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String str = "";

        for(int i=0;i<this.locations.length;i++)
        {
            str+="["+((GridNode)this.locations[i]).getX()+","+((GridNode)this.locations[i]).getY()+"]";
        }
        return str;
    }
}
