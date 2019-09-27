package Model.ProblemCreators;

import Model.Components.Agent;
import Model.Components.GridCostFunction;
import Model.Components.Node;
import Model.Components.Problem;
import javafx.util.Pair;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a MAPF problem creator
 */
public class MAScenarioProblemCreator extends ScenarioProblemCreator {

    private int num_of_agents;//Number of agents
    Map<Integer,String []>[] scenarios;//The scenarios
    final int NUM_OF_SCENARIOS = 2000;// Number of wanted scenarios pre map
    private int scenIndex;//The index of the current scenario
    private int type;//The type

    /**
     * The constructor.
     * @param num_of_agents - Number of agents
     * @param type  - the given type
     */
    public MAScenarioProblemCreator(int num_of_agents,int type)
    {
        super();
        scenIndex = 0;
        this.num_of_agents = num_of_agents;
        this.scenarios = new Map[NUM_OF_SCENARIOS];
        this.type = type;

    }

    /**
     * This function will set the number of agents in the problem
     * @param num_of_agents - The number of agents
     */
    public void setNum_of_agents(int num_of_agents) {
        this.num_of_agents = num_of_agents;
    }

    @Override
    protected void getGraphAndScenarios(String mapPath, String scenariosPath) {
        getGraph(mapPath);

        String fileName = scenariosPath.substring(0,scenariosPath.indexOf("."));
        System.out.println(fileName);
        File file = new File(fileName);
        if(file.exists())
        {

            getScenariosFromFolder(file);
        }
        else {
            getScenario();
        }
    }

    /**
     * This function will get the scenarios from the folder
     * @param folder - The given folder file
     */
    private void getScenariosFromFolder(File folder)
    {
        File []  scenFiles = folder.listFiles();
        this.scenarios = new Map[scenFiles.length];
        for(int i=0;i<scenFiles.length;i++)
        {
            this.scenarios[i] = getSingleScenarioFromFolder(scenFiles[i].getAbsolutePath());
        }
    }

    /**
     * This function will get a single scenario from a single file
     * @param path - The path to the file
     * @return - A Scenario
     */
    private Map<Integer,String []> getSingleScenarioFromFolder(String path)
    {
        String [] scenData;
        Map<Integer,String []> scen = new HashMap<>();
        BufferedReader br = null;
        int id=0;
        try {
            br = new BufferedReader(new FileReader(path));
            String line;

            br.readLine();//NO use

            while((line = br.readLine())!=null && id<num_of_agents) {
                scenData = getSingleAgentScen(line);
                scen.put(id,scenData);
                id++;

            }
            return scen;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * This function will get a single scenario for a single agent
     * @param scen - The given scenario as string
     * @return - The scenario of the agent
     */
    private String [] getSingleAgentScen(String scen)
    {

        String[] split = scen.split("\t");
        String[] scenario = new String[4];


        scenario[0] = split[7];//Start - x
        scenario[1] = split[6];//Start - y
        scenario[2] = split[5];//End - x
        scenario[3] = split[4];//End - y

        return scenario;

    }
    /**
     * This function sets the scenarios
     */
    protected void getScenario()
    {
        for(int i=0;i<this.NUM_OF_SCENARIOS;i++)
        {
            getSingleScenario(i);
        }
    }

    /**
     * This function sets a single random scenario
     * @param index - Thee index of the scenario
     */
    private void getSingleScenario(int index)
    {

        HashSet<Node> starts = new HashSet<>();
        HashSet<Node> goals = new HashSet<>();
        Map<Integer, String []> scen = new HashMap<>();
        for(int i=0;i<this.num_of_agents;i++)
        {
            String [] single_agent_scen = new String[4];
            int x = (int)(Math.random()*graph.length);
            int y = (int)(Math.random()*graph[x].length);
            while(graph[x][y]==null || starts.contains(graph[x][y]))
            {
                x = (int)(Math.random()*graph.length);
                y = (int)(Math.random()*graph[x].length);
            }
            starts.add(graph[x][y]);
            single_agent_scen[0] = ""+x;
            single_agent_scen[1] = ""+y;


            x = (int)(Math.random()*graph.length);
            y = (int)(Math.random()*graph[x].length);
            while(graph[x][y]==null || goals.contains(graph[x][y]))
            {
                x = (int)(Math.random()*graph.length);
                y = (int)(Math.random()*graph[x].length);
            }
            single_agent_scen[2] = ""+x;
            single_agent_scen[3] = ""+y;
            goals.add(graph[x][y]);
            scen.put(i,single_agent_scen);
        }
        this.scenarios[index] = scen;


    }

    @Override
    public Problem next() {
        if(scenIndex == NUM_OF_SCENARIOS)
            return null;

        Map<Agent,Pair<Node, Node>> start_and_goal = new HashMap<>();
        Map<Integer,String []> scenerio = this.scenarios[this.scenIndex];
        scenIndex++;
        Set<Integer> agent_ids = scenerio.keySet();
        for(int id : agent_ids)
        {
            String [] scen = scenerio.get(id);
            int x_start = Integer.parseInt(scen[0]);
            int y_start =Integer.parseInt(scen[1]);
            int x_end = Integer.parseInt(scen[2]);
            int y_end =Integer.parseInt(scen[3]);
            Node start = graph[x_start][y_start];
            Node goal = graph[x_end][y_end];
            if(start == null || goal == null)
            {
                String message="In scenario "+scenIndex+", agent "+id+" - ";
                if(start == null)
                {
                    message+= "start node ["+x_start+","+y_start+"] is null ";
                }
                if(goal == null)
                {
                    message+= "goal node ["+x_end+","+y_end+"] is null ";
                }
                System.out.println(message+" Message");
                this.scenIndex++;
                return next();
            }
            System.out.println("Agent "+id+" start "+ start+" goal "+ goal);
            Agent agent = new Agent(id,goal,type);
            start_and_goal.put(agent,new Pair<>(start,goal));
        }
        System.out.println("scenario "+(this.scenIndex-1));
        return new Problem(this.graph,start_and_goal,getToDevelop(),new GridCostFunction(),getVisionRadius());
    }
}
