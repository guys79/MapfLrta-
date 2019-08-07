import java.util.HashMap;
import java.util.Map;

/**
 * This class will represent a node in the graph
 * Each edge is directed
 */
public class Node {
    private int id;//The id of the node
    private Map<Node,Double> neighbors;//The neighbors of the node

    /**
     * The constructor of the node
     * @param id - The given id
     */
    public Node(int id)
    {
        this.id = id;
        this.neighbors = new HashMap<>();
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
     * @return
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
}
