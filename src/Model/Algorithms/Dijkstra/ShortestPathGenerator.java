package Model.Algorithms.Dijkstra;


import Model.Node;
import javafx.util.Pair;

import java.io.*;
import java.util.*;

/**
 * This class will generate for each node in the graph it's shortest path
 * Using Singleton DP
 */
public class ShortestPathGenerator {
    private Map<Integer,Map<Integer,Double>> shortestPaths;//Key - origin node id, value - map -{ key - target node id }
    private  static  ShortestPathGenerator shortestPathGenerator;//The instance
    private Node[][] graph;//The graph
    public String filename;//the name of the map
    private String prev;//The previous map name
    private Set<String[]> res;//The Dijkstra results


    /**
     * The constructor
     */
    private ShortestPathGenerator()
    {
        this.shortestPaths = new HashMap<>();
        this.graph = null;
        this.prev = "";
        this.res = new HashSet<>();
    }

    /**
     * This function will return the instance of the singleton
     * @return - The instance of this class
     */
    public static ShortestPathGenerator getInstance()
    {
        if(shortestPathGenerator == null)
            shortestPathGenerator = new ShortestPathGenerator();
        return shortestPathGenerator;
    }

    /**
     * This function will set the name of the file
     * @param filename - The given filename
     */
    public void setFilename(String filename) {
        this.filename = filename;
        this.shortestPaths = new HashMap<>();
        this.getShortestPath(null, null);


    }

    /**
     * This function will set the graph (compute the shortest paths from the given graph)
     * @param agentsAndStartGoalNodes - A map from an agent to a pair of start/goal nodes
     * @param graph - The given graph
     */
    public void setGraph(Node[][] graph,Collection<Pair<Node,Node>> agentsAndStartGoalNodes)
    {
        if(graph==null || this.graph == graph) {
            if(this.graph == graph)
                retrieveGoal(agentsAndStartGoalNodes);
            return;
        }
        if(!prev.equals(filename)) {
            this.graph = graph;
            init(graph);
        }
        retrieveGoal(agentsAndStartGoalNodes);
        prev = filename;
    }

    /**
     * This function will retrieve a heuristic information on a single node
     * @param id - The given id
     * @param backwards - True IFF the node's info that we want to retrieve is as the target
     */
    private void retrieveSingle(int id,boolean backwards)
    {
        if(backwards)
        {
            retrieveSingleBackwards(id);
        }
        else
        {
            retrieveSingleRegular(id);
        }

    }

    /**
     * This function will retrieve a heuristic information on a single node with the node as the origin (classic Dijkstra)
     * @param id - The given id
     */
    private void retrieveSingleRegular(int id)
    {
        String rel = "C:\\Users\\guys79\\Desktop\\תזה\\Heuristics2";
        String path = rel+"\\perfectHeuristics"+filename;
        String filePath;


        filePath = path+"\\"+id+".txt";
        BufferedReader br;
        String line;
        try {
            br = new BufferedReader(new FileReader(filePath));

            Map<Integer,Double> map;
            String [] data;
            int originId,targetId;
            double cost;
            String extra="";
            while((line = br.readLine())!=null)
            {

                line = extra+line;
                extra="";
                map = new HashMap<>();
                data = line.split(",");
                originId = Integer.parseInt(data[0]);
                for(int i=1;i<data.length;i++)
                {
                    if(data[i].indexOf("-") == -1)
                    {
                        extra = data[i];
                        continue;
                    }
                    else {
                        targetId = Integer.parseInt(data[i].substring(0, data[i].indexOf("-")));
                        cost = Double.parseDouble(data[i].substring(data[i].indexOf("-") + 1, data[i].length()));
                        map.put(targetId, cost);
                    }
                }
                shortestPaths.put(originId,map);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (StringIndexOutOfBoundsException e)
        {
            //System.out.println(line);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This function will retrieve a heuristic information on a single node with the node as the target (opposite classic Dijkstra)
     * @param id - The given id
     */
    private void retrieveSingleBackwards(int id)
    {
        String rel = "C:\\Users\\guys79\\Desktop\\תזה\\Heuristics2";
        String path = rel+"\\perfectHeuristics"+filename;
        String filePath;


        filePath = path+"\\"+id+".txt";
        BufferedReader br;
        String line;
        try {
            br = new BufferedReader(new FileReader(filePath));

            Map<Integer,Double> map;
            String [] data;
            int originId,targetId;
            double cost;
            String extra="";


            while((line = br.readLine())!=null)
            {

                line = extra+line;
                extra="";
                data = line.split(",");
                targetId = Integer.parseInt(data[0]);
                for(int i=1;i<data.length;i++)
                {

                    if(data[i].indexOf("-") == -1)
                    {
                        extra = data[i];
                        continue;
                    }
                    else {
                        //Get the origin id
                        originId = Integer.parseInt(data[i].substring(0, data[i].indexOf("-")));
                        cost = Double.parseDouble(data[i].substring(data[i].indexOf("-") + 1, data[i].length()));
                        map = this.shortestPaths.get(originId);

                        if(map == null) {
                            map = new HashMap<>();
                            this.shortestPaths.put(originId, map);
                        }

                        map.put(targetId, cost);
                    }
                }


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (StringIndexOutOfBoundsException e)
        {
            //System.out.println(line);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * This function will retrieve the info on the goal states
     * @param agentsAndStartGoalNodes - The Pairs of start/end goals
     */
    private void retrieveGoal(Collection<Pair<Node,Node>> agentsAndStartGoalNodes)
    {
        this.shortestPaths = new HashMap<>();
        int size = agentsAndStartGoalNodes.size();
        int i=1;
        long start =System.currentTimeMillis();
        for(Pair<Node,Node>pair :agentsAndStartGoalNodes) {

            retrieveSingle(pair.getValue().getId(),true);
            System.out.println("finished "+i+"/"+size);
            i++;
        }
        long time =System.currentTimeMillis() - start;
        System.out.println("It took "+time+" ms to retrieve data");

    }
    /**
     * This function will calculate the shortest paths from each node to the other
     * @param graph - The given graph
     */
    private void init(Node[][]graph)
    {

        String rel = "C:\\Users\\guys79\\Desktop\\תזה\\Heuristics2";
        String path = rel+"\\perfectHeuristics"+filename;
        File f = new File(path);
        if(f.exists())
            return;

        f.mkdirs();

        long start = System.currentTimeMillis();
        for(int i=0;i<this.graph.length;i++)
        {
            System.out.println((i+1)+"/"+this.graph.length);
            for(int j=0;j<this.graph[i].length;j++)
                if (this.graph[i][j] != null) {

                    calculateCost(graph[i][j]);

                    String filePath = path+"\\"+this.graph[i][j].getId()+".txt";
                    try {
                        new File(filePath).createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try (FileWriter fw = new FileWriter(filePath, true);
                         BufferedWriter bw = new BufferedWriter(fw);
                         PrintWriter out = new PrintWriter(bw)) {
                        for (String[] str : res) {
                            for (int k = 0; k < str.length; k++) {
                                out.print(str[k]);
                            }
                        }

                        res.clear();
                    } catch (IOException e) {
                        //exception handling left as an exercise for the reader
                    }

                }
        }

        long end =System.currentTimeMillis();
        System.out.println("TIme "+(end-start));
    }

    /**
     * This function will return the cost of the shortest path from the Origin node to the Target node
     * @param origin - The origin node
     * @param target - The target node
     * @return - The cost of the shortest path from the origin node to the target node
     */
    public double getShortestPath(Node origin, Node target)
    {
        if(origin == null || target == null)
            return Double.MAX_VALUE;
        Map<Integer,Double> given = this.shortestPaths.get(origin.getId());
        if(given == null) {

            retrieveSingle(origin.getId(),false);
        }

        if(given == null)
            return Double.MAX_VALUE;
        Double cost = given.get(target.getId());

        if(cost == null)
            return Double.MAX_VALUE;
        return cost;

    }

    /**
     * This function will calculate the cost of the shortest path from the Origin node to the other nodes
     * @param origin - The origin node
     */
    private void calculateCost(Node origin)

    {
       //this.shortestPaths.put(origin.getId(),Dijkstra.getInstance().calculateCosts(origin));

        Map<Integer,Double> map = Dijkstra.getInstance().calculateCosts(origin);
        String line = origin.getId()+",";
        int count = 1;
        int size = map.size();



        String [] lines = new String[size+1];
        lines[0] = line;
        for(Map.Entry<Integer,Double> entry : map.entrySet()) {
            lines[count] = entry.getKey() + "-" + entry.getValue() + ",";
            count++;

        }
        lines[count-1] = lines[count-1].substring(0,lines[count-1].length()-1)+"\n";

        res.add(lines);

    }


}
