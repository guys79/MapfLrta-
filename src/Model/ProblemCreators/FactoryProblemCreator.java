package Model.ProblemCreators;


/**
 * This class will use the Factory DP + Singleton DP to create IProblemCreator
 */
public class FactoryProblemCreator {
    
    
    private static FactoryProblemCreator factoryProblemCreator;//The instance

    /**
     * The private constructor
     */
    private FactoryProblemCreator()
    {
        
    }

    /**
     * This function will return the instance of the class 
     * @return - The instance
     */
    public static FactoryProblemCreator getInstance()
    {
        if(factoryProblemCreator == null)
            factoryProblemCreator = new FactoryProblemCreator();
        return factoryProblemCreator;
    }

    /**
     * This function will return  new IProblemCreator using the given type and number of agents 
     * @param type - The given type
     * @param numOfAgents - The given number of agents
     * @param prefixLength - the kength of the prefix in each round
     * @return - A new IProblemCreator
     */
    public IProblemCreator getProblemCreator(int type, int numOfAgents, int prefixLength)
    {
        
        IProblemCreator problemCreator;
        if(type == 0)
        {

            //throw new UnsupportedOperationException();
            problemCreator = new RandomProblemCreator();
        }
        else
        {
            if(type == 1)
            {

                //this.problemCreator = new CSVProblem();
                problemCreator = new ScenarioProblemCreator();
            }
            else
            {

                problemCreator = new MAScenarioProblemCreator(numOfAgents,type,prefixLength);
            }
        }
        return problemCreator;
    }
}
