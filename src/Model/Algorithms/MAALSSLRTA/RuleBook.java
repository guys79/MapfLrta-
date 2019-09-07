package Model.Algorithms.MAALSSLRTA;


/**
 * This class represents the Rule Book for MAPF
 */
public class RuleBook implements IRules {

    private MAALSSLRTA maalsslrta;//The Multi Agent aLSS-LRTA* algorithm

    /**
     * The constructor
     * @param maalsslrta - The Multi Agent aLSS-LRTA* algorithm
     */
    public RuleBook(MAALSSLRTA maalsslrta)
    {
        this.maalsslrta = maalsslrta;
    }

    /**
     * This function will set the Multi Agent aLSS-LRTA* algorithm
     * @param maalsslrta - The Multi Agent aLSS-LRTA* algorithm
     */
    public void setMaalsslrta(MAALSSLRTA maalsslrta) {
        this.maalsslrta = maalsslrta;
    }


    @Override
    public boolean isValidMove(int origin, int target, int time) {


        // 1.  No collisions
        if(!this.checkForCollisions(target,time)) {
           // System.out.println("Interesting");
            return false;
        }

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
    private boolean checkForCollisions(int target,int time) {
        return maalsslrta.canReserve(time,target);
    }

    /**
     * This function checks if the move will cause swapping at the given time
     * @param origin - The origin node
     * @param target - The target node
     * @param time - The given time
     * @return - True IFF there will be no swapping if the move described will cause no swapping between agents
     */
    private boolean checkForSwappings(int origin, int target,int time) {
        //At time 0 is the start positioning
        //The assumption the time>1 when swapping
        int id1 = this.maalsslrta.getAgent(origin,time);
        if(id1 == -1)
            return true;
        int id2 = this.maalsslrta.getAgent(target,time-1);
        if(id2 == -1)
            return true;
        if(id2==-2)
            return false;

        //agent 1 wants to move from origin to target
        return id1!=id2;
    }

}
