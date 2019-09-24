package Model.Components;

/**
 * This class represents a GridCOstFunction
 * between every adjacent cells the cost will be 1, for diagonal cells the cost will be SQRT(2)
 */
public class GridCostFunction implements ICostFunction {
    @Override
    public double getCost(Node origin, Node target) {


        double sqrt2 = Math.sqrt(2);
        if(origin instanceof GridNode && target instanceof GridNode)
        {
            int x_origin = ((GridNode)origin).getX();
            int y_origin=((GridNode)origin).getY();
            int x_target=((GridNode)target).getX();
            int y_target=((GridNode)target).getY();
            //Diagonal
            if(Math.abs(x_origin-x_target)==1 && Math.abs((y_origin-y_target))==1)
                return sqrt2;
            if(x_origin == x_target&& y_origin == y_target )
                return 0;
            if(Math.abs(x_origin-x_target)==1 || Math.abs((y_origin-y_target))==1)
                return 1;
        }
        if(!origin.isNeighbor(target))
            return Double.MAX_VALUE;
        return 1;
    }
}
