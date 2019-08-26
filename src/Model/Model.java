package Model;

import Controller.Controller;
import Model.ALSSLRTA.AlssLrtaRealTimeSearchManager;
import Model.LRTA.RealTimeSearchManager;
import javafx.util.Pair;

import java.util.*;

/**
 * This class represents the Model
 */
public class Model {

    private boolean first;
 //   public boolean toPrint = false;
    private final int NUM_OF_AGENTS = 1;//Number of agents
    private final int HEIGHT = 12;//The number of columns
    private final int WIDTH = 12;//The number of rows
    private final double DENSITY = 0.6;//The ratio between the number of walls to the overall number of nodes in the grid
    private final int NUM_OF_NODES_TO_DEVELOP = 15;//The number of nodes that can be developed in a single iteration
    private final int TYPE =1;// 0 - LRTA*, 1-aLSS-LRTA*
    private final String fileName = "AR0011SR";
    private final String mapPath = "C:\\Users\\guys79\\IdeaProjects\\MapfLrta-\\res\\Maps\\"+fileName+".map";
    private final String scenPath = "C:\\Users\\guys79\\IdeaProjects\\MapfLrta-\\res\\Scenarios\\"+fileName+".map.scen";
    private final String outputPath = "C:\\Users\\guys79\\Desktop\\outputs\\output.csv";
    private Controller controller;
    private Map<Agent, Pair<Node,Node>> prev;
    private ScenarioProblemCreator problemCreator;
    private IRealTimeSearchManager realTimeSearchManager;

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
                realTimeSearchManager = new AlssLrtaRealTimeSearchManager(problem);
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
    private void printPathCost(List<Node> path,Problem problem)
    {
        double sum = 0;
        for(int i=0;i<path.size()-1;i++)
        {
            sum+= problem.getCost(path.get(i),path.get(i+1));
        }
        System.out.println("sum = "+sum);
    }
}