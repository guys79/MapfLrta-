package Model.Algorithms.ALSSLRTA;

import Model.Algorithms.IRealTimeSearchAlgorithm;
import Model.Algorithms.MAALSSLRTA.MaAlssLrtaSearchNode;
import Model.Components.Agent;
import Model.Components.Node;
import Model.Components.Problem;

import java.util.*;

/**
 * This class represents the aLSS-LRTA* algorithm
 */
public class ALSSLRTA implements IRealTimeSearchAlgorithm {

    private AlssLrtaSearchNode current;//The current state
    protected PriorityQueue<AlssLrtaSearchNode> open;//The open list
    private PriorityQueue<AlssLrtaSearchNode> open_min_update;//The open list
    private PriorityQueue<AlssLrtaSearchNode> open_min;//The open list
    protected Map<Integer,AlssLrtaSearchNode> open_id;//Key - node's id, Value - the search node itself
    protected Map<Integer,AlssLrtaSearchNode> closed;//The closed list
    protected Problem problem;//Tye given problem
    private Agent agent;// The given agent
    private HashSet<Integer> needToBeUpdated;//Set of nodes that need their update flag =true
    protected int toDevelop;//Number of nodes to develop




    /**
     * The constructor
     * @param problem - The given problem
     */
    public ALSSLRTA(Problem problem)
    {

        this.problem = problem;
        this.toDevelop = -1;
        this.closed = new HashMap<>();
        open = new PriorityQueue<>(new CompareAlssNode());
        open_min = new PriorityQueue<>(new CompareHeuristicAlssNode());
        open_min_update = new PriorityQueue<>(new CompareHeuristicUpdateAlssNode());
        open_id = new HashMap<>();

    }

    /**
     * This function will return the current SearchNode
     * @return - The current search node
     */
    protected AlssLrtaSearchNode getCurrent()
    {
        return this.current;
    }

    /**
     * This function will set the current SearchNode
     * @param current - The current state
     */
    public void setCurrent(AlssLrtaSearchNode current) {
        this.current = current;
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
     * @param agentId  - The agent id
     */
    protected void inhabitAgent(int nodeId,int agentId)
    {

    }
    @Override
    public  List<Node> calculatePrefix(Node start, Node goal, int numOfNodesToDevelop, Agent agent) {

        clearOpen();//Clear open
        List<Node> prefix = new ArrayList<>();
        this.agent = agent;
        this.toDevelop = numOfNodesToDevelop;
        closed.clear();
        this.needToBeUpdated = agent.getNeedToBeUpdated();
        current = transformSingleNode(start,0);



        //The A* procedure
        aStarPrecedure();

        if(open.size() == 0) {
            System.out.println("No solution due to 0 size in a star precedure");
            return null;
        }
        //Extract best state
        AlssLrtaSearchNode next = ExtractBestState();


        // Check is the agent is done (if not that we will update the node's heuristics)

       if(next.getNode().equals(goal))
        {

            agent.done();

        }
        /*
        else
        {
            //The dijkstra procedure
            if(!){
                return null;
            }

        }*/
        dijkstra();

        //Calculating prefix

        int id = current.getNode().getId();
        boolean flag = current instanceof MaAlssLrtaSearchNode && next instanceof MaAlssLrtaSearchNode;
        boolean first = false;
        while(true) {
            while (next.getNode().getId() != id || first) {
                first = false;
                prefix.add(0, next.getNode());
                next = next.getBack();
            }
            if (flag) {
                if (!(((MaAlssLrtaSearchNode) next).getTime() > ((MaAlssLrtaSearchNode) current).getTime()))
                    break;
            } else
                break;
            first = true;

        }
        prefix.add(0, next.getNode());
        if(agent.isDone())
        {

            inhabitAgent(goal.getId(),prefix.size());


        }

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
    protected void clearOpen()
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
    protected void openAdd(AlssLrtaSearchNode node)
    {
        if(canAdd(node)) {
            open_min_update.add(node);
            open.add(node);
            open_min.add(node);
            open_id.put(node.getNode().getId(), node);
        }

    }

    /**
     * This function will if it is possible to add the node to the open list
     * @param node - The given node
     * @return - True IFF it is possible to add the node to the open list
     */
    protected boolean canAdd(AlssLrtaSearchNode node)
    {
        return !open_id.containsKey(node.getNode().getId());
    }
    /**
     * This function will remove a node from the open list
     * @param node - The given node
     */
    protected void openRemove(AlssLrtaSearchNode node)
    {
        if(node!=null) {
            open_min_update.remove(node);
            open.remove(node);
            open_min.remove(node);
            open_id.remove(node.getNode().getId());

        }
    }
    /**
     * This function will remove a node from the open list
     * @param node - The given node
     */
    protected void openRemoveNoId(AlssLrtaSearchNode node)
    {

            open_min_update.remove(node);
            open.remove(node);
            open_min.remove(node);


    }
    /**
     * This function will set the gValue of a node
     * @param node - The given node
     * @param gValue - The new gValue
     */
    protected void setGNode(AlssLrtaSearchNode node,double gValue )
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

        while(expansions<toDevelop)
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

                boolean flag;
                if(state.getBack() == null)//In place
                    flag =canBeAtTime(time(state),state.getNode(),state.getNode());
                else
                    flag =canBeAtTime(time(state.getBack()),state.getBack().getNode(),state.getNode());
                //if can inhabit
                if(flag) {
                    System.out.println();
                    //closed.clear();
                    return;
                }
            }
            openRemove(state);
            closed.put(state.getNode().getId(),state);
            state.getNode().updateAverage(false);

            //Get the neighbors
            Set<Node> neighbors = getNeighbors(state.getNode());
            int time = time(state);
            Set<AlssLrtaSearchNode> comp_neighbors = transformNodes(neighbors,time);

            //For each neighbor
            for(AlssLrtaSearchNode node : comp_neighbors)
            {
                if(node.getNode().getId() == this.agent.getGoal().getId())
                {
                    if(!canInhabit(node))
                        continue;
                }
                if(!canBeAtTime(time,state.getNode(),node.getNode()))
                    continue;
                double temp = state.getG()+ problem.getCost(state.getNode(),node.getNode());
                if(node.getG()>temp)
                {

                    setGNode(node,temp);
                    node.setBack(state);
                    node.setNumInChain(state.getNumInChain()+1);
                    openAdd(node);
                }
                else
                {
                    if(node.getG()==temp) {
                        AlssLrtaSearchNode pulled = this.closed.get(node.getNode().getId());
                        if (pulled != null) {
                            if (node instanceof MaAlssLrtaSearchNode && pulled instanceof MaAlssLrtaSearchNode) {
                                if (((MaAlssLrtaSearchNode) node).getTime() < ((MaAlssLrtaSearchNode) pulled).getTime()) {
                               //     System.out.println(agent.getId() + " agent id");
                                    setGNode(node, temp);
                                      node.setBack(state);
                                    node.setNumInChain(state.getNumInChain()+1);
                                    openAdd(node);
                                }

                            }
                        }
                    }
                }
            }
            expansions++;


        }

        System.out.println();

    }

    /**
     * This function will return true IFF the agent can inhabit the node
     * @param node - The given node
     * @return - true IFF the agent can inhabit the node
     */
    protected boolean canInhabit(AlssLrtaSearchNode node)
    {
        System.out.println("");
        return true;

    }

    /**
     * This function will check if the agent can move from the origin node to the target node at the given time
     * @param time - The given time
     * @param origin - The origin node
     * @param target  - The target node
     * @return - True IFF the agent can move from the origin node to the target node at the given time
     */
    protected boolean canBeAtTime(int time, Node origin,Node target)
    {
        return true;
    }

    /**
     * This funcion will return the time of which the agent will be at if he were to a node FROM the given node
     * @param node - The given node
     * @return - The time of which the agent will be at if he were to a node FROM the given node
     */
    protected int time(AlssLrtaSearchNode node)
    {
        return 0;
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


        while(closed.size()!=0 && open_min.size()>0)
        {

            AlssLrtaSearchNode min_h_node = open_min.poll();
            openRemove(min_h_node);


            //If H(s) > H0(S)
            if(agent.getHeuristicValue(min_h_node.getNode()) > agent.getInitialHeuristicValue(min_h_node.getNode()))
            {

                this.needToBeUpdated.add(min_h_node.getNode().getId());
            }

            // If closed contains the state
            if(closed.containsKey(min_h_node.getNode().getId()))
            {
                closed.remove(min_h_node.getNode().getId());

            }

            //Update the neighbors heuristics
            Set<Node> neighbors = getNeighbors(min_h_node.getNode());
            int time = time(min_h_node);
            Set<AlssLrtaSearchNode> comp_neighbors = transformNodes(neighbors,time);
            double heuristic_value = agent.getHeuristicValue(min_h_node.getNode());

            //For each neighbor
            for(AlssLrtaSearchNode node : comp_neighbors)
            {
                double temp = problem.getCost(node.getNode(),min_h_node.getNode())+heuristic_value;
                if(closed.containsKey(node.getNode().getId()) && agent.getHeuristicValue(node.getNode())>temp)
                {
                    setHNode(node,temp);
                    node.getNode().updateAverage(true);
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
     * This function will return the neighbors of the node
     * @param node - The given node
     * @return  - a Set of nodes (the given node's neighbors)
     */
    protected Set<Node> getNeighbors(Node node)
    {
        return node.getNeighbors().keySet();
    }

    /**
     * The Extract-Best-State procedure in the aLSS-LRTA*
     * @return - The best node
     */
    protected AlssLrtaSearchNode ExtractBestState()
    {

        AlssLrtaSearchNode best =open_min_update.poll();

        return best;
    }

    /**
     * Peek the Extract-Best-State procedure in the aLSS-LRTA*
     * @return - The best node
     */
    protected AlssLrtaSearchNode peekBestState()
    {

        AlssLrtaSearchNode best =open_min_update.peek();

        return best;
    }
    /**
     * This function will transform a node from class Node to a node from class AlssLrtaSearchNode
     * @param node - The given node
     * @param time - the gien time
     * @return - an AlssLrtaSearchNode object
     */
    protected AlssLrtaSearchNode transformSingleNode(Node node,int time)
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
     * @param - The given time
     * @return - The new set of objects of type AlssLrtaSearchNode
     */
    protected Set<AlssLrtaSearchNode> transformNodes(Set<Node> nodes,int time)
    {
        Set<AlssLrtaSearchNode> new_node_set = new HashSet<>();
        for(Node node : nodes)
        {
            AlssLrtaSearchNode new_node;
            new_node = transformSingleNode(node,time);
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

            if(o1.getNode().getId() == agent.getGoal().getId())
                return -1;
            if(o2.getNode().getId() == agent.getGoal().getId())
                return 1;

                double f1 = getF(o1);
                double f2 = getF(o2);
                if (f1 == f2) {
                    if(o1 instanceof MaAlssLrtaSearchNode && o2 instanceof MaAlssLrtaSearchNode) {
                        boolean flag1 = o1.getBack()!=null && o1.getBack().getNode().getId()!=o1.getNode().getId();
                        boolean flag2 = o2.getBack()!=null && o2.getBack().getNode().getId()!=o2.getNode().getId();
                        if(!((flag1 && flag2) || (!flag1 && !flag2))) {
                            if (flag1)
                                return -1;
                            return 1;
                        }
                        return ((MaAlssLrtaSearchNode) o1).getTime() - ((MaAlssLrtaSearchNode) o2).getTime();
                    }

                    return 0;
                }
                if (f1 < f2)
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



                boolean flag =false;
                if(o1 instanceof MaAlssLrtaSearchNode && o2 instanceof MaAlssLrtaSearchNode)
                {
                    flag = true;
                    boolean flag1 = canInhabit(o1);
                    boolean flag2 = canInhabit(o2);
                    if(!((flag1 && flag2) ||(!flag1 && !flag2)))
                    {
                        if(flag1)
                            return -1;
                        return 1;
                    }
                }
                if(!(o1.getNumInChain()<=problem.getPrefixLength() && (o2.getNumInChain()<=problem.getPrefixLength()))) {
                     if(o1.getNumInChain()<=problem.getPrefixLength())
                         return -1;
                     return 1;
                }
                boolean isUpdated1 = o1.isUpdated();
                boolean isUpdated2 = o2.isUpdated();
                if (isUpdated1 && isUpdated2) {
                    return super.compare(o1, o2);
                }
                if (isUpdated1 || isUpdated2) {
                    if (!isUpdated1)//Smaller
                        return -1;
                    return 1;
                }
                int res =  super.compare(o1, o2);
                if(res!=0)
                    return res;
                if(flag)
                {
                    return ((MaAlssLrtaSearchNode) o1).getTime() - ((MaAlssLrtaSearchNode) o2).getTime();
                }
                return 0;



        }
    }


}
