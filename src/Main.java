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

import java.io.*;
import java.util.*;

import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showOptionDialog;

public class Main extends Application {


    // TODO: 24/08/2019 Problem creator that read maps and scenarios




    public static void main (String [] args)
    {
        launch(args);
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        final int NUM_OF_AGENTS = 1;//Number of agents
        final int HEIGHT = 12;//The number of columns
        final int WIDTH = 12;//The number of rows
        final double DENSITY = 0.6;//The ratio between the number of walls to the overall number of nodes in the grid
        final int NUM_OF_NODES_TO_DEVELOP = 15;//The number of nodes that can be developed in a single iteration
        final int TYPE =1;// 0 - LRTA*, 1-aLSS-LRTA*
        Map<Agent, List<Node>> paths;//The paths for each agent

        long startTime;
        Problem problem;
        int numProblems =0;
        IProblemCreator problemCreator = new CSVProblem();

      //while(isThereSolution(paths)) {

            //The search
            //problem = problemCreator.getProblem(NUM_OF_AGENTS, HEIGHT, WIDTH, DENSITY, NUM_OF_NODES_TO_DEVELOP, TYPE);
            problem = problemCreator.getProblem("C:\\Users\\guys79\\Desktop\\outputs\\output.csv",NUM_OF_NODES_TO_DEVELOP,TYPE);
            //IRealTimeSearchManager realTimeSearchManager =new RealTimeSearchManager(problem);
            IRealTimeSearchManager realTimeSearchManager = new AlssLrtaRealTimeSearchManager(problem);
            problemCreator.output( "C:\\Users\\guys79\\Desktop\\outputs\\output.csv");
            startTime = System.currentTimeMillis();
            paths = realTimeSearchManager.search();
            numProblems++;
            System.out.println(numProblems);
      //  }

        //Time calculation
        long endTime = System.currentTimeMillis();
        long time = endTime-startTime;
        final int NUMBER_OF_DIGITS = 4;
        int help = (int)Math.pow(10,NUMBER_OF_DIGITS);
        double timeInSeconds = (time*1.0)/1000;
        timeInSeconds = Math.round(timeInSeconds*help)/(help*1.0);

        //Print the results
        System.out.println("TIme elapsed "+time +" ms");
        System.out.println("TIme elapsed "+timeInSeconds +" seconds");

        //The GUI
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("Controller/view.fxml").openStream());
        Controller controller = fxmlLoader.getController();
        primaryStage.setTitle("MA-LRTA*");
        primaryStage.setScene(new Scene(root, 800, 540));
        primaryStage.show();
        int [][] intGrid = problemCreator.getGridGraph();
        controller.initialize(intGrid);

        Set<Agent> agents=problem.getAgentsAndStartGoalNodes().keySet();
        for (Agent agent : agents) controller.addAgent(paths.get(agent),agent.getGoal());
        controller.draw();


    }

    /**
     * This function will check if the algorithm had found a solution for the problem
     * @param paths - The paths found for each agent
     * @return - True IFF a solution was found
     */
    public static boolean isThereSolution(Map<Agent, List<Node>> paths){
        for(List<Node> path : paths.values())
        {
            if(path.size()==1)
                return false;
        }
        return true;
    }

}
