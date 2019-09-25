package Model.Algorithms.LRTA;

import Model.Algorithms.IRealTimeSearchAlgorithm;
import Model.Algorithms.AbstractRealTimeSearchManager;
import Model.Components.Agent;
import Model.Components.Node;
import Model.Components.Problem;

import java.util.*;

/**
 * This class represents the Real-Time search manager
 */
public class RealTimeSearchManager extends AbstractRealTimeSearchManager {


    /**
     * The constructor of the class
     * @param problem - The given problem
     */
    public RealTimeSearchManager(Problem problem)
    {
        super(problem);

    }

    /**
     * This function will calculate for each agent prefix
     */
    protected void calculatePrefix()
    {

        HashSet<Agent> agents = new HashSet<>(problem.getAgentsAndStartGoalNodes().keySet());
        for(Agent agent : agents)
        {
            if(agent.isDone())
                continue;

            Node current = agent.getCurrent();
            IRealTimeSearchAlgorithm searchAlgorithm= new LRTA();
            List<Node> prefix = searchAlgorithm.calculatePrefix(current,problem.getAgentsAndStartGoalNodes().get(agent).getValue(),problem.getNumberOfNodeToDevelop(),agent);
            this.prefixesForAgents.put(agent,prefix);
            List<Node> path = this.pathsForAgents.get(agent);
            path.remove(path.size()-1);
            path.addAll(prefix);
        }

    }



}
