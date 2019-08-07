/**
 * This class represents an agent
 */
public class Agent {

    private IAgentHeuristics heuristics;//The agent's individual heuristics
    private int id;//The id of the agent

    /**
     * The constructor of the agent
     * @param id - The id of the agent
     */
    public Agent(int id)
    {
        heuristics = new AgentHeuristics();
        this.id = id;
    }

    /**
     * This function will return the id of the agent
     * @return - The id of the agent
     */
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof  Agent))
            return false;
        return ((Agent) obj).id == this.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
