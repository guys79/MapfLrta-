package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public abstract class AbstractProblemCreator implements IProblemCreator {
    protected Node [][] graph;//The graph
    protected String problemInString;//The problem representation in string

    public AbstractProblemCreator()
    {

    }
    public static boolean isThereSolution(Map<Agent, List<Node>> paths)
    {
        for(List<Node> path : paths.values())
        {
            if(path.size()==1)
                return false;
        }
        return true;
    }

    public Node[][] getGraph() {
        return graph;
    }

    @Override
    public int[][] getGridGraph() {
        return intoIntGrid(graph);
    }

    /**
     * This function will convert the Node[][] graph into int[][] graph where
     * each cell is equal to -1 if the cell is null (wall), else, the value of
     * the cell will be the id of the node
     * @param graph - The given graph
     * @return - The same graph in the new format int [][]
     */
    private int [][] intoIntGrid(Node [][]graph)
    {
        int [][] intGrid = new int[graph.length][graph[0].length];
        for(int i=0;i<graph.length;i++)
        {
            for(int j=0;j<graph[i].length;j++)
            {
                if(graph[i][j]==null)
                    intGrid[i][j] = -1;
                else
                {
                    intGrid[i][j]=graph[i][j].getId();
                }
            }
        }
        return  intGrid;
    }

    @Override
    public void output(int height,int width,String fileName)
    {
        //showMessageDialog(null, problemInString);
        try (PrintWriter writer = new PrintWriter(new File("C:\\Users\\guys79\\Desktop\\outputs\\"+fileName+".csv"))) {

            StringBuilder sb = new StringBuilder();
            // System.out.println(problemInString);
            String [] toCsv = problemInString.split("\n");

            for(int i=width-1;i>=0;i--)
            {
                sb.append(i+",");
            }
            sb.append("?,");
            sb.append("\n,");


            for(int i=0;i<height;i++)
            {

                for(int j=toCsv[i].length()-1;j>=0;j--)
                {
                    sb.append(toCsv[i].charAt(j)+",");
                }
                sb.append(i+",");
                sb.append("\n,");

            }

            writer.write(sb.toString());

            // System.out.println("done!");

        } catch (FileNotFoundException e) {
            char last = fileName.charAt(fileName.length()-1);
            String num;
            if(last<'0' ||last>'9')
            {
                output(height,width,fileName+"1");
                return;
            }
            else
            {
                num = "";
                while (last>='0' && last<='9')
                {
                    num = last + num;
                    fileName = fileName.substring(0,fileName.length()-1);
                    last = fileName.charAt(fileName.length()-1);
                }
            }
            int numInt = Integer.parseInt(num);
            numInt++;
            output(height,width,fileName+numInt);
        }

    }
}
