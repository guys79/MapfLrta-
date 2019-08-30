package Model;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a Problem Creator that imports the maps and scenarios from a .map and .map.scen files
 */
public class ScenarioProblemCreator extends AbstractProblemCreator{

    private int index;//The Scenario index
    private String [][] scenarios;//All the scenarios (and their data)
    private int toDevelop;//Number of nodes to develop
    private int type;//The type of search


    /**
     * This constructor of the class
     */
    public ScenarioProblemCreator()
    {
        super();
        scenarios = new String[1][1];
    }

    public int getToDevelop() {
        return toDevelop;
    }

    @Override
    public Problem getProblem(String mapPath, String senerioPath, int toDevelop, int type) {

        this.toDevelop = toDevelop;
        this.type = type;
        //this.scenarios = null;
        getGraphAndScenarios(mapPath,senerioPath);
        index =0;
        return next();
    }

    @Override
    public Problem next() {
        if(index>scenarios.length-1)
        {
            return null;
        }

        Map<Agent,Pair<Node,Node>> start_and_goal = new HashMap<>();
        String [] scen = this.scenarios[index];

        int x_start = Integer.parseInt(scen[1]);
        int y_start =Integer.parseInt(scen[2]);
        int x_end = Integer.parseInt(scen[3]);
        int y_end =Integer.parseInt(scen[4]);
        Node start = this.graph[x_start][y_start];
        Node goal = this.graph[x_end][y_end];
        if(start == null || goal == null)
        {
            String message="In scenario "+scen[0]+" ";
            if(start == null)
            {
                message+= "start node ["+x_start+","+y_start+"] is null ";
            }
            if(goal == null)
            {
                message+= "goal node ["+x_end+","+y_end+"] is null ";
            }
          //  System.out.println(message);
            index++;
            return next();
        }
        System.out.println("scenario "+(index+1) +" in map "+scen[0]);
        System.out.println("Start - "+start +" Goal - "+goal);
        System.out.println("Optimal cost - "+scen[5]);
        /*
        scenario[0] = split[1];//The name of the map
        scenario[1] = split[4];//Start - x
        scenario[2] = split[5];//Start - y
        scenario[3] = split[6];//End - x
        scenario[4] = split[7];//End - y
        scenario[5] = split[8];//Optimal length
         */
        Agent agent = new Agent(0,goal,type);
        start_and_goal.put(agent,new Pair<>(start,goal));
        index++;
        //problemInString = Problem.print(graph,start_and_goal);
        return new Problem(this.graph,start_and_goal,toDevelop,new GridCostFunction());
    }

    public Problem setScenarios(int index)
    {

        if(index>=scenarios.length)
        {
            return null;
        }
        this.index = index;
        return next();
    }

    /**
     * This function will import the graph and the scenarios from the files
     * @param mapPath - The path for the map file
     * @param scenariosPath - The path for the scenario files
     */
    protected void getGraphAndScenarios(String mapPath,String scenariosPath)
    {

        getGraph(mapPath);
        getScenarios(scenariosPath);

    }

    /**
     * This function will import the graph from the file
     * @param path - The path to the file
     */
    protected void getGraph(String path)
    {
        BufferedReader br = null;
        String line="";
        Node[][] grid = null;
        try {

            br = new BufferedReader(new FileReader(path));
            line = br.readLine();//No use

            line = br.readLine();//Height size
            int height = Integer.parseInt(line.split(" ")[1]);

            line = br.readLine();//Width size
            int width = Integer.parseInt(line.split(" ")[1]);

            line = br.readLine();//useless

            grid = new Node[height][width];


            for(int i=0;i<height;i++) {

                line = br.readLine();
                for(int j=0;j<width;j++) {

                    if(line.charAt(j)=='.' || line.charAt(j)=='G')
                        grid[i][j] = new GridNode(i, j);
                    else {
                        grid[i][j] = null;
                    }
                }
            }



        }catch (Exception e)
        {
            System.out.println(line);
            System.out.println("fuckkk");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        this.graph = grid;
    }

    /**
     * Thgis function will take a scenario represented by a string and will parse it
     * @param scen - The scenario
     * @return - An array of Strings that describes the scenario
     */
    private String [] parseScenerio(String scen) {
        String[] split = scen.split("\t");
        String[] scenario = new String[6];

        scenario[0] = split[1];//The name of the map
        scenario[1] = split[7];//Start - x
        scenario[2] = split[6];//Start - y
        scenario[3] = split[5];//End - x
        scenario[4] = split[4];//End - y
        scenario[5] = split[8];//Optimal length
        return scenario;
    }

    /**
     * This function will import the scenarios from the file
     * @param path - The path to the file
     */
    private void getScenarios(String path)
    {
        BufferedReader br = null;
        String line;
        List<String> scen = new ArrayList<>();

        try {


            br = new BufferedReader(new FileReader(path));
            line = br.readLine();//No use

            while((line = br.readLine())!=null) {
                scen.add(line);
            }
            scenarios = new String[scen.size()][6];
            for(int i=0;i<this.scenarios.length;i++)
            {
                if(scen.get(i).equals("193\tAR0011SR.map\t512\t512\t62\t307\t318\t468\t774.82041778"))
                {
                    System.out.println();
                }
                scenarios[i] = this.parseScenerio(scen.get(i));
            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



    }
}
