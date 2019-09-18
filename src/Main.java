import Model.*;
import Controller.*;
import Model.Algorithms.Dijkstra.DijkstraSearchNode;
import Model.Algorithms.Dijkstra.ShortestPathGenerator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.xml.bind.SchemaOutputResolver;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main extends Application {

    public static void main (String [] args)
    {
        launch(args);
    }

    // TODO: 16/09/2019 Ma should read MA scenarios
    // TODO: 17/09/2019 add test.clear
    // TODO: 17/09/2019 print no solution from maps with no option
    //Then we can read from the file
    public void start(Stage primaryStage) throws Exception {

        //The GUI
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("Controller/view.fxml").openStream());
        Controller controller = fxmlLoader.getController();
    /*    PriorityQueue<DijkstraSearchNode> p = new PriorityQueue<>(new DijkstraComperator()) ;
        GridNode gridNode = new GridNode(2,3);
        DijkstraSearchNode dijkstraSearchNode = new DijkstraSearchNode(gridNode);
        DijkstraSearchNode dijkstraSearchNode2 = new DijkstraSearchNode(gridNode);
        add(p,dijkstraSearchNode);
        add(p,dijkstraSearchNode2);*/
       String filename = "arena";
        Model model = new Model(controller,filename);
        ShortestPathGenerator.getInstance().setFilename(filename);
        model.next();
        primaryStage.setTitle(model.toString());
       // TestPreformer.getInstance().printInfo("");
       //test(controller);
       // test2(controller);
        primaryStage.setScene(new Scene(root, 1200, 700));
        primaryStage.show();


        //Laptop
        //primaryStage.setScene(new Scene(root, 1200, 600));
        //<Canvas fx:id="canvas" height="540.0" width="1150.0" BorderPane.alignment="CENTER" />

        //PC
        //primaryStage.setScene(new Scene(root, 1400, 975));
        //<Canvas fx:id="canvas" height="850.0" width="1300.0" BorderPane.alignment="CENTER" />


    }

    public static void add(PriorityQueue<DijkstraSearchNode> open,DijkstraSearchNode node)
    {
        open.remove(node);
        open.add(node);
    }
    /**
     * This class will compare two DijkstraSearchNodes using their distance from the origin n
     */
    class DijkstraComperator implements Comparator<DijkstraSearchNode>
    {

        @Override
        public int compare(DijkstraSearchNode o1, DijkstraSearchNode o2) {
            double dis1 = o1.getDistance();
            double dis2 = o2.getDistance();

            if(dis1 == dis2)
                return 0;
            if(dis1<dis2)
                return -1;
            return 1;
        }
    }
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
                model = new Model(controller, filename);
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
