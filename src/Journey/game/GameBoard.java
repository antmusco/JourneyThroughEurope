package Journey.game;

import static Journey.game.Player.countOfPlayers;
import Journey.ui.GamePlayPane;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Surface on which the game is played. Wraps the map image in a StackPane, 
 * on top of which CityNodes and edges are rendered.
 * 
 * @author 
 *          Anthony G. Musco
 * <dt><b>Date Created</b><dd>
 *          Oct-29-2014
 * <dt><b>Class</b><dd>
 *          CSE 219
 */
public class GameBoard extends Canvas {
    
    /**
     * Constant indicating the number of pixels the map will shift for 
     * key presses.
     */
    public static final double MAP_SHIFT_AMOUNT = 75;
    
    /**
     * Origin points for the map relative to the canvas origin (0,0). If these
     * values are negative, it indicates that the viewport begins at the 
     * indicated value * -1.
     */
    private double mapOriginX = 0, mapOriginY = 0;
    
    /**
     * Horizontal and vertical offsets accounting for the position of the 
     * game board in its parent pane.
     */
    private double offsetX = 0, offsetY = 0;
    
    /**
     * Variables which indicates the viewport width and height within the 
     * GamePlayPane.
     */
    private double viewWidth, viewHeight;
    
    /**
     * Start and change values for dragging the map with the mouse.
     */
    public double dragStartX, dragStartY, dragDelX = 0, dragDelY = 0;
    
    /**
     * Scale of the map. This is changed using the mouse scroll wheel, or the
     * shortcut command CTRL_+ or CTRL_-.
     */
    private double scale;
    
    /**
     * Variables for keeping track of the mouse position, both relative to the
     * canvas and relative to the map.
     */
    private double mouseX, mouseY, mouseMapX, mouseMapY;
    
    /**
     * Image on which the Sprites are to be drawn. Represents the map on which
     * the game is to be played.
     */
    private final Image map;
    
    /**
     * GamePlayPane in which this game board sits. This reference is required
     * for layout purposes.
     */
    private GamePlayPane parent;
    
    /**
     * Collection of CityNodes for this game board. Each node is stored in a
     * hash map to provide quick access.
     */
    HashMap<String, CityNode> cities;
    
    /**
     * Collection of Players for this game board. Each player is stored in a
     * hash map to provide quick access.
     */
    ArrayList<Player> players;
    
    /**
     * Public constructor for the GameMap which initializes the map Image to
     * the indicated parameter.
     * 
     * @param map 
     *          Image to initialize the map for the GameBoard.
     */
    public GameBoard(Image map, GamePlayPane parent) {

        this.map = map;
        this.parent = parent;
        cities = new HashMap<>();
        players = new ArrayList<>();
        scale = 1;
        drawMap();
        
    }
    
    /**
     * Draws the map image with the indicated scale on the GameBoard using its
     * graphics context.
     */
    public final void drawMap() {
        
        calculateDimensions();
        
        /* Draw the map at the origin point with the indicated scale. */
        getGraphicsContext2D().drawImage(map, mapOriginX, 
            mapOriginY, map.getWidth() * scale, 
            map.getHeight() * scale);
        
        drawNodes();
        //debugStats();
        drawPanes();
        
    }
    
    /**
     * Starts the drag gesture. This method is typically called on a 
     * MOUSE_PRESSED event, and sets the dragStartX and dragStartY values to 
     * the current mouse coordinates.
     * 
     * @param startX
     *          Current x coordinate of the mouse.
     * @param startY 
     *          Current y coordinate of the mouse.
     */
    public void beginDrag(double startX, double startY) {
        
        dragStartX = startX; 
        dragStartY = startY;
            
    }
    
    /**
     * Shifts the map in the viewport according to the indicated offset from
     * the drag point. The drag point is updated at the end of this method for
     * future drag requests.
     * 
     * @param dragNewX
     *          New x coordinate to calculate the offset from the drag point.
     * @param dragNewY 
     *          New y coordinate to calculate the offset from the drag point.
     */
    public void dragMap(double dragNewX, double dragNewY) {
        
        /* Get the offset from the drag point. */
        dragDelX = dragNewX - dragStartX;
        dragDelY = dragNewY - dragStartY;

        /* If the map is dragged past x origin, set x = 0. */
        if((mapOriginX + dragDelX) > 0) {

            dragStartX = dragNewX;
            mapOriginX = 0;
            dragDelX = 0;

        }
        /* If the map is dragged past the map width, set x = mapWidth. */
        else if ((mapOriginX + dragDelX) < (viewWidth - 
            (map.getWidth() * scale ))) {

            dragStartX = dragNewX;
            mapOriginX = viewWidth - (map.getWidth() * scale);
            dragDelX = 0;

        } 
        /* Otherwise, move the map horizontally. */
        else {
            
            mapOriginX += dragDelX;
            
        }

        /* If the map is dragged past y origin, set y = 0. */
        if((mapOriginY + dragDelY) > 0) {

            dragStartY = dragNewY;
            mapOriginY = 0;
            dragDelY = 0;

        } 
        /* If the map is dragged past the map height, set y = mapHeight. */
        else if ((mapOriginY + dragDelY) < (viewHeight - 
            (map.getHeight() * scale)))  {

            dragStartY = dragNewY;
            mapOriginY = viewHeight - (map.getHeight() * scale);
            dragDelY = 0;

        } 
        /* Otherwise, move the map vertically. */
        else {
            
            mapOriginY += dragDelY;
            
        }
        
        /* Draw the map, then draw the nodes on top of the map. */
        drawMap();
        
        /* Reset the drag point. */
        dragStartX = dragNewX;
        dragStartY = dragNewY;

    }

    /**
     * Moves the map in the viewport by setting the origin values of the map
     * to the indicated offset. If the offset proceeds out of bounds, the 
     * origin values are set to the bounding values.
     * 
     * @param offsetX
     *          Change in the coordinate of the x origin.
     * @param offsetY 
     *          Change in the coordinate of the y origin.
     */
    public void moveMap(double offsetX, double offsetY) {
        
        /* If the map is moved past x origin, set x = 0. */
        if((mapOriginX + offsetX) > 0) {

            mapOriginX = 0;

        } 
        /* If the map is moved past the map width, set x = mapWidth. */
        else if ((mapOriginX + offsetX) < (viewWidth - 
            (map.getWidth() * scale))) {

            mapOriginX = viewWidth - (map.getWidth() * scale); 

        } 
        /* Otherwise, move the map horizontally. */
        else {

            mapOriginX += offsetX;

        }

        /* If the map is moved past y origin, set y = 0. */
        if((mapOriginY + offsetY) > 0) {

            mapOriginY = 0;

        } 
        /* If the map is moved past the map height, set y = mapHeight. */
        else if ((mapOriginY + offsetY) < (viewHeight - 
            (map.getHeight()* scale))) {

            mapOriginY = viewHeight - (map.getHeight() * scale);

        }
        /* Otherwise, move the map vertically. */
        else {

            mapOriginY += offsetY;

        }
        
        /* Redraw the map, then draw all nodes on top of the map. */
        drawMap();
        
    }
    
    
    /**
     * Scales the map image by the amount indicated by the parameter. If the
     * zoom will reduce the map inside of the bounds, the zoom is set to the
     * smallest valid value such that there will be no whitespace.
     * 
     * The zoom is scales as the inverse of the current zoom. That is, the 
     * larger the zoom, the slower the zoom changes, and the smaller the zoom,
     * the faster the zoom changes.
     * 
     * @param zoom 
     *          Factor by which the image will be enlarged / reduced. The 
     *          default for a mouse scroll is +40 / -40.
     */
    public void zoomSource(double zoom) {

        /* Calculate the change in zoom using the current zoom. */
        double zoomTest = scale + (zoom / (500 * (1 / scale)));
        
        /* Update the scale value and refactor the image. */
        scale = zoomTest;
        
        /* Adjust the scale depending on the bounds. */
        adjustZoom();
        
    }

    /**
     * Adjusts the scale of the map image depending on the bounds of the canvas.
     * If the zoom will reduce the map inside of the bounds, the zoom is set 
     * to the smallest valid value such that there will be no whitespace.
     */
    public void adjustZoom() {
        
        calculateDimensions();
        
        /* If the width of the canvas is greater than the scaled width of the
         * map image, set the scale factor to the canvas width / map width. */
        if(viewWidth > (map.getWidth() * scale)) {
            
            scale = viewWidth / map.getWidth();
            
        } 
        /* If the height of the canvas is greater than the scaled height of the
         * map image, set the scale factor to the canvas height / map height. */
       if(viewHeight > (map.getHeight() * scale)) {
            
            scale = viewHeight / map.getHeight();
            
        }
     
        /* Repaint the map with no offset. */
        moveMap(0,0);
        
    }

    /**
     * Handle a key presses from the keyboard.
     * 
     * @param e 
     *          KeyEvent which is passed to this method from the handling 
     *          function.
     */
    public void handleKeyPress(KeyEvent e) {
        
        KeyCode code = e.getCode();
        /* Switch on the keycode. */
        switch (code) {
            
            case LEFT:
                moveMap(MAP_SHIFT_AMOUNT, 0);
                break;
            case RIGHT:
                moveMap(-MAP_SHIFT_AMOUNT, 0);
                break;
            case UP:
                moveMap(0, MAP_SHIFT_AMOUNT);
                break;
            case DOWN:
                moveMap(0, -MAP_SHIFT_AMOUNT);
                break;
            case EQUALS: case ADD:
                if(e.isControlDown()) zoomSource(40);
                break;
            case MINUS: case SUBTRACT:
                if(e.isControlDown()) zoomSource(-40);
                break;
            default:
                break;
        }
        
    }

    /**
     * Method which updates the current coordinates of the mouse, relative to
     * the canvas (mouseX, mouseY) and relative to the map (mouseMapX, 
     * mouseMapY).
     * 
     * @param x
     *          Current x coordinate of the mouse.
     * @param y 
     *          Current y coordinate of the mouse.
     */
    public void getMouseCoordinates(double x, double y) {
        
        mouseX = x;
        mouseY = y;
        mouseMapX = (x - mapOriginX + offsetX) / scale;
        mouseMapY = (y - mapOriginY + offsetY) / scale;
        
    }
    
    /**
     * Draws all of the nodes on the game board using the boards graphics
     * context. Cities are drawn on the board first, so that players can be 
     * seen as standing "on top" of them.
     */
    private void drawNodes() {
        
        /* Draw all of the cities at their corresponding location. */
        for (Map.Entry<String, CityNode> entry : cities.entrySet()) {
            
            CityNode city = entry.getValue();
            city.center(mapOriginX + offsetX, mapOriginY + offsetY, scale);
            city.toFront();
            
        }
        
        /* Draw all of the players at their corresponding location. */
        for (Player p : players) {

            p.center(mapOriginX + offsetX, mapOriginY + offsetY, scale);
            p.toFront();
            
        }
        if(CityNode.indicator != null)
            CityNode.indicator.toFront();
        
    }

    /**
     * Puts a player in the hash map of players for this game map.
     * 
     * @param player 
     *          Player to insert into the hash map.
     */
    public void addPlayer(Player player) {
       
        players.add(player);
        
    }

    /**
     * Puts a city in the hash map of cities for this game map.
     * 
     * @param city 
     *          City to insert into the hash map.
     */
    public void addCity(CityNode city) {
        
        cities.put(city.getName(), city);
        
    }
     
    /**
     * Displays coordinate values of different components of the game map
     * for debugging purposes.
     */
    public void debugStats() {
        
        GraphicsContext gc = getGraphicsContext2D();
        
        String imgText = String.format("Image W/H:\t\t%6.2f\t%6.2f\n", map.getWidth(), map.getHeight());
        String cpText = String.format("View W/H:\t\t%6.2f\t%6.2f\n", getWidth(), getHeight());
        String cpStart = String.format("View X/Y:\t\t\t%6.2f\t%6.2f\n", -mapOriginX, -mapOriginY);
        String mouseText = String.format("Mouse X/Y:\t\t%6.2f\t%6.2f\n", mouseX, mouseY);
        String mouseSText = String.format("Mouse Source X/Y:\t%6.2f\t%6.2f\n", mouseMapX, mouseMapY);
        String scaleText = String.format("Zoom Factor:\t\t%6.5f\n", scale);
        
        gc.strokeText(imgText, 20, 20);
        gc.strokeText(cpText, 20, 35);
        gc.strokeText(cpStart, 20, 50);
        gc.strokeText(mouseText, 20, 65);
        gc.strokeText(mouseSText, 20, 80);
        gc.strokeText(scaleText, 20, 95);
        
    }
    
    /**
     * Get the width of the map image.
     * 
     * @return
     *          The width of the map image.
     */
    public double getMapCenterX() {
        
        return map.getWidth() / 2;
        
    }
    
    /**
     * Get the height of the map image.
     * 
     * @return 
     *          The height of the map image.
     */
    public double getMapCenterY() {
        
        return map.getWidth() / 2;
        
    }

    private void drawPanes() {
 
        if(parent != null)
            parent.drawPanes();
        
    }

    /**
     * Calculates the layout dimensions for this gameboard.
     */
    private void calculateDimensions() {
        
        /* If the parent pane is null, the offset is 0. */
        if(parent == null) {
            
            offsetX = 0;
            offsetY = 0;
            
        } else{
        
            offsetX = parent.getLeftWidth();
            offsetY = parent.getTopHeight();
            viewWidth = parent.getWidth() - parent.getRightWidth()
                    - parent.getLeftWidth();
            viewHeight = parent.getHeight() - parent.getTopHeight()
                    - parent.getBottomHeight();
        
        }
        
    }

    public Player getPlayer(int i) {
        
        return players.get(i);
        
    }

    public void initPlayerPositions() {
        
        for(Player p : players) {
            
            p.setScaleX(1);
            p.setScaleY(1);
            p.setX((getWidth() / 2) - (p.getImage().getWidth() / 3));
            p.setY((getHeight() / 2) - (p.getImage().getHeight() / 2) );
            
        }
        
    }
   
}
