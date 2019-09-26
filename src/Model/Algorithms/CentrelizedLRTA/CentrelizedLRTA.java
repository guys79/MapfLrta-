package Model.Algorithms.CentrelizedLRTA;

import Model.Algorithms.IRealTimeSearchAlgorithm;
import Model.Components.Agent;
import Model.Components.Node;
import javafx.util.Pair;

import java.util.*;

public class CentrelizedLRTA{

    private CentrelizedLRTAState goal;
    private CentrelizedLRTAState current;
    private int numberOfNodes;
    private PriorityQueue<CentrelizedLRTASearchNode> open;
    private PriorityQueue<CentrelizedLRTASearchNode> close;
    private Map<String, Pair<Double,Double>>info; // key - id, pair - key - gVal val - hVal

    public CentrelizedLRTA()
    {
        info = new HashMap<>();
    }
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

    private boolean checkIfGoal(CentrelizedLRTAState state)
    {
        return state.equals(goal);
    }
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
    private void setGVal(CentrelizedLRTASearchNode node, double val)
    {
        node.setgVal(val);
        Pair<Double,Double> vals = this.info.get(node.getState().getId());
        if(vals == null)
            this.info.put(node.getState().getId(),new Pair<>(val,node.gethVal()));
        else
            this.info.put(node.getState().getId(),new Pair<>(val,vals.getValue()));
    }
    private Set<CentrelizedLRTASearchNode> transformSet(Set<CentrelizedLRTAState> states)
    {
        Set<CentrelizedLRTASearchNode> transStates = new HashSet<>();
        for(CentrelizedLRTAState state : states)
        {
            transStates.add(transformSingle(state));
        }
        return transStates;
    }
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
            res.setgVal(vals.getKey());
            res.sethVal(vals.getValue());
            return res;
        }
    }
    private CentrelizedLRTASearchNode bestState()
    {
        return open.poll();
    }


    
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