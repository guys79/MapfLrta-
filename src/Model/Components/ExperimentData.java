package Model.Components;

public class ExperimentData {

    private double numberOfUpdates;//The number of updates in a search
    private long searchTime;//The searchTIme
    private double sumOfCosts;//The sum of costs
    private double makeSpan;//The make span
    private int iteration;//The number of iterations
    private boolean complete;//True IFF the problem was solved
    private double averageTimePerIteration;//The average time for iteration

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


    public int getIteration() {
        return iteration;
    }

    public double getAverageTimePerIteration() {
        return averageTimePerIteration;
    }

    public double getMakeSpan() {
        return makeSpan;
    }

    public double getNumberOfUpdates() {
        return numberOfUpdates;
    }

    public double getSumOfCosts() {
        return sumOfCosts;
    }

    public long getSearchTime() {
        return searchTime;
    }

    public boolean isComplete() {
        return complete;
    }


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
        str+=this.complete+",";
        str+=this.averageTimePerIteration+"\n";
        return str;
    }
}
