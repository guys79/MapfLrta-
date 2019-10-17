package Model.Algorithms.BudgetPolicy;

import Model.Algorithms.MAALSSLRTA.MAALSSLRTA;
import Model.Components.Agent;
import Model.Components.Node;
import Model.Components.Problem;
import javafx.util.Pair;

import java.util.*;

/**
 * This class represents the policy of the prioritized agents
 */
public class PrioritizedPolicy implements IBudgetPolicy  {

    private final int AXIS = 2;//Te number of Axis an agent is capable of moving simultaneously
    private final int DIMENSIONS = 2;//The number of dimensions in the given problem

    @Override
    public Map<Agent, Integer> getBudgetMap(Problem problem) {

        //Iinit + adding the agents into the PriorityQueue
        PriorityQueue<Agent> pAgents = new PriorityQueue<>(new MAALSSLRTA.CompareAgentsPriority());
        Map<Agent,Pair<Node,Node>> agentsAndStartGoalNodes = problem.getAgentsAndStartGoalNodes();
        pAgents.addAll(agentsAndStartGoalNodes.keySet());
        Map<Agent, Integer> budgets = new HashMap<>();
        int prefixLength = problem.getPrefixLength();

        //Give the first agent enough budget for a prefix (will always choose the lowest h,
        Agent agent = pAgents.poll();
        budgets.put(agent,prefixLength);

        //calculate the tree size and total budget
        int treeSize = (int)treeSize(prefixLength,this.AXIS,this.DIMENSIONS);
        int totalBudget = agentsAndStartGoalNodes.size()*problem.getNumberOfNodeToDevelop() - prefixLength;
        
        //If there is not "enough" budget
        if(totalBudget< treeSize*problem.getAgentsAndStartGoalNodes().size()) {
            while (pAgents.size() > 0 && totalBudget >= treeSize) {
                agent = pAgents.poll();
                budgets.put(agent, treeSize);
                totalBudget -= treeSize;


            }
            agent = pAgents.poll();
            budgets.put(agent, totalBudget);
            while (pAgents.size() > 0) {
                agent = pAgents.poll();
                budgets.put(agent, 0);
            }
        }
        else//If there is enough budget
        {
            agent = pAgents.poll();
            budgets.put(agent, totalBudget/problem.getAgentsAndStartGoalNodes().size()+totalBudget%problem.getAgentsAndStartGoalNodes().size());
            totalBudget -= totalBudget/problem.getAgentsAndStartGoalNodes().size()+totalBudget%problem.getAgentsAndStartGoalNodes().size();
            while (pAgents.size() > 0) {
                agent = pAgents.poll();
                budgets.put(agent, totalBudget/problem.getAgentsAndStartGoalNodes().size());
                totalBudget -= treeSize;

            }

        }
        return budgets;
    }

    /**
     * This class will compare between agents by comparing their priority
     */
    private class CompareAgentsPriority implements Comparator<Agent>
    {
        public CompareAgentsPriority()
        {

        }

        @Override
        public int compare(Agent o1, Agent o2) {
            double p1 = o1.getPriority();
            double p2 = o2.getPriority();
            if(p1>p2)
                return 1;
            if(p2>p1)
                return -1;
            return 0;
        }
    }

    /**
     * This function will add the route (after being parsed) into the routes set
      * @param route - The given raw (not parsed) route
     * @param routes - The given set of routes
     */
    private void addRoute(String route,Set<Map<Integer,Integer>> routes)
    {
        String [] split = route.split(",");
        Map<Integer,Integer> parsed = new HashMap<>();
        Map<Integer,Integer> parsedMinus = new HashMap<>();


        int num;
        for(int i=0;i<split.length;i++)
        {
            num = Integer.parseInt(split[i]);
            Integer numOfTimes = parsed.get(num);
            if(numOfTimes == null)
            {
                numOfTimes =0;
            }
            numOfTimes++;
            parsed.put(num,numOfTimes);
            parsedMinus.put(num,numOfTimes);
        }

        routes.add(parsed);


    }

    /**
     * This function will find the number of ways to divide x resources to z consumers when each consumer can get a maximum of y resources
     * @param x - The number of resources
     * @param y - The maximun number
     * @param z - The number of consumers
     * @param route - The route
     * @param routes - The set of routes
     * @return - The number of ways to divide x resources to z consumers when each consumer can get a maximum of y resources
     */
    private int divideResources(int x, int y, int z, String route, Set<Map<Integer,Integer>> routes)
    {
        if(x==0 && z == 0) {
            addRoute(route.substring(0,route.length()-1),routes);
            return 1;
        }
        if(z==0 || x<=0 || z>x)
            return 0;

        int sum = 0;
        for(int i=1;i<=y;i++)
        {
            sum+= 2*divideResources(x-i,i,z-1,route+i+",",routes);

        }

        return sum;
    }

    /**
     * This function will calculate the size of the tree after *iterations* iterations where the agent can move on "axis" axis at the same time on a "dimensions" dimensional space
     * @param iterations - The number of iterations
     * @param axis - The number of axis the agent can move simultaneously
     * @param dimensions - The number of dimensions
     * @return - The size of the tree  created from the agent's movements
     */
    private long treeSize(int iterations,int axis, int dimensions)
    {
        int min = Math.min(dimensions,iterations*axis);
        long sum =1l;
        long sumRound =1l;
        double bin;
        for(int j=1;j<=min;j++)
        {
            bin = binomialCoeff(dimensions,j);
            sumRound = 0l;
            for(int i=1;i<=iterations*axis;i++)
            {
                Set<Map<Integer,Integer>> routes = new HashSet<>();
                int res = +divideResources(i,iterations,j,"",routes);



                for(Map<Integer,Integer> route: routes) {
                    sumRound +=  getCombinaions(j, route.values());
                }

            }
            sumRound = (long)(bin*Math.pow(2,j) *sumRound);
            sum+=sumRound;

        }

        return sum;
    }

    /**
     * This function will calculate the factorial value of the given number n
     * @param n - The given number
     * @return - n!
     */
    private long factorialUsingRecursion(int n) {
        if (n <= 2) {
            return n;
        }
        return n * factorialUsingRecursion(n - 1);
    }

    /**
     * This function will return the number of combinations to arrange the given quantities on a "num_of_choises".
     * For example, given num_of_choices = 5 and parsed = {1,2,2} then the function will return 5!/(1!*2!*2!)
     * @param num_of_choices - The number of choises
     * @param parsed - The duplicates info
     * @return - The number of combinations
     */
    private long getCombinaions(int num_of_choices,Collection<Integer> parsed)
    {

        double factorial = factorialUsingRecursion(num_of_choices);
        for(int innner: parsed)
        {
            factorial = factorial / factorialUsingRecursion(innner);
        }

        return (long)factorial;
    }




    // Returns value of Binomial
    // Coefficient C(n, k)

    /**
     * This function will return the value of Binomial Coefficient C(n,k)
     * @param n - The first number
     * @param k - The second number
     * @return - C(n.k)
     */
    static int binomialCoeff(int n, int k)
    {

        // Base Cases
        if (k == 0 || k == n)
            return 1;

        // Recur
        return binomialCoeff(n - 1, k - 1) +
                binomialCoeff(n - 1, k);
    }

}
