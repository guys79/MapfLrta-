package Model.Algorithms.BudgetOrientedMALRTA;

import Model.Algorithms.ALSSLRTA.AlssLrtaSearchNode;
import Model.Algorithms.MAALSSLRTA.MAALSSLRTA;
import Model.Algorithms.MAALSSLRTA.MaAlssLrtaSearchNode;
import Model.Components.Agent;
import Model.Components.Node;
import Model.Components.Problem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class represents the BudgetOrientedMALRTA*
 */
public class BudgetOrientedMALRTA extends MAALSSLRTA {

    private int leftover;//The leftovers of the budget
    /**
     * The constructor
     *
     * @param problem - The given problem
     */
    public BudgetOrientedMALRTA(Problem problem) {
        super(problem);
        leftover = 0;
    }



    @Override
    protected void aStarPrecedure() {









        // TODO: 12/10/2019 this is just a replica of the ALSSLRTA*, convert it to the correct strategy
        AlssLrtaSearchNode current  = getCurrent();
        setGNode(current,0);
        clearOpen();
        openAdd(current);

        int expansions =0;
        AlssLrtaSearchNode state;

        while(expansions<problem.getNumberOfNodeToDevelop())
        {



            //The condition
            state = open.peek();
            if(state == null)
            {
                System.out.println("No solution, but weird");
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
                if(flag) {
                    //closed.clear();
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

                    setGNode(node,temp);
                    node.setBack(state);
                    node.setNumInChain(state.getNumInChain());
                    openAdd(node);
                }
                else
                {
                    if(node.getG()==temp) {
                        AlssLrtaSearchNode pulled = this.closed.get(node.getNode().getId());
                        if (pulled != null) {
                            if (node instanceof MaAlssLrtaSearchNode && pulled instanceof MaAlssLrtaSearchNode) {
                                if (((MaAlssLrtaSearchNode) node).getTime() < ((MaAlssLrtaSearchNode) pulled).getTime()) {
                                    setGNode(node, temp);
                                    node.setBack(state);
                                    openAdd(node);
                                }

                            }
                        }
                    }
                }
            }
            expansions++;


        }



    }

    @Override
    public List<Node> calculatePrefix(Node start, Node goal, int numOfNodesToDevelop, Agent agent) {
        int budget = numOfNodesToDevelop +leftover;//Let the agent use the leftovers
        List<Node> list =  super.calculatePrefix(start, goal, budget, agent);//Calculate prefix
        this.leftover = budget-agent.getUsedBudget();//Update the amount of leftovers
        agent.setUsedBudget(0);//Reset agent's budget
        return list;

    }


}
