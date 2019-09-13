package Model.Algorithms.Dijkstra;

import Model.Node;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This class will generate for each node in the graph it's shortest path
 * Using Singleton DP
 */
public class ShortestPathGenerator {
    private Map<Integer,Map<Integer,Double>> shortestPaths;//Key - origin node id, value - map -{ key - target node id }
    private  static  ShortestPathGenerator shortestPathGenerator;//The instance
    private Node[][] graph;//The graph
    public String filename;
    private String prev;
    private String res;
    /**
     * The constructor
     */
    private ShortestPathGenerator()
    {
        this.shortestPaths = null;
        this.graph = null;
        this.prev = "";
        this.res = "";
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
     * This function will set the graph (compute the shortest paths from the given graph)
     * @param graph - The given graph
     */
    public void setGraph(Node[][] graph)
    {
        if(graph==null || this.graph == graph)
            return;
        boolean flag = true;
        boolean flag1,flag2;
        if(this.graph!=null) {
            for (int i = 0; i < this.graph.length && flag; i++) {
                for (int j = 0; j < this.graph[i].length && flag; j++) {
                    flag1 = this.graph[i][j]==null;
                    flag2 = graph[i][j]==null;
                    if(flag1|| flag2) {


                        if (((flag1 && !flag2) || (!flag1 && flag2))) {
                            flag = false;

                        }

                    }
                    else {
                        if (this.graph[i][j].getId() != graph[i][j].getId())
                            flag = false;
                    }
                }
            }
            if(flag)
                return;
        }

        this.graph = graph;
        if(!prev.equals(filename)) {

            init(graph);
        }
        prev = filename;
    }

    /**
     * This function will calculate the shortest paths from each node to the other
     * @param graph - The given graph
     */
    private void init(Node[][]graph)
    {
        System.out.println("Start");
        for(int i=0;i<this.graph.length;i++)
        {
            System.out.println((i+1)+"/"+this.graph.length);
            for(int j=0;j<this.graph[i].length;j++)
            {

                //System.out.println("sakd;lkas");
                if(this.graph[i][j]!=null)
                    calculateCost(graph[i][j]);

            }
        }
           BufferedWriter writer;
        try {
            String rel = new File("help.txt").getAbsolutePath();
            rel = rel.substring(0,rel.indexOf("help.txt"));
            String path = rel+"res\\Heuristics\\perfect_heuristics_"+filename+".txt";
            writer = new BufferedWriter(new FileWriter(path));
            writer.write(res);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Finish");
    }

    /**
     * This function will return the cost of the shortest path from the Origin node to the Target node
     * @param origin - The origin node
     * @param target - The target node
     * @return - The cost of the shortest path from the origin node to the target node
     */
    public double getShortestPath(Node origin, Node target)
    {

        if(this.shortestPaths == null)
        {
            getFromFile();
        }
        Map<Integer,Double> given = this.shortestPaths.get(origin.getId());
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
        String rel = new File("help.txt").getAbsolutePath();
        rel = rel.substring(0,rel.indexOf("help.txt"));
        String path = rel+"res\\Heuristics\\perfect_heuristics_"+filename+".txt";
        File f = new File(path);
        if(f.exists())
            return;
        Map<Integer,Double> map = Dijkstra.getInstance().calculateCosts(origin);
        String line = origin.getId()+",";
        for(Integer targetId : map.keySet())
            line+="["+targetId+"-"+map.get(targetId)+"]"+",";
        line = line.substring(0,line.length()-1)+"\n";

        res+=line;

    }

    public void getFromFile()
    {
        String rel = new File("help.txt").getAbsolutePath();
        rel = rel.substring(0,rel.indexOf("help.txt"));
        String path = rel+"res\\Heuristics\\perfect_heursitics_"+filename+".txt";
        shortestPaths = new HashMap<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(path));
            String line;
            //Map<Integer,Map<Integer,Double>>
            Map<Integer,Double> map;
            String [] data;
            int originId,targetId;
            double cost;
            while((line = br.readLine())!=null)
            {
                    map = new HashMap<>();
                    data = line.split(",");
                    originId = Integer.parseInt(data[0]);
                    for(int i=1;i<data.length;i++)
                    {
                        targetId = Integer.parseInt(data[i].substring(1,data[i].indexOf("-")));
                        cost = Double.parseDouble(data[i].substring(data[i].indexOf("-")+1,data.length-1));
                        map.put(targetId,cost);
                    }
                    shortestPaths.put(originId,map);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
