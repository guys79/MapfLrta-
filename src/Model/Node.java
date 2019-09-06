package Model;

import java.util.HashMap;
import java.util.Map;

/**
 * This class will represent a node in the graph
 * Each edge is directed
 */
public class Node {
    private int id;//The id of the node
    private Map<Node,Double> neighbors;//The neighbors of the node
    public static int numOfNodes = 0;//Determines the id of the node
    private int occupationId;//The id of the agent that is currently occupying the node

    /**
     * The constructor of the node
     */
    public Node()
    {
        this.id = numOfNodes;
        numOfNodes++;
        this.neighbors = new HashMap<>();
        this.occupationId = -1;
    }

    /**
     * This function will return the weight between the node to the given node
     * @param n - The given node
     * @return - The weight of the edge that connects the, In case there is no edge,the weight is infinity
     */
    public double getWeight(Node n)
    {
        if(this.neighbors.containsKey(n))
            return this.neighbors.get(n);
        return Double.MAX_VALUE;
    }
    /**
     * This function will add a neighbor to the node
     * @param neighbor - The given neighbor
     * @param weight - the weight of the edge that connects the node
     *               to it's neighbor (not the other way around)
     */
    public void addNeighbor(Node neighbor, double weight)
    {
        this.neighbors.put(neighbor,weight);
    }


    /**
     * This function will return the id of the node
     * @return -The id of the mode
     */
    public int getId() {
        return id;
    }



    /**
     * This function will return the neighbors of this node
     * @return - Key - Node, Value - The weight of the edge that connects this node to the neighbor
     */
    public Map<Node, Double> getNeighbors() {
        return neighbors;
    }


    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof  Node))
            return false;
        return ((Node) obj).id == this.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    /**
     * This function will set the id of the agent that is currently occupying the node
     * @param occupationId - The given id
     */
    private void setOccupationId(int occupationId) {
        this.occupationId = occupationId;
    }

    /**
     * This function will set the occupation id
     * @return - The occupation id
     */
    public int getOccupationId() {
        return occupationId;
    }

    /**
     * This function will remove the agent from the current node
     */
    public void moveOut()
    {
        this.occupationId = -1;
    }

    /**
     * This function will declare the agent with the given id
     * As the one who occupies the node
     * If it cannot be done, the function will return false
     * @param occupationId - The given id
     * @return - True IFF it was possible to declare the agent as the agent that is currently occupies the node
     */
    public boolean moveIn(int occupationId)
    {
        if(!canMoveIn(occupationId))
            return false;
        this.occupationId = occupationId;
        return true;

    }

    /**
     * This function checks if it is possible for the agent to occupy the node
     * @param occupationId - The agent's id
     * @return - True IFF it is possible to declare the agent as the agent that is currently occupies the node
     */
    private boolean canMoveIn(int occupationId)
    {
        return this.occupationId == -1 || occupationId == this.occupationId;
    }

    /**
     * This function will return true IFF the given node is a neighbor of this node
     * @param node - The given node
     * @return - true IFF the given node is a neighbor of this node
     */
    public boolean isNeighbor(Node node)
    {
        return this.equals(node) || this.neighbors.containsKey(node);
    }

    @Override
    public String toString() {
        return ""+this.id;
    }
}
