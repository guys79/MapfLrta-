package Model.MAALSSLRTA;

import Model.ALSSLRTA.AlssLrtaSearchNode;

/**
 * This class represents the Rule Book for MAPF
 */
public class RuleBook implements IRules {

    @Override
    public boolean isValidMove(AlssLrtaSearchNode origin, AlssLrtaSearchNode target, int time) {
        if(!(origin instanceof MaAlssLrtaSearchNode && target instanceof MaAlssLrtaSearchNode))
            return false;
        // TODO: 8/26/2019  complete the function..
        // 1.  No collisions
        if(!this.checkForCollisions(target,time))
            return false;
        // 2. No swapping
        // 3. Agents do not disappear (Should be ok...)
        return this.checkForSwappings(origin,target,time);
    }

    /**
     * This function check if the move to the target node will cause a collision
     * At the given time
     * @param target - the given target node
     * @param time - The given time
     * @return - True IFF there will be no collision if an agent will move there
     */
    private boolean checkForCollisions(AlssLrtaSearchNode target,int time) {
        return ((MaAlssLrtaSearchNode)target).canReserve(time);
    }

    /**
     * This function checks if the move will cause swapping at the given time
     * @param origin - The origin node
     * @param target - The target node
     * @param time - The given time
     * @return - True IFF there will be no swapping if the move described will cause no swapping between agents
     */
    private boolean checkForSwappings(AlssLrtaSearchNode origin, AlssLrtaSearchNode target,int time) {
        //At time 0 is the start positioning
        //The assumption the time>1 when swapping
        int id1 = ((MaAlssLrtaSearchNode)origin).getAgent(time);
        if(id1 == -1)
            return true;
        int id2 = ((MaAlssLrtaSearchNode)target).getAgent(time-1);
        if(id2 == -1)
            return true;

        //agent 1 wants to move from origin to target
        return id1!=id2;
    }

}
