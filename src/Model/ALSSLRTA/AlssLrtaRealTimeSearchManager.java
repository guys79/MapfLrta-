package Model.ALSSLRTA;

import Model.*;
import javafx.util.Pair;

import java.util.*;

/**
 * This class represents a manager for the aLSS-LRTA* algorithm
 */
public class AlssLrtaRealTimeSearchManager extends AbstractRealTimeSearchManager {

    private int iteration;
    /**
     * The constructor
     * @param problem - A given problem
     */
    public AlssLrtaRealTimeSearchManager(Problem problem)
    {
       super(problem);
       iteration = 0;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    @Override
    protected void calculatePrefix()
    {
        Map<Agent,Pair<Node,Node>> agent_goal_start = problem.getAgentsAndStartGoalNodes();
        Collection<Agent> agents = agent_goal_start.keySet();

        for(Agent agent: agents)
        {
            ALSSLRTA alsslrta = new ALSSLRTA(problem);
            alsslrta.setIteration(iteration);
            Node current = agent.getCurrent();
            List<Node> prefix = alsslrta.calculatePrefix(current,agent_goal_start.get(agent).getValue(),problem.getNumberOfNodeToDevelop(),agent);
            if(prefix==null)
            {
                System.out.println("No Solution");
                this.prefixesForAgents.put(agent,prefix);
                List<Node> fail = new ArrayList<>();
                fail.add(agent_goal_start.get(agent).getKey());
                pathsForAgents.put(agent,fail);
                return;
            }

            this.prefixesForAgents.put(agent,prefix);
            List<Node> path = pathsForAgents.get(agent);
            path.remove(path.size()-1);
            path.addAll(prefix);
        }
    }

}
