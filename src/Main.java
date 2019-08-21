import Model.*;
import Controller.*;

import Model.ALSSLRTA.AlssLrtaRealTimeSearchManager;
import Model.LRTA.RealTimeSearchManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showOptionDialog;

public class Main extends Application {


    // TODO: 8/10/2019 Combine tw or more agents (as of right now they can collide)


    static Node [][] graph;//The graph
    static String problemInString;//The problem representation in string
    public static void main (String [] args)
    {

        launch(args);
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        final int NUM_OF_AGENTS = 1;
        final int HEIGHT = 22;
        final int WIDTH = 20;
        final double DENSITY = 0.4;
        final int NUM_OF_NODES_TO_DEVELOP = 15;

        //The search
        Problem problem = getRandomProblem(NUM_OF_AGENTS,HEIGHT,WIDTH,DENSITY,NUM_OF_NODES_TO_DEVELOP);
        //IRealTimeSearchManager realTimeSearchManager =new RealTimeSearchManager(problem);
        IRealTimeSearchManager realTimeSearchManager =new AlssLrtaRealTimeSearchManager(problem);
        output(HEIGHT,WIDTH,"output");
        System.out.println(problemInString);
        long startTime = System.currentTimeMillis();
        Map<Agent, List<Node>> paths = realTimeSearchManager.search();


        //Time calculation
        long endTime = System.currentTimeMillis();
        long time = endTime-startTime;
        final int NUMBER_OF_DIGITS = 4;
        int help = (int)Math.pow(10,NUMBER_OF_DIGITS);
        double timeInSeconds = (time*1.0)/1000;

        timeInSeconds = Math.round(timeInSeconds*help)/(help*1.0);
        System.out.println("TIme elapsed "+time +" ms");
        System.out.println("TIme elapsed "+timeInSeconds +" seconds");

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


    public static void output(int height,int width,String fileName)
    {
        //showMessageDialog(null, problemInString);
        try (PrintWriter writer = new PrintWriter(new File("C:\\Users\\guys79\\Desktop\\outputs"+fileName+".csv"))) {

            StringBuilder sb = new StringBuilder();
            System.out.println(problemInString);
            String [] toCsv = problemInString.split("\n");
            sb.append("\n,");
            for(int i=width-1;i>=0;i--)
            {
                sb.append(i+",");
            }
            sb.append("?,");
            sb.append("\n,");


            for(int i=0;i<height;i++)
            {

                for(int j=toCsv[i].length()-1;j>=0;j--)
                {
                    sb.append(toCsv[i].charAt(j)+",");
                }
                sb.append(i+",");
                sb.append("\n,");

            }

            writer.write(sb.toString());

            // System.out.println("done!");

        } catch (FileNotFoundException e) {
            char last = fileName.charAt(fileName.length()-1);
            String num;
            if(last<'0' ||last>'9')
            {
                output(height,width,fileName+"1");
                return;
            }
            else
            {
                num = "";
                while (last>='0' && last<='9')
                {
                    num = last + num;
                    fileName = fileName.substring(0,fileName.length()-1);
                    last = fileName.charAt(fileName.length()-1);
                }
            }
            int numInt = Integer.parseInt(num);
            numInt++;
            output(height,width,fileName+numInt);
        }

    }

    /**
     * This function will generate a random problem with the given parameters
     * @param numOfAgents - The number of agents
     * @param height - The height of the graph
     * @param width - The width of the graph
     * @param density - The density of the walls in the graph  (the ratio of the walls)
     * @return - A random problem
     */
    public static Problem getRandomProblem(int numOfAgents,int height,int width, double density,int toDevelop)
    {
        Map<Agent, Pair<Node,Node>> agent_start_goal_nodes = new HashMap<>();
        graph = getRandomGraph(height,width,density);
        HashSet<Node> starts = new HashSet<>();
        HashSet<Node> goals = new HashSet<>();
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

        Problem problem = new Problem(graph,agent_start_goal_nodes,toDevelop,new GridCostFunction());
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
    public static Node [][] getRandomGraph(int height , int width,double density)
    {
        Node [][] graph= new Node[height][width];
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
