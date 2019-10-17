package Model.Algorithms.BudgetOrientedMALRTA;

import Model.Algorithms.ALSSLRTA.AlssLrtaSearchNode;
import Model.Algorithms.MAALSSLRTA.MAALSSLRTA;
import Model.Algorithms.MAALSSLRTA.MaAlssLrtaSearchNode;
import Model.Components.Agent;
import Model.Components.Node;
import Model.Components.Problem;

import java.util.*;

/**
 * This class represents the BudgetOrientedMALRTA*
 */
public class BudgetOrientedMALRTA extends MAALSSLRTA {

    private int leftover;//The leftovers of the budget
    public static int g=0;

    /**
     * The constructor
     *
     * @param problem - The given problem
     */
    public BudgetOrientedMALRTA(Problem problem) {
        super(problem);
        leftover = 0;
        g++;

    }


    private int goByH(MaAlssLrtaSearchNode current,int base)
    {

        current.setNumInChain(0);
        int prefixLength = problem.getPrefixLength();
        int counter=current.getNumInChain();
        Set<Node> neigbors;
        double h;
        Node min;
        MaAlssLrtaSearchNode newNode;
        int used = base;
       // Agent agent = getAgent();
        clearOpen();
        while(true)
        {
            used++;
            if(current.getNode().getId() == getAgent().getGoal().getId()) {
                openAdd(current);
                //agent.setUsedBudget(used);
                return used;

            }
            neigbors = current.getNode().getNeighbors().keySet();
            h = Double.MAX_VALUE;
            min = null;
            double val;
            for(Node neighbor : neigbors)
            {
                val = getAgent().getInitialHeuristicValue(neighbor);
                if(val <h)
                {
                    h = val;
                    min = neighbor;
                }
            }

            if(canBeAtTime((current).getTime()+1,current.getNode(),min))
            {
                newNode = new MaAlssLrtaSearchNode(new AlssLrtaSearchNode(min),current.getTime()+1);
                newNode.setBack(current);
                closed.put(newNode.getNode().getId(),newNode);
                newNode.setNumInChain(current.getNumInChain() + 1);
                current = newNode;
                counter++;
            }
            else {
                openAdd(current);
                return used;
            }
            if(counter == prefixLength) {
                openAdd(current);
                return used;
            }

        }
    }
    @Override
    protected void aStarPrecedure() {
        Agent agent = getAgent();





        int develop = this.toDevelop;

        MaAlssLrtaSearchNode current =(MaAlssLrtaSearchNode) transformSingleNode(getCurrent().getNode(),0);
        int prefixLength = problem.getPrefixLength();
        int used = goByH(current,0);

        current = (MaAlssLrtaSearchNode) open.peek();
        int numInChain  = current.getNumInChain();
        if( numInChain== prefixLength || (current.getNode().getId() == agent.getGoal().getId() && canInhabit(current))) {

                agent.setUsedBudget(used);
                return;

        }


        // TODO: 12/10/2019 this is just a replica of the ALSSLRTA*, convert it to the correct strategy

        develop -=used;
        setGNode(current,0);
        clearOpen();
        openAdd(current);

        int expansions =0;
        AlssLrtaSearchNode state;

        while(expansions<develop- (prefixLength-numInChain) &&(state =open.peek()).getNumInChain()<prefixLength)
        {
           // System.out.println("Start");
            //The condition

            if(state == null)
            {
                System.out.println("No solution, but weird");
                agent.setUsedBudget(used+expansions);
                return;
            }

            if(state.getNode().getId() == getAgent().getGoal().getId())
            {

                boolean flag;
                if(state.getBack() == null)//In place
                    flag =canBeAtTime(time(state),state.getNode(),state.getNode());
                else
                    flag =canBeAtTime(time(state.getBack()),state.getBack().getNode(),state.getNode());
                //if can inhabit
                if(flag && canInhabit(state)) {

                    agent.setUsedBudget(used+expansions);
                    return;
                }
            }

            openRemove(state);
            closed.put(state.getNode().getId(),state);

            state.getNode().updateAverage(false);

            //Get the neighbors
            Set<Node> neighbors = getNeighbors(state.getNode());
            int time = time(state);
            Set<AlssLrtaSearchNode> comp_neighbors = transformNodes(neighbors,time);

            boolean isOver = open.size()==0;
         //   System.out.println("1 "+isOver);
            //For each neighbor
            for(AlssLrtaSearchNode node : comp_neighbors)
            {
                if(node.getNode().getId() == getAgent().getGoal().getId())
                {
                    if(!canInhabit(node))
                        continue;
                }
                if(!canBeAtTime(time,state.getNode(),node.getNode()))
                    continue;
                double temp = state.getG()+ problem.getCost(state.getNode(),node.getNode());
                if(node.getG()>temp)
                {
               //     System.out.println("ddd");
                    isOver = false;
                 //   System.out.println("sasasdasd");
                    setGNode(node,temp);
                    node.setBack(state);
                    node.setNumInChain(state.getNumInChain()+1);
                    openAdd(node);
                }
                else
                {
                    if(node.getG()==temp) {

                        AlssLrtaSearchNode pulled = this.closed.get(node.getNode().getId());
                        if (pulled != null) {
                            if (node instanceof MaAlssLrtaSearchNode && pulled instanceof MaAlssLrtaSearchNode) {
                                if (((MaAlssLrtaSearchNode) node).getTime() < ((MaAlssLrtaSearchNode) pulled).getTime()) {
                                    isOver = false;
                             //       System.out.println("ddd2");
                                    setGNode(node, temp);
                                    node.setBack(state);
                       //             System.out.println("9234927342");
                                    node.setNumInChain(state.getNumInChain()+1);
                                    openAdd(node);
                                }

                            }
                        }
                    }
                }

            }
            if(isOver)
            {
                openAdd(current);
                agent.setUsedBudget(expansions+used);
                break;
            }
      //      System.out.println("finish");
            expansions++;



        }

    /*    int id1 = 1;
        int id2 = 51;
        int iter = 5;
        if(g==iter && agent.getId() == id1)
            System.out.println(id1);
        if(g==iter && agent.getId() == id2)
            System.out.println(id2);*/
        used = used+expansions;

        MaAlssLrtaSearchNode best = (MaAlssLrtaSearchNode)peekBestState();

        if(best.getNumInChain() <prefixLength)
        {
            //Need to complete
      //      used = goByH(best,used);


        }
        agent.setUsedBudget(used);



    }

    public int getLeftover() {
        return leftover;
    }

    @Override
    public List<Node> calculatePrefix(Node start, Node goal, int numOfNodesToDevelop, Agent agent) {
     /*  int id1 = 42;
        int id2 = 136;

        int iter = 10;
        if(g==iter && agent.getId() == id1)
            System.out.println(id1);
        if(g==iter && agent.getId() == id2) {
            System.out.println(id2);
            System.out.println(g);
        }
*/
        int budget = numOfNodesToDevelop +leftover;//Let the agent use the leftovers
        List<Node> list =  super.calculatePrefix(start, goal, budget, agent);//Calculate prefix
       // System.out.println("used - "+agent.getUsedBudget());
    //    if(agent.getUsedBudget() == 144)
       //     System.out.println(getAgent().getId());
        this.leftover = budget-agent.getUsedBudget();//Update the amount of leftovers
        agent.setUsedBudget(0);//Reset agent's budget
        return list;

    }


}
