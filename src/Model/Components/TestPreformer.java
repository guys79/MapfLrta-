package Model.Components;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class represents the Test Preformer
 * This class will preform all tests
 */
public class TestPreformer {
    private static TestPreformer testPreformer;//Singelton

    private int numberOfSearches;//Number of searches
    private double sumOfNodeUpdateAverage;//The sum of node update
    private double sumOfAverageTimePerIteration;//Sum of Time per iteration
    private long sumOfTimePerSearch;//Sum of Time per Search
    private int sumNonComplete;//Sum of complete tests
    private double sumOfSumOfCosts;//Sum of sumOfCosts
    private double sumOfMakeSpan;//Sum of make span


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

        this.numberOfSearches = 0;
        this.sumOfNodeUpdateAverage = 0;
        this.sumOfTimePerSearch = 0;
        this.sumNonComplete = 0;
        this.sumOfSumOfCosts = 0;
        this.sumOfMakeSpan = 0;
        this.sumOfAverageTimePerIteration = 0;


    }

    /**
     * This function updates the data for a single problem
     * @param numberOfUpdates - The number of updates
     * @param searchTime - The search time
     * @param sumOfCosts - The sumOfCosts
     * @param makeSpan - The makeSpan
     */
    public void updatePerSearch(double numberOfUpdates,long searchTime,double sumOfCosts, double makeSpan)
    {
        this.numberOfSearches++;
        this.sumOfTimePerSearch+=searchTime;
        this.sumOfNodeUpdateAverage+=numberOfUpdates;

        this.sumOfSumOfCosts+=sumOfCosts;
        this.sumOfMakeSpan+=makeSpan;


    }

    /**
     * This function will update the number of non-complete problems
     */
    public void updateNonComplete(){this.sumNonComplete++;}

    /**
     * This function will update the average of average iteration time [er search
     * @param averageTimePerIteration - The given average iteration time for the specific problem
     */
    public void updateAverageIteration(double averageTimePerIteration)
    {
        this.sumOfAverageTimePerIteration+= averageTimePerIteration;
    }


    /**
     * This function will print the calculated information of all the problems tested so far
     * @param path - The output file destination
     */
    public void printInfo(String path)
    {
        String message = "";
        message+="Node average update per search "+(this.sumOfNodeUpdateAverage/this.numberOfSearches)+"\n";
        message+="Average of average time per iteration "+(this.sumOfAverageTimePerIteration/this.numberOfSearches)+"\n";
        message+="Average time per search problem "+((this.sumOfTimePerSearch*1.0)/this.numberOfSearches)+"\n";
        message+="Completion rate - "+((((this.numberOfSearches-this.sumNonComplete)*1.0)/this.numberOfSearches)*100)+"\n";
        message+="Average sum of costs "+(this.sumOfSumOfCosts/this.numberOfSearches)+"\n";
        message+="Average makeSpan "+(this.sumOfMakeSpan/this.numberOfSearches)+"\n";
        System.out.println(message);
        BufferedWriter writer = null;//
        try {
            File f= new File(path);
            if(!f.exists())
                f.createNewFile();
            writer = new BufferedWriter(new FileWriter(path));
            writer.write(message);
            writer.close();
        } catch (IOException e) {
            //   e.printStackTrace();
        }
        clear();
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
