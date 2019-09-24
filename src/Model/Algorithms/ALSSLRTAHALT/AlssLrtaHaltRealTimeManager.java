package Model.Algorithms.ALSSLRTAHALT;

import Model.Components.Agent;
import Model.Algorithms.ALSSLRTAIGNOREOTHERS.AlssLrtaIgnoreOthersRealTimeManager;
import Model.Components.Node;
import Model.Components.Problem;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * This class represents an aLSS-LRTA* manager (half before crush)
 */
public class AlssLrtaHaltRealTimeManager extends AlssLrtaIgnoreOthersRealTimeManager {
    /**
     * The constructor of the class
     *
     * @param problem - The given problem
     */
    public AlssLrtaHaltRealTimeManager(Problem problem) {
        super(problem);
    }


    @Override
    public void move() {
        Collection<Agent> agents = new HashSet<>(problem.getAgentsAndStartGoalNodes().keySet());
        int maxLength = -1;
        this.numOfFinish = 0;
        for (Agent agent : agents) {
            List<Node> prefix = this.prefixesForAgents.get(agent);
            if(agent.isDone())
                numOfFinish++;
            //  System.out.println("Agent "+agent.getId()+" prefix is "+prefix);
            if (prefix == null) {
                return;
            }
            if (maxLength < prefix.size())
                maxLength = prefix.size();
        }

        for (int i = 0; i < maxLength; i++) {
            for (Agent agent : agents) {

                List<Node> prefix = this.prefixesForAgents.get(agent);
                if (prefix == null)
                    return;

                if (i <= prefix.size() - 1) {
                    //Halt
                    if (!agent.moveAgent(prefix.get(i))) {
                        prefix.remove(i);
                        prefix.add(agent.getCurrent());

                      //  System.out.println("Collision between agent " + agent.getId() + " and agent " + prefix.get(i).getOccupationId() + " in " + prefix.get(i) +" at iteration "+iteration);
                        prefixesForAgents.put(agent, null);
                        return;
                    }
                }
            }
        }
        for (Agent agent : agents) {

            List<Node> prefix = this.prefixesForAgents.get(agent);
            int index = prefix.size() - 1;
            Node node=prefix.get(index);
            node.moveOut();

        }
    }
}
