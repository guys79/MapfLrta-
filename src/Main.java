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


        IProblemCreator problemCreator = new ScenarioProblemCreator();


        //The GUI
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("Controller/view.fxml").openStream());
        Controller controller = fxmlLoader.getController();
        Model model = new Model(controller, new ScenarioProblemCreator());
        model.next();
        primaryStage.setTitle("MA-LRTA*");
        primaryStage.setScene(new Scene(root, 800/8*14, 540/8*14));
        primaryStage.show();






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
