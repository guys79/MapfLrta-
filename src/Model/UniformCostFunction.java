package Model;

public class UniformCostFunction implements ICostFunction {
    @Override
    public double getCost(Node origin, Node target) {
        return 1;
    }
}
