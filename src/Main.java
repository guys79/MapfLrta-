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


    // TODO: 24/08/2019 Problem creator that read maps and senerios
    // TODO: 24/08/2019 Comments.. 



    public static void main (String [] args)
    {
        launch(args);
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        final int NUM_OF_AGENTS = 1;
        final int HEIGHT = 12;
        final int WIDTH = 12;
        final double DENSITY = 0.6;
        final int NUM_OF_NODES_TO_DEVELOP = 15  ;
        final int TYPE =1;
        Map<Agent, List<Node>> paths = new HashMap<>();
        long startTime=0;
        Problem problem = null;
        int numProblems =0;
        IProblemCreator problemCreator = new RandomProblem();
      //  while(isThereSolution(paths)) {

            //The search
            problem = problemCreator.getProblem(NUM_OF_AGENTS, HEIGHT, WIDTH, DENSITY, NUM_OF_NODES_TO_DEVELOP, TYPE);

            //IRealTimeSearchManager realTimeSearchManager =new RealTimeSearchManager(problem);
            IRealTimeSearchManager realTimeSearchManager = new AlssLrtaRealTimeSearchManager(problem);
            problemCreator.output(HEIGHT, WIDTH, "output");
       //     System.out.println(problemInString);
            startTime = System.currentTimeMillis();
            paths = realTimeSearchManager.search();
            numProblems++;
            System.out.println(numProblems);
     //   }

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
        int [][] intGrid = problemCreator.getGridGraph();
        controller.initialize(intGrid);

        Set<Agent> agents=problem.getAgentsAndStartGoalNodes().keySet();
        for (Agent agent : agents) controller.addAgent(paths.get(agent),agent.getGoal());
        controller.draw();


    }






}
