package Model.ALSSLRTA;

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
    private Node goal;//The goal node
    private HashMap<Integer,Node> needToBeUpdated;



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


    protected Agent getAgent() {
        return agent;
    }

    protected List<Node> calculatePrefix(Node start, Node goal, int numOfNodesToDevelop, Agent agent, PriorityQueue<AlssLrtaSearchNode> open, PriorityQueue<AlssLrtaSearchNode> open_min, PriorityQueue<AlssLrtaSearchNode> open_min_update, Map<Integer,AlssLrtaSearchNode> closed) {
        this.open = open;
        this.open_min = open_min;
        this.open_min_update = open_min_update;
        this.closed = closed;
        return calculatePrefix(start,goal,numOfNodesToDevelop,agent);
    }
    protected void inhabitAgent(int nodeId)
    {

    }
    @Override
    public  List<Node> calculatePrefix(Node start, Node goal, int numOfNodesToDevelop, Agent agent) {
        this.goal = goal;
        clearOpen();
        //this.closed = agent.getClosed();
        List<Node> prefix = new ArrayList<>();
        this.agent = agent;
        this.needToBeUpdated = agent.getNeedToBeUpdated();
        current = transformSingleNode(start);



        aStarPrecedure();

        if(open.size() == 0) {
            System.out.println("No solution due to 0 size in a star precedure");
            return null;
        }

        AlssLrtaSearchNode next = ExtractBestState();

        if(next.getNode().equals(goal))
        {
            agent.done();
        }
        else
        {
            if(!dijkstra())
            {
                return null;
            }

        }
        AlssLrtaSearchNode prev= next;
        while(next != current)
        {

            prefix.add(0,next.getNode());
            next = next.getBack();
        }
        prefix.add(0,next.getNode());
        for(int i=0;i<prefix.size();i++)
        {
            updateNode(next.getNode().getId(),i);
        }
        if(prev.getNode().equals(goal))
        {
            inhabitAgent(prev.getNode().getId());
        }
        //agent.setClosed(this.closed);
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
                return;
            }
            if(state.getNode().getId() == this.agent.getGoal().getId())
            {
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
                    if(open_id.containsKey(node.getNode().getId()))
                        openRemove(node);
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
        Iterator<AlssLrtaSearchNode> iter =nodes.iterator();
        while(iter.hasNext())
        {
            AlssLrtaSearchNode node = iter.next();
            //agent.updateHeuristic(node.getNode(),Double.MAX_VALUE);
            setHNode(node, Double.MAX_VALUE);
        }

        //boolean first = true;
        while(closed.size()!=0)
        {

          /*  if(first)
            {
                AlssLrtaSearchNode temp = open_min.poll();
                if(temp!=null)
                    open_min.add(temp);
                first = false;
            }
          */
            AlssLrtaSearchNode min_h_node = open_min.poll();
            openRemove(min_h_node);

            if(min_h_node == null)
            {
                return false;
            }
            if(agent.getHeuristicValue(min_h_node.getNode()) > agent.getInitialHeuristicValue(min_h_node.getNode()))
            {
                min_h_node.setUpdated(true);
                this.needToBeUpdated.put(min_h_node.getNode().getId(),min_h_node.getNode());
            }

            if(closed.containsKey(min_h_node.getNode().getId()))
            {
                closed.remove(min_h_node.getNode().getId());

            }

            Set<Node> neighbors = min_h_node.getNode().getNeighbors().keySet();
            Set<AlssLrtaSearchNode> comp_neighbors = transformNodes(neighbors);
            double heuristic_value = agent.getHeuristicValue(min_h_node.getNode());
            //For each neighbor
            for(AlssLrtaSearchNode node : comp_neighbors)
            {
                double temp = problem.getCost(node.getNode(),min_h_node.getNode())+heuristic_value;
                if(closed.containsKey(node.getNode().getId()) && agent.getHeuristicValue(node.getNode())>temp)
                {
                    //agent.updateHeuristic(node.getNode(),temp);
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
        //Init
      //  AlssLrtaSearchNode temp = open_min_update.poll();
        //if(temp!=null)
          //  open_min_update.add(temp);
        AlssLrtaSearchNode best =open_min_update.peek();
        //testOpenMinUpdate(best);
       // openRemove(best);
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
             //   System.out.println(agent);
                if(this.needToBeUpdated.containsKey(new_node.getNode().getId()))
                {
                    new_node.setUpdated(true);
                }


            }
        }

        return new_node;
    }
    private void testOpen(AlssLrtaSearchNode node)
    {
        for(AlssLrtaSearchNode node2:this.open)
        {
            if(getF(node2)<getF(node))
            {
                System.out.println("fuck1");
                return;
            }
        }
    }
    private void testOpenMin(AlssLrtaSearchNode node)
    {
        for(AlssLrtaSearchNode node2:this.open)
        {
            if(agent.getHeuristicValue(node2.getNode())<agent.getHeuristicValue(node.getNode()))
            {
                System.out.println("fuck2");
                return;
            }
        }
    }
    private void testOpenMinUpdate(AlssLrtaSearchNode node)
    {
        boolean flag = node.isUpdated();
        if(!flag)
        {
            for(AlssLrtaSearchNode node2:this.open)
            {
                if(node2.isUpdated())
                    continue;
                if(getF(node2)<getF(node))
                {
                    System.out.println("fuck3");
                    return;
                }
            }
        }
        else {
            for (AlssLrtaSearchNode node2 : this.open) {

                if (getF(node2) < getF(node)) {
                    System.out.println("fuck3");
                    return;
                }
            }
        }
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
     * This function describes the condition for continuing the A* procedure
     * @param numOfExpensions - The number of expansions
     * @return - Number of nodes with minimal f IFF the A* procedure will continue
     */
    private int aStarCondition (int numOfExpensions)
    {
        Set<AlssLrtaSearchNode> min_f_value_nodes = getMinFNodes();
        int ans = 0;
        if(numOfExpensions>=problem.getNumberOfNodeToDevelop() || min_f_value_nodes.size() == 0)
        {

            ans = 0;
        }
        else {
            AlssLrtaSearchNode goal = transformSingleNode(this.goal);
            if (!min_f_value_nodes.contains(goal)) {
                ans= min_f_value_nodes.size();
            }

        }
        for(AlssLrtaSearchNode node: min_f_value_nodes)
        {
            openAdd(node);
        }
        return ans;

    }


    /**
     * This function will return all the nodes eith the minimal f values
     * @return - A list of the nodes
     */
    private Set<AlssLrtaSearchNode> getMinFNodes()
    {
        Set<AlssLrtaSearchNode> min_f_value_nodes = new HashSet<>();
        double min;

        //Initializing - finding the f value
     /*   if(open.size()!=0) {
            AlssLrtaSearchNode temp = open.poll();
            open.add(temp);
        }*/
       // printF(this.open);
        AlssLrtaSearchNode poped = open.poll();
        //testOpen(poped);
        //System.out.println("chosen "+poped);
        double f_value;
        if(poped!=null)
        {
            openRemove(poped);
            f_value = getF(poped);
            min = f_value;
            min_f_value_nodes.add(poped);
        }
        else//If the open queue is empty, return an empty list
        {
            return min_f_value_nodes;
        }


        poped = open.poll();
       // testOpen(poped);
        f_value = getF(poped);
        //While the queue is not empty and the node has the same f value
        while(poped!=null &&min == f_value)
        {
            openRemove(poped);
            min_f_value_nodes.add(poped);
            poped = open.poll();
          //  testOpen(poped);
            f_value = getF(poped);
        }
        if(min>f_value && poped!=null)
        {
            System.out.println("fuckkkk");
        }
        //Adding the last node (has a bigger f value)

        if(poped!=null)
        {
            open.add(poped);
            if(min>f_value)
            {
                System.out.println("WOWWWWWWWWWWWWWWWWWWWWWWWW");
            }
        }


        return min_f_value_nodes;

    }
    private void printF(PriorityQueue<AlssLrtaSearchNode> list)
    {
        for(AlssLrtaSearchNode node: list)
        {
            System.out.println( node+" "+getF(node));
        }
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
           // boolean isUpdated1=agent.isUpdatesd(o1.getNode()) && o1.getG()!=Double.MAX_VALUE ;
            //boolean isUpdated2=agent.isUpdatesd(o2.getNode()) && o2.getG()!=Double.MAX_VALUE;
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
