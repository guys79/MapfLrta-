package Model.Algorithms.CentrelizedLRTA;

import Model.Algorithms.Dijkstra.ShortestPathGenerator;
import Model.Components.GridNode;
import Model.Components.Node;

/**
 * This cass handles the heuristics of the centralized state (Singleton DP used)
 */
public class CentrelizedHeuristics {
    private static CentrelizedHeuristics instance;//The instance
    private CentrelizedLRTAState goal;//The goal state

    /**
     * The private constructor
     */
    private CentrelizedHeuristics() {

    }

    /**
     * This function will set the goal with the give state
     * @param goal - The given goal state
     */
    public void setGoal(CentrelizedLRTAState goal) {
        this.goal = goal;
    }

    /**
     * This function will return the instance of the class (the Singleton)
     * @return - the instance
     */
    public static CentrelizedHeuristics getInstance() {
        if (instance == null)
            instance = new CentrelizedHeuristics();
        return instance;
    }

    /**
     * This function will return the heuristic value
     * @param state - The given state
     * @return - The heuristic value of the given state
     */
    public double getVal(CentrelizedLRTAState state) {
        double sum = 0;
        for (int i = 0; i < state.getNumOfAgets(); i++) {
            sum += getHeuristicsForTwoFromFunction2(state.getLocationAt(i), goal.getLocationAt(i));
            //sum =Double.max(sum, getHeuristicsForTwoFromFunction2(state.getLocationAt(i), goal.getLocationAt(i)));
           // sum += getHeuristicsForTwoFromFunction(state.getLocationAt(i), goal.getLocationAt(i));
        }

        return sum;
    }

    /**
     * This function will calculate the heuristic of the origin goal af if the target state was the goal
     * @param origin - The origin state
     * @param target - The target state
     * @return - The heuristic value
     */
    protected double getHeuristicsForTwoFromFunction(Node origin, Node target) {
        //Manheten Distance
        double value = 0;
        if (origin instanceof GridNode && target instanceof GridNode) {
            GridNode gd1 = (GridNode) origin;
            GridNode gd2 = (GridNode) target;
            double D = 1;
            double D2 = Math.sqrt(2);
            double dx = Math.abs(gd1.getX() - gd2.getX());
            double dy = Math.abs(gd1.getY() - gd2.getY());

            value = D * (dx + dy) + (D2 - 2 * D) * Math.min(dx, dy);
        } else//In case the node is not GridNode, in our case it is unexpected
        {
            System.out.println("Fuck");
        }
        return value;
    }/**
     * This function will calculate the heuristic of the origin goal af if the target state was the goal
     * @param origin - The origin state
     * @param target - The target state
     * @return - The heuristic value
     */
    protected double getHeuristicsForTwoFromFunction2(Node origin, Node target) {
      return ShortestPathGenerator.getInstance().getShortestPath(origin,target);
    }
}
