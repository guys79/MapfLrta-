package Model.Algorithms.BudgetOrientedMALRTA;

import Model.Algorithms.ALSSLRTA.AlssLrtaSearchNode;
import Model.Algorithms.MAALSSLRTA.MAALSSLRTA;
import Model.Algorithms.MAALSSLRTA.MaAlssLrtaRealTimeSearchManager;
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
    private boolean backtracking;
    private Agent other;



    /**
     * The constructor
     *
     * @param problem - The given problem
     */
    public BudgetOrientedMALRTA(Problem problem, MaAlssLrtaRealTimeSearchManager maneger) {
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
            if(counter == prefixLength || counter>=toDevelop) {
                openAdd(current);
                return used;
            }

        }
    }
    private int aStar(int develop,MaAlssLrtaSearchNode current,int used)
    {
        Agent agent = getAgent();
        int prefixLength = problem.getPrefixLength();
        setGNode(current,0);
        clearOpen();
        openAdd(current);

        int expansions =0;
        AlssLrtaSearchNode state;

        while(expansions<develop-used &&(state =open.peek()).getNumInChain()<prefixLength)
        {
            // System.out.println("Start");
            //The condition

            if(state == null)
            {
                System.out.println("No solution, but weird");
                return used+expansions;

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


                    return used+expansions;
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
        return used+expansions;
    }
    @Override
    protected void aStarPrecedure() {


        backtracking = false;
        other=null;
        Agent agent = getAgent();

        int develop = this.toDevelop;

        MaAlssLrtaSearchNode current =(MaAlssLrtaSearchNode) transformSingleNode(getCurrent().getNode(),0);
        MaAlssLrtaSearchNode firstNode = current;
        int prefixLength = problem.getPrefixLength();
        int used = goByH(current,0);

        current = (MaAlssLrtaSearchNode) open.peek();

        int numInChain  = current.getNumInChain();
        if( numInChain== prefixLength || (current.getNode().getId() == agent.getGoal().getId() && canInhabit(current))) {

                agent.setUsedBudget(used);
                return;

        }



        develop -=used;
        used = aStar(develop,current,used);



        MaAlssLrtaSearchNode best = (MaAlssLrtaSearchNode)peekBestState();

        if(!canInhabit(best))
        {
            closed.clear();

            this.setCurrent(firstNode);
            used = aStar(toDevelop,firstNode,used);//Agent recalculate

            //Nothing changed
            if(best.getNode().getId() == peekBestState().getNode().getId())
            {

                backtracking = true;
                other = whoInhabit(best);
                other.setProblomatic(agent);
                if(checkForCircle(agent))
                {
                    backtracking = false;
                }
                System.out.println("best - "+best.getNode().getId()+ " " +best+" agent "+agent.getId());
              /*  if(other.getId() == 172 && agent.getId() == 190)
                {
                    if(gf)
                    {
                        System.out.println(canInhabit(best));
                    }

                    gf = true;
                }*/

            }
        }

        agent.setUsedBudget(used);



    }
    private void removeUpdate(Node node,int time)
    {
        this.ocuupied_times.get(node.getId()).remove(time);
    }
    public int getLeftover() {
        return leftover;
    }

    @Override
    public List<Node> calculatePrefix(Node start, Node goal, int numOfNodesToDevelop, Agent agent) {
/*        int id1 = 125;
        int id2 = 13;

        int iter = 2;
        if(g==iter && agent.getId() == id1)
            System.out.println(id1);
        if(g==iter && agent.getId() == id2) {
            System.out.println(id2);
            System.out.println(g);
        }
*/
        if(g == 2 && agent.getId() ==125)
        {
            System.out.println();
        }




        int budget = numOfNodesToDevelop +leftover;//Let the agent use the leftovers
        List<Node> list =  super.calculatePrefix(start, goal, budget, agent);//Calculate prefix

       // System.out.println("used - "+agent.getUsedBudget());
    //    if(agent.getUsedBudget() == 144)
       //     System.out.println(getAgent().getId());

        this.leftover = budget-agent.getUsedBudget();//Update the amount of leftovers


        if(backtracking)
        {
            backtracking = false;

            List<Node> prefix = getPrefix(other);
        /*    System.out.println("Other "+other.getId());
            if(prefix == null)
            {
                System.out.println();
            }*/
            removePrefix(other);
            if(prefix == null)
            {
                System.out.println("???");
                return null;
            }
            for(int i=0;i<prefix.size();i++)
                removeUpdate(prefix.get(i),i);
            this.goals.remove(prefix.get(prefix.size()-1).getId());

            //Add other to queue
            this.addAgentToQueue(other);

            return calculatePrefix(start,goal,0,agent);
        }
       // System.out.println("Agent "+agent.getId()+" used "+agent.getUsedBudget()+" there is "+leftover+" left");
        agent.setUsedBudget(0);//Reset agent's budget
        return list;

    }



    protected Agent whoInhabit(AlssLrtaSearchNode node) {


        if(this.goals.containsKey(node.getNode().getId()))
            return goals.get(node.getNode().getId()).getValue();


        MaAlssLrtaSearchNode ma = (MaAlssLrtaSearchNode)node;
        Map<Integer,Agent> time_agents = this.ocuupied_times.get(ma.getNode().getId());

        Set<Integer> times = time_agents.keySet();
        int time = ma.getTime();

        for(Integer timeSet : times)
        {
            if(time<=timeSet)
                return time_agents.get(timeSet);
        }
        return null;


    }

    private boolean checkForCircle(Agent agent)
    {
        int counter = 0;
        while(agent!=null)
        {
            counter++;
            agent = agent.getProblomatic();
            if(problem.getAgentsAndStartGoalNodes().size() <=counter)
                return true;
        }
        return false;
    }

}
