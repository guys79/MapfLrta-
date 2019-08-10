import javafx.util.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Main {

    public static void main (String [] args)
    {
        Problem problem = getRandomProblem(1,4,4,0.2);
        RealTimeSearchManager realTimeSearchManager =new RealTimeSearchManager(problem);
    }

    public static Problem getRandomProblem(int numOfAgents,int height,int width, double density)
    {
        Map<Agent, Pair<Node,Node>> agent_start_goal_nodes = new HashMap<>();
        Node [][] graph = getRandomGraph(height,width,density);
        HashSet<Node> starts = new HashSet<>();
        HashSet<Node> goals = new HashSet<>();
        final int TO_DEVELOP = 10;
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

            Agent agent = new Agent(i,goal);
            agent_start_goal_nodes.put(agent,new Pair<>(start,goal));
        }

        Problem problem = new Problem(graph,agent_start_goal_nodes,TO_DEVELOP);
        return problem;
    }

    public static Node [][] getRandomGraph(int height , int width,double density)
    {
        Node [][] graph= new Node[width][height];
        int numOfRemainingWalls = (int)(density*width*height);
        int numOfRemainingNodes = width*height - numOfRemainingWalls;
        double probForHole;

        for(int i=0;i<width;i++)
        {
            for(int j=0;j<height;j++)
            {
                numOfRemainingNodes = height*width -(i*height+ j) - numOfRemainingNodes;
                probForHole = numOfRemainingWalls/(numOfRemainingNodes+numOfRemainingWalls);
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
