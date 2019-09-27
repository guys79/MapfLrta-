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
    private Set<CentrelizedLRTASearchNode> close;//The close list
    private Map<String, Pair<Double,Double>>info; // key - id, pair - key - gVal val - hVal
    private Map<String,Set<CentrelizedLRTASearchNode>> needToUpdateg;


    public int g;

    /**
     * The constructor
     */
    public CentrelizedLRTA()
    {
        info = new HashMap<>();
        g=0;
    }

    /**
     * This function will calculate the prefixes for the agents
     * @param current - The current state
     * @param goal - The goal state
     * @param numOfNodesToDevelop - The number of nodes
     * @return - The prefixes
     */
    public List<CentrelizedLRTAState> calculatePrefixes(CentrelizedLRTAState current, CentrelizedLRTAState goal,int numOfNodesToDevelop) {

        g++;
        this.goal = goal;
        this.current = current;
        this.needToUpdateg = new HashMap<>();
        this.numberOfNodes = numOfNodesToDevelop;
        this.open =new PriorityQueue<>(new CompareCentrelizedSearchNodes());
        this.close =new HashSet<>();
        if(g==2)
        {
            System.out.println();
        }
        //Lookahead
        lookAhead();
        //Extract th best state
        CentrelizedLRTASearchNode centrelizedLRTASearchNode = bestState();


        update();


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


    public void update()
    {
        updateClose(transformSingle(current,null));
    }

    private double updateClose(CentrelizedLRTASearchNode node)
    {
        Set<CentrelizedLRTASearchNode> children = this.needToUpdateg.get(node.getState().getId());
        if(children!=null && children.size()>0) {
            double min_f = Double.MAX_VALUE;
            for (CentrelizedLRTASearchNode child : children) {
                min_f = Double.min(min_f, getF(child) - node.getState().getCost(child.getState()));

            }
            Pair <Double,Double> p =this.info.get(node.getState().getId());
            this.info.put(node.getState().getId(),new Pair<>(p.getKey(),min_f));
            children.clear();
            return min_f;
        }
        return getF(node);
    }
    /**
     * This function will handle the lookahead prt of the algorithm
     */
    private void lookAhead()
    {

        
        CentrelizedLRTASearchNode current = new CentrelizedLRTASearchNode(this.current,null);
        setGVal(current,0,null);
        this.needToUpdateg.put(current.getState().getId(),null);
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

            Set<CentrelizedLRTASearchNode> transformedNeighbors = transformSet(neighbors,polled);
            double temp;
         //   double min_f = Double.MAX_VALUE;
            for(CentrelizedLRTASearchNode node : transformedNeighbors)
            {
                temp = node.getState().getCost(polled.getState()) + polled.getgVal();
                if(node.getgVal()>temp)
                {
                    setGVal(node,temp,polled);
                    node.setBack(polled);
                    open.remove(node);
                    open.add(node);
                   // min_f = Double.min(min_f,getF(node));
                }
            }
        }
        
    }
    private  double getF(CentrelizedLRTASearchNode node)
    {
        return node.getgVal() + node.gethVal();
    }
    /**
     * This function will set the G value of the node while updating all elements necessary
     * @param node - The given node
     * @param val - The new val
     */
    private void setGVal(CentrelizedLRTASearchNode node, double val,CentrelizedLRTASearchNode pre)
    {


        //update
        if(pre!=null) {
            Set<CentrelizedLRTASearchNode> preBack = this.needToUpdateg.get(pre.getState().getId());
            if(preBack == null)
                preBack = new HashSet<>();
            preBack.add(node);
        }



        Set<CentrelizedLRTASearchNode>back = this.needToUpdateg.get(node.getState().getId());
        if(back == null)
            back = new HashSet<>();
        this.needToUpdateg.put(node.getState().getId(),back);

        node.setgVal(val,pre);
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
    private Set<CentrelizedLRTASearchNode> transformSet(Set<CentrelizedLRTAState> states,CentrelizedLRTASearchNode pre)
    {
        Set<CentrelizedLRTASearchNode> transStates = new HashSet<>();
        for(CentrelizedLRTAState state : states)
        {

            transStates.add(transformSingle(state,pre));

        }
        return transStates;
    }

    /**
     * This function will transform a single state into a search node
     * @param state - The given state
     * @return - The search node
     */
    private CentrelizedLRTASearchNode transformSingle(CentrelizedLRTAState state,CentrelizedLRTASearchNode pre)
    {

        Pair<Double,Double> vals = this.info.get(state.getId());
        CentrelizedLRTASearchNode res;
        if(vals == null)
        {
            double gVal;

            res = new CentrelizedLRTASearchNode(state,pre);
            if(this.needToUpdateg.containsKey(state.getId()))
                gVal = res.getgVal();
            else
                gVal = Double.MAX_VALUE;

            this.info.put(state.getId(),new Pair<>(gVal,res.gethVal()));
            return res;
        }
        else
        {
            res = new CentrelizedLRTASearchNode(state,pre);
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

            boolean isG1=checkIfGoal(o1.getState());
            boolean isG2=checkIfGoal(o2.getState());
            if(isG1 && isG2)
            {
                return o1.getState().getTime() - o2.getState().getTime();
            }
            else
            {
                if(!(!isG1 && !isG2))
                {
                    if(isG1)
                        return -1;
                    return 1;
                }
            }
            int g1 = o1.getState().getNumInGoal();
            int g2 = o2.getState().getNumInGoal();
            if( g1!= g2)
            {
                return g2-g1;
            }
            double f1 = o1.getgVal() + o1.gethVal();
            double f2 = o2.getgVal() + o2.gethVal();
            if(f1<f2)
                return -1;
            if(f1==f2) {
                if(o1.getNumOfMoving() != o2.getNumOfMoving())
                    return o2.getNumOfMoving() - o1.getNumOfMoving();
                return o1.getState().getTime() - o2.getState().getTime();
            }
            return 1;
        }
    }
}