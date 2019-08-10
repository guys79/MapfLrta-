import java.util.*;

/**
 * This class represents the LRTA* algorithm
 */
public class LRTA implements IRealTimeSearchAlgorithm {

    private Node current;
    private Agent agent;
    /**
     * This function will calculate a prefix from the start node to the goal node path
     * @param start - The start node
     * @param goal - The goal node
     * @param numOfNodesToDevelop - The number of nodes to develop in the search
     * @param agent - The agent
     * @return - A prefix from the start node to the goal node path
     */
    public List<Node> calculatePrefix(Node start,Node goal, int numOfNodesToDevelop, Agent agent) {

        this.agent = agent;
        List<Node> prefix = new ArrayList<>();//The prefix to be
        PriorityQueue<Node> open = new PriorityQueue<>(new CompareNode());// The open queue
        open.add(start);
        boolean first = true;
        Node prev = null;
        //For as many node as possible to develop
        while(numOfNodesToDevelop>0)
        {

            if(!first)
            {
                double f = calculateF(current);
                agent.updateHeuristic(prev,f);
                prev = current;



            }
            //Pop best node
            current = open.poll();
            numOfNodesToDevelop--;
            prefix.add(current);

            //If we have reached our destination
            if(current.equals(goal))
            {
                return prefix;
            }



            first = false;



            //Get the neighbors
            Set<Node> neighbors = current.getNeighbors().keySet();
            for(Node neighbor : neighbors)
            {
                if(open.contains(neighbor))
                    continue;
                numOfNodesToDevelop--;
                open.add(neighbor);
            }

        }
        return prefix;
    }

    /**
     * This class is used to compare between to node in the open list
     * In our case, we compare the f value
     */
    public class CompareNode implements Comparator<Node>
    {
        public CompareNode()
        {

        }


        @Override
        public int compare(Node o1, Node o2) {

            double f1 = calculateF(o1);
            double f2 = calculateF(o2);
            return (int)(f2 - f1);
        }


    }
    /**
     * This function will calculate the value f of the given node
     * @param n - The given node
     * @return - The f value of the given node
     */
    private double calculateF(Node n)
    {
        double g = current.getWeight(n);
        double h = agent.getHeuristicValue(n);
        return g+h;
    }
}
