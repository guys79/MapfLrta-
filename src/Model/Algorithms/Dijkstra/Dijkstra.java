package Model.Algorithms.Dijkstra;

import Model.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class represents the Dijkstra algorithm
 * Implemented using Singleton DP
 */
public class Dijkstra {

    private Node origin;//The origin node
    private Set<Integer> updatedNodes;//The nodes with the updated cost
    private static Dijkstra dijkstra;;//The instance of the class

    /**
     * The constructor
     */
    private Dijkstra()
    {
        this.updatedNodes = new HashSet<>();
    }


    /**
     * This function will return the instance of the class
     * @return - The instance of the class
     */
    public static Dijkstra getInstance()
    {
        if(dijkstra == null)
            dijkstra = new Dijkstra();
        return dijkstra;
    }

    /**
     * This function will calculate the costs from the origin node to the other nodes
     * @param origin - the origin node
     * @return - The costs
     */
    public Map<Integer,Double> calculateCosts(Node origin)
    {
        this.origin = origin;
        this.updatedNodes.clear();
        Map<Integer,Double> costs = new HashMap<>();


        // TODO: 9/11/2019 Complete this

        return costs;
    }


}
