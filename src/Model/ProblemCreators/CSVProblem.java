package Model.ProblemCreators;

import Model.Components.*;
import Model.Model;
import Model.ProblemCreators.AbstractProblemCreator;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a Problem creator using a csv file that represents the problem
 * This problem creator assumes there is only one agent
 */
public class CSVProblem extends AbstractProblemCreator {

    private int x_start;//The X coordinates of the start node
    private int y_start;//The Y coordinates of the start node
    private int x_end;//The X coordinates of the end node
    private int y_end;//The Y coordinates of the end node

    /**
     * The constructor of the class
     */
    public CSVProblem()
    {
        super();
    }


    @Override
    public Problem getProblem(String path, int toDevelop, int type, int visionRadius, int prefixLength) {
        Map<Agent, Pair<Node, Node>> agent_start_goal_nodes = new HashMap<>();
        graph = getGraphFromCSV(path);
        Agent agent = new Agent(0,graph[x_end][y_end],type);
        agent_start_goal_nodes.put(agent,new Pair<>(graph[x_start][y_start],graph[x_end][y_end]));
        Problem problem = new Problem(graph,agent_start_goal_nodes,toDevelop,new GridCostFunction(),visionRadius,type,prefixLength);
        problemInString = Problem.print(graph,agent_start_goal_nodes);
        System.out.println(problemInString);
        return problem;
    }

    /**
     * This function will retrieve the graph from the csv file in the given path
     * @param path - The given path
     * @return - The grid
     */

    public Node[][] getGraphFromCSV(String path)
    {
        BufferedReader br = null;
        String line;
        List<List<Node>> grid = new ArrayList<>();
        int index = 0;
        try {

            br = new BufferedReader(new FileReader(path));
            line = br.readLine();
            String [] lineSplit = line.split(",");
            String lastNumber = lineSplit[0];
            //int num_of_colums = Integer.parseInt(lastNumber);
            int num_of_colums = Integer.parseInt(lastNumber)+1;
            System.out.println(num_of_colums);
            line = br.readLine();
            lineSplit = line.split(",");

            while (line != null && lineSplit.length>1) {
                List<Node> nodesInRow = new ArrayList<>();
                // use comma as separator
                for(int i=1;i<lineSplit.length-1;i++)
                {
                    if(lineSplit[i].equals("0"))
                        nodesInRow.add(0,null);
                    else
                        {
                            nodesInRow.add(0,new GridNode(index,num_of_colums-i));
                            if(!lineSplit[i].equals("1"))
                            {
                                if(lineSplit[i].equals("S"))
                                {
                                    x_start = index;
                                    y_start = num_of_colums - i;
                                }
                                else
                                {
                                    x_end = index;
                                    y_end = num_of_colums - i;
                                }
                            }
                        }


                }

                grid.add(nodesInRow);
                index++;
                line = br.readLine();
                if(line !=null) {
                    lineSplit = line.split(",");
                }

            }
            int num_of_rows = grid.size();
            Node[][] nodeGrid = new Node[num_of_rows][num_of_colums];
            for(int i=0;i<num_of_rows;i++)
            {
                for(int j=0;j<num_of_colums;j++)
                {
                    nodeGrid[i][j] = grid.get(i).get(j);
                }
            }

            return nodeGrid;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return null;
    }

}
