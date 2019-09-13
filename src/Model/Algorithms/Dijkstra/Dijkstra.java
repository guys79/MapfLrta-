package Model.Algorithms.Dijkstra;

import Model.Node;

import javax.print.MultiDoc;
import java.util.*;

/**
 * This class represents the Dijkstra algorithm
 * Implemented using Singleton DP
 */
public class Dijkstra {

    private Node origin;//The origin node
    private int test;
    private static Dijkstra dijkstra;;//The instance of the class
    private Map<Integer, DijkstraSearchNode> alreadyReached;//The nodes with the updated cost

    /**
     * The constructor
     */
    private Dijkstra()
    {
        this.alreadyReached = new HashMap<>();
        test=0;
    }


    /**
     * This function will return the instance of the class
     * @return - The instance of the class
     */
    public static Dijkstra getInstance()
    {
        if(dijkstra == null)
            dijkstra = new Dijkstra();
        return dijkstra;
    }

    /**
     * This function will calculate the costs from the origin node to the other nodes
     * @param origin - the origin node
     * @return - The costs
     */
    public Map<Integer,Double> calculateCosts(Node origin)
    {
        this.origin = origin;
        this.alreadyReached.clear();
        Map<Integer,Double> costs = new HashMap<>();

        search();

        Set<Integer> ids = this.alreadyReached.keySet();

        for(Integer id :ids)
        {
            costs.put(id,this.alreadyReached.get(id).getDistance());
        }

        return costs;
    }

    /**
     * This function will preform the Dijkstra algorithm
     */
    private void search()
    {
        DijkstraSearchNode originNode = new DijkstraSearchNode(origin);
        originNode.setDistance(0);
        this.alreadyReached.put(originNode.getNode().getId(),originNode);
        PriorityQueue <DijkstraSearchNode> open = new PriorityQueue(new DijkstraComperator());
        open.add(originNode);
        while(open.size()>0)
        {
            DijkstraSearchNode node = open.poll();
            test(node,open);
            Set<Node> neighbors = node.getNode().getNeighbors().keySet();
            double help;
            double dis = node.getDistance();

            for(Node neighbor : neighbors)
            {

                DijkstraSearchNode neigh = transformNode(neighbor);
                double neighDis = node.getNode().getWeight(neighbor);
                help = dis + neighDis;//New distance val


                double neighDistance = neigh.getDistance();//old val

                //Will not update distance
                if(neighDistance != Double.MAX_VALUE && help >= neighDistance)
                {
                    continue;
                }



                neigh.setDistance(help);
                if(!this.alreadyReached.containsKey(neigh.getNode().getId()))
                {
                    open.remove(neigh);
                }
                else
                {
                    this.alreadyReached.put(neigh.getNode().getId(),neigh);
                }
                open.add(neigh);
            }

        }

    }

    /**
     * This function will transform a Node instance into the right DijkstaSearchNode instance
     * @param node - the given node
     * @return - The right DijkstraSearchNode instance
     */
    private DijkstraSearchNode transformNode(Node node)
    {
        if(this.alreadyReached.containsKey(node.getId())) {
            return this.alreadyReached.get(node.getId());
        }
        return new DijkstraSearchNode(node);
    }

    /**
     * This class will compare two DijkstraSearchNodes using their distance from the origin n
     */
    class DijkstraComperator implements Comparator<DijkstraSearchNode>
    {

        @Override
        public int compare(DijkstraSearchNode o1, DijkstraSearchNode o2) {
            double dis1 = o1.getDistance();
            double dis2 = o2.getDistance();
            if(dis1 == dis2)
                return 0;
            if(dis1<dis2)
                return -1;
            return 1;
        }
    }

    private void test(DijkstraSearchNode neigh,PriorityQueue <DijkstraSearchNode> open )
    {
        boolean flag = true;
        for(DijkstraSearchNode node : open)
        {
            if(neigh.getDistance()>node.getDistance()) {
                System.out.println("sssssssssssssssss");
                flag = false;
            }

        }
        if(!flag)
        {
            test++;
        }
        if(!flag && test>1)
        {
            System.out.println();

        }
    }


}
