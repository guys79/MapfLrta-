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
    final static int NUM_OF_AGENTS =10;
    final static int NUM_TO_DEV =45;
    final static int PREFIX_LENGTH = 7;
    final static String filename = "ht_mansion_n";

    public void start(Stage primaryStage) throws Exception {

        //The GUI
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("Controller/view.fxml").openStream());
        Controller controller = fxmlLoader.getController();


       Model model = new Model(controller,filename,TYPE);
        model.setNUM_OF_AGENTS(NUM_OF_AGENTS);
        model.setNUM_OF_NODES_TO_DEVELOP(NUM_TO_DEV);
        model.setPrefixLength(PREFIX_LENGTH);
        ShortestPathGenerator.getInstance().setFilename(filename);
        model.next();
        primaryStage.setTitle(model.toString());
        primaryStage.setScene(new Scene(root, 1600, 975));
         primaryStage.show();
       // TestPreformer.getInstance().printInfo("");
        //test(controller);




        //Laptop
        //primaryStage.setScene(new Scene(root, 1200, 600));
        //<Canvas fx:id="canvas" height="540.0" width="1150.0" BorderPane.alignment="CENTER" />

        //PC
        //primaryStage.setScene(new Scene(root, 1400, 975));
        //<Canvas fx:id="canvas" height="850.0" width="1300.0" BorderPane.alignment="CENTER" />


    }





    public void test(Controller controller)
    {
        //int num_scene = 20;
        int num_scene = 2;
        List<Integer> numOfAgents = new ArrayList<>();
        numOfAgents.add(1);
        numOfAgents.add(10);
        numOfAgents.add(50);
        numOfAgents.add(75);
        numOfAgents.add(100);
        List<Integer> dev = new ArrayList<>();
        dev.add(5);
        dev.add(15);
        dev.add(50);
        dev.add(100);
        dev.add(300);


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
        int [] types = {2,7,3,4,5,6};
        String rawPath;
        String prefix = "res\\Outputs\\tests";
        for(int k=0;k<types.length;k++) {
            folder = new File(rel + prefix + "\\" +types[k]);
            if(!folder.exists())
                folder.mkdir();

            for (String filename : names) {

                System.out.println("File name - " + filename);
                folder = new File(rel + prefix + "\\" +types[k]+"\\" +filename);
                if(folder.exists())
                    continue;
                folder.mkdir();
                for (int i = 0; i < numOfAgents.size(); i++) {
                    ShortestPathGenerator.getInstance().setFilename(filename);
                    model = new Model(controller, filename, types[k]);

                    model.setNUM_OF_AGENTS(numOfAgents.get(i));
                    for(int h = 0 ;h<dev.size();h++) {
                        model.setNUM_OF_NODES_TO_DEVELOP(dev.get(h));

                        for (int j = 0; j < num_scene; j++) {
                            model.next();

                        }

                        path =rel + prefix + "\\" +types[k]+"\\" +filename + "\\perfectHeuristics_" + numOfAgents.get(i) +"_"+dev.get(h)+ ".txt";
                        rawPath =rel + prefix + "\\" +types[k]+"\\" +filename + "\\perfectHeuristics_" + numOfAgents.get(i) +"_"+dev.get(h)+ ".csv";
                        TestPreformer.getInstance().printInfo(path, rawPath);
                    }



                }
            }
        }

    }




}
