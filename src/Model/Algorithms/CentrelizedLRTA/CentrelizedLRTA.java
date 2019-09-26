package Model.Algorithms.CentrelizedLRTA;

import Model.Algorithms.IRealTimeSearchAlgorithm;
import Model.Components.Agent;
import Model.Components.Node;
import javafx.util.Pair;

import java.util.*;

// TODO: 9/26/2019 Dijkstra phase
/**
 * This class represents the centralized-Lrta*
 */
public class CentrelizedLRTA{

    private CentrelizedLRTAState goal;//The goal
    private CentrelizedLRTAState current;//The current state
    private int numberOfNodes;///The number of nodes
    private PriorityQueue<CentrelizedLRTASearchNode> open;//The open list
    private PriorityQueue<CentrelizedLRTASearchNode> close;//The close list
    private Map<String, Pair<Double,Double>>info; // key - id, pair - key - gVal val - hVal

    /**
     * The constructor
     */
    public CentrelizedLRTA()
    {
        info = new HashMap<>();
    }

    /**
     * This function will calculate the prefixes for the agents
     * @param current - The current state
     * @param goal - The goal state
     * @param numOfNodesToDevelop - The number of nodes
     * @return - The prefixes
     */
    public List<CentrelizedLRTAState> calculatePrefixes(CentrelizedLRTAState current, CentrelizedLRTAState goal,int numOfNodesToDevelop) {

        this.goal = goal;
        this.current = current;

        this.numberOfNodes = numOfNodesToDevelop;
        this.open =new PriorityQueue<>(new CompareCentrelizedSearchNodes());
        this.close =new PriorityQueue<>();

        //Lookahead
        lookAhead();
        //Extract th best state
        CentrelizedLRTASearchNode centrelizedLRTASearchNode = bestState();

        List<CentrelizedLRTAState> prefix = new ArrayList<>();
        
        while(centrelizedLRTASearchNode !=null)
        {
            prefix.add(0,centrelizedLRTASearchNode.getState());
            centrelizedLRTASearchNode = centrelizedLRTASearchNode.getBack();
        }
        return prefix;

    }

    /**
     * This function will check id the state is a goal state
     * @param state -The given state
     * @return - True IFF the given state is the goal state
     */
    private boolean checkIfGoal(CentrelizedLRTAState state)
    {
        return state.equals(goal);
    }

    /**
     * This function will handle the lookahead prt of the algorithm
     */
    private void lookAhead()
    {

        
        CentrelizedLRTASearchNode current = new CentrelizedLRTASearchNode(this.current);
        current.setgVal(0);
        int expansions  = 0;
        open.add(current);
        while(  expansions  <this.numberOfNodes)
        {
            CentrelizedLRTASearchNode polled =  open.peek();
            if(checkIfGoal(polled.getState()))
                return;
            open.poll();
            expansions++;
            close.add(polled);

            Set<CentrelizedLRTAState> neighbors = polled.getState().getLegalStates();
            Set<CentrelizedLRTASearchNode> transformedNeighbors = transformSet(neighbors);
            double temp;
            for(CentrelizedLRTASearchNode node : transformedNeighbors)
            {
                temp = node.getState().getCost(polled.getState());
                if(node.getgVal()>temp)
                {
                    setGVal(node,temp);
                    node.setBack(polled);
                    open.add(node);
                    
                }
            }
        }
        
    }

    /**
     * This function will set the G value of the node while updating all elements necessary
     * @param node - The given node
     * @param val - The new val
     */
    private void setGVal(CentrelizedLRTASearchNode node, double val)
    {
        node.setgVal(val);
        Pair<Double,Double> vals = this.info.get(node.getState().getId());
        if(vals == null)
            this.info.put(node.getState().getId(),new Pair<>(val,node.gethVal()));
        else
            this.info.put(node.getState().getId(),new Pair<>(val,vals.getValue()));
    }

    /**
     * This function will transform all the set of states into a set of search nodes
     * @param states- The given set of states
     * @return -  A new set of transformed states
     */
    private Set<CentrelizedLRTASearchNode> transformSet(Set<CentrelizedLRTAState> states)
    {
        Set<CentrelizedLRTASearchNode> transStates = new HashSet<>();
        for(CentrelizedLRTAState state : states)
        {
            transStates.add(transformSingle(state));
        }
        return transStates;
    }

    /**
     * This function will transform a single state into a search node
     * @param state - The given state
     * @return - The search node
     */
    private CentrelizedLRTASearchNode transformSingle(CentrelizedLRTAState state)
    {

        Pair<Double,Double> vals = this.info.get(state.getId());
        CentrelizedLRTASearchNode res;
        if(vals == null)
        {
            res = new CentrelizedLRTASearchNode(state);
            this.info.put(state.getId(),new Pair<>(res.getgVal(),res.gethVal()));
            return res;
        }
        else
        {
            res = new CentrelizedLRTASearchNode(state);
            //res.setgVal(vals.getKey());
            res.sethVal(vals.getValue());
            return res;
        }
    }

    /**
     * This function will return the best state
     * @return - the best state
     */
    private CentrelizedLRTASearchNode bestState()
    {
        return open.poll();
    }


    /**
     * This class will compare between two nodes (Using f value)
     */
    public class CompareCentrelizedSearchNodes implements Comparator<CentrelizedLRTASearchNode>
    {

        @Override
        public int compare(CentrelizedLRTASearchNode o1, CentrelizedLRTASearchNode o2) {
            double f1 = o1.getgVal() + o1.gethVal();
            double f2 = o2.getgVal() + o2.gethVal();
            if(f1<f2)
                return -1;
            if(f1==f2)
                return 0;
            return 1;
        }
    }
}