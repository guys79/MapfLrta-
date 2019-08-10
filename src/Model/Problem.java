package Model;

import javafx.util.Pair;

import java.util.Map;
import java.util.Set;

/**
 * This class represents an problem
 */
public class Problem {

    private Map<Agent,Pair<Node,Node>> agentsAndStartGoalNodes;//A map from an agent to a pair of start/goal nodes
    private int numberOfNodeToDevelop;//The number of nodes that will be developed for each agent in each iteration
    /**
     * The constructor of the class
     * @param pathTOFIle - the given path to the problem instance
     * @param agentsAndStartGoalNodes - A map from an agent to a pair of start/goal nodes
     * @param numIfNodeToDevelop - The number of nodes that will be developed for each agent in each iteration
     */
    public Problem(String pathTOFIle, Map<Agent,Pair<Node,Node>> agentsAndStartGoalNodes, int numIfNodeToDevelop)
    {
        this.agentsAndStartGoalNodes = agentsAndStartGoalNodes;
        this.numberOfNodeToDevelop = numIfNodeToDevelop;
        initializeMap(pathTOFIle);

    }
    /**
     * The constructor of the class
     * @param graph - the given graph
     * @param agentsAndStartGoalNodes - A map from an agent to a pair of start/goal nodes
     * @param numIfNodeToDevelop - The number of nodes that will be developed for each agent in each iteration
     */
    public Problem(Node[][] graph, Map<Agent,Pair<Node,Node>> agentsAndStartGoalNodes, int numIfNodeToDevelop)
    {
        this.agentsAndStartGoalNodes = agentsAndStartGoalNodes;
        this.numberOfNodeToDevelop = numIfNodeToDevelop;
        initializeMap(graph);

        print(graph,agentsAndStartGoalNodes);

    }

    private void print(Node [][] graph, Map<Agent,Pair<Node,Node>> agentsAndStartGoalNodes)
    {
        for(int i=0;i<graph.length;i++)
        {
            for(int j=0;j<graph[i].length;j++)
            {
                if(graph[i][j]!=null)
                    System.out.print(1);
                else
                    System.out.print(0);
            }
            System.out.println();
        }

        Set<Agent> agents = agentsAndStartGoalNodes.keySet();
        for(Agent agent : agents)
        {
            System.out.println("Model.Agent "+agent.getId()+" starts from "+agentsAndStartGoalNodes.get(agent).getKey()+" to "+agentsAndStartGoalNodes.get(agent).getValue());
        }


    }

    /**
     * This function will return the number of nodes developed in each iteration
     * @return - The number of nodes developed in each iteration
     */
    public int getNumberOfNodeToDevelop() {
        return numberOfNodeToDevelop;
    }

    /**
     * This function returns the agents and their start/goal states
     * @return
     */
    public Map<Agent, Pair<Node, Node>> getAgentsAndStartGoalNodes() {
        return agentsAndStartGoalNodes;
    }

    /**
     * This function will initialize the graph using a path to ta file
     * @param pathTOFIle - the given path
     */
    private void initializeMap(String pathTOFIle)
    {

    }
    /**
     * This function will initialize the graph using an array of nodes
     * The assumption that the array of nodes is an array of Model.GridNode
     * each edge costs 1, diagonal cost sqrt(2), eah edge is undirected
     * @param graph - the given graph
     */
    private void initializeMap(Node [][] graph)
    {
        double sqrt2 = Math.sqrt(2);
        for(int i=0;i<graph.length;i++)
        {
            for(int j=0;j<graph[i].length;j++)
            {
                if(graph[i][j]==null)
                    continue;
                //Up
                if(i>0 && graph[i-1][j]!=null)
                    createDirectedEdge(graph[i][j],graph[i-1][j],1);
                //Down
                if(i<graph.length-1 && graph[i+1][j]!=null)
                    createDirectedEdge(graph[i][j],graph[i+1][j],1);
                //Left
                if(j>0 && graph[i][j-1]!=null)
                    createDirectedEdge(graph[i][j],graph[i][j-1],1);
                //Right
                if(j<graph[i].length-1 && graph[i][j+1]!=null)
                    createDirectedEdge(graph[i][j],graph[i][j+1],1);
                //Up-left
                if(i>0 && j>0 && graph[i-1][j-1]!=null)
                    createDirectedEdge(graph[i][j],graph[i-1][j-1],sqrt2);
                //Up-right
                if(i>0 && j<graph[i].length-1 && graph[i-1][j+1]!=null)
                    createDirectedEdge(graph[i][j],graph[i-1][j+1],sqrt2);
                //Down-left
                if(i<graph.length-1 && j>0 && graph[i+1][j-1]!=null)
                    createDirectedEdge(graph[i][j],graph[i+1][j-1],sqrt2);
                //Down-right
                if(i<graph.length-1 && j<graph[i].length-1 && graph[i+1][j+1]!=null)
                    createDirectedEdge(graph[i][j],graph[i+1][j+1],sqrt2);

            }
        }
    }

    /**
     * This function will create a directed edge from the origin to the target wighting the given wight
     * @param origin - The origin node
     * @param target - The target node
     * @param weight - The weight of the edge that connects the two nodes
     */
    private void createDirectedEdge(Node origin, Node target, double weight)
    {
        origin.addNeighbor(target,weight);
    }

    /**
     * This function will create an undirected edge from the origin to the target wighting the given wight (both ways)
     * @param origin - The origin node
     * @param target - The target node
     * @param weight - The weight of the edge that connects the two nodes
     */
    private void createUndirectedEdge(Node origin, Node target, double weight)
    {
        origin.addNeighbor(target,weight);
        target.addNeighbor(origin,weight);
    }





}
