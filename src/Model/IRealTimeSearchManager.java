package Model;

import java.util.List;
import java.util.Map;

public interface IRealTimeSearchManager {

    /**
     * This function will search for all the agents their paths using Real-Time Search
     * @return - Key - agent, Value - The agent's path
     */
    public Map<Agent,List<Node>> search();

}
