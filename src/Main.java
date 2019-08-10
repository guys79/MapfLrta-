import Model.*;
import Controller.*;

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
    static Node [][] graph;
    public static void main (String [] args)
    {

        launch(args);
    }

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

        Problem problem = new Problem(graph,agent_start_goal_nodes,TO_DEVELOP);
        return problem;
    }

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

    @Override
    public void start(Stage primaryStage) throws Exception {
        //The search
        Problem problem = getRandomProblem(2,10,10,0.33);
        RealTimeSearchManager realTimeSearchManager =new RealTimeSearchManager(problem);
        Map<Agent, List<Node>> paths = realTimeSearchManager.search();

        //The GUI
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("Controller/view.fxml").openStream());
        Controller controller = fxmlLoader.getController();
        primaryStage.setTitle("iBundle");
        primaryStage.setScene(new Scene(root, 800, 540));
        primaryStage.show();
        int [][] intGrid = intoIntGrid(graph);
        controller.initialize(intGrid);

        Set<Agent> agents=problem.getAgentsAndStartGoalNodes().keySet();
        for (Agent agent : agents) controller.addAgent(paths.get(agent));
        controller.draw();


    }
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
