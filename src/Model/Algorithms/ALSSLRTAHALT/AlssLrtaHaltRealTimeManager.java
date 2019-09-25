package Model.Algorithms.ALSSLRTAHALT;

import Model.Algorithms.ALSSLRTA.ALSSLRTA;
import Model.Components.AbstractRealTimeSearchManager;
import Model.Components.Agent;
import Model.Components.Node;
import Model.Components.Problem;
import javafx.util.Pair;

import java.util.*;

/**
 * This class represents an aLSS-LRTA* manager (half before crush)
 */
public class AlssLrtaHaltRealTimeManager extends AbstractRealTimeSearchManager {

    private int maxLength;

    Set<Integer> haltedAgents;
    /**
     * The constructor of the class
     *
     * @param problem - The given problem
     */
    public AlssLrtaHaltRealTimeManager(Problem problem) {
        super(problem);
        this.haltedAgents = new HashSet<>();
    }



    @Override
    protected void calculatePrefix() {
        prev = new HashMap<>();


        //Calculate the prefixes for all agents
        for (Agent agent : this.prefixesForAgents.keySet()) {
            //Calculate the prefixes for all agents who are not done
            //  if(!agent.isDone())
            prev.put(agent, agent.getCurrent());
        }


        //Get prefixes
        Map<Agent, Pair<Node, Node>> agent_goal_start = problem.getAgentsAndStartGoalNodes();
        Collection<Agent> agents = agent_goal_start.keySet();
        Map<Integer, List<Node>> prefixes = new HashMap<>();
        List<Node> prefix;
        for(Agent agent : agents)
        {

            ALSSLRTA alsslrta = new ALSSLRTA(problem);
            this.prev.clear();
            prefix = alsslrta.calculatePrefix(agent.getCurrent(),agent.getGoal(),problem.getNumberOfNodeToDevelop(),agent);
            if(prefix == null)
            {
                prefixesForAgents.put(agent, null);
                List<Node> fail = new ArrayList<>();
                //fail.add(agent_goal_start.get(agent).getKey());
                fail.add(agent.getCurrent());
                prefix = fail;

            }
            prefixes.put(agent.getId(),prefix);
        }



        //If there is no solution
        if (prefixes.containsValue(null)) {

            for (Agent agent : agents) {
                prefix = prefixes.get(agent);
                if (prefix == null) {
                    //System.out.println("No Solution");
                    prefixesForAgents.put(agent, null);
                    List<Node> fail = new ArrayList<>();
                    //fail.add(agent_goal_start.get(agent).getKey());
                    fail.add(agent.getCurrent());
                    prefix = fail;
                    //    pathsForAgents.put(agent, fail);
                }

                //Adding the prefix to the path
                List<Node> path = pathsForAgents.get(agent);
                path.remove(path.size() - 1);
                path.addAll(prefix);

            }
            return;
        }

        //Calculate the maximum prefix size
        maxLength = -1;
        for (Agent agent : agents) {
            this.prefixesForAgents.put(agent, prefixes.get(agent.getId()));
            prefix = this.prefixesForAgents.get(agent);
            if (prefix == null) {
                return;
            }
            if (maxLength < prefix.size())
                maxLength = prefix.size();
        }


    }

    @Override
    public void move() {

        this.haltedAgents.clear();
        Collection<Agent> agents = new HashSet<>(problem.getAgentsAndStartGoalNodes().keySet());

        for (int i = 0; i < maxLength; i++) {
            for (Agent agent : agents) {

                List<Node> prefix = this.prefixesForAgents.get(agent);
                if (prefix == null)
                    return;

                if (i <= prefix.size() - 1) {
                    Node nextNode = prefix.get(i);
                    //Halt


                        //System.out.println("second");
                    if (this.haltedAgents.contains(agent.getId()) || !agent.moveAgent(nextNode)) {
                        prefix.remove(i);
                        prefix.add(i,agent.getCurrent());
                       // if(!this.haltedAgents.contains(agent.getId()))
                          //  System.out.println("Collision between agent " + agent.getId() + " and agent " + nextNode.getOccupationId() + " in " + nextNode+" time "+i);
                        this.haltedAgents.add(agent.getId());
                  //      prefixesForAgents.put(agent, null);
                        //return;
                    }
                }
            }
        }




        for (Agent agent : agents) {



            List<Node> prefix = this.prefixesForAgents.get(agent);

            //System.out.println("The Prefix of the agent "+agent.getId()+" "+prefix);
            int index = prefix.size() - 1;
            Node node=prefix.get(index);
            node.moveOut();


            prefix = this.prefixesForAgents.get(agent);

            int length = prefix.size();
            Node last = prefix.get(length - 1);
            if (last.getId() != agent.getGoal().getId()) {
                for (int i = length; i < maxLength; i++) {
                    prefix.add(last);
                }
            }
            //Adding the prefix to the path
            List<Node> path = pathsForAgents.get(agent);
            path.remove(path.size() - 1);
            path.addAll(prefix);

        }


    }

    @Override
    public boolean isDone() {
        if(super.isDone())
            return true;
        return this.haltedAgents.size() == problem.getAgentsAndStartGoalNodes().size();
    }
}
