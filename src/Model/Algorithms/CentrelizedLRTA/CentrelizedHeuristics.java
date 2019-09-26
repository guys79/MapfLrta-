package Model.Algorithms.CentrelizedLRTA;

import Model.Components.GridNode;
import Model.Components.Node;

public class CentrelizedHeuristics {
    private static CentrelizedHeuristics instance;
    private CentrelizedLRTAState goal;

    private CentrelizedHeuristics() {

    }

    public void setGoal(CentrelizedLRTAState goal) {
        this.goal = goal;
    }

    public static CentrelizedHeuristics getInstance() {
        if (instance == null)
            instance = new CentrelizedHeuristics();
        return instance;
    }

    public double getVal(CentrelizedLRTAState state) {
        double sum = 0;
        for (int i = 0; i < state.getNumOfAgets(); i++) {
            sum += getHeuristicsForTwoFromFunction(state.getLocationAt(i), goal.getLocationAt(i));
        }

        return sum;
    }

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
    }
}
