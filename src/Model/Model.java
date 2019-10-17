package Model;

import Controller.Controller;
import Model.Algorithms.ALSSLRTA.AlssLrtaRealTimeSearchManager;
import Model.Algorithms.ALSSLRTAHALT.AlssLrtaHaltRealTimeManager;
import Model.Algorithms.ALSSLRTAIGNOREOTHERS.AlssLrtaIgnoreOthersRealTimeManager;
import Model.Algorithms.FactoryRealTimeManager;
import Model.ProblemCreators.FactoryProblemCreator;
import Model.Algorithms.LRTA.RealTimeSearchManager;
import Model.Algorithms.MAALSSLRTA.MaAlssLrtaRealTimeSearchManager;
import Model.Components.*;
import Model.ProblemCreators.IProblemCreator;
import Model.ProblemCreators.MAScenarioProblemCreator;
import Model.ProblemCreators.ScenarioProblemCreator;
import javafx.util.Pair;

import java.io.File;
import java.util.*;

/**
 * This class represents the Model
 */
public class Model {

    private boolean first;//True IFF it's the first scenario
    private int NUM_OF_AGENTS ;//Number of agents
    private final int VISION_RADIUS = 20;//Number of agents
    private final int HEIGHT = 12;//The number of columns
    private final int WIDTH = 12;//The number of rows
    private final double DENSITY = 0.6;//The ratio between the number of walls to the overall number of nodes in the grid
    private  int NUM_OF_NODES_TO_DEVELOP ;//The number of nodes that can be developed in a single iteration
    private int TYPE;// 0 - LRTA*, 1-aLSS-LRTA* 2- MA-aLSS-LRTA* 3- IgnoreOthers-Ma-aLSS-LRTA*
    private String fileName;
    private String mapPath;//The path to the map file
    private String scenPath;//The path to the scenario file
    private String outputPath;//The path to the output file
    private Controller controller;//The controller
    private int prefixLength;
    private Map<Agent, Pair<Node,Node>> prev;//The previous agent's goals
    private IProblemCreator problemCreator;//The problem creator
    private IRealTimeSearchManager realTimeSearchManager;//The real time search manager

    /**
     * This function will set the num of node to develop
     * @param NUM_OF_NODES_TO_DEVELOP - The number of nodes to develop per iteration
     */
    public void setNUM_OF_NODES_TO_DEVELOP(int NUM_OF_NODES_TO_DEVELOP) {
        this.NUM_OF_NODES_TO_DEVELOP = NUM_OF_NODES_TO_DEVELOP;
    }

    /**
     * This function will set the prefix's length
     * @param prefixLength - The length of the prefix
     */
    public void setPrefixLength(int prefixLength) {
        this.prefixLength = prefixLength;
    }

    /**
     *
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

    /**
     * This function will set the number of agents
     * @param NUM_OF_AGENTS - The given number of agents
     */
    public void setNUM_OF_AGENTS(int NUM_OF_AGENTS) {
        this.NUM_OF_AGENTS = NUM_OF_AGENTS;
        if(problemCreator!=null)
            ((MAScenarioProblemCreator)problemCreator).setNum_of_agents(NUM_OF_AGENTS);
    }

    /**
     * The constructor of the class
     * @param controller - The given Controller
     * @param filename - The given file name (the name of the map)
     * @param type - The type of search
     */
    public Model(Controller controller,String filename,int type) {
        this(controller,type);

        this.fileName = filename;
        String rel = new File("help.txt").getAbsolutePath();
        rel = rel.substring(0,rel.indexOf("help.txt"));
        mapPath = rel+"res\\Maps\\"+fileName+".map";
        scenPath =rel+"res\\Scenarios\\"+fileName+".map.scen";
        outputPath = rel+"res\\Outputs\\output.csv";
    }
    /**
     * The constructor of the class
     * @param controller - The controller
     * @param type  - The given type
     */
    public Model(Controller controller,int type) {
        this.TYPE = type;
        this.prefixLength = 0;
        String rel = new File("help.txt").getAbsolutePath();
        rel = rel.substring(0,rel.indexOf("help.txt"));
        fileName = "w_woundedcoast";//The name of the file
        mapPath = rel+"res\\Maps\\"+fileName+".map";
        scenPath =rel+"res\\Scenarios\\"+fileName+".map.scen";
        outputPath = rel+"res\\Outputs\\output.csv";
        first = true;
        prev = new HashMap<>();
        this.problemCreator = FactoryProblemCreator.getInstance().getProblemCreator(this.TYPE,this.NUM_OF_AGENTS,this.prefixLength);
        this.controller = controller;
        this.controller.setModel(this);
    }

    /**
     * This function will set the scenario with the given index
     * @param index - The given index
     */
    public void setScenario(int index) {

        Problem problem = ((ScenarioProblemCreator) problemCreator).setScenarios(index);
        HashSet<int[]> locs = new HashSet();
        for (Pair<Node, Node> p : prev.values()) {
            int[] a = {((GridNode) p.getKey()).getX(), ((GridNode) p.getKey()).getY()};
            int[] b = {((GridNode) p.getValue()).getX(), ((GridNode) p.getValue()).getY()};
            locs.add(a);
            locs.add(b);
        }
        controller.clear(locs);
        if (problem != null) {
            realTimeSearchManager = FactoryRealTimeManager.getInstance().getProblemCreator(this.TYPE, problem);


            Map<Agent, List<Node>> paths;//The paths for each agent

            long startTime;

            startTime = System.currentTimeMillis();
            paths = realTimeSearchManager.search();

            //Time calculation
            long endTime = System.currentTimeMillis();
            long time = endTime - startTime;
            final int NUMBER_OF_DIGITS = 4;
            int help = (int) Math.pow(10, NUMBER_OF_DIGITS);
            double timeInSeconds = (time * 1.0) / 1000;
            timeInSeconds = Math.round(timeInSeconds * help) / (help * 1.0);

            //Print the results
            prev = problem.getAgentsAndStartGoalNodes();
            Set<Agent> agents = prev.keySet();

            for (Agent agent : agents) {
                controller.addAgent(paths.get(agent), agent.getGoal());
            }
            System.out.println("TIme elapsed " + time + " ms");
            System.out.println("TIme elapsed " + timeInSeconds + " seconds");

            System.out.println();
            controller.draw();


        }
    }


    /**
     * This function will move to the next scenario on the same map
     * The function will solve the problem and present the solution
     *
     */
    public void next() {

        Problem problem;
        if (first) {

            problem = problemCreator.getProblem(mapPath, scenPath, NUM_OF_NODES_TO_DEVELOP, TYPE,VISION_RADIUS,prefixLength );
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
            realTimeSearchManager = FactoryRealTimeManager.getInstance().getProblemCreator(this.TYPE,problem);
            Map<Agent, List<Node>> paths;//The paths for each agent

            long startTime;
            Node.reset();
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

            double sumOfCosts=0;
            double maxSpan = -1;
            double cost;
            for (Agent agent : agents){

                controller.addAgent(paths.get(agent), agent.getGoal());
                List<Node> path = paths.get(agent);
                double pathCost = 0;
                for(int i=0;i<path.size()-1;i++) {
                    cost = problem.getCost(path.get(i), path.get(i + 1));
                    pathCost+=cost;
                }

                if(pathCost>maxSpan)
                    maxSpan = pathCost;
                sumOfCosts+=pathCost;
            }
            System.out.println("TIme elapsed "+time +" ms");
            System.out.println("TIme elapsed "+timeInSeconds +" seconds");
            System.out.println("Average updates " +Node.average);
            TestPreformer.getInstance().updatePerSearch(Node.average,time,sumOfCosts,maxSpan,((AbstractRealTimeSearchManager)realTimeSearchManager).getIteration(),((AbstractRealTimeSearchManager)realTimeSearchManager).isSuccess(),((AbstractRealTimeSearchManager)realTimeSearchManager).getIterationAvergae());
            System.out.println();
            controller.draw();


        }


    }


    @Override
    public String toString() {
        if(this.TYPE == 0)
            return "LRTA*";
        if(TYPE == 1)
            return "aLSS-LRTA*";
        if(this.TYPE == 2)
            return "Prioritized Multi Agent aLSS-LRTA*";
        if(this.TYPE == 3)
            return "Multi Agent aLSS-LRTA* (Ignore crushes)";
        if(this.TYPE == 4)
            return "Multi Agent aLSS-LRTA* (Halt upon crush)";
        if(this.TYPE == 5)
            return "Prioritized Multi Agent aLSS-LRTA* (Collective learning)";
        if(this.TYPE == 6)
            return "Centralized LSS-LRTA*";
        return "Efficient Prioritized Multi Agent aLSS-LRTA*";
    }
}