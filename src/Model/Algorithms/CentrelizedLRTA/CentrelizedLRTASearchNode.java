package Model.Algorithms.CentrelizedLRTA;

import Model.Heuristics.HeuristicFactory;
import Model.Heuristics.IAgentHeuristics;

/**
 * This class represents a search node in the Centralized-Lrta* algorithm
 */
public class CentrelizedLRTASearchNode {

    private CentrelizedLRTAState state;//The state
    private CentrelizedLRTASearchNode back;//The predecessor
    private double gVal;//The g value
    private double hVal;//The h value
    private int numOfMoving;//The number of moving agents



    /**
     * The constructor of the class
     * @param state - The given state
     */
    public CentrelizedLRTASearchNode (CentrelizedLRTAState state, CentrelizedLRTASearchNode pre)
    {

        if(pre == null)
            this.numOfMoving = 0;
        else
            this.numOfMoving = pre.getState().numOfMovingAgents(state);
        this.state = state;
        back = null;
        gVal = Double.MAX_VALUE;
        hVal = CentrelizedHeuristics.getInstance().getVal(state);
    }



    /**
     * Ths function will return the h val of the node
     * @return - The h val of the node
     */
    public double gethVal() {
        return hVal;
    }

    /**
     * This function will set the h val of the node
     * @param hVal - The h value
     */
    public void sethVal(double hVal) {
        this.hVal = hVal;
    }

    /**
     * This function will set the g val of the node
     * @param gVal - The g value
     */
    public void setgVal(double gVal,CentrelizedLRTASearchNode pre) {
        this.gVal = gVal;
        if(pre == null)
            this.numOfMoving = 0;
        else
            this.numOfMoving = pre.getState().numOfMovingAgents(state);

    }

    /**
     * This function will return the number of moving agents in this state
     * @return - The number of moving agents in this state
     */
    public int getNumOfMoving() {
        return numOfMoving;
    }

    /**
     * Ths function will return the g val of the node
     * @return - The g val of the node
     */
    public double getgVal() {
        return gVal;
    }

    /**
     * This function will return the state
     * @return - The state
     */
    public CentrelizedLRTAState getState() {
        return state;
    }

    /**
     * This function will set the predecessor of the state
     * @param back - The given predecessor
     */
    public void setBack(CentrelizedLRTASearchNode back) {
        this.back = back;
    }

    /**
     * This function will return the predecessor of the state
     * @return - The predecessor of the state
     */
    public CentrelizedLRTASearchNode getBack() {
        return back;
    }
}
