package Model.Components;

import Model.Algorithms.ALSSLRTA.AlssLrtaAgentHeuristics;
import Model.Algorithms.LRTA.AgentHeuristics;

import java.util.HashSet;

/**
 * This class represents an agent
 */
public class Agent {

    private IAgentHeuristics heuristics;//The agent's individual heuristics
    private int id;//The id of the agent
    private Node current;//The current node the agent is on
    private boolean isDone;//True IFF the agent reached the goal
    private HashSet<Integer> needToBeUpdated;//The set of nodes that their "updated" flag  = true
    /**
     * The constructor of the agent
     * @param id - The id of the agent
     * @param goal - The goal node
     * @param type - The type of search
     */
    public Agent(int id, Node goal, int type)
    {
        if(type == 0)
            heuristics = new AgentHeuristics(goal);
        else
        {
            heuristics = new AlssLrtaAgentHeuristics(goal);
            //heuristics = new ShortestPathAgentHeuristics(goal);
        }
        this.id = id;
        this.isDone = false;

        this.needToBeUpdated = new HashSet();
    }


    /**
     * This funciton will set the Set of agents that their "updated" flag is true
     * @param needToBeUpdated - The Set
     */
    public void setNeedToBeUpdated(HashSet<Integer> needToBeUpdated) {
        this.needToBeUpdated = needToBeUpdated;
    }

    /**
     * This function will return a Set of the agents that their "updated" flag is true
     * @return - a Set of the agents that their "updated" flag is true
     */
    public HashSet<Integer> getNeedToBeUpdated() {
        return needToBeUpdated;
    }

    /**
     * This function will return the goal nde of the agent
     * @return - The goal node of the agent
     */
    public Node getGoal()
    {
        return ((AlssLrtaAgentHeuristics)heuristics).getGoal();

    }
    /**
     * This function will return true IFF the agent has reached the goal
     * @return True IFF the agent has reached the goal
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * This function will update the heuristic value for the given node wih the new Value
     * @param node - The given node
     * @param newVal - The new Value
     */
    public void updateHeuristic(Node node, double newVal)
    {
        this.heuristics.updateHeuristics(node,newVal);

    }

    /**
     * This function will return the id of the agent
     * @return - The id of the agent
     */
    public int getId() {
        return id;
    }

    /**
     * This function will return the heuristic value of the node from this agent's perspective.
     * @param n - The given node
     * @return - The heuristic value
     */
    public double getHeuristicValue(Node n)
    {
        return this.heuristics.getHeuristics(n);
    }

    /**
     * This function will return the heuristic value of the node from this agent's perspective.
     * @param n - The given node
     * @return - The heuristic value
     */
    public double getInitialHeuristicValue(Node n)
    {
        return this.heuristics.getInitialHeuristicValue(n);
    }
    /**
     * This function will return the current state of the agent
     * @return - The current state of the agent
     */
    public Node getCurrent() {
        return current;
    }

    /**
     * This function will return the agent's current location
     * @return - The agent's current location
     */
    public Node getCurrentLocation() {
        return current;
    }

    /**
     * This function will set the current state the agent is in
     * @param current - The current state
     */
    public void setCurrent(Node current) {
        this.current = current;
        current.moveIn(this.id);
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
        {

            return false;
        }
        if(current.getId()!= target.getId()) {
            current.moveOut();
            current = target;
        }


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

    /**
     * This function declares that an agent is done searching for it's path
     */
    public void done() {
        isDone = true;
    }

    /**
     * This function will check whether the given node's heuristic value has been updated
     * @param node - The given node
     * @return - True IFF the node's heuristic value has been updated
     */
    public boolean checkIfUpdated(Node node)
    {
        if(heuristics instanceof AlssLrtaAgentHeuristics)
            return ((AlssLrtaAgentHeuristics)heuristics).checkIfUpdated(node);
        return false;
    }
}
