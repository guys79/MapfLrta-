package Model.Algorithms.MAALSSLRTA;

import Model.AbstractRealTimeSearchManager;
import Model.Agent;
import Model.Algorithms.ALSSLRTA.AlssLrtaSearchNode;
import Model.Node;
import Model.Problem;
import javafx.util.Pair;

import java.util.*;

/**
 * This class represents a Multi Agent aLSS-LRTA*
 */
public class MaAlssLrtaRealTimeSearchManager extends AbstractRealTimeSearchManager {
    /**
     * The constructor of the class
     *
     * @param problem - The given problem
     */
    public MaAlssLrtaRealTimeSearchManager(Problem problem) {
        super(problem);
        System.out.println("hello");
    }


    @Override
    protected void calculatePrefix()
    {
        Map<Agent,Pair<Node,Node>> agent_goal_start = problem.getAgentsAndStartGoalNodes();
        Collection<Agent> agents = agent_goal_start.keySet();
        MAALSSLRTA maalsslrta = new MAALSSLRTA(problem);
        Map<Integer,List<Node>> prefixes = maalsslrta.getPrefixes(problem.getNumberOfNodeToDevelop());
        if(prefixes==null)
        {
            for(Agent agent: agents)
            {
                //System.out.println("No Solution");
                prefixesForAgents.put(agent,null);
                List<Node> fail = new ArrayList<>();
                fail.add(agent_goal_start.get(agent).getKey());
                pathsForAgents.put(agent,fail);
            }
            return;
        }
        for(Agent agent: agents)
        {
            this.prefixesForAgents.put(agent,prefixes.get(agent.getId()));
            List<Node> path = pathsForAgents.get(agent);
            path.remove(path.size()-1);
            path.addAll(prefixes.get(agent.getId()));
        }
    }



}
