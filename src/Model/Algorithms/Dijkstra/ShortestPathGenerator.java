package Model.Algorithms.Dijkstra;

import Model.GridNode;
import Model.Node;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    private Set<String[]> res;
    /**
     * The constructor
     */
    private ShortestPathGenerator()
    {
        this.shortestPaths = null;
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

    public void setFilename(String filename) {
        this.filename = filename;
        shortestPaths = null;
      //  this.getShortestPath(null,null);
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
            for (int i = 0; i < this.graph.length && i<graph.length && flag; i++) {
                for (int j = 0; j < this.graph[i].length && j<graph[i].length&&flag; j++) {
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
        String rel = new File("help.txt").getAbsolutePath();
        rel = rel.substring(0,rel.indexOf("help.txt"));
        String path = rel+"res\\Heuristics\\perfect_heuristics_"+filename+".txt";
        File f = new File(path);
        if(f.exists())
            return;
        this.shortestPaths = null;
        long start = System.currentTimeMillis();
        for(int i=0;i<this.graph.length;i++)
        {
            System.out.println((i+1)+"/"+this.graph.length);
            for(int j=0;j<this.graph[i].length;j++)
            {

                //System.out.println("sakd;lkas");
                if(this.graph[i][j]!=null) {

                    calculateCost(graph[i][j]);
                    BufferedWriter writer;
                    try {

                        writer = new BufferedWriter(new FileWriter(path));
                        for(String [] str:res)
                        {
                            for(int k=0;k<str.length;k++)
                            {
                                writer.write(str[k]);
                            }
                        }
                       //System.out.println("DONE");
                        res.clear();
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //  System.out.println(System.currentTimeMillis()-s);
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

        if(this.shortestPaths == null)
        {
            System.out.println("godam");
            getFromFile();
        }
        if(origin == null || target == null)
            return Double.MAX_VALUE;
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

        Map<Integer,Double> map = Dijkstra.getInstance().calculateCosts(origin);
        String line = origin.getId()+",";
        int count = 0;
        int size = map.size();



        String [] lines = new String[size];
        for(Map.Entry<Integer,Double> entry : map.entrySet()) {
            lines[count] = entry.getKey() + "-" + entry.getValue() + ",";
            count++;

        }
        lines[count-1] = lines[count-1].substring(0,line.length()-1)+"\n";





        res.add(lines);

    }

    public void getFromFile()
    {
        String rel = new File("help.txt").getAbsolutePath();
        rel = rel.substring(0,rel.indexOf("help.txt"));
        String path = rel+"res\\Heuristics\\perfect_heuristics_"+filename+".txt";
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
                        targetId = Integer.parseInt(data[i].substring(0,data[i].indexOf("-")));
                        cost = Double.parseDouble(data[i].substring(data[i].indexOf("-")+1,data.length));
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
