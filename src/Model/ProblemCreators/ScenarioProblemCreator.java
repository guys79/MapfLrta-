package Model.ProblemCreators;

import Model.Components.*;
import Model.Model;
import Model.ProblemCreators.AbstractProblemCreator;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * This class represents a Problem Creator that imports the maps and scenarios from a .map and .map.scen files
 */
public class ScenarioProblemCreator extends AbstractProblemCreator {

    private int index;//The Scenario index
    private String [][] scenarios;//All the scenarios (and their data)
    private int toDevelop;//Number of nodes to develop
    private int visionRadius;//The vision radius for each agent
    private int type;//The type of search
    protected boolean canT;//True if the char 'T' represents a passable way in the map
    protected String mapPath;
    protected int prefixLength;
    /**
     * This constructor of the class.
     */
    public ScenarioProblemCreator()
    {
        super();
        scenarios = new String[1][1];
    }

    /**
     * This function will set the vision radius of each agent in the problem
     * @param visionRadius - The given vision radius
     */
    public void setVisionRadius(int visionRadius) {
        this.visionRadius = visionRadius;
    }

    /**
     * This function will return the vision radius of all the agents
     * @return - The vision radius of all the agents
     */
    public int getVisionRadius() {
        return visionRadius;
    }

    /**
     * This function will return the number of nodes to develop
     * @return - Number of nodes
     */
    public int getToDevelop() {
        return toDevelop;
    }

    @Override
    public Problem getProblem(String mapPath, String senerioPath, int toDevelop, int type, int visionRadius,int prefixLength) {

        this.toDevelop = toDevelop;
        this.type = type;
        this.visionRadius = visionRadius;
        //this.scenarios = null;
        canT = true;
        this.mapPath = mapPath;
        getGraphAndScenarios(mapPath,senerioPath);
        index =999;
        this.prefixLength = prefixLength;
        return next();
    }


    @Override
    public Problem next() {
        if(index>scenarios.length-1)
        {
            return null;
        }

        Map<Agent,Pair<Node, Node>> start_and_goal = new HashMap<>();
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
            System.out.println(message);
            index++;
            return next();
        }
        System.out.println("scenario "+(index+1) +" in map "+scen[0]);
        System.out.println("Start - "+start +" Goal - "+goal);

        Agent agent = new Agent(0,goal,type);
        start_and_goal.put(agent,new Pair<>(start,goal));
        index++;
        return new Problem(this.graph,start_and_goal,toDevelop,new GridCostFunction(),this.visionRadius,type,prefixLength);
    }

    /**
     * This function will set the scenario at the given index
     * @param index - the given index
     * @return - The problem that the scenario represents
     */
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
        Set<String> names = new HashSet<>();

        names.add("arena");
        names.add("ost003d");
        names.add("den312d");
        names.add("den520d");
        names.add("brc000d");
        names.add("brc100d");
        names.add("brc101d");
        names.add("brc300d");
        names.add("ht_chantry");
        names.add("lak303d");
        names.add("ht_mansion_n");
        names.add("lt_gallowstemplar_n");
        canT = true;
        for(String str:names)
        {
            if(path.contains(str))
            {

                canT = false;
                break;
            }
        }

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

            Node.reset();

            for(int i=0;i<height;i++) {

                line = br.readLine();
                for(int j=0;j<width;j++) {

                    if(line.charAt(j)=='.' || line.charAt(j)=='G' || (line.charAt(j)=='T' && canT))
                        grid[i][j] = new GridNode(i, j);
                    else {
                        grid[i][j] = null;
                    }
                }
            }



        }catch (Exception e)
        {
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
