import java.util.HashMap;
import java.util.Map;

public class AgentHeuristics implements IAgentHeuristics {


    private Map<Node,Double> localHeuristics;//Key - nodeId, value - heuristic value
    private Node goal;
    public AgentHeuristics(Node goal)
    {
        localHeuristics = new HashMap<>();
        this.goal = goal;
        localHeuristics.put(goal,0d);
    }

    @Override
    public double getHeuristics(Node node) {

        if(localHeuristics.containsKey(node))
            return getHeuristicsFromMemory(node);
        return getHeuristicsFromFunction(node);
    }

    @Override
    public void updateHeuristics(Node node, double newVal) {
        this.localHeuristics.put(node,newVal);
    }

    private double getHeuristicsFromMemory(Node n)
    {
        return this.localHeuristics.get(n);
    }
    private double getHeuristicsFromFunction(Node n)
    {
        // TODO: 8/10/2019 Implement heuristic function
        double value = 0;
        if(n instanceof GridNode && goal instanceof GridNode) {
            GridNode gd1 = (GridNode) n;
            GridNode gd2 = (GridNode) goal;
            value = Math.sqrt(Math.pow(gd1.getX()-gd2.getX(),2)+Math.pow(gd1.getY()-gd2.getY(),2));
        }
        else
        {
            System.out.println("Fuck");
        }
        return value;
    }
}

