package Journey.game;

import java.io.Serializable;
import java.util.HashMap;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Class representing an edge between two city nodes. An edge is held by a
 * specific CityNode, and it points to another CityNode. Note that since the
 * game graph is NOT directional, for each Edge in a CityNode there exists a
 * corresponding Edge in the CityNode to which the original Edge points.
 * 
 * @author 
 *          Anthony G. Musco
 * <dt><b>Date Created</b><dd>
 *          Oct-22-2014
 * <dt><b>Class</b><dd>
 *          CSE 219
 */
public class Edge extends Line implements Serializable {
    
    /**
     * Name of the CityNode to which this edge points.
     */
    private final String neighborName;
    
    /**
     * Reference to the neighboring node.
     */
    private CityNode neighborNode;
    
    /**
     * Parent node to which this edge is attached.
     */
    private CityNode parent;
    
    /**
     * The type of this edge.
     */
    private final EdgeType type;
    
    /**
     * Indicates whether the edge is active or not.
     */
    private boolean active = false;
    
    /**
     * Constructor which builds an Edge object based on a neighborName and a type.
     * 
     * @param neighbor
     *          Neighboring city to which this edge connects.
     * @param type 
     *          The type of Edge. See @EdgeType.
     */
    public Edge(String neighbor, EdgeType type) {
        
        this.neighborName = neighbor;
        this.type = type;
        
    }
    
    public String getNeighborName() {
        
        return neighborName;
        
    }
    
    public EdgeType getEdgeType() {
        
        return type;
        
    }
    
    public void construct(CityNode parent, HashMap<String, CityNode> map) {
        this.parent = parent;
        neighborNode = map.get(neighborName);
        Image img = parent.getImage();
        
        try {
            startXProperty().bind(parent.xProperty().add(img.getWidth()/2));
            startYProperty().bind(parent.yProperty().add(img.getHeight()/2));
            endXProperty().bind(neighborNode.xProperty().add(img.getWidth()/2));
            endYProperty().bind(neighborNode.yProperty().add(img.getHeight()/2));
            setStroke(Color.BLACK);
            setStrokeWidth(1);
        }catch(Exception e) {
            startXProperty().unbind();
            startYProperty().unbind();
            setStartX(-1);
            setStartY(-1);
            setEndX(-1);
            setEndY(-1); 
        }
        setVisible(false);
        toBack();
    }
    
    public CityNode getNeighbor() {
        
        return neighborNode;
        
    }
    
    /**
     * Enumeration listing the type of the edge.
     */
    public enum EdgeType {
        
        ROAD, SEA
        
    }
    
    public void activate(boolean on) {
        
        if(on) {
            if(type == EdgeType.ROAD)
                setStroke(Color.RED);
            else
                setStroke(Color.BLUE);
            setStrokeWidth(5);
            setVisible(true);
        } else {
            setStroke(Color.BLACK);
            setStrokeWidth(1);
            setVisible(false);
        }
        
    }
    
}
