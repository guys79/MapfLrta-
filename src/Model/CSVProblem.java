package Model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CSVProblem extends AbstractProblemCreator {

    public CSVProblem()
    {
        super();
    }
    @Override
    public Problem getProblem(int numOfAgents, int height, int width, double density, int toDevelop, int type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Problem getProblem(String path) {
        return null;
    }

    public int [] getGridSize(String path)
    {
        BufferedReader br = null;
        String line;
        int [] loc = new int[2];
        try {

            br = new BufferedReader(new FileReader(path));
            line = br.readLine();
            String [] lineSplit= line.split(",");
            String lastNumber = lineSplit[0];
            loc[1] = Integer.parseInt(lastNumber)+1;
            loc[0] = -1;
            while (( line = br.readLine()) != null) {

                //  System.out.println(line);
                // use comma as separator
                loc[0]++;




            }

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
        return loc;
    }
    public Node [][] getGraphFromCSV(String path)
    {
        BufferedReader br = null;
        String line;
        int [] size = getGridSize(path);
        Node [][] grid = new Node[size[0]][size[1]];
        int index = 0;
        try {

            br = new BufferedReader(new FileReader(path));
            line = br.readLine();
            while ((line = br.readLine()) != null && index!=grid.length) {

                // use comma as separator
                String [] lineSplit = line.split(",");
                for(int i=1;i<lineSplit.length-1;i++)
                {
                    if(lineSplit[i].equals("0"))
                        grid[index][i-1] = null;
                    else
                        grid[index][grid[index].length-i] = new GridNode(index,grid[index].length-i);
                }

                index++;

            }

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
        return grid;
    }

    public int [][] getLocFromCSV(String path)
    {
        BufferedReader br = null;
        String line;
        int [][] loc = new int[2][2];
        int index = 0;
        try {

            br = new BufferedReader(new FileReader(path));
            line = br.readLine();
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String [] lineSplit = line.split(",");
                for(int i=1;i<lineSplit.length-1;i++)
                {
                    if(lineSplit[i].equals("S"))
                    {
                        loc[0][0] = index;
                        loc[0][1] = graph[index].length-i;
                    }
                    if(lineSplit[i].equals("E"))
                    {
                        loc[1][0] = index;
                        loc[1][1] = graph[index].length-i;
                    }
                }

                index++;

            }

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
        return loc;
    }


}
