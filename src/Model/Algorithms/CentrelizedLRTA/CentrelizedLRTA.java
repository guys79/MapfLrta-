package Model.Algorithms.CentrelizedLRTA;

import Model.Algorithms.IRealTimeSearchAlgorithm;
import Model.Components.Agent;
import Model.Components.Node;
import javafx.util.Pair;

import java.util.*;

// TODO: 27/09/2019 Do OD + conclution, since a state is a combination of number of states, it will take some time to identify that the state is not good,
// TODO: 27/09/2019 Maybe implement aLSS-LRTA* (first do our algorithm)
// TODO: 27/09/2019 notes! 
/**
 * This class represents the centralized-Lrta*
 */
public class CentrelizedLRTA{

    protected CentrelizedLRTAState goal;//The goal
    protected CentrelizedLRTAState current;//The current state
    protected int numberOfNodes;///The number of nodes
    protected PriorityQueue<CentrelizedLRTASearchNode> open;//The open list
    protected PriorityQueue<CentrelizedLRTASearchNode> open_best;//The open list
    protected Set<CentrelizedLRTASearchNode> close;//The close list
    protected Map<String, Pair<Double,Double>>info; // key - id, pair - key - gVal val - hVal
    protected Map<String,Set<CentrelizedLRTASearchNode>> needToUpdateg;
    protected Map<String,CentrelizedLRTASearchNode> open_id;



    public int g;

    /**
     * The constructor
     */
    public CentrelizedLRTA()
    {
        info = new HashMap<>();
        g=0;
    }
    private void addOpen(CentrelizedLRTASearchNode node) {
        if (!this.open_id.containsKey(node.getState().getId())) {
            open.add(node);
            this.open_id.put(node.getState().getId(),node);
            this.open_best.add(node);
        }
    }
    private void removeOpen(CentrelizedLRTASearchNode node)
    {
        CentrelizedLRTASearchNode to_delete = this.open_id.remove(node.getState().getId());
        this.open.remove(to_delete);
        this.open_best.remove(to_delete);
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
        this.open =new PriorityQueue<>(new CompareCentrelizedSearchNodesF());
        this.open_id = new HashMap<>();
        this.open_best = new PriorityQueue<>(new CompareCentrelizedSearchNodes());
        this.close =new HashSet<>();
        if(g==100)
            System.out.println();
        //Lookahead
        CentrelizedLRTASearchNode centrelizedLRTASearchNode =lookAhead();
        //Extract th best state
        if(centrelizedLRTASearchNode ==null)
            centrelizedLRTASearchNode = bestState();
        else
            bestState();



      /*  System.out.println(centrelizedLRTASearchNode.getState()+" "+"real - "+centrelizedLRTASearchNode.getState().isRealState()+" "+getF(centrelizedLRTASearchNode)+" id - "+centrelizedLRTASearchNode.getState().getId());
        while(open_best.size() >0)
        {
            CentrelizedLRTASearchNode state = this.open_best.poll();
            System.out.println(state.getState()+" "+"real - "+state.getState().isRealState()+" "+getF(state)+" id - "+state.getState().getId());
        }*/
        update();

        List<CentrelizedLRTAState> prefixes = getPrefix(centrelizedLRTASearchNode);

      return prefixes;

    }
    protected List<CentrelizedLRTAState> getPrefix(CentrelizedLRTASearchNode centrelizedLRTASearchNode)
    {
        List<CentrelizedLRTAState> prefix = new ArrayList<>();
        CentrelizedLRTAState state;
        prefix.add(0,centrelizedLRTASearchNode.getState());
        centrelizedLRTASearchNode = centrelizedLRTASearchNode.getBack();
        while(centrelizedLRTASearchNode !=null)
        {
            state = centrelizedLRTASearchNode.getState();
            if(state.isRealState())
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
        Set<String> set = new HashSet<>();
        for(CentrelizedLRTASearchNode node: close)
            updateClose(node, set);
    }

    private double updateClose(CentrelizedLRTASearchNode node, Set<String> dontSummon)
    {
        dontSummon.add(node.getState().getId());
        Set<CentrelizedLRTASearchNode> children = this.needToUpdateg.get(node.getState().getId());
        if(children!=null && children.size()>0) {
            double min_f = Double.MAX_VALUE;
            for (CentrelizedLRTASearchNode child : children) {
                if(!dontSummon.contains(child.getState().getId()))
                    min_f = Double.min(min_f, updateClose(child,dontSummon) - node.getState().getCost(child.getState()));
                else
                {
                    min_f = Double.min(min_f, getF(child) - node.getState().getCost(child.getState()));
                }

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
    private CentrelizedLRTASearchNode lookAhead()
    {

        
        CentrelizedLRTASearchNode current = new CentrelizedLRTASearchNode(this.current,null);
        setGVal(current,0,null);
        this.needToUpdateg.put(current.getState().getId(),null);
        int expansions  = 0;
        addOpen(current);
        int gf = 0;

        while(  expansions  <this.numberOfNodes)
        {
            CentrelizedLRTASearchNode polled =  open.peek();
            if(polled == null) {
                System.out.println();
                return null;
            }
            if(checkIfGoal(polled.getState()))
                return polled;
            removeOpen(polled);
            if(polled.getState().isRealState())
                expansions++;
            close.add(polled);
            if(open_best.size() == 0)
            {
                if(gf>0)
                    System.out.println();
                else
                    gf++;
            }



            Set<CentrelizedLRTAState> neighbors = polled.getState().getLegalStatesOD();
            //Set<CentrelizedLRTAState> neighbors = polled.getState().getLegalStates();

            Set<CentrelizedLRTASearchNode> transformedNeighbors = transformSet(neighbors,polled);
            double temp;
         //   double min_f = Double.MAX_VALUE;
            for(CentrelizedLRTASearchNode node : transformedNeighbors)
            {
                temp = node.getState().getCost(polled.getState()) + polled.getgVal();
                if(node.getgVal()>temp )
                {
                    setGVal(node,temp,polled);
                    node.setBack(polled);
                    removeOpen(node);
                    addOpen(node);
                   // min_f = Double.min(min_f,getF(node));
                }
            }

        }
        System.out.println(expansions);
        return null;
    }
    private  double getF(CentrelizedLRTASearchNode node)
    {
        Pair <Double,Double> vals = this.info.get(node.getState().getId());
        double res;
        if(vals == null)
            res =node.getgVal() + node.gethVal();
        else
            res = node.getgVal() + vals.getValue();
        if(res>=Double.MAX_VALUE)
            System.out.println();
        return res;
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
        CentrelizedLRTASearchNode best = open_best.peek();
        removeOpen(best);
        return best;
    }



    /**
     * This class will compare between two nodes (Using f value ONLY)
     */
    public class CompareCentrelizedSearchNodesF implements Comparator<CentrelizedLRTASearchNode>
    {

        @Override
        public int compare(CentrelizedLRTASearchNode o1, CentrelizedLRTASearchNode o2) {
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
            boolean real1 = o1.getState().isRealState();
            boolean real2 = o2.getState().isRealState();
            if(!((real1&&real2) || (!real1&&!real2)))
            {
                if(real1)
                    return -1;
                return 1;
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
                    return o1.getNumOfMoving() - o2.getNumOfMoving();
                return o1.getState().getTime() - o2.getState().getTime();
            }
            return 1;
        }
    }
}