package Model;

public interface IProblemCreator {
    public Problem getProblem(int numOfAgents,int height,int width, double density,int toDevelop,int type);
    public Problem getProblem(String path);
    public int [][]getGridGraph();
    public void output(int height,int width,String fileName);

}
