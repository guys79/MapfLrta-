package Model;
/**
 * This class represents a node that is a part of a grid
 */
public class GridNode extends Node {

    private int x;//The X coordinates
    private int y;//The Y coordinates

    /**
     * The constructor of the node
     * @param x - The X coordinates
     * @param y - The Y coordinates
     */
    public GridNode(int x,int y) {
        super();
        this.x =x;
        this.y = y;
    }

    /**
     * This function will return the X coordinates of the node
     * @return - The X coordinates of the node
     */
    public int getX() {
        return x;
    }

    /**
     * This function will return the Y coordinates of the node
     * @return - The Y coordinates of the node
     */
    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "["+x+","+y+"]";
    }
}
