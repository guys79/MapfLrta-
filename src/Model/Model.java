package Model;

import Controller.Controller;
import Model.ALSSLRTA.AlssLrtaRealTimeSearchManager;
import Model.LRTA.RealTimeSearchManager;
import Model.MAALSSLRTA.MaAlssLrtaRealTimeSearchManager;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * This class represents the Model
 */
public class Model {

    private boolean first;//True IFF it's the first scenario
 //   public boolean toPrint = false;
    private final int NUM_OF_AGENTS = 2;//Number of agents
    private final int HEIGHT = 12;//The number of columns
    private final int WIDTH = 12;//The number of rows
    private final double DENSITY = 0.6;//The ratio between the number of walls to the overall number of nodes in the grid
    private final int NUM_OF_NODES_TO_DEVELOP = 25;//The number of nodes that can be developed in a single iteration
    private final int TYPE =2;// 0 - LRTA*, 1-aLSS-LRTA* 2- MA-aLSS-LRTA*
    //private String fileName = "arena";//The name of the file
    private String fileName = "AR0011SR";//The name of the file
    private String mapPath;//The path to the map file
    private String scenPath;//The path to the scenario file
    private String outputPath;//The path to the output file
    private Controller controller;//The controller
    private Map<Agent, Pair<Node,Node>> prev;//The previous agent's goals
    private IProblemCreator problemCreator;//The problem creator
    private IRealTimeSearchManager realTimeSearchManager;//The real time search manager
    private double test;


    public int getTYPE() {
        return TYPE;
    }

    /**
     * This function will srt the filename with the given file name
     * @param fileName - The given file name
     */
    public void setFileName(String fileName) {


        String rel = new File("help.txt").getAbsolutePath();
        rel = rel.substring(0,rel.indexOf("help.txt"));
        mapPath = rel+"res\\Maps\\"+fileName+".map";
        File f = new File(mapPath);
        if(!f.exists() || f.isDirectory()) {
            return;
        }
        scenPath =rel+"res\\Scenarios\\"+fileName+".map.scen";
        this.fileName = fileName;
        first = true;
        next();
    }
    public void test()
    {
        for(int i=0;i<300;i++)
        {
            System.out.println("scenario "+(i+1));
            next();
        }
        System.out.println("test result "+test);
        System.out.println("Avg "+(test));
    }
    /**
     * The constructor of the class
     * @param controller - The controller
     *
     */
    public Model(Controller controller) {
        String rel = new File("help.txt").getAbsolutePath();
        rel = rel.substring(0,rel.indexOf("help.txt"));
        mapPath = rel+"res\\Maps\\"+fileName+".map";
        scenPath =rel+"res\\Scenarios\\"+fileName+".map.scen";
        outputPath = rel+"res\\Outputs\\output.csv";
        test = 0;
        first = true;
        prev = new HashMap<>();
        if(this.TYPE == 0)
        {
            //throw new UnsupportedOperationException();
            this.problemCreator = new RandomProblemCreator();
        }
        else
        {
            if(TYPE == 1)
            {

               //this.problemCreator = new CSVProblem();
                this.problemCreator = new ScenarioProblemCreator();
            }
            else
            {
                this.problemCreator = new MAScenarioProblemCreator(this.NUM_OF_AGENTS);
            }
        }

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
    public void setScenario(int index)
    {

        Problem problem = ((ScenarioProblemCreator)problemCreator).setScenarios(index);
        HashSet <int []> locs = new HashSet();
        for(Pair<Node,Node> p : prev.values())
        {
            int [] a = {((GridNode)p.getKey()).getX(),((GridNode)p.getKey()).getY()};
            int [] b = {((GridNode)p.getValue()).getX(),((GridNode)p.getValue()).getY()};
            locs.add(a);
            locs.add(b);
        }
        controller.clear(locs);
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
     * This function will move to the next scenario on the same map
     * The function will solve the problem and present the solution
     */
    public void next() {

        Problem problem;
        if (first) {
            problem = problemCreator.getProblem(mapPath, scenPath, NUM_OF_NODES_TO_DEVELOP, TYPE);
            //problem = problemCreator.getProblem(outputPath,NUM_OF_NODES_TO_DEVELOP,TYPE);
            int[][] intGrid = problemCreator.getGridGraph();
            controller.initialize(intGrid);
            first = false;
            controller.clear(new HashSet<>());
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
            test+=time;
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
     *
     */
    private void printPathCost(List<Node> path,Problem problem)
    {
        double sum = 0;
        double delta;
        int numOfDIag = 0;
        for(int i=0;i<path.size()-1;i++)
        {
            delta = problem.getCost(path.get(i),path.get(i+1));
            if(delta!= 1)
            {
                numOfDIag++;
            }
            sum+= delta;
        }
        System.out.println("sum = "+sum);
        System.out.println("number of diagonal "+numOfDIag);
        System.out.println("number of non-diagonal "+(path.size()-numOfDIag-1));

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