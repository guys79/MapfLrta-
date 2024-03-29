package Model.Components;

import Model.Algorithms.Dijkstra.ShortestPathGenerator;
import javafx.util.Pair;

import java.util.Map;
import java.util.Set;

/**
 * This class represents an problem
 */
public class Problem {

    private Map<Agent,Pair<Node,Node>> agentsAndStartGoalNodes;//A map from an agent to a pair of start/goal nodes
    private int numberOfNodeToDevelop;//The number of nodes that will be developed for each agent in each iteration
    private ICostFunction costFunction;//The costFunction
    private int visionRadius;//The vision radius for each agent
    private int prefixLength;//The vision radius for each agent
    private int type;//The type

    /**
     * This function will return the cost from moving from node 'origin' to node 'target'
     * @param origin - The origin node
     * @param target - The origin target
     * @return - The cost for moving from the 'origin' n9ode to the 'target' node
     */
    public double getCost(Node origin, Node target)
    {
        return costFunction.getCost(origin,target);
    }
    /**
     * The constructor of the class
     * @param graph - the given graph
     * @param agentsAndStartGoalNodes - A map from an agent to a pair of start/goal nodes
     * @param numIfNodeToDevelop - The number of nodes that will be developed for each agent in each iteration
     * @param costFunction - The given costFunction
     * @param visionRadius - The vision radius for each agent
     */
    public Problem(Node[][] graph, Map<Agent,Pair<Node,Node>> agentsAndStartGoalNodes, int numIfNodeToDevelop,ICostFunction costFunction,int visionRadius,int type,int prefixLength)
    {
        this.agentsAndStartGoalNodes = agentsAndStartGoalNodes;
        this.numberOfNodeToDevelop = numIfNodeToDevelop;
        this.prefixLength = prefixLength;
        this.type = type;
        this.costFunction = costFunction;
        this.visionRadius = visionRadius;
        initializeMap(graph);
        ShortestPathGenerator.getInstance().setGraph(graph,agentsAndStartGoalNodes.values());


    }

    /**
     * This function will return the prefix's length
     * @return - The prefix's length
     */
    public int getPrefixLength() {
        return prefixLength;
    }

    /**
     * This function will return the type of algorithm
     * @return - The type of algorithm
     */
    public int getType() {
        return type;
    }

    /**
     * This function will print the problem's stats
     * @param graph - The graph
     * @param agentsAndStartGoalNodes - The agents's start and goal nodes
     * @return - A string with the print info
     */
    public static String print(Node [][] graph, Map<Agent,Pair<Node,Node>> agentsAndStartGoalNodes)
    {
        String toString = "";
        for(int i=0;i<graph.length;i++)
        {
            for(int j=0;j<graph[i].length;j++)
            {
                if(graph[i][j]!=null)
                    toString+=1;
                else
                    toString+=0;
            }
            toString+="\n";
        }

        Set<Agent> agents = agentsAndStartGoalNodes.keySet();

        for(Agent agent : agents)
        {
            toString+= "Agent "+agent.getId()+" starts from "+agentsAndStartGoalNodes.get(agent).getKey()+" to "+agentsAndStartGoalNodes.get(agent).getValue()+"\n";
            int x_start = ((GridNode) agentsAndStartGoalNodes.get(agent).getKey()).getX();
            int y_start = ((GridNode)agentsAndStartGoalNodes.get(agent).getKey()).getY();
            int index = x_start*(graph[0].length+1)+y_start;
            toString = toString.substring(0,index)+'S'+toString.substring(index+1);

            int x_end = ((GridNode)agentsAndStartGoalNodes.get(agent).getValue()).getX();
            int y_end = ((GridNode)agentsAndStartGoalNodes.get(agent).getValue()).getY();
            index = x_end*(graph[0].length+1)+y_end;
            if(x_end == x_start && y_end == y_start)
            {
                toString = toString.substring(0,index)+'F'+toString.substring(index+1);
            }
            else
            {
                toString = toString.substring(0,index)+'E'+toString.substring(index+1);
            }



        }

        return toString;
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
     * @return - The agents and their start/goal states
     */
    public Map<Agent, Pair<Node, Node>> getAgentsAndStartGoalNodes() {
        return agentsAndStartGoalNodes;
    }


    /**
     * This function will initialize the graph using an array of nodes
     * The assumption that the array of nodes is an array of Model.Components.GridNode
     * each edge costs 1, diagonal cost sqrt(2), eah edge is undirected
     * @param graph - the given graph
     */
    private void initializeMap(Node [][] graph)
    {
        boolean right,left,up,down;
        for(int i=0;i<graph.length;i++)
        {
            for(int j=0;j<graph[i].length;j++)
            {
                if(graph[i][j]==null)
                    continue;
                graph[i][j].clear();
                right = false;
                left = false;
                up = false;
                down = false;

                //Up
                if(i>0 && graph[i-1][j]!=null) {
                    createDirectedEdge(graph[i][j], graph[i - 1][j], costFunction.getCost(graph[i][j], graph[i - 1][j]));
                    up = true;
                }
                //Down
                if(i<graph.length-1 && graph[i+1][j]!=null) {
                    createDirectedEdge(graph[i][j], graph[i + 1][j], costFunction.getCost(graph[i][j], graph[i + 1][j]));
                    down = true;
                }
                //Left
                if(j>0 && graph[i][j-1]!=null) {
                    createDirectedEdge(graph[i][j], graph[i][j - 1], costFunction.getCost(graph[i][j], graph[i][j - 1]));
                    left = true;
                }
                //Right
                if(j<graph[i].length-1 && graph[i][j+1]!=null) {
                    createDirectedEdge(graph[i][j], graph[i][j + 1], costFunction.getCost(graph[i][j], graph[i][j + 1]));
                    right = true;
                }

                //Up-left
                if(i>0 && j>0 && graph[i-1][j-1]!=null && (up || left))
                    createDirectedEdge(graph[i][j],graph[i-1][j-1],costFunction.getCost(graph[i][j],graph[i-1][j-1]));
                //Up-right
                if(i>0 && j<graph[i].length-1 && graph[i-1][j+1]!=null && (up || right))
                    createDirectedEdge(graph[i][j],graph[i-1][j+1],costFunction.getCost(graph[i][j],graph[i-1][j+1]));
                //Down-left
                if(i<graph.length-1 && j>0 && graph[i+1][j-1]!=null && (down ||  left))
                    createDirectedEdge(graph[i][j],graph[i+1][j-1],costFunction.getCost(graph[i][j],graph[i+1][j-1]));
                //Down-right
                if(i<graph.length-1 && j<graph[i].length-1 && graph[i+1][j+1]!=null && (down || right))
                    createDirectedEdge(graph[i][j],graph[i+1][j+1],costFunction.getCost(graph[i][j],graph[i+1][j+1]));


            }
        }
    }

    /**
     * This function will return the vision radius of akk the agents
     * @return - The vision radius of akk the agents
     */
    public int getVisionRadius() {
        return visionRadius;
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
