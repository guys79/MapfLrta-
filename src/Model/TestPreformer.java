package Model;

public class TestPreformer {
    private static TestPreformer testPreformer;
    private int totalItrerationAmount;
    private int numberOfEpisodes;
    private long totalAmountOfTime;
    private double totalAveragePerIterationTime;
    private double totalCost;
    private TestPreformer()
    {
        clear();
    }
    private void clear()
    {
        this.totalCost = 0;
        totalItrerationAmount = 0;
        numberOfEpisodes = 0;
        totalAmountOfTime = 0;
        this.totalAveragePerIterationTime = 0;
    }
    public void updateSearch(int iteration,long totalTime,double cost)
    {
        this.totalItrerationAmount+=iteration;
        this.numberOfEpisodes++;
        totalAmountOfTime+= totalTime;
        totalAveragePerIterationTime+= (totalTime*1.0/iteration);
        this.totalCost+= cost;
    }
    public void printInfo()
    {
        System.out.println("Number of problems "+this.numberOfEpisodes);
        System.out.println("Average Cost - "+(this.totalCost/numberOfEpisodes));
        System.out.println("Total Time - "+this.totalAmountOfTime);
        System.out.println("Average Amount of time - "+(this.totalAmountOfTime/numberOfEpisodes));
        System.out.println("Average Amount of time per episode - "+(this.totalAveragePerIterationTime/numberOfEpisodes));
    }
    public static TestPreformer getInstance()
    {
        if(testPreformer == null)
            testPreformer = new TestPreformer();
        return testPreformer;
    }


}
