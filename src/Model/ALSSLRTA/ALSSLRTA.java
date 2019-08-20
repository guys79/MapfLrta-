package Model.ALSSLRTA;

import Model.Agent;
import Model.IRealTimeSearchAlgorithm;
import Model.Node;
import Model.Problem;

import java.util.*;

/**
 * This class represents the aLSS-LRTA* algorithm
 */
public class ALSSLRTA implements IRealTimeSearchAlgorithm {

    private AlssLrtaSearchNode current;//The current state
    private PriorityQueue<AlssLrtaSearchNode> open;//The open list
    private Map<Integer,AlssLrtaSearchNode> open_id;//Key - node's id, Value - the search node itself
    private Map<Integer,AlssLrtaSearchNode> closed;//The closed list
    private Problem problem;//Tye given problem
    private Agent agent;// The given agent

    /**
     * The constructor
     * @param problem - The given problem
     */
    public ALSSLRTA(Problem problem)
    {
        this.problem = problem;
        this.closed = new HashMap<>();
        open = new PriorityQueue<>();
        open_id = new HashMap<>();
    }

    @Override
    public List<Node> calculatePrefix(Node start, Node goal, int numOfNodesToDevelop, Agent agent) {
        current = new AlssLrtaSearchNode(start);
        this.agent = agent;
        return null;
    }

    /**
     * The A* procedure described in the aLSS-LRTA* algorithm
     */
    private void aStarPrecedure()
    {
        current.setG(0);
        open.clear();
        open_id.clear();
        open.add(current);
        open_id.put(current.getNode().getId(),current);
        int expansions =0;

        Set<AlssLrtaSearchNode> min_f_value_nodes = getMinFNodes();
        AlssLrtaSearchNode state;
        Iterator<AlssLrtaSearchNode> iter = min_f_value_nodes.iterator();
        while(aStarCondition(min_f_value_nodes,expansions))
        {
            state = iter.next();//Get best state
            iter.remove();

            //Insert to close
            closed.put(state.getNode().getId(),state);
            open.remove(state);
            open_id.remove(state.getNode().getId());

            //Get the neigbors
            Set<Node> neighbors = state.getNode().getNeighbors().keySet();
            Set<AlssLrtaSearchNode> comp_neighbors = transformNodes(neighbors);

            //For each neighbor
            for(AlssLrtaSearchNode node : comp_neighbors)
            {
                double temp = state.getG()+ problem.getCost(state.getNode(),node.getNode());
                if(node.getG()>temp)
                {
                    node.setG(temp);
                    node.setBack(state.getNode());
                    open_id.put(node.getNode().getId(),node);
                    open.add(node);
                }
            }

        }
        //Adding the rest of the nodes to the open list
        for(AlssLrtaSearchNode node : min_f_value_nodes)
        {
            open_id.put(node.getNode().getId(),node);
            open.add(node);
        }

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
     * This function describes the condition for continuing the A* procedure
     * @param min_f_value_nodes - The Set of nodes with the minimal f values
     * @param numOfExpensions - The number of expansions
     * @return - True IFF the A* procedure will continue
     */
    private boolean aStarCondition (Set<AlssLrtaSearchNode> min_f_value_nodes,int numOfExpensions)
    {
        if(numOfExpensions>=problem.getNumberOfNodeToDevelop() || min_f_value_nodes.size() == 0)
        {
            return false;
        }
        Node goal = this.problem.getAgentGoalNode(agent);

        return !min_f_value_nodes.contains(goal);


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
        AlssLrtaSearchNode poped = open.poll();

        double f_value;
        if(poped!=null)
        {
            open_id.remove(poped.getNode().getId());
            f_value = getF(poped);
            min = f_value;
            min_f_value_nodes.add(poped);
        }
        else//If the open queue is empty, return an empty list
        {
            return min_f_value_nodes;
        }


        poped = open.poll();
        f_value = getF(poped);
        //While the queue is not empty and the node has the same f value
        while(poped!=null &&min == f_value)
        {
            open_id.remove(poped.getNode().getId());
            min_f_value_nodes.add(poped);
            poped = open.poll();
            f_value = getF(poped);
        }

        //Adding the last node (has a bigger f value)
        if(poped!=null)
        {
            open.add(poped);
            open_id.put(poped.getNode().getId(),poped);
        }

        return min_f_value_nodes;

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
}
