public class GridNode extends Node {

    private int x;
    private int y;

    /**
     * The constructor of the node
     */
    public GridNode(int x,int y) {
        super();
        this.x =x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "["+x+","+y+"]";
    }
}
