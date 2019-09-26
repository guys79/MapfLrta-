package Model.Algorithms.CentrelizedLRTA;

import Model.Heuristics.HeuristicFactory;
import Model.Heuristics.IAgentHeuristics;

public class CentrelizedLRTASearchNode {

    private CentrelizedLRTAState state;
    private CentrelizedLRTASearchNode back;
    private double gVal;
    private double hVal;
    public CentrelizedLRTASearchNode (CentrelizedLRTAState state)
    {
        this.state = state;
        back = null;
        gVal = Double.MAX_VALUE;
        hVal = CentrelizedHeuristics.getInstance().getVal(state);
    }

    public double gethVal() {
        return hVal;
    }

    public void sethVal(double hVal) {
        this.hVal = hVal;
    }

    public void setgVal(double gVal) {
        this.gVal = gVal;
    }

    public double getgVal() {
        return gVal;
    }

    public CentrelizedLRTAState getState() {
        return state;
    }

    public void setBack(CentrelizedLRTASearchNode back) {
        this.back = back;
    }

    public CentrelizedLRTASearchNode getBack() {
        return back;
    }
}
