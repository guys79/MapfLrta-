/**
 * This class represents an agent
 */
public class Agent {

    private IAgentHeuristics heuristics;//The agent's individual heuristics
    private int id;//The id of the agent
    private Node current;//The current node the agent is on

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



    /**
     * This function will return the agent's current location
     * @return - The agent's current location
     */
    public Node getCurrentLocation() {
        return current;
    }

    /**
     * This function will move the agent to the targeted if possible
     * @param target - The target node
     * @return - True IFF it was possible to move the agent to the target
     */
    public boolean moveAgent(Node target)
    {
        //If the node is a neighbor of the current node and is not occupied
        if(!current.isNeighbor(target) || !target.moveIn(this.id))
            return false;
        current = target;
        return true;

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
