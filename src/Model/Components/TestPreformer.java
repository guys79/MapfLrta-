package Model.Components;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the Test Preformer
 * This class will preform all tests
 */
public class TestPreformer {
    private static TestPreformer testPreformer;//Singelton
    private List<ExperimentData> rawResults;
    private int numberOfSearches;//Number of searches
    private double sumOfNodeUpdateAverage;//The sum of node update
    private double sumOfAverageTimePerIteration;//Sum of Time per iteration
    private long sumOfTimePerSearch;//Sum of Time per Search
    private int sumNonComplete;//Sum of complete tests
    private double sumOfSumOfCosts;//Sum of sumOfCosts
    private double sumOfMakeSpan;//Sum of make span
    private int iteration;//The umber of iterations


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

        this.rawResults = new ArrayList<>();
        this.iteration = 0;
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
     * @param iteration - Number of iterations
     * @param averageTimePerIteration - The given average iteration time for the specific problem
     */
    public void updatePerSearch(double numberOfUpdates,long searchTime,double sumOfCosts, double makeSpan,int iteration,boolean complete,double averageTimePerIteration)
    {

        this.rawResults.add(new ExperimentData(numberOfUpdates,searchTime,sumOfCosts,makeSpan,iteration,complete,averageTimePerIteration));
        this.numberOfSearches++;
        this.sumOfTimePerSearch+=searchTime;
        this.sumOfNodeUpdateAverage+=numberOfUpdates;
        this.iteration+=iteration;
        this.sumOfSumOfCosts+=sumOfCosts;
        this.sumOfMakeSpan+=makeSpan;
        if(!complete)
            sumNonComplete++;
        this.sumOfAverageTimePerIteration+= averageTimePerIteration;

    }






    /**
     * This function will print the calculated information of all the problems tested so far
     * @param textPath - The output text file destination
     * @param rawPath  - The output csv file
     */
    public void printInfo(String textPath,String rawPath )
    {
        String message = "";
        message+="Node average update per search "+(this.sumOfNodeUpdateAverage/this.numberOfSearches)+"\n";
        message+="Average of average time per iteration "+(this.sumOfAverageTimePerIteration/this.numberOfSearches)+"\n";
        message+="Average iterations per search "+((this.iteration*1.0)/this.numberOfSearches)+"\n";
        message+="Average time per search problem "+((this.sumOfTimePerSearch*1.0)/this.numberOfSearches)+"\n";
        message+="Completion rate - "+((((this.numberOfSearches-this.sumNonComplete)*1.0)/this.numberOfSearches)*100)+"\n";
        message+="Average sum of costs "+(this.sumOfSumOfCosts/this.numberOfSearches)+"\n";
        message+="Average makeSpan "+(this.sumOfMakeSpan/this.numberOfSearches)+"\n";
        System.out.println(message);
        BufferedWriter writer = null;//
        try {
            File f= new File(textPath);
            if(!f.exists())
                f.createNewFile();
            writer = new BufferedWriter(new FileWriter(textPath));
            writer.write(message);
            writer.close();
        } catch (IOException e) {
            //   e.printStackTrace();
        }
        writer = null;//
        message = "";
        for(int i=0;i<this.rawResults.size();i++)
        {
            message+=this.rawResults.get(i).ToPrintCSV();
        }
        try {
            File f= new File(rawPath);
            if(!f.exists())
                f.createNewFile();
            writer = new BufferedWriter(new FileWriter(rawPath));
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
