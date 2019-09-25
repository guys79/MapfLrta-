package Model.Components;

import Model.Algorithms.ALSSLRTA.AlssLrtaAgentHeuristics;
import Model.Algorithms.LRTA.AgentHeuristics;

/**
 * This class will use the Factory DP + Singleton DP to create IAgentHeuristics
 */
public class HeuristicFactory {
    private static HeuristicFactory heuristicFactory;//The instance

    /**
     * The private constructor
     */
    private HeuristicFactory()
    {

    }

    /**
     * This function will return the singleton
     * @return - The instance of the class
     */
    public static HeuristicFactory getInstance()
    {
        if(heuristicFactory == null)
            heuristicFactory = new HeuristicFactory();
        return heuristicFactory;
    }


    /**
     * This function will return the heuristics mechanism for an agent
     * @param type - The type
     * @param goal - The goal
     * @return - The heuristics mechanism for an agent
     */
    public IAgentHeuristics getAgentHeuristics(int type,Node goal)
    {
        IAgentHeuristics heuristics;
        if(type == 0)
            heuristics = new AgentHeuristics(goal);
        else
        {
            heuristics = new AlssLrtaAgentHeuristics(goal);

        }
        return heuristics;
    }
}
