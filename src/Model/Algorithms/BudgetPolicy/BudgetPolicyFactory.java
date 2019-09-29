package Model.Algorithms.BudgetPolicy;

public class BudgetPolicyFactory {

    private static BudgetPolicyFactory instance;//The instance


    private BudgetPolicyFactory()
    {

    }

    public static BudgetPolicyFactory getInstance()
    {
        if(instance == null)
            instance = new BudgetPolicyFactory();
        return instance;
    }

    public IBudgetPolicy getPolicy(int type)
    {
        IBudgetPolicy policy;
        if(type == 2)
            policy = new EqualBudget();//Regular
        else // type = 7
        {
            policy = new LongestPathBudgetPolicy();
        }


        return policy;
    }
}
