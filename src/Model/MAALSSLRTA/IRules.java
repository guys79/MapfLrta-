package Model.MAALSSLRTA;

import Model.ALSSLRTA.AlssLrtaSearchNode;

/**
 * This interface represents a generic set of rules
 */
public interface IRules {

    /**
     * This function will check if the move from the origin node
     * to the target node at the specific time is valid
     * @param origin - The given origin node
     * @param target - The given target node
     * @param time - The given time
     * @return - True IFF the move described is valid
     */
    public boolean isValidMove(int origin, int target,int time);
}
