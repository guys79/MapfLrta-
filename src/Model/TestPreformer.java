package Model;

/**
 * This class represents the Test Preformer
 * This class will preform all tests
 */
public class TestPreformer {
    private static TestPreformer testPreformer;//Singelton
    private int totalItrerationAmount;//Total number of iteration for all problems
    private int numberOfEpisodes;//Number of problems
    private long totalAmountOfTime;//Total amount of time for all problems
    private double totalAveragePerIterationTime;//The sum of all problem's average iteration solve
    private double totalCost;//The total cost for all problems

    /**
     * The constructor
     */
    private TestPreformer()
    {
        clear();
    }

    /**
     * This function will reset all the classes values in order to preform another test
     */
    private void clear()
    {
        this.totalCost = 0;
        totalItrerationAmount = 0;
        numberOfEpisodes = 0;
        totalAmountOfTime = 0;
        this.totalAveragePerIterationTime = 0;
    }

    /**
     * This function updates the data for a single problem
     * @param iteration - The number of iterations
     * @param totalTime - The total amount of time
     * @param cost - The cost of the solution
     */
    public void updateSearch(int iteration,long totalTime,double cost)
    {
        this.totalItrerationAmount+=iteration;
        this.numberOfEpisodes++;
        totalAmountOfTime+= totalTime;
        totalAveragePerIterationTime+= (totalTime*1.0/iteration);
        this.totalCost+= cost;
    }

    /**
     * This function will print the calculated information of all the problems tested so far
     */
    public void printInfo()
    {
        System.out.println("Number of problems "+this.numberOfEpisodes);
        System.out.println("Average Cost - "+(this.totalCost/numberOfEpisodes));
        System.out.println("Total Time - "+this.totalAmountOfTime);
        System.out.println("Average Amount of time - "+(this.totalAmountOfTime/numberOfEpisodes));
        System.out.println("Average Amount of time per episode - "+(this.totalAveragePerIterationTime/numberOfEpisodes));
    }

    /**
     * This function will return the instance of the Test Preformer (Singleton)
     * @return -the instance of the TestPerformer
     */
    public static TestPreformer getInstance()
    {
        if(testPreformer == null)
            testPreformer = new TestPreformer();
        return testPreformer;
    }


}
