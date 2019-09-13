import Model.*;
import Controller.*;
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

public class Main extends Application {

    public static void main (String [] args)
    {
        launch(args);
    }

    // TODO: 13/09/2019 Something that writes the dic each line to a file.
    //Then we can read from the file
    public void start(Stage primaryStage) throws Exception {

        //The GUI
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("Controller/view.fxml").openStream());
        Controller controller = fxmlLoader.getController();

        //Model model = new Model(controller);
        //model.next();
        test(controller);
        //primaryStage.setTitle(model.toString());
        primaryStage.setScene(new Scene(root, 1200, 700));
        primaryStage.show();


        //Laptop
        //primaryStage.setScene(new Scene(root, 1200, 600));
        //<Canvas fx:id="canvas" height="540.0" width="1150.0" BorderPane.alignment="CENTER" />

        //PC
        //primaryStage.setScene(new Scene(root, 1400, 975));
        //<Canvas fx:id="canvas" height="850.0" width="1300.0" BorderPane.alignment="CENTER" />


    }

    public static void test(Controller controller)
    {
        int maxNumAgent =21;
        int num_scene = 100;
        double sum ,totalSum=0;
        String res="";
        Model model;

        for(int k=0;k<7;k++) {
            String filename;

                if(k== 0)
                {

                    filename = "arena";
                }
                else
                {
                    if(k== 1)
                    {
                        filename = "AR0011SR";
                    }
                    else
                    {
                        if(k== 2)
                        {
                            filename = "AR0602SR";
                        }
                        else
                        {
                            if(k== 3)
                            {
                                filename = "AR0700SR";
                            }
                            else
                            {
                                if(k== 4)
                                {
                                    filename = "orz103d";
                                }
                                else
                                {
                                    if(k== 5)
                                    {
                                        filename = "orz702d";
                                    }
                                    else
                                    {
                                        filename = "orz900d";
                                    }
                                }
                            }
                        }
                    }
                }










            ShortestPathGenerator.getInstance().filename = filename;

            for (int i = 1; i <= maxNumAgent; i += 10) {
                model = new Model(controller,filename);
                sum = 0;
                model.setNUM_OF_AGENTS(i);
                for (int j = 0; j < num_scene; j++) {
                    System.out.println("FIleName "+filename);
                    sum += model.next();
                }
                res += "Average updates for " + i + " is " + (((sum * 1.0) / num_scene) / i) + "\n";
                System.out.println("Average updates for " + i + " is " + (((sum * 1.0) / num_scene) / i) + "\n");
                totalSum += ((sum * 1.0) / num_scene) / i;
            }
            res += "total average is " + ((totalSum * 1.0) / maxNumAgent);


            BufferedWriter writer = null;
            try {
                String rel = new File("help.txt").getAbsolutePath();
                rel = rel.substring(0, rel.indexOf("help.txt"));
                String path = rel + "res\\Outputs\\perfect_heursitics_results"+filename+".txt";//paste - res\Outputs\
                writer = new BufferedWriter(new FileWriter(path));
                writer.write(res);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
}
