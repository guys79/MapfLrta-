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


    // TODO: 25/08/2019 Try to make the canvas change the locations of the agents only (instead of drawing everything eberyProblem) 
    // TODO: 25/08/2019 comments 

    public static void main (String [] args)
    {
        launch(args);
    }



    @Override
    public void start(Stage primaryStage) throws Exception {

        //The GUI
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("Controller/view.fxml").openStream());
        Controller controller = fxmlLoader.getController();
        Model model = new Model(controller, new ScenarioProblemCreator());
        model.next();
        primaryStage.setTitle("MA-LRTA*");
        primaryStage.setScene(new Scene(root, 1500, 1045));
        primaryStage.show();

    }



}
