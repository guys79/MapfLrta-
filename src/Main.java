import Model.*;
import Controller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main extends Application {

    public static void main (String [] args)
    {
        launch(args);
    }

    // TODO: 07/09/2019 comments 
    // TODO: 07/09/2019 There are collisions for some reason, need to make sure that that is only because there are no more options left
    @Override
    public void start(Stage primaryStage) throws Exception {

        //The GUI
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("Controller/view.fxml").openStream());
        Controller controller = fxmlLoader.getController();

        Model model = new Model(controller);
        //model.next();
        test(model);
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

    public static void test(Model model)
    {
        int maxNumAgent =10;
        int num_scene = 2000;
        double sum =0;
        String res="";
        for(int i=1;i<=maxNumAgent;i++)
        {
            model.setNUM_OF_AGENTS(i);
            for(int j =0;j<num_scene;j++)
            {
                sum+=model.next();
            }
            res+="Average updates for "+i+" is "+((sum*1.0)/num_scene)+"\n";
        }



        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("C:\\Users\\Vardana\\Desktop\\results.txt"));
            writer.write(res);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}
