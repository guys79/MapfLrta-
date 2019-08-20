import Model.*;
import Controller.*;

import Model.ALSSLRTA.AlssLrtaRealTimeSearchManager;
import Model.LRTA.RealTimeSearchManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.*;

public class Main extends Application {

    // TODO: 8/10/2019 Take care of depression (Felner's solution)
    // TODO: 8/10/2019 Combine tw or more agents (as of right now they can collide)


    static Node [][] graph;//The graph
    public static void main (String [] args)
    {

        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        //The search
        Problem problem = getRandomProblem(1,10,10,0.33);
        //IRealTimeSearchManager realTimeSearchManager =new RealTimeSearchManager(problem);
        IRealTimeSearchManager realTimeSearchManager =new AlssLrtaRealTimeSearchManager(problem);
        Map<Agent, List<Node>> paths = realTimeSearchManager.search();

        //The GUI
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("Controller/view.fxml").openStream());
        Controller controller = fxmlLoader.getController();
        primaryStage.setTitle("MA-LRTA*");
        primaryStage.setScene(new Scene(root, 800, 540));
        primaryStage.show();
        int [][] intGrid = intoIntGrid(graph);
        controller.initialize(intGrid);

        Set<Agent> agents=problem.getAgentsAndStartGoalNodes().keySet();
        for (Agent agent : agents) controller.addAgent(paths.get(agent));
        controller.draw();


    }

    /**
     * This function will generate a random problem with the given parameters
     * @param numOfAgents - The number of agents
     * @param height - The height of the graph
     * @param width - The width of the graph
     * @param density - The density of the walls in the graph  (the ratio of the walls)
     * @return - A random problem
     */
    public static Problem getRandomProblem(int numOfAgents,int height,int width, double density)
    {
        Map<Agent, Pair<Node,Node>> agent_start_goal_nodes = new HashMap<>();
        graph = getRandomGraph(height,width,density);
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

        Problem problem = new Problem(graph,agent_start_goal_nodes,TO_DEVELOP,new GridCostFunction());
        return problem;
    }

    /**
     * This function will generate a new graph using the given parameters
     * @param height - The height of the graph
     * @param width - The width of the graph
     * @param density - The density of the graph
     * @return - A new graph
     */
    public static Node [][] getRandomGraph(int height , int width,double density)
    {
        Node [][] graph= new Node[width][height];
        int numOfRemainingWalls = (int)(density*width*height);
        int numOfRemainingNodes;
        double probForHole;

        for(int i=0;i<width;i++)
        {
            for(int j=0;j<height;j++)
            {
                numOfRemainingNodes = height*width -(i*height+ j) - numOfRemainingWalls;
                probForHole = numOfRemainingWalls*1.0/(numOfRemainingNodes+numOfRemainingWalls);
                double rand  = Math.random();
                //System.out.println("rand "+rand+" prob "+probForHole +" holes "+numOfRemainingWalls+ " nodes "+numOfRemainingNodes);
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


    /**
     * This function will convert the Node[][] graph into int[][] graph where
     * each cell is equal to -1 if the cell is null (wall), else, the value of
     * the cell will be the id of the node
     * @param graph - The given graph
     * @return - The same graph in the new format int [][]
     */
    public static int [][] intoIntGrid(Node [][]graph)
    {
        int [][] intGrid = new int[graph.length][graph[0].length];
        for(int i=0;i<graph.length;i++)
        {
            for(int j=0;j<graph[i].length;j++)
            {
                if(graph[i][j]==null)
                    intGrid[i][j] = -1;
                else
                {
                    intGrid[i][j]=graph[i][j].getId();
                }
            }
        }
        return  intGrid;
    }
}
