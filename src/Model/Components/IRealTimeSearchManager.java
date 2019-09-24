package Model.Components;

import Model.Components.Agent;
import Model.Model;

import java.util.List;
import java.util.Map;

/**
 * This class represents a generic Real time saerch manager
 */
public interface IRealTimeSearchManager {

    /**
     * This function will search for all the agents their paths using Real-Time Search
     * @return - Key - agent, Value - The agent's path
     */
    public Map<Agent,List<Node>> search();

}
