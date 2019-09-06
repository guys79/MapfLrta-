package Model.Algorithms.MAALSSLRTA;

import Model.Algorithms.ALSSLRTA.AlssLrtaSearchNode;
import Model.Node;

public class MaAlssLrtaSearchNode extends AlssLrtaSearchNode {

    private int time;//Time dimension
    /**
     * The constructor of the class
     *
     * @param node - The node that this class represents
     */
    public MaAlssLrtaSearchNode(Node node,int time) {
        super(node);
        this.time = time;
    }
    /**
     * The constructor of the class
     *
     * @param node - The node that this class represents
     */
    public MaAlssLrtaSearchNode(AlssLrtaSearchNode node,int time) {
        super(node);
        this.time = time;

    }
    /**
     * This function will return the time
     * @return - The time
     */
    public int getTime() {
        return time;
    }


}
