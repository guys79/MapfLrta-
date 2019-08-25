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

public class ScenarioProblemCreator extends AbstractProblemCreator{
    private int index;
    private String [][] scenarios;
    private int toDevelop;
    private int type;



    public ScenarioProblemCreator()
    {
        super();
    }
    @Override
    public Problem getProblem(String mapPath, String senerioPath, int toDevelop, int type) {
        index =0;
        this.toDevelop = toDevelop;
        this.type = type;
        this.scenarios = null;
        getGraphAndScenarios(mapPath,senerioPath);
        return next();
    }

    @Override
    public Problem next() {
        if(index==scenarios.length)
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
            System.out.println(message);
            index++;
            return next();
        }
        System.out.println("scenario "+(index+1) +" in map "+scen[0]);
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
        problemInString = Problem.print(graph,start_and_goal);
        return new Problem(this.graph,start_and_goal,toDevelop,new GridCostFunction());
    }
    private void getGraphAndScenarios(String mapPath,String scenariosPath)
    { 
        getGraph(mapPath);
        getScenarios(scenariosPath);
    }
    
    private void getGraph(String path)
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

    private String [] parseScenerio(String scen) {
        String[] split = scen.split("\t");
        String[] scenario = new String[6];

        scenario[0] = split[1];//The name of the map
        scenario[1] = split[4];//Start - x
        scenario[2] = split[5];//Start - y
        scenario[3] = split[6];//End - x
        scenario[4] = split[7];//End - y
        scenario[5] = split[8];//Optimal length
        return scenario;
    }
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
