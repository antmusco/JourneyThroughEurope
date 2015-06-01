package Journey.game;

import Journey.ui.JourneyUI;
import Journey.ui.PlayerCardsTray;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import properties_manager.PropertiesManager;

/**
 * Represents a generic player for the Journey Framework. The player is 
 * assigned a specific color, and contains a 'hand' of cards which represent 
 * the cities the player must visit during the game. In addition, the player 
 * has a number of points, which represent the number of moves the player can 
 * make before their turn ends.
 * 
 * @author 
 *          Anthony G. Musco
 * <dt><b>Date Created</b><dd>
 *          Oct-22-2014
 * <dt><b>Class</b><dd>
 *          CSE 219
 */
public class Player extends Sprite implements Serializable {
    
    public static int countOfPlayers = 0;
    
    public static final double ALIGN_X = 73;
    public static final double ALIGN_Y = 140;
    public static final double PIXELS_PER_SECOND = 1000;
    
    /**
     * Number of moves the player can make before their turn ends. Note that
     * the player's points may be altered by an Instruction, and points will
     * carry over between rounds.
     */
    int movePoints;
    
    /**
     * The type of the Player (i.e. Computer vs. Human).
     */
    PlayerType playerType;
    
    /**
     * Collection of 'cards' which are just a collection of CityNodes which
     * represent the cities the player must visit before completing the game.
     */
    ArrayList<CityNode> hand;
    
    /**
     * List of edges already traversed.
     */
    ArrayList<Edge> path;
    
    /**
     * City at which the player must begin and end the game.
     */
    CityNode homeCity;
    
    /**
     * City the player is currently at.
     */
    CityNode currentCity;
    
    /**
     * Indication of whether the player is waiting at a port city.
     */
    private boolean atPort;

    /**
     * Optimum path used by the computer to automate the travel.
     */
    ArrayList<CityNode> shortestPath;
    
    /**
     * Card tray for the player.
     */
    private PlayerCardsTray tray;
    
    private double originX, originY, clickStartX, clickStartY;
    
    public static final Duration animationSpeed = Duration.millis(500);

    /**
     * Public getter method for the current city of this player.
     * 
     * @return 
     *          The current city of this player.
     */
    public CityNode getCurrentCity() {
        
        return currentCity;
        
    }

    /**
     * Adds points to this player's move points..
     * 
     * @param rollValue 
     *          The value of the die roll for this player's roll.
     */
    public void addPoints(int rollValue) {
        
        movePoints += rollValue;
        
    }

    /**
     * Public getter method for this players move points.
     * 
     * @return 
     *          The value of the move points left for this player.
     */
    public int getPoints() {
        
        return movePoints;
        
    }

    public PlayerType getPlayerType() {
      
        return this.playerType;
        
    
    }

    public ArrayList<CityNode> getShortestPath() {
        
        if(shortestPath == null)
            shortestPath = PathFinder.findShortestPath(currentCity, hand);
    
        return shortestPath;
    
    }

    public void setShortestPath(ArrayList<CityNode> path) {
        
        this.shortestPath = path;
        
    }

    /**
     * Enumeration representing the possible colors a player may have.
     */
    public enum PlayerColor {
        
        BLACK, WHITE, RED, BLUE, GREEN, YELLOW;
        
    }
    
    /**
     * Enumeration representing the possible type of player.
     */
    public enum PlayerType {
        
        HUMAN, COMPUTER;
        
    }
    
    public Player() {
        super(null,null,0,0);
    }
    
    /**
     * Default constructor which initializes the name of the player.
     * 
     * @param color
     *          Color of the player.
     * @param name
     *          Name of the player.
     */
     public Player(Color color, PlayerType playerType, String name, JourneyUI ui) {
        
        super(color, name, -1, -1);
        hand = new ArrayList<>();
        tray = new PlayerCardsTray(this, ui);
        path = new ArrayList<>();
        this.playerType = playerType;
        movePoints = 1; /* Set to 1 to allow for first move to home city. */
        countOfPlayers++;
        
         setOnMouseEntered(e->{
         
             setSelected(true);
             if (currentCity == null) return;
             for(Edge edge : currentCity.getEdges())
                 edge.activate(true);
             
         });
         
         setOnMouseExited(e->{
         
             setSelected(false);
             if (currentCity == null) return;
             for(Edge edge : currentCity.getEdges())
                 edge.activate(false);
             
         });
         
         setOnMouseClicked(e->{
         
            e.consume();
             
         });
         
         setOnMouseReleased(e->{

            alignToCity(currentCity);
             e.consume();
         
         });
         
         setOnMouseDragEntered(e->{
             if(playerType == PlayerType.HUMAN) {
                clickStartX = e.getSceneX();
                clickStartY = e.getSceneY();
                originX = getX();
                originY = getY();
                e.consume();
             }
         });
         
         setOnMouseDragged(e->{
             
            if(playerType == PlayerType.HUMAN) {
                setX(originX + (e.getSceneX() - clickStartX));
                setY(originY + (e.getSceneY() - clickStartY));
               e.consume();
            }
            
         });
         
         setOnMouseDragExited(e->{
         
             if(playerType == PlayerType.HUMAN) {
                alignToCity(currentCity);
                e.consume();
             }
         });
         
     }

    /**
     * Public setter method for the Type of this Player.
     * 
     * @param type 
     *          The PlayerType for this Player.
     */
    public void setType(PlayerType type) {
        
        this.playerType = type;
        
    }

    @Override
    void initImages() {
            
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(GameProperties.IMG_PATH);
        
        if(Color.BLUE.equals(color)) {
            
            imgPath += props.getProperty(GameProperties.PIECE_BLUE);
            
       } else if (Color.RED.equals(color)) {
           
            imgPath += props.getProperty(GameProperties.PIECE_RED);
           
       } else if (Color.BLACK.equals(color)) {
           
            imgPath += props.getProperty(GameProperties.PIECE_BLACK);
           
       } else if (Color.WHITE.equals(color)) {
           
            imgPath += props.getProperty(GameProperties.PIECE_WHITE);
           
       } else if (Color.YELLOW.equals(color)) {
           
            imgPath += props.getProperty(GameProperties.PIECE_YELLOW);
           
       } else if (Color.GREEN.equals(color)) {
           
            imgPath += props.getProperty(GameProperties.PIECE_GREEN);
           
       }
        
        unselected = new Image(imgPath);
        selected = unselected;
        
        setImage(unselected);
        
    }

    public void alignToCity(CityNode city) {
        
        if(city == null) return;
        
        if (currentCity != null)
            currentCity.setHighlighted(false);
        currentCity = city;
        setMapX(city.getMapX() - ALIGN_X);
        setMapY(city.getMapY() - ALIGN_Y);
        
    }
    
    public ParallelTransition moveToCity(CityNode city) {
        
        /* Get position of city in viewport. */
        double finalX = city.getX();
        double finalY = city.getY();
        
        /* Adjust for scaling of city. */
        finalX += (city.getImage().getWidth() - (city.getImage().getWidth() * scale)) /2 ;
        finalY += (city.getImage().getHeight() - (city.getImage().getHeight() * scale)) /2;
        
        /* Adjust for scaling of player. */
        finalX -= ((getImage().getWidth() - (getImage().getWidth() * scale)) / 2);
        finalY -= ((getImage().getHeight() - (getImage().getHeight() * scale)) / 2);
        
        /* Align piece on top of city. */
        finalX -= (ALIGN_X * scale);
        finalY -= (ALIGN_Y * scale);
        
        finalX -= getX();
        finalY -= getY();
        
        /* Set key values for animation. */
        TranslateTransition tt = new TranslateTransition(animationSpeed, this);
        tt.setAutoReverse(false);
        tt.setToX(finalX);
        tt.setToY(finalY);
        
        ScaleTransition st = new ScaleTransition(animationSpeed, this);
        st.setAutoReverse(false);
        st.setToX(scale);
        st.setToY(scale);
        
        ParallelTransition pt = new ParallelTransition(tt, st);
        
        pt.setAutoReverse(false);
        
        movePoints--;
        alignToCity(city);
        return pt;
        
    }
    
    
    
    /**
     * Calculates the distance in pixels from the player's current position to
     * the indicated point.
     * 
     * @param endX
     *          x-coordinate of the indicated point.
     * @param endY
     *          y-coordinate of the indicated point.
     * @return 
     *          The distance in pixels from the player's current position to
     *          the indicated point.
     */
    private double getDistanceTo(double endX, double endY) {
        
        double startX = getMapX();
        double startY = getMapY();
        
        double distance = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
        
        System.out.println(name + ": " + Math.abs(distance));
        
        return(Math.abs(distance));
        
    }
    
    /**
     * Adds a city to the player's hand of cities.
     * 
     * @param node
     *          CityNode to add to the players hand.
     * @return 
     *          A TranslateTransition object representing the dealing of the
     *          card.
     */
    public TranslateTransition dealCity(CityNode node) {
        
        /* Set the player's home city. */
        if(homeCity == null) {
            
            setHomeCity(node);
            
        }
        
        /* Put the card in the player's hand. */
        hand.add(node);
        
        /* Put the card in the player's tray. */
        return tray.addCard(node);
        
    }
    
    /**
     * Public getter method for the home city of this player.
     * 
     * @return
     *          The home city of this player.
     */
    public CityNode getHomeCity() {
        
        return homeCity;
        
    }
    
    /**
     * Public setter method for the players home city. 
     * 
     * @param home 
     *          New home city of the player.
     */  
    public void setHomeCity(CityNode home) {
        
        homeCity = home;
        
    }
       
    public void setAtPort(boolean atPort) {
        
        this.atPort = atPort;
        
    }
    
    public boolean isAtPort() {
        
        return atPort;
        
    }
    
    public ArrayList<CityNode> getHand() {
        
        return hand;
        
    }
    
    /**
     * Public getter method for the cards tray for the player.
     * 
     * @return
     *          A reference to the cards tray for the player.
     */
    public PlayerCardsTray getCardTray() {
        
        return tray;
        
    }

    @Override
    public String toString() {
        
        String s = String.format(" %s: MapX=%f MapY=%f | x=%f y=%f | City: %s", 
            name, mapX, mapY, getX(), getY(), currentCity);
        return s;
        
    }
    
}
