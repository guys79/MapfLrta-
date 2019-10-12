package Model.Components;

/**
 * This function represent data for an experiment
 */
public class ExperimentData {

    private double numberOfUpdates;//The number of updates in a search
    private long searchTime;//The searchTIme
    private double sumOfCosts;//The sum of costs
    private double makeSpan;//The make span
    private int iteration;//The number of iterations
    private boolean complete;//True IFF the problem was solved
    private double averageTimePerIteration;//The average time for iteration

    /**
     * The constructor of the class
     * @param numberOfUpdates - The average number of updatesof a node in the search
     * @param searchTime - The total search time
     * @param sumOfCosts - The sum of costs
     * @param makeSpan - the make span
     * @param iteration -the number of iterations
     * @param complete - True IFF the search was successful
     * @param averageTimePerIteration - The average time per iteration
     */
    public ExperimentData(double numberOfUpdates,long searchTime,double sumOfCosts, double makeSpan,int iteration,boolean complete,double averageTimePerIteration)
    {
        this.numberOfUpdates = numberOfUpdates;
        this.searchTime = searchTime;
        this.sumOfCosts = sumOfCosts;
        this.makeSpan = makeSpan;
        this.iteration = iteration;
        this.complete = complete;
        this.averageTimePerIteration = averageTimePerIteration;
    }

    /**
     * This function will return the number of iterations
     * @return - The number of iterations
     */
    public int getIteration() {
        return iteration;
    }

    /**
     * This function will return the average of average time per iteration
     * @return - The average of average time per iteration
     */
    public double getAverageTimePerIteration() {
        return averageTimePerIteration;
    }

    /**
     * This function will return the make span
     * @return - The make span
     */
    public double getMakeSpan() {
        return makeSpan;
    }

    /**
     * this function will return the average number of updates per node
     * @return - The average number of updates per node
     */
    public double getNumberOfUpdates() {
        return numberOfUpdates;
    }

    /**
     * This function will return the sum of costs
     * @return - The sum of costs
     */
    public double getSumOfCosts() {
        return sumOfCosts;
    }

    /**
     * This function will return the total search time
     * @return - The total search time
     */
    public long getSearchTime() {
        return searchTime;
    }

    /**
     * This function will return if the search was complete
     * @return - True IFF the search was complete
     */
    public boolean isComplete() {
        return complete;
    }


    /**
     * This function will return a string that represent the print format of the data in CSV file
     * @return - A string that represent the print format of the data in CSV file
     */
    public String ToPrintCSV()
    {
        String str="";
        str+=this.numberOfUpdates+",";
        str+=this.searchTime+",";
        str+=this.sumOfCosts+",";
        str+=this.makeSpan+",";
        str+=this.iteration+",";
        if(complete)
            str+="1,";
        else
            str+="0,";
     //   str+=this.complete+",";
        str+=this.averageTimePerIteration+"\n";
        return str;
    }
}
