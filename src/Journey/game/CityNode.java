package Journey.game;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import properties_manager.PropertiesManager;

/**
 * Node in a map representing a occupy-able position on the Journey game board. 
 * Each CityNode contains a collection of Edges to other CityNodes in the map. 
 * In addition, some CityNodes may have one or more Instructions associated 
 * with it. These instructions are applied once a player holding the
 * CityNode card arrives at the node.
 * 
 * @author 
 *          Anthony G. Musco
 * <dt><b>Date Created</b><dd>
 *          Oct-22-2014
 * <dt><b>Class</b><dd>
 *          CSE 219
 */
public class CityNode extends Sprite implements Serializable {
    
    public static ImageView indicator;
    
    /**
     * List of possible city colors.
     */
    public static Color[] CityColor = {Color.RED, Color.GREEN, Color.YELLOW};
    
    /**
     * Boolean value indicating whether the CityNode is a city on the game 
     * flight plan, i.e., a player may fly to this city from any other flight
     * city.
     */
    boolean isFlightCity;
    
    /**
     * Used for pathfinding.
     */
    public CityNode prev;
    
    /* Image which represents the front of the card for this CityNode. */
    Image cardFront;
    
    /* Image which represents the back of the card for this CityNode. */
    Image cardBack;
    
    /**
     * Collection of Edges to other CityNodes.
     */
    ArrayList<Edge> edges;
    
    double flightX, flightY;
    
    int section;
    
    /**
     * Collection of instructions associated with this CityNode. Note that this
     * collection may be empty - i.e. there are no instructions associated with
     * this CityNode.
     */
    //ArrayList<Instruction> instructions;
    
    /**
     * Custom constructor which assembles the CityNode from all of its
     * components.
     * 
     * @param color
     *          Color of the city.
     * @param name
     *          Name of the city.
     * @param x
     *          x coordinate of the city in the game map.
     * @param y
     *          y coordinate of the city in the game map.
     * @param isFlightCity
     *          Whether the city exists on the game flight plan.
     * @param cardFront
     *          Path to the image for front of this card.
     * @param cardBack
     *          Path to the image for the back of this card.
     */
    public CityNode(Color color, String name, double x, double y, int section, double flightX,
            double flightY, boolean isFlightCity, Image cardFront, Image cardBack) {
        
        super(color, name, x, y);
        this.section = section;
        this.isFlightCity = isFlightCity;
        this.cardFront = cardFront;
        this.cardBack = cardBack;
        this.flightX = flightX;
        this.flightY = flightY;
        edges = new ArrayList<>();
        
        if(indicator == null) {
            
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String indPath = props.getProperty(GameProperties.IMG_PATH);
            indPath += props.getProperty(GameProperties.INDICATOR_IMAGE);
            indicator = new ImageView(indPath);
            
        }
        
        setOnMouseEntered(e->{
        
            setHighlighted(true);
      
        });
        
        setOnMouseExited(e->{
        
            setHighlighted(false);
        
        });
        
        setOnMouseClicked(e->{
            
            if(eventManager != null)
                eventManager.respondToClickCity(this);
            
        });
        
    }
    
    public CityNode() {
        super(null,null,0,0);
    }
    
    /**
     * Returns the player currently occupying the CityNode. If no player 
     * currently occupies the city, then null is returned.
     * 
     * @return
     *          The Player currently occupying the city. If no player is
     *          currently occupying the city, null is returned.
     */
    public Player getOccupingPlayer() {
        
        for(Player p : gameData.players) {
            
            if(p.currentCity == this) {
                return p;
            }
            
        }
        
        return null;
        
    }

    @Override
    void initImages() {
       
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(GameProperties.IMG_PATH);
        
        String selStr = null, unselStr = null;
        
        if(Color.RED.equals(color)) {
            
            selStr = props.getProperty(GameProperties.RED_CITY_UNSELECTED);
            unselStr = props.getProperty(GameProperties.RED_CITY_SELECTED);
            
        }  else if (Color.GREEN.equals(color)) {
            
            selStr = props.getProperty(GameProperties.RED_CITY_UNSELECTED);
            unselStr = props.getProperty(GameProperties.RED_CITY_SELECTED);
            
        }  else if (Color.YELLOW.equals(color)) {
            
            selStr = props.getProperty(GameProperties.RED_CITY_UNSELECTED);
            unselStr = props.getProperty(GameProperties.RED_CITY_SELECTED);
            
        }
        
        unselected = new Image(imgPath + selStr);
        selected = new Image(imgPath + unselStr);
        
        setImage(unselected);
        
    }
    /**
     * Returns the reference to the front image associated with this CityNode.
     * 
     * @return
     *          The reference to the image of the front of this card.
     */     
    public Image getFrontImage() {
        
        return cardFront;
        
    }
        
    /**
     * Returns the reference to the back image associated with this CityNode.
     * 
     * @return
     *          The reference to the image of the back of this card.
     */
    public Image getBackImage() {
        
        return cardBack;
        
    }  
    
    /**
     * Public getter method for the color of this city node.
     * 
     * @return
     *          The color of this city node.
     */
    public Color getColor() {
        
       return color; 
        
    }

    public void addEdge(Edge e) {
    
        if (edges != null) {
            
            edges.add(e);
            
        }
    
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for(Edge e : edges) {
            b.append("{");
            b.append(e.getNeighborName());
            b.append("|");
            b.append(e.getEdgeType());
            b.append("}");
        }
        return "City Node: name=" + name + " edges=[" + b.toString() + "]";
    }

    public ArrayList<Edge> getEdges() {
    
        return edges;
    
    }
    
    public void setIndicated(boolean indicated) {
        
        if(indicated) {
           
            double offsetY = (getImage().getHeight() * scale * 2);
            double offsetX = ((getImage().getWidth() * scale) / 2.4);
            indicator.setX(getX() + offsetX);
            indicator.setY(getY() + offsetY);
            indicator.setScaleX(getScaleX() * 3);
            indicator.setScaleY(getScaleY() * 3);
            indicator.setVisible(true);
            
        } else { 
            
            indicator.setVisible(false);
            
        }
        
    }

    public void setHighlighted(boolean on) {
        
        setSelected(on);
        for(Edge edge : edges)
            edge.activate(on);
            
    }
    
    public Edge getEdge(CityNode neighbor) {
        
        if (neighbor == null) return null;
        for(Edge e : edges) {
            if(e.getNeighbor() == neighbor) {
                return e;
            }
        }
        
        return null;
        
    }

    public boolean isFlightCity() {
        
        return isFlightCity;
        
    }

    public double getFlightX() {
    
        return flightX;
    
    }

    public double getFlightY() {
    
        return flightY;
    
    }

    public int getSection() {
    
        return section;
    
    }
    
}
