import Model.*;
import Controller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main (String [] args)
    {
        launch(args);
    }

    // TODO: 05/09/2019 comments 
    // TODO: 05/09/2019 Fix Transition from map A to B in MA (create he scenarios) 
    @Override
    public void start(Stage primaryStage) throws Exception {

        //The GUI
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("Controller/view.fxml").openStream());
        Controller controller = fxmlLoader.getController();

        Model model = new Model(controller);
        //Model model = new Model(controller, new MAScenarioProblemCreator(1));
        model.next();
        //model.test();
        primaryStage.setTitle(model.toString());
        primaryStage.setScene(new Scene(root, 1200, 700));
        primaryStage.show();


        //Laptop
        //primaryStage.setScene(new Scene(root, 1200, 600));
        //<Canvas fx:id="canvas" height="540.0" width="1150.0" BorderPane.alignment="CENTER" />

        //PC
        //primaryStage.setScene(new Scene(root, 1400, 975));
        //<Canvas fx:id="canvas" height="850.0" width="1300.0" BorderPane.alignment="CENTER" />


    }

}
