package Model.Algorithms.CentrelizedLRTA;

import Model.Components.AbstractRealTimeSearchManager;
import Model.Components.Agent;
import Model.Components.Node;
import Model.Components.Problem;
import javafx.util.Pair;

import java.util.*;

/**
 * This class represents a centralized Lrta* manager
 */
public class CentrelizedLRTARealTimeSearchManager extends AbstractRealTimeSearchManager {
    CentrelizedLRTA centrelizedLRTA;//The algorithm
    /**
     * The constructor of the class
     *
     * @param problem - The given problem
     */
    public CentrelizedLRTARealTimeSearchManager(Problem problem) {
        super(problem);
        this.centrelizedLRTA = new CentrelizedLRTA();
    }


    @Override
    public void move() {
        Collection<Agent> agents = new HashSet<>(problem.getAgentsAndStartGoalNodes().keySet());
        int maxLength = -1;
        this.numOfFinish = 0;
        for (Agent agent : agents) {
            List<Node> prefix = this.prefixesForAgents.get(agent);
            maxLength = prefix.size();
            break;
        }

        for (int i = 0; i < maxLength; i++) {
            for (Agent agent : agents) {

                List<Node> prefix = this.prefixesForAgents.get(agent);
                if (prefix == null)
                    return;

                if (i <= prefix.size() - 1) {
                    if (!agent.moveAgent(prefix.get(i))) {
                        System.out.println("Collision between agent " + agent.getId() + " and agent " + prefix.get(i).getOccupationId() + " in " + prefix.get(i) );
                        prefixesForAgents.put(agent, null);
                        return;
                    }
                }
            }
            for (Agent agent : agents) {
                agent.getCurrent().moveOut();
            }
        }
        for (Agent agent : agents) {

            List<Node> prefix = this.prefixesForAgents.get(agent);
            int index = prefix.size() - 1;
            Node node=prefix.get(index);
            node.moveOut();

        }
    }

    @Override
    protected void calculatePrefix() {
        //Init
        Map<Agent, Pair<Node,Node>> startGoal = problem.getAgentsAndStartGoalNodes();
        Node[] goal = new Node[startGoal.size()];
        Node[] start = new Node[startGoal.size()];
        Agent [] order = new Agent[startGoal.size()];
        int i=0;
        Map<Agent,List<Node>>prefixes = new HashMap<>();

        //Create start/Goal state
        for(Agent agent: startGoal.keySet())
        {
            prefixes.put(agent,new ArrayList<>());
            Pair<Node,Node> vals = startGoal.get(agent);
            order[i] = agent;
            goal[i] =vals.getValue();
            start[i] =agent.getCurrent();
            i++;
        }
        CentrelizedLRTAState goalState = new CentrelizedLRTAState(goal,Integer.MAX_VALUE,null,goal.length,false);
        CentrelizedLRTAState startState = new CentrelizedLRTAState(start,0,goalState,start.length,false);
        CentrelizedHeuristics.getInstance().setGoal(goalState);


        //Get prefixes
        List<CentrelizedLRTAState> states = this.centrelizedLRTA.calculatePrefixes(startState,goalState,problem.getNumberOfNodeToDevelop()*startGoal.size());
        for(CentrelizedLRTAState st :states)
        {
            System.out.println(st);
        }
        System.out.println();
        Agent agent;

        //If done
        if(states.get(states.size()-1).equals(goalState))
        {
            for(Agent agent2: order)
                agent2.done();
        }

        //Getting prefix for each agent
        //For each state
        for (int k=0;k<states.size();k++) {
            //Add the node from the state for each agent
            for (int j=0;j<startGoal.size();j++) {
                agent = order[j];
                prefixes.get(agent).add(states.get(k).getLocationAt(j));

            }

        }
        this.prefixesForAgents = prefixes;

        //Updating paths
        for (int j=0;j<order.length;j++) {
            agent = order[j];
            //Adding the prefix to the path
            List<Node> path = pathsForAgents.get(agent);
            path.remove(path.size()-1);
            path.addAll(prefixes.get(agent));
        }



    }
}
