package Model.Algorithms.Dijkstra;

import Model.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * This class will generate for each node in the graph it's shortest path
 * Using Singleton DP
 */
public class ShortestPathGenerator {
    private Map<Integer,Map<Integer,Double>> shortestPaths;//Key - origin node id, value - map -{ key - target node id }
    private  static  ShortestPathGenerator shortestPathGenerator;//The instance
    private Node[][] graph;//The graph

    /**
     * The constructor
     */
    private ShortestPathGenerator()
    {
        this.shortestPaths = new HashMap<>();
        this.graph = null;
    }

    /**
     * This function will return the instance of the singleton
     * @return - The instance of this class
     */
    public static ShortestPathGenerator getInstance()
    {
        if(shortestPathGenerator == null)
            shortestPathGenerator = new ShortestPathGenerator();
        return shortestPathGenerator;
    }

    /**
     * This function will set the graph (compute the shortest paths from the given graph)
     * @param graph - The given graph
     */
    public void setGraph(Node[][] graph)
    {
        if(graph==null || this.graph == graph)
            return;
        boolean flag = true;
        boolean flag1,flag2;
        if(this.graph!=null) {
            for (int i = 0; i < this.graph.length && flag; i++) {
                for (int j = 0; j < this.graph[i].length && flag; j++) {
                    flag1 = this.graph[i][j]==null;
                    flag2 = graph[i][j]==null;
                    if(flag1|| flag2) {


                        if (((flag1 && !flag2) || (!flag1 && flag2))) {
                            flag = false;

                        }

                    }
                    else {
                        if (this.graph[i][j].getId() != graph[i][j].getId())
                            flag = false;
                    }
                }
            }
            if(flag)
                return;
        }

        this.graph = graph;
        init(graph);
    }

    /**
     * This function will calculate the shortest paths from each node to the other
     * @param graph - The given graph
     */
    private void init(Node[][]graph)
    {
        System.out.println("Start");
        for(int i=0;i<this.graph.length;i++)
        {
            System.out.println((i+1)+"/"+this.graph.length);
            for(int j=0;j<this.graph[i].length;j++)
            {

                if(this.graph[i][j]!=null)
                    calculateCost(graph[i][j]);

            }
        }
        System.out.println("Finish");
    }

    /**
     * This function will return the cost of the shortest path from the Origin node to the Target node
     * @param origin - The origin node
     * @param target - The target node
     * @return - The cost of the shortest path from the origin node to the target node
     */
    public double getShortestPath(Node origin, Node target)
    {

        Map<Integer,Double> given = this.shortestPaths.get(origin.getId());
        if(given == null)
            return Double.MAX_VALUE;
        Double cost = given.get(target.getId());
        if(cost == null)
            return Double.MAX_VALUE;
        return cost;

    }

    /**
     * This function will calculate the cost of the shortest path from the Origin node to the other nodes
     * @param origin - The origin node
     */
    private void calculateCost(Node origin)
    {
        this.shortestPaths.put(origin.getId(),Dijkstra.getInstance().calculateCosts(origin));
    }
}
