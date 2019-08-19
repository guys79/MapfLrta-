package Model.ALSSLRTA;

import Model.Agent;
import Model.IRealTimeSearchAlgorithm;
import Model.Node;
import Model.Problem;

import java.util.List;
import java.util.PriorityQueue;

/**
 * This class represents the aLSS-LRTA* algorithm
 */
public class ALSSLRTA implements IRealTimeSearchAlgorithm {

    private AlssLrtaSearchNode current;
    private PriorityQueue<AlssLrtaSearchNode> open;
    private Problem problem;

    public ALSSLRTA(Problem problem)
    {
        this.problem = problem;
        open = new PriorityQueue<>();
    }

    @Override
    public List<Node> calculatePrefix(Node start, Node goal, int numOfNodesToDevelop, Agent agent) {
        current = new AlssLrtaSearchNode(start);
        return null;
    }

    private void aStarPrecedure(Node start)
    {
        current.setG(0);
        open.clear();
        open.add(current);
        int expansions =0;

    }
}
