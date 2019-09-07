package Model;

import javafx.util.Pair;

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

    /**
     * The constructor.
     * @param num_of_agents - Number of agents
     */
    public MAScenarioProblemCreator(int num_of_agents)
    {
        super();
        scenIndex = 0;
        this.num_of_agents = num_of_agents;
        this.scenarios = new Map[NUM_OF_SCENARIOS];

    }

    @Override
    protected void getGraphAndScenarios(String mapPath, String scenariosPath) {
        getGraph(mapPath);
        getScenario();
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
        //getGraph(mapPath);
        Map<Agent,Pair<Node,Node>> start_and_goal = new HashMap<>();
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
            Agent agent = new Agent(id,goal,2);
            start_and_goal.put(agent,new Pair<>(start,goal));
        }
        System.out.println("scenario "+(this.scenIndex-1));
        return new Problem(this.graph,start_and_goal,getToDevelop(),new GridCostFunction(),getVisionRadius());
    }
}
