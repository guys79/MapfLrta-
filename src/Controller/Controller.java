package Controller;

import Model.GridNode;
import Model.Model;
import Model.Node;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;

import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * This class represents the controller that manages the GUI
 */
public class Controller{

    @FXML
    public Text timeText;//The text that indicates the time we are currently watching (t=?)
    public Canvas canvas;//The canvas
    private Model model;//The model
    public Button nextButton;//Moves the time forward
    public Button forwardButton;//Moves the time forward
    public Slider slider;//Can control the time
    public Button backButton;//Moves the time backwards
    public GraphicsContext context;//The context
    public TextField text_field;//The text field get as an input the scenario number
    public Button scen_btn;//The enter button for the scenario number
    public IntegerProperty time = new SimpleIntegerProperty();
    public int[][] grid;//The graph
    public HashMap<Integer, int[]> nodeLocations = new HashMap<>();//Key - node's id, value - the location of the node [x,y]
    public HashMap<int[], Color> paths = new HashMap<>();//Key - path, Value - Agent's color
    private int agentCount = 0;//Number of agents
    private HashSet<int [] > noSolLocs;
    private Color[] colors = {//The colors available
            Color.RED,
            Color.DARKRED,
            Color.ORANGE,
            Color.DARKORANGE,
            Color.GOLDENROD,
            Color.DARKGOLDENROD,
            Color.GREEN,
            Color.DARKGREEN,
            Color.BLUE,
            Color.DARKBLUE
    };
    private double cellWidth;//The height of the cell
    private double cellHeight;//The width of the cell
    private int maxTime = 0;

    /**
     * This function will handle the next problem drawing
     */
    public void next()
    {
        model.next();
    }

    /**
     * This function check whether he given string represents an integer
     * @param str - The given string
     * @return - True IFF the String represents an integer
     */
    private boolean checkIfInt(String str)
    {
        try{
            int g = Integer.parseInt(str);
        }
        catch (NumberFormatException e)
        {
            return false;
        }
        return true;
    }
    /**
     * This function will set the scenario
     */
    public void setScenario()
    {

        String text = text_field.getText();
        if(checkIfInt(text)) {
            int index = Integer.parseInt(text);
            model.setScenario(index-1);
            return;
        }
        model.setFileName(text);

    }
    /**
     * This function will the controller
     * @param locs - The locations of the prev agents
     */
    public void clear(HashSet<int []> locs)
    {
        slider.setValue(0);
        paths.clear();
        context.setFill(Color.WHITE);
       // System.out.println("CLEARRRRRRRRRR");
        for(int [] loc : locs)
        {
            context.fillRect(loc[1] * cellWidth, loc[0] * cellHeight, cellWidth + 1, cellHeight + 1);
        }
        for(int [] loc : noSolLocs)
        {
            context.fillRect(loc[1] * cellWidth, loc[0] * cellHeight, cellWidth + 1, cellHeight + 1);
            this.grid[loc[0]][loc[1]] = 0;

        }
        this.noSolLocs.clear();
        this.maxTime = 0;


    }

    /**
     * This function will set the model of the controller
     * @param model - The given model
     */
    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * This function will initialize the Controller
     * @param grid - The given grid
     */
    public void initialize(int[][] grid){
        slider.setBlockIncrement(1);
        this.noSolLocs = new HashSet<>();
        slider.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> {
//            time.setValue((int)(slider.getValue()/100*maxTime));
            time.setValue((int)slider.getValue());
        });
        time.addListener((ChangeListener) (arg0, arg1, arg2) -> {
            draw();
        });
        addGrid(grid);
        context = canvas.getGraphicsContext2D();
        cellWidth = canvas.getWidth()/grid[0].length;
        cellHeight = canvas.getHeight()/grid.length;

    }

    /**
     * This function will draw an instance of the GUI ( a single point in time)
     */
    public void draw(){
        timeText.setText("t = "+time.getValue());
        drawGrid();
        drawAgents();
        //first = false;
    }

    /**
     * This function will draw the grid
     */
    private void drawGrid(){
       // if(first) {
            context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[0].length; col++) {
                    if (grid[row][col] == -1) context.setFill(Color.BLACK);
                    else if (grid[row][col] == -2) context.setFill(Color.YELLOW);
                    else context.setFill(Color.WHITE);
                    context.fillRect(col * cellWidth, row * cellHeight, cellWidth + 1, cellHeight + 1);
                }
            }
      //  }
    }

    /**
     * This function will draw the agents
     */
    private void drawAgents(){
        if (grid == null) return;
        //drawGrid();
        for (HashMap.Entry<int[], Color> entry : paths.entrySet()){
            int[] path = entry.getKey();
            int nodeID = getNode(path, time.getValue());
            int[] pos = nodeLocations.get(nodeID);

            int endNodeID = getNode(path, path.length-1);
            int[] endPos = nodeLocations.get(endNodeID);


            context.setFill(entry.getValue());
            context.fillRect(endPos[0] * cellWidth, endPos[1] * cellHeight, cellWidth, cellHeight);


            if(time.getValue() >= path.length-1)
            {
                context.setFill(Color.web("#414A4C"));
            }

            context.fillOval(pos[0] * cellWidth, pos[1] * cellHeight, cellWidth, cellHeight);

        }
    }

    /**
     * This function will return the value of a path in a single time
     * Which means, a path in a single point in time (node)
     * @param path - The given path
     * @param t - The given time
     * @return - The node from the path in th given time
     */
    private int getNode(int[] path, int t) {
        if (t > path.length-1) return path[path.length-1];
        else return path[t];
    }

    /**
     * This function will add an agent to the GUI
     * @param path - The path of the agent
     * @param goal - The goal node
     */
    public void addAgent(List<Node> path,Node goal){
        int [] pathArr = new int[path.size()];
        if(pathArr.length==1)//No Solution
        {
            GridNode gridNode = (GridNode)goal;
            int [] loc = new int[2];
            loc[0] = gridNode.getX();
            loc[1] = gridNode.getY();
            this.noSolLocs.add(loc);
            grid[loc[0]][loc[1]] = -2;



        }
        for(int i=0;i<path.size();i++)
        {
            pathArr[i] = path.get(i).getId();
        }
        paths.put(pathArr, colors[agentCount++ % colors.length]);
        if (pathArr.length-1 > maxTime){
            maxTime = pathArr.length-1;
            slider.setMax(maxTime);
        }
    }

    /**
     * This function will add the grid (Graph) to the GUI
     * @param grid - The grid(Graph)
     */
    private void addGrid(int[][] grid){
        this.grid = grid;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (grid[row][col] != -1){
                    int[] pos = {col, row};
                    nodeLocations.put(grid[row][col], pos);
                }
            }
        }
    }

    /**
     * This function will move the time forward
     */
    public void timeForward() {
        if (time.getValue() < maxTime){
            updateSlider(1);
        }
    }

    /**
     * This function will move the time backward
     */
    public void timeBackward() {
        if (time.getValue() > 0){
            updateSlider(-1);
        }
    }

    /**
     * This function is responsible for the slider update
     * @param delta - The dela between the two most recent locations of the slider
     */
    private void updateSlider(int delta){
        slider.setValue(slider.getValue()+delta);
    }
}
