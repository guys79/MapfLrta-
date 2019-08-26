package Model;

import Controller.Controller;
import Model.ALSSLRTA.AlssLrtaRealTimeSearchManager;
import Model.LRTA.RealTimeSearchManager;
import Model.MAALSSLRTA.MaAlssLrtaRealTimeSearchManager;
import javafx.util.Pair;

import java.util.*;

/**
 * This class represents the Model
 */
public class Model {

    private boolean first;//True IFF it's the first scenario
 //   public boolean toPrint = false;
    private final int NUM_OF_AGENTS = 1;//Number of agents
    private final int HEIGHT = 12;//The number of columns
    private final int WIDTH = 12;//The number of rows
    private final double DENSITY = 0.6;//The ratio between the number of walls to the overall number of nodes in the grid
    private final int NUM_OF_NODES_TO_DEVELOP = 15;//The number of nodes that can be developed in a single iteration
    private final int TYPE =2;// 0 - LRTA*, 1-aLSS-LRTA* 2- MA-aLSS-LRTA*
    private final String fileName = "arena";//The name of the file
    //private final String fileName = "AR0011SR";//The name of the file
    private String rel = "C:\\Users\\guys7\\IdeaProjects\\MapfLrta-\\res";//The path to the project's resource dir
    private final String mapPath = rel+"\\Maps\\"+fileName+".map";//The path to the map file
    private final String scenPath =rel+"\\Scenarios\\"+fileName+".map.scen";//The path to the scenario file
    private final String outputPath = "C:\\Users\\guys79\\Desktop\\outputs\\output.csv";//The path to the output file
    private Controller controller;//The controller
    private Map<Agent, Pair<Node,Node>> prev;//The previous agent's goals
    private ScenarioProblemCreator problemCreator;//The problem creator
    private IRealTimeSearchManager realTimeSearchManager;//The real time search manager

    /**
     * The constructor of the class
     * @param controller - The controller
     * @param problemCreator - The problem creator
     */
    public Model(Controller controller,IProblemCreator problemCreator) {

        first = true;
        prev = new HashMap<>();
        this.problemCreator = (ScenarioProblemCreator)problemCreator;
        this.controller = controller;
        this.controller.setModel(this);
    }


    /**
     * This function will check if the algorithm had found a solution for the problem
     * @param paths - The paths found for each agent
     * @return - True IFF a solution was found
     */
    public boolean isThereSolution(Map<Agent, List<Node>> paths){
        for(List<Node> path : paths.values())
        {
            if(path.size()==1)
                return false;
        }
        return true;
    }

    /**
     * This function will move to the next scenario on the same map
     * The function will solve the problem and present the solution
     */
    public void next() {
        Problem problem;
        if (first) {
            problem = problemCreator.getProblem(mapPath, scenPath, NUM_OF_NODES_TO_DEVELOP, TYPE);
            int[][] intGrid = problemCreator.getGridGraph();
            controller.initialize(intGrid);
            first = false;
        } else {
            problem = problemCreator.next();
            HashSet <int []> locs = new HashSet();
            for(Pair<Node,Node> p : prev.values())
            {
                int [] a = {((GridNode)p.getKey()).getX(),((GridNode)p.getKey()).getY()};
                int [] b = {((GridNode)p.getValue()).getX(),((GridNode)p.getValue()).getY()};
                locs.add(a);
                locs.add(b);
            }
            controller.clear(locs);
        }
        if (problem != null) {
            if(this.TYPE == 0)
            {
                realTimeSearchManager = new RealTimeSearchManager(problem);
            }
            else
            {
                if(this.TYPE == 1)
                    realTimeSearchManager = new AlssLrtaRealTimeSearchManager(problem);
                else
                    realTimeSearchManager = new MaAlssLrtaRealTimeSearchManager(problem);
            }
            Map<Agent, List<Node>> paths;//The paths for each agent

            long startTime;
           // if(toPrint)
          //      problemCreator.output(outputPath);
            startTime = System.currentTimeMillis();
            paths = realTimeSearchManager.search();

            //Time calculation
            long endTime = System.currentTimeMillis();
            long time = endTime-startTime;
            final int NUMBER_OF_DIGITS = 4;
            int help = (int)Math.pow(10,NUMBER_OF_DIGITS);
            double timeInSeconds = (time*1.0)/1000;
            timeInSeconds = Math.round(timeInSeconds*help)/(help*1.0);

            //Print the results
            prev = problem.getAgentsAndStartGoalNodes();
            Set<Agent> agents = prev.keySet();

            for (Agent agent : agents){
                controller.addAgent(paths.get(agent), agent.getGoal());
                printPathCost(paths.get(agent),problem);
            }
            System.out.println("TIme elapsed "+time +" ms");
            System.out.println("TIme elapsed "+timeInSeconds +" seconds");
            System.out.println();
            controller.draw();

        }
    }

    /**
     * This function will print the cost of the path
     * @param path - The given path
     * @param problem - The given problem
     */
    private void printPathCost(List<Node> path,Problem problem)
    {
        double sum = 0;
        for(int i=0;i<path.size()-1;i++)
        {
            sum+= problem.getCost(path.get(i),path.get(i+1));
        }
        System.out.println("sum = "+sum);
    }

    @Override
    public String toString() {
        if(this.TYPE == 0)
        {
            return "LRTA*";
        }
        if(TYPE == 1)
        {
            return "aLSS-LRTA*";
        }
        return "Multi Agent aLSS-LRTA*";
    }
}