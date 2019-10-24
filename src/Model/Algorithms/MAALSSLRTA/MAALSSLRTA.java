package Model.Algorithms.MAALSSLRTA;

import Model.Algorithms.ALSSLRTA.ALSSLRTA;
import Model.Algorithms.BudgetOrientedMALRTA.BudgetOrientedMALRTA;
import Model.Algorithms.Rules.IRules;
import Model.Algorithms.Rules.RuleBook;
import Model.Components.Agent;
import Model.Algorithms.ALSSLRTA.AlssLrtaSearchNode;
import Model.Components.Node;
import Model.Components.Problem;
import javafx.util.Pair;
import java.util.*;

/**
 * This class represents the Multi Agent aLSS-LRTA*
 */
public class MAALSSLRTA extends ALSSLRTA {
    protected Map<Integer,Map<Integer,Agent>> ocuupied_times;//Key - Node's id, value - dic
    //Key -int time ,Value - Agent's id
    private IRules rules;//The rules for Real Time MAPF
    private int visionRadius;//The vision radius of all agents
    protected Map<Integer,Pair<Integer,Agent>> goals;//Key - node id , value - pair - { key - time, value - agent)
    private Map<Integer,Map<Integer,AlssLrtaSearchNode>> open_time;//Key - node id, value - key-time, value - node
    Map<Integer,List<Node>> prefixes;
    PriorityQueue<Agent> pAgents;


    /**
     * The constructor
     *
     * @param problem - The given problem
     */
    public MAALSSLRTA(Problem problem) {
        super(problem);

        Set<Agent> agents = problem.getAgentsAndStartGoalNodes().keySet();
        this.goals = new HashMap<>();
       // for(Agent agent: agents)
        //{
            //if(agent.isDone())
               // this.goals.put(agent.getGoal().getId(),new Pair<>(0,agent)); // TODO: 22/09/2019 try to remove line
        //}
        rules = new RuleBook(this);
        ocuupied_times = new HashMap<>();
        this.visionRadius = problem.getVisionRadius();

    }

    protected List<Node> getPrefix(Agent agent)
    {
        return this.prefixes.get(agent.getId());
    }
    protected void removePrefix(Agent agent)
    {
        this.prefixes.remove(agent.getId());
    }

    protected void addAgentToQueue(Agent agent)
    {
        pAgents.add(agent);
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
     * @param budgetMap - The budget for each agent
     * @return - The prefixes of the agents after an iteration
     */
    public Map<Integer,List<Node>> getPrefixes(Map<Agent,Integer>budgetMap)
    {
        Map<Agent,Pair<Node,Node>> agent_goal_start = problem.getAgentsAndStartGoalNodes();
        Set<Agent> agents = agent_goal_start.keySet();
        pAgents = new PriorityQueue<>(new CompareAgentsPriority());
        //PriorityQueue<Agent> pAgents = new PriorityQueue<>(new CompareAgentsHeurstics());

        prefixes = new HashMap<>();
        pAgents.addAll(agents);
        int numOfNodesToDevelop;
        while(pAgents.size()>0)
        {
            Agent agent = pAgents.poll();

         /*   if(agent.isDone())
            {

                List<Node> done = new ArrayList<>();
                done.add(agent.getGoal());
                prefixes.put(agent.getId(),done);
               // inhabitAgent(agent.getGoal().getId(),0);// TODO: 22/09/2019 remove

            }*/
            //else {
                numOfNodesToDevelop = budgetMap.get(agent);
                Node current = agent.getCurrent();
                List<Node> prefix;


                if(this instanceof BudgetOrientedMALRTA)
                    prefix = calculatePrefix(current, agent.getGoal(), numOfNodesToDevelop, agent);
                else
                    prefix = super.calculatePrefix(current, agent.getGoal(), numOfNodesToDevelop, agent);

                if (prefix == null) {
                    System.out.println("No Solution agent " + agent.getId()+" can't move from "+agent.getCurrent() + " id = "+agent.getCurrent().getId());
                    prefixes.put(agent.getId(), prefix);
                    return prefixes;

                }


                    //Updating node
                    for (int i = 0; i < prefix.size()  - 1; i++) {
                        updateNode(prefix.get(i).getId(), i);
                    }
                    inhabitAgent(prefix.get(prefix.size() - 1).getId(),prefix.size() - 1 );
                    prefixes.put(agent.getId(), prefix);

            //}

        }

      //  if(this instanceof BudgetOrientedMALRTA)
       //     System.out.println("The amount of leftover after this round = "+((BudgetOrientedMALRTA)this).getLeftover());

        return prefixes;
    }


    @Override
    protected void updateNode(int nodeId,int time) {



        Map<Integer, Agent> occupations = this.ocuupied_times.get(nodeId);

        if(occupations==null)
        {
            occupations = new HashMap<>();
            this.ocuupied_times.put(nodeId,occupations);

        }

        occupations.put(time,getAgent());
    }




    /**
     * This function will check if it is possible to save a spot at the given time
     * @param time - The given time
     * @param nodeId - The node's id
     * @return - True IFF it is possible to save a spot at the given time
     */
    public boolean canReserve(int time,int nodeId)
    {

        Pair<Integer,Agent> pair = this.goals.get(nodeId);
        //It is a goal
        if(pair!=null)
        {
            Integer time2 = pair.getKey();
            if(time2 != null) {
                if (time2 <= time) {
                    return false;
                }
            }
        }


        Map<Integer,Agent> times = this.ocuupied_times.get(nodeId);
        //first time
        if(times ==null)
        {
            //First time
            Map<Integer,Agent> map = new HashMap<>();
            this.ocuupied_times.put(nodeId,map);
            return true;
        }

        boolean b = !times.containsKey(time);;
        return b;



    }

    /**
     * Thiis function will check for swapping of two agents at a given time
     * @param time - The given time
     * @param originNodeId - The original node ID
     * @param targetNodeId - The target node ID
     * @return
     */
    public boolean checkSwapping(int time,int originNodeId,int targetNodeId)
    {

        //If any book the origin node at time
        Map<Integer,Agent> times =this.ocuupied_times.get(originNodeId);
        if(times == null) {
            return true;
        }
        Agent agentObj = times.get(time);
        if(agentObj == null)
            return true;

        Integer agent =agentObj.getId();
        //If any book the target node at time -1
        times = this.ocuupied_times.get(targetNodeId);
        if(times == null)
            return true;
        agentObj = times.get(time-1);
        if(agentObj ==null)
            return true;
        Integer prevId = agentObj.getId();
        return agent.intValue() != prevId.intValue();

    }
    /**
     * This function "inhabits" an agent in the node
     * It means that the agent has reached it's goal and now he is not moving from that spot
     * @param nodeId - The node's id
     */
    @Override
    protected void inhabitAgent(int nodeId,int time)
    {
        this.goals.put(nodeId,new Pair<>(time,getAgent()));
        updateNode(nodeId,time);
    }

    @Override
    protected boolean canInhabit(AlssLrtaSearchNode node) {

        if(!(node instanceof MaAlssLrtaSearchNode))
        {

            return super.canInhabit(node);
        }



        if(this.goals.containsKey(node.getNode().getId())) {
            return false;
        }
        MaAlssLrtaSearchNode ma = (MaAlssLrtaSearchNode)node;
        Map<Integer,Agent> time_agents = this.ocuupied_times.get(ma.getNode().getId());
        if(time_agents == null)
        {
          //  return !this.goals.containsKey(node.getNode().getId());
            return true;
        }
        Set<Map.Entry<Integer,Agent>> times = time_agents.entrySet();
        int time = ma.getTime();

        Agent agent = getAgent();
        for(Map.Entry<Integer,Agent> timeSet : times)
        {
            if(time<=timeSet.getKey() && agent.getId()!=timeSet.getValue().getId())
                return false;
        }
        return this.canBeAtTime(time,this.getAgent().getCurrent(),node.getNode());


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
            if(this.goals.containsKey(nodeId))
                return -2;
            Map<Integer,Agent> map = new HashMap<>();
            this.ocuupied_times.put(nodeId,map);
            return -1;
        }
        Integer id = this.ocuupied_times.get(nodeId).get(time).getId();
        if(id== null)
            return -1;
        return id;

    }

    /**
     * This class will compare between agents by comparing their current node's heuristics
     */
    public static class CompareAgentsHeurstics implements Comparator<Agent>
    {
        public CompareAgentsHeurstics()
        {

        }

        @Override
        public int compare(Agent o1, Agent o2) {
            double h1 = o1.getHeuristicValue(o1.getCurrent());
            if(h1==0)
                return 1;
            double h2 = o2.getHeuristicValue(o2.getCurrent());
            if(h2 == 0)
                return -1;
            if(h1==h2)
                return 0;
            if(h1<h2)
                return -1;
            return 1;
        }
    }
    /**
     * This class will compare between agents by comparing their priority
     */
    public static class CompareAgentsPriority implements Comparator<Agent>
    {
        public CompareAgentsPriority()
        {

        }

        @Override
        public int compare(Agent o1, Agent o2) {
            double p1 = o1.getPriority();
            double p2 = o2.getPriority();
            if(p1>p2)
                return -1;
            if(p2>p1)
                return 1;
            return 0;
        }
    }
    @Override
    protected boolean canBeAtTime(int time, Node origin, Node target) {

        return rules.isValidMove(origin.getId(),target.getId(),time) ;
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
        if(node.getNumInChain()>problem.getPrefixLength())
            return false;
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
