package Model.Algorithms.ALSSLRTA;

import Model.*;

import java.util.*;

/**
 * This class represents the aLSS-LRTA* algorithm
 */
public class ALSSLRTA implements IRealTimeSearchAlgorithm {

    private AlssLrtaSearchNode current;//The current state
    private PriorityQueue<AlssLrtaSearchNode> open;//The open list
    private PriorityQueue<AlssLrtaSearchNode> open_min_update;//The open list
    private PriorityQueue<AlssLrtaSearchNode> open_min;//The open list
    private Map<Integer,AlssLrtaSearchNode> open_id;//Key - node's id, Value - the search node itself
    private Map<Integer,AlssLrtaSearchNode> closed;//The closed list
    protected Problem problem;//Tye given problem
    private Agent agent;// The given agent
    private HashSet<Integer> needToBeUpdated;//Set of nodes that need their update flag =true



    /**
     * The constructor
     * @param problem - The given problem
     */
    public ALSSLRTA(Problem problem)
    {
        //this.needToBeUpdated = new HashSet<>();
        this.problem = problem;
        this.closed = new HashMap<>();
        open = new PriorityQueue<>(new CompareAlssNode());
        open_min = new PriorityQueue<>(new CompareHeuristicAlssNode());
        open_min_update = new PriorityQueue<>(new CompareHeuristicUpdateAlssNode());
        open_id = new HashMap<>();

    }

    /**
     * This function returns the current agent
     * @return - The current agent's instance
     */
    protected Agent getAgent() {
        return agent;
    }

    /**
     * This function inhabits an agent in it's goal
     * @param nodeId - The given node id
     */
    protected void inhabitAgent(int nodeId)
    {

    }
    @Override
    public  List<Node> calculatePrefix(Node start, Node goal, int numOfNodesToDevelop, Agent agent) {
        clearOpen();//Clear open
        List<Node> prefix = new ArrayList<>();
        this.agent = agent;
        this.needToBeUpdated = agent.getNeedToBeUpdated();
        current = transformSingleNode(start);


        //The A* procedure
        aStarPrecedure();

        if(open.size() == 0) {
            System.out.println("No solution due to 0 size in a star precedure");
            return null;
        }
        //Extract best state
        AlssLrtaSearchNode next = ExtractBestState();

        /**
         * Check is the agent is done (if not that we will update the node's heuristics)
         */
        if(next.getNode().equals(goal))
        {

            agent.done();
        }
        else
        {
            //The dijkstra procedure
            if(!dijkstra()){
                return null;
            }

        }

        //Calculating prefix

        int id = current.getNode().getId();
        while(next.getNode().getId() != id)
        {

            prefix.add(0,next.getNode());
            next = next.getBack();
        }
        prefix.add(0,next.getNode());

        agent.setNeedToBeUpdated(needToBeUpdated);
        return prefix;
    }

    /**
     * This function will update the node's occupation
     * @param nodeId - The given node id
     * @param time - The time when the agent is occupying the node
     */
    protected void updateNode(int nodeId,int time)
    {

    }
    /**
     * This function will clear the open list
     */
    private void clearOpen()
    {
        open = new PriorityQueue<>(new CompareAlssNode());
        open_min = new PriorityQueue<>(new CompareHeuristicAlssNode());
        open_min_update = new PriorityQueue<>(new CompareHeuristicUpdateAlssNode());
        open_id = new HashMap<>();
    }

    /**
     * This function will add a node to the open list
     * @param node - The given node
     */
    private void openAdd(AlssLrtaSearchNode node)
    {
        if(!open_id.containsKey(node.getNode().getId())) {
            open_min_update.add(node);
            open.add(node);
            open_min.add(node);
            open_id.put(node.getNode().getId(), node);
        }
    }

    /**
     * This function will remove a node from the open list
     * @param node - The given node
     */
    private void openRemove(AlssLrtaSearchNode node)
    {
        if(node!=null) {
            open_min_update.remove(node);
            open.remove(node);
            open_min.remove(node);
            open_id.remove(node.getNode().getId());

        }
    }

    /**
     * This function will set the gValue of a node
     * @param node - The given node
     * @param gValue - The new gValue
     */
    private void setGNode(AlssLrtaSearchNode node,double gValue )
    {
        node.setG(gValue );
        if(open_id.containsKey(node.getNode().getId()))
        {
            open.remove(node);
            open_min_update.remove(node);

            open.add(node);
            open_min_update.add(node);
        }
    }
    /**
     * This function will set the gValue of a node
     * @param node - The given node
     * @param hValue - The new gValue
     */
    private void setHNode(AlssLrtaSearchNode node,double hValue)
    {
        agent.updateHeuristic(node.getNode(),hValue);
        if(open_id.containsKey(node.getNode().getId()))
        {
            openRemove(node);
            openAdd(node);
        }

    }
    /**
     * The A* procedure described in the aLSS-LRTA* algorithm
     */
    protected void aStarPrecedure()
    {


        setGNode(current,0);
        clearOpen();
        openAdd(current);

        int expansions =0;
        AlssLrtaSearchNode state;

        while(expansions<problem.getNumberOfNodeToDevelop())
        {
            //The condition
            state = open.peek();
            if(state == null)
            {
                System.out.println("No solution, but weird");
                return;
            }
            if(state.getNode().getId() == this.agent.getGoal().getId())
            {
                System.out.println("Reached Goal "+agent.getId());
                closed.clear();
                return;
            }
            openRemove(state);
            closed.put(state.getNode().getId(),state);


            //Get the neighbors
            Set<Node> neighbors = state.getNode().getNeighbors().keySet();
            Set<AlssLrtaSearchNode> comp_neighbors = transformNodes(neighbors);

            //For each neighbor
            for(AlssLrtaSearchNode node : comp_neighbors)
            {
                double temp = state.getG()+ problem.getCost(state.getNode(),node.getNode());
                if(node.getG()>temp)
                {

                    setGNode(node,temp);
                    node.setBack(state);
                    openAdd(node);
                }
            }
            expansions++;


        }



    }

    /**
     * The Dijkstra procedure described in the aLSS-LRTA* algorithm
     */
    private boolean dijkstra()
    {

        Collection<AlssLrtaSearchNode> nodes = new HashSet<>(closed.values());
        for(AlssLrtaSearchNode node: nodes)
        {
            setHNode(node, Double.MAX_VALUE);
        }


        while(closed.size()!=0 && open.size()>0)
        {

            AlssLrtaSearchNode min_h_node = open_min.poll();
            openRemove(min_h_node);

            //If H(s) > H0(S)
            if(agent.getHeuristicValue(min_h_node.getNode()) > agent.getInitialHeuristicValue(min_h_node.getNode()))
            {
                min_h_node.setUpdated(true);
                this.needToBeUpdated.add(min_h_node.getNode().getId());
            }

            // If closed contains the state
            if(closed.containsKey(min_h_node.getNode().getId()))
            {
                closed.remove(min_h_node.getNode().getId());

            }

            //Update the neighbors heuristics
            Set<Node> neighbors = min_h_node.getNode().getNeighbors().keySet();
            Set<AlssLrtaSearchNode> comp_neighbors = transformNodes(neighbors);
            double heuristic_value = agent.getHeuristicValue(min_h_node.getNode());

            //For each neighbor
            for(AlssLrtaSearchNode node : comp_neighbors)
            {
                double temp = problem.getCost(node.getNode(),min_h_node.getNode())+heuristic_value;
                if(closed.containsKey(node.getNode().getId()) && agent.getHeuristicValue(node.getNode())>temp)
                {
                    setHNode(node,temp);
                    if(!open_id.containsKey(node.getNode().getId()))
                    {
                        openAdd(node);
                    }
                }
            }

        }

        return true;
    }

    /**
     * The Extract-Best-State procedure in the aLSS-LRTA*
     * @return - The best node
     */
    private AlssLrtaSearchNode ExtractBestState()
    {

        AlssLrtaSearchNode best =open_min_update.poll();

        return best;
    }
    /**
     * This function will transform a node from class Node to a node from class AlssLrtaSearchNode
     * @param node - The given node
     * @return - an AlssLrtaSearchNode object
     */
    private AlssLrtaSearchNode transformSingleNode(Node node)
    {
        AlssLrtaSearchNode new_node;
        if(closed.containsKey(node.getId()))
        {
            new_node = closed.get(node.getId());
        }
        else
        {

            if(open_id.containsKey(node.getId()))
            {
                new_node = open_id.get(node.getId());
            }
            else
            {
                new_node = new AlssLrtaSearchNode(node);
                if(this.needToBeUpdated.contains(node.getId()))
                {
                    new_node.setUpdated(true);

                }


            }

        }

        return new_node;
    }

    /**
     * This function will transform the given set of type Node to a set of type AlssLrtaSearchNode
     * @param nodes - The given set
     * @return - The new set of objects of type AlssLrtaSearchNode
     */
    private Set<AlssLrtaSearchNode> transformNodes(Set<Node> nodes)
    {
        Set<AlssLrtaSearchNode> new_node_set = new HashSet<>();
        for(Node node : nodes)
        {
            AlssLrtaSearchNode new_node;
            new_node = transformSingleNode(node);
            new_node_set.add(new_node);
        }
        return new_node_set;
    }

    /**
     * This function will return the f value of the given node
     * @param node - The given node
     * @return - The f value of the given node
     */
    private double getF(AlssLrtaSearchNode node)
    {
        if(node==null)
            return -1;
        return node.getG()+agent.getHeuristicValue(node.getNode());
    }

    /**
     * This class is used to compare between to node in the open list
     * In our case, we compare the f value
     */
    public class CompareAlssNode implements Comparator<AlssLrtaSearchNode>
    {
        public CompareAlssNode()
        {

        }

        @Override
        public int compare(AlssLrtaSearchNode o1, AlssLrtaSearchNode o2) {

            double f1 = getF(o1);
            double f2 = getF(o2);
            if(f1==f2)
                return 0;
            if(f1<f2)
                return -1;
            return 1;
        }
    }

    /**
     * This class is used to compare between to node in the open list
     * In our case, we compare the h value
     */
    public class CompareHeuristicAlssNode implements Comparator<AlssLrtaSearchNode>
    {
        public CompareHeuristicAlssNode()
        {

        }

        @Override
        public int compare(AlssLrtaSearchNode o1, AlssLrtaSearchNode o2) {
            double h1 = agent.getHeuristicValue(o1.getNode());
            double h2 = agent.getHeuristicValue(o2.getNode());
            if(h1==h2)
                return 0;
            if(h1<h2)
                return -1;
            return 1;
        }
    }
    /**
     * This class is used to compare between to node in the open list
     * In our case, we compare the h value
     */
    public class CompareHeuristicUpdateAlssNode extends CompareAlssNode
    {
        public CompareHeuristicUpdateAlssNode()
        {

        }

        @Override
        public int compare(AlssLrtaSearchNode o1, AlssLrtaSearchNode o2) {
            boolean isUpdated1 = o1.isUpdated();
            boolean isUpdated2 = o2.isUpdated();
            if(isUpdated1 && isUpdated2)
            {
                return super.compare(o1,o2);
            }
            if(isUpdated1 || isUpdated2)
            {
                if(!isUpdated1)//Smaller
                    return -1;
                return 1;
            }
            return super.compare(o1,o2);
        }
    }
}
