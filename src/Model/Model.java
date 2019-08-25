package Model;

import Controller.Controller;
import Model.ALSSLRTA.AlssLrtaRealTimeSearchManager;
import Model.LRTA.RealTimeSearchManager;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Model {

    private boolean first;
    private final int NUM_OF_AGENTS = 1;//Number of agents
    private final int HEIGHT = 12;//The number of columns
    private final int WIDTH = 12;//The number of rows
    private final double DENSITY = 0.6;//The ratio between the number of walls to the overall number of nodes in the grid
    private final int NUM_OF_NODES_TO_DEVELOP = 15;//The number of nodes that can be developed in a single iteration
    private final int TYPE =1;// 0 - LRTA*, 1-aLSS-LRTA*
    private final String mapPath = "C:\\Users\\guys79\\Desktop\\AR0011SR.map";
    private final String scenPath = "C:\\Users\\guys79\\Desktop\\AR0011SR.map.scen";
    private final String outputPath = "C:\\Users\\guys79\\Desktop\\outputs\\output.csv";
    private Controller controller;
    private ScenarioProblemCreator problemCreator;
    private IRealTimeSearchManager realTimeSearchManager;

    public Model(Controller controller,IProblemCreator problemCreator) {
        first = true;
        this.problemCreator = (ScenarioProblemCreator)problemCreator;
        this.controller = controller;
        this.controller.setModel(this);
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
            controller.clear();
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
            int numProblems =0;
            problemCreator.output(outputPath);
            startTime = System.currentTimeMillis();
            paths = realTimeSearchManager.search();

            //Time calculation
            long endTime = System.currentTimeMillis();
            numProblems++;
            System.out.println(numProblems);
            long time = endTime-startTime;
            final int NUMBER_OF_DIGITS = 4;
            int help = (int)Math.pow(10,NUMBER_OF_DIGITS);
            double timeInSeconds = (time*1.0)/1000;
            timeInSeconds = Math.round(timeInSeconds*help)/(help*1.0);

            //Print the results
            System.out.println("TIme elapsed "+time +" ms");
            System.out.println("TIme elapsed "+timeInSeconds +" seconds");
            System.out.println();
            Set<Agent> agents = problem.getAgentsAndStartGoalNodes().keySet();
            for (Agent agent : agents) controller.addAgent(paths.get(agent), agent.getGoal());
            controller.draw();
        }
    }
}