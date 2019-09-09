package Model.Algorithms.MAALSSLRTA;

import Model.Algorithms.ALSSLRTA.ALSSLRTA;

import Model.Agent;
import Model.Algorithms.ALSSLRTA.AlssLrtaSearchNode;
import Model.Node;
import Model.Problem;
import javafx.util.Pair;

import java.util.*;

/**
 * This class represents the Multi Agent aLSS-LRTA*
 */
public class MAALSSLRTA extends ALSSLRTA {
    private Map<Integer,Map<Integer,Integer>> ocuupied_times;//Key - Node's id, value - dic
    //Key -int time ,Value - Agent's id
    private IRules rules;//The rules for Real Time MAPF
    private int visionRadius;//The vision radius of all agents
    private Set<Integer> goals;//The agent's fulfilled goals
    private Map<Integer,Map<Integer,AlssLrtaSearchNode>> open_time;//Key - node id, value - key-time, value - node

    /**
     * The constructor
     *
     * @param problem - The given problem
     */
    public MAALSSLRTA(Problem problem) {
        super(problem);

        Set<Agent> agents = problem.getAgentsAndStartGoalNodes().keySet();
        this.goals = new HashSet<>();
        for(Agent agent: agents)
        {
            if(agent.isDone())
                this.goals.add(agent.getGoal().getId());
        }
        rules = new RuleBook(this);
        ocuupied_times = new HashMap<>();
        this.visionRadius = problem.getVisionRadius();

    }


    /**
     * This function will return the vision radius of akk the agents
     * @return - The vision radius of akk the agents
     */
    public int getVisionRadius() {
        return visionRadius;
    }

    /**
     * This function will return the prefixes of the agents after an iteration
     * @param numOfNodesToDevelop - The number of nodes allowed to develop
     * @return - The prefixes of the agents after an iteration
     */
    public Map<Integer,List<Node>> getPrefixes(int numOfNodesToDevelop)
    {
        Map<Agent,Pair<Node,Node>> agent_goal_start = problem.getAgentsAndStartGoalNodes();
        Set<Agent> agents = agent_goal_start.keySet();
        PriorityQueue<Agent> pAgents = new PriorityQueue<>(new CompareAgentsHeurstics());

        Map<Integer,List<Node>> prefixes = new HashMap<>();
        pAgents.addAll(agents);
    //    System.out.println("start");
        while(pAgents.size()>0)
        {
            Agent agent = pAgents.poll();
            if(agent.isDone())
            {
                //System.out.println("DONE");
                List<Node> done = new ArrayList<>();
                done.add(agent.getGoal());
                prefixes.put(agent.getId(),done);
                inhabitAgent(agent.getGoal().getId());

            }
            else {
              //  System.out.println("Not done");
                Node current = agent.getCurrent();

                List<Node> prefix = super.calculatePrefix(current, agent.getGoal(), numOfNodesToDevelop, agent);

                if (prefix == null) {
                    System.out.println("No Solution agent " + agent.getId());
                    return null;
                }
                //Updating node
                for(int i=0;i<prefix.size()-1;i++)
                {
                    updateNode(prefix.get(i).getId(),i);
                }
                inhabitAgent(prefix.get(prefix.size()-1).getId());
                prefixes.put(agent.getId(), prefix);
            }

        }



        return prefixes;
    }


    @Override
    protected void updateNode(int nodeId,int time) {



        Map<Integer, Integer> occupations = this.ocuupied_times.get(nodeId);

        if(occupations==null)
        {
            occupations = new HashMap<>();
            this.ocuupied_times.put(nodeId,occupations);

        }

        occupations.put(time,getAgent().getId());
    }




    /**
     * This function will check if it is possible to save a spot at the given time
     * @param time - The given time
     * @param nodeId - The node's id
     * @return - True IFF it is possible to save a spot at the given time
     */
    public boolean canReserve(int time,int nodeId)
    {
        if(this.goals.contains(nodeId))
            return false;
        //first time
        if(this.ocuupied_times.get(nodeId) ==null)
        {
            //First time
            Map<Integer,Integer> map = new HashMap<>();
            this.ocuupied_times.put(nodeId,map);
            return true;
        }

        return !this.ocuupied_times.get(nodeId).containsKey(time);


    }

    /**
     * This function "inhabits" an agent in the node
     * It means that the agent has reached it's goal and now he is not moving from that spot
     * @param nodeId - The node's id
     */
    @Override
    protected void inhabitAgent(int nodeId)
    {

        this.ocuupied_times.remove(nodeId);

        this.goals.add(nodeId);
    }

    @Override
    protected boolean canInhabit(AlssLrtaSearchNode node) {
        if(!(node instanceof MaAlssLrtaSearchNode))
        {
            return super.canInhabit(node);
        }
        MaAlssLrtaSearchNode ma = (MaAlssLrtaSearchNode)node;
        Map<Integer,Integer> time_agents = this.ocuupied_times.get(ma.getNode().getId());
        if(time_agents == null)
        {
            return !this.goals.contains(node.getNode().getId());
        }
        Set<Integer> times = time_agents.keySet();
        int time = ma.getTime();

        for(Integer timeSet : times)
        {
            if(time<=timeSet)
                return false;
        }
        return true;


    }

    /**
     * This function will return the agent that is occupying he node with the given node's id
     * at the given time
     * @param nodeId - The given node's id
     * @param time - The given time
     * @return - The id of the agent that will occupy the node at the given time
     */
    public int getAgent(int nodeId,int time)
    {
        if(this.ocuupied_times.get(nodeId) ==null)
        {
            if(this.goals.contains(nodeId))
                return -2;
            Map<Integer,Integer> map = new HashMap<>();
            this.ocuupied_times.put(nodeId,map);
            return -1;
        }
        Integer id = this.ocuupied_times.get(nodeId).get(time);
        if(id== null)
            return -1;
        return id;

    }
    public static class CompareAgentsHeurstics implements Comparator<Agent>
    {
        public CompareAgentsHeurstics()
        {

        }

        @Override
        public int compare(Agent o1, Agent o2) {
            double h1 = o1.getHeuristicValue(o1.getCurrent());
            double h2 = o2.getHeuristicValue(o2.getCurrent());
            if(h1==h2)
                return 0;
            if(h1<h2)
                return -1;
            return 1;
        }
    }

    @Override
    protected boolean canBeAtTime(int time, Node origin, Node target) {
        return rules.isValidMove(origin.getId(),target.getId(),time);
    }

    @Override
    protected AlssLrtaSearchNode transformSingleNode(Node node,int time)
    {
        AlssLrtaSearchNode res = super.transformSingleNode(node,time);
       return new MaAlssLrtaSearchNode(res,time);
    }

    @Override
    protected int time(AlssLrtaSearchNode node) {
        if(node instanceof MaAlssLrtaSearchNode)
        {
            return ((MaAlssLrtaSearchNode)node).getTime()+1;
        }
        return super.time(node);
    }

    @Override
    protected Set<Node> getNeighbors(Node node) {
        Set<Node> superNodes =  new HashSet<>(super.getNeighbors(node));
        superNodes.add(node);
        return superNodes;

    }
    /**
     * This function will clear the open list
     */
    protected void clearOpen()
    {
        super.clearOpen();
        this.open_time = new HashMap<>();
    }

    @Override
    protected boolean canAdd(AlssLrtaSearchNode node) {

        MaAlssLrtaSearchNode maNode = (MaAlssLrtaSearchNode)node;
        if(!this.open_time.containsKey(node.getNode().getId()))
        {
            Map<Integer,AlssLrtaSearchNode> set = new HashMap<>();
            set.put(maNode.getTime(),maNode);
            open_time.put(node.getNode().getId(),set);
            return true;
        }

        Map<Integer,AlssLrtaSearchNode> list = this.open_time.get(node.getNode().getId());
        if(!list.containsKey(maNode.getTime()))
        {
            list.put(maNode.getTime(),maNode);
            return true;
        }
        return false;

    }

    /**
     * This function will remove a node from the open list
     * @param node - The given node
     */
    protected void openRemove(AlssLrtaSearchNode node)
    {
        //If not null
      if(node!=null)
      {
          //If the node exists in open
          AlssLrtaSearchNode alsn = this.open_id.get(node.getNode().getId());
          if(alsn == null)
              return;
          //Remove the node from open_time
          MaAlssLrtaSearchNode maNode = (MaAlssLrtaSearchNode)node;
          Map<Integer,AlssLrtaSearchNode> set = this.open_time.get(node.getNode().getId());
          set.remove(maNode.getTime());

          //If this node was the last one in the map
          if(set.size() ==0) {
              this.open_time.remove(node.getNode().getId());
              //Remove from others
              super.openRemove(node);
              return;
          }


          //If the node is the node of the open_id - replace it
          if(((MaAlssLrtaSearchNode)alsn).getTime() == ((MaAlssLrtaSearchNode)node).getTime())
          {
              //Remove from others
              super.openRemove(node);

              for(AlssLrtaSearchNode node2: set.values())
              {
                  this.open_id.put(node2.getNode().getId(),node2);
                  return;
              }

          }
          super.openRemoveNoId(node);

      }

    }

}