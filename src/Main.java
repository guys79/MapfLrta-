import Model.*;
import Controller.*;
import Model.Algorithms.Dijkstra.ShortestPathGenerator;
import Model.Components.TestPreformer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.*;

public class Main extends Application {

    public static void main (String [] args)
    {
        launch(args);
    }

    final static int TYPE =2;// 0 - LRTA*, 1-aLSS-LRTA* 2- MA-aLSS-LRTA* 3- IgnoreOthers-Ma-aLSS-LRTA*

    public void start(Stage primaryStage) throws Exception {

        //The GUI
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("Controller/view.fxml").openStream());
        Controller controller = fxmlLoader.getController();
       String filename = "den312d";

        Model model = new Model(controller,filename,TYPE);
        ShortestPathGenerator.getInstance().setFilename(filename);
        model.next();
        primaryStage.setTitle(model.toString());
       // TestPreformer.getInstance().printInfo("");
       //test(controller);
       // test2(controller);
        primaryStage.setScene(new Scene(root, 1600, 975));
        primaryStage.show();


        //Laptop
        //primaryStage.setScene(new Scene(root, 1200, 600));
        //<Canvas fx:id="canvas" height="540.0" width="1150.0" BorderPane.alignment="CENTER" />

        //PC
        //primaryStage.setScene(new Scene(root, 1400, 975));
        //<Canvas fx:id="canvas" height="850.0" width="1300.0" BorderPane.alignment="CENTER" />


    }



     /**
     * This class will compare two DijkstraSearchNodes using their distance from the origin n
     */

    public void test2(Controller controller)
    {
        int num_scene = 10;
        List<Integer> numOfAgents = new ArrayList<>();
        numOfAgents.add(1);
        numOfAgents.add(10);
        numOfAgents.add(50);
        numOfAgents.add(75);
        numOfAgents.add(100);


        String rel = new File("help.txt").getAbsolutePath();
        rel = rel.substring(0,rel.indexOf("help.txt"));
        String path = rel+"res\\Maps";
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        Set<String> names = new HashSet<>();
        for (int k = 0; k < listOfFiles.length; k++) {
            if (listOfFiles[k].isFile()) {
                names.add(listOfFiles[k].getName().substring(0, listOfFiles[k].getName().indexOf(".")));
            }
        }

        Model model;
        String prefix = "res\\Outputs\\tests";
        for (String filename : names) {

            System.out.println("File name - "+filename);
            folder = new File(rel+prefix+"\\"+filename);
            folder.mkdir();

            for (int i = 0; i < numOfAgents.size(); i++) {
                ShortestPathGenerator.getInstance().setFilename(filename);
                model = new Model(controller, filename,TYPE);
                model.setNUM_OF_AGENTS(numOfAgents.get(i));
                for (int j = 0; j < num_scene; j++) {
                    model.next();

                }
                path = rel+prefix+"\\"+filename+"\\perfectHeuristics_"+numOfAgents.get(i)+".txt";
                TestPreformer.getInstance().printInfo(path);



            }
        }


    }




}
