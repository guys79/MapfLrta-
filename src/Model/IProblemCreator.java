package Model;

/**
 * This interface represents a generic problem creator
 */
public interface IProblemCreator {
    /**
     * This function will create a problem using the height, width, number of agents, density of walls, number of nodes to develop, the type of search
     * @param numOfAgents - The number of agents
     * @param height - The height of the graph(the number of columns in the grid)
     * @param width - The width of the graph(the number of rows in the grid)
     * @param density - The density of the walls in the overall grid
     * @param toDevelop - The number of nodes allowed to be developed in a single iteration
     * @param type - Thw type of search
     * @return - A problem
     */
    public Problem getProblem(int numOfAgents,int height,int width, double density,int toDevelop,int type);

    /**
     * This function will import a problem from a file
     * @param path - The path to the file
     * @param toDevelop - The number of nodes allowed to be developed in a single iteration
     * @param type - Thw type of search
     * @return - The problem from the file
     */
    public Problem getProblem(String path, int toDevelop, int type);

    /**
     * This function will import a problem from a file
     * @param mapPath - The path to the map file
     * @param scenarioPath- The path to the scenario file
     * @param toDevelop - The number of nodes allowed to be developed in a single iteration
     * @param type - Thw type of search
     * @return - The problem from the file
     */
    public Problem getProblem(String mapPath,String scenarioPath, int toDevelop, int type);

    /**
     * This function will return the graph as a integer matrix
     * @return - The grid represented bt an integer matrix object
     */
    public int [][]getGridGraph();

    /**
     * This function will write the output to the file
     * @param file - the path to the file
     */
    public void output(String file);

}
