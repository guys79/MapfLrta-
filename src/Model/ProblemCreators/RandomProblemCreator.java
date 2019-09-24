package Model.ProblemCreators;

import Model.Components.*;
import Model.Model;
import Model.ProblemCreators.AbstractProblemCreator;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * This class represents a Random Problem Creator
 */
public class RandomProblemCreator extends AbstractProblemCreator {

    /**
     * The constructor
     */
    public RandomProblemCreator()
    {
        super();
    }

    @Override
    public Problem getProblem(int numOfAgents, int height, int width, double density, int toDevelop, int type, int visionRadius) {
        return getRandomProblem(numOfAgents,height,width,density,toDevelop,type,visionRadius);
    }



    /**
     * This function will generate a random problem with the given parameters
     * @param numOfAgents - The number of agents
     * @param height - The height of the graph
     * @param width - The width of the graph
     * @param density - The density of the walls in the graph  (the ratio of the walls)
     * @param type - The given search type
     * @param toDevelop  - number of nodes allowed to be developed in one iteration
     * @param visionRadius - The vision radius for each agent
     * @return - A random problem
     */
    public Problem getRandomProblem(int numOfAgents, int height, int width, double density, int toDevelop, int type, int visionRadius)
    {
        Map<Agent, Pair<Node, Node>> agent_start_goal_nodes = new HashMap<>();
        HashSet<Node> starts = new HashSet<>();
        HashSet<Node> goals = new HashSet<>();

        graph = getRandomGraph(height,width,density);
        for(int i=0;i<numOfAgents;i++)
        {
            int x = (int)(Math.random()*graph.length);
            int y = (int)(Math.random()*graph[x].length);
            while(graph[x][y]==null || starts.contains(graph[x][y]))
            {
                x = (int)(Math.random()*graph.length);
                y = (int)(Math.random()*graph[x].length);
            }
            Node start =graph[x][y];

            x = (int)(Math.random()*graph.length);
            y = (int)(Math.random()*graph[x].length);
            while(graph[x][y]==null || goals.contains(graph[x][y]))
            {
                x = (int)(Math.random()*graph.length);
                y = (int)(Math.random()*graph[x].length);
            }
            Node goal =graph[x][y];

            Agent agent = new Agent(i,goal,type);
            agent_start_goal_nodes.put(agent,new Pair<>(start,goal));
        }

        Problem problem = new Problem(graph,agent_start_goal_nodes,toDevelop,new GridCostFunction(),visionRadius);
        problemInString = Problem.print(graph,agent_start_goal_nodes);
        return problem;
    }


    /**
     * This function will generate a new graph using the given parameters
     * @param height - The height of the graph
     * @param width - The width of the graph
     * @param density - The density of the graph
     * @return - A new graph
     */
    public Node[][] getRandomGraph(int height , int width, double density)
    {
        Node[][] graph= new Node[height][width];
        int numOfRemainingWalls = (int)(density*width*height);
        int numOfRemainingNodes;
        double probForHole;

        for(int i=0;i<height;i++)
        {
            for(int j=0;j<width;j++)
            {
                numOfRemainingNodes = height*width -(i*width+ j) - numOfRemainingWalls;
                probForHole = numOfRemainingWalls*1.0/(numOfRemainingNodes+numOfRemainingWalls);
                double rand  = Math.random();
                if(rand<=probForHole)
                {
                    graph[i][j]=null;
                    numOfRemainingWalls--;
                }
                else
                {
                    graph[i][j] = new GridNode(i,j);
                }
            }
        }
        return graph;
    }
}
