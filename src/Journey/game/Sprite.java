package Journey.game;

import Journey.manager.EventManager;
import java.io.Serializable;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 *
 * @author Anthony
 */
abstract class Sprite extends ImageView implements Serializable{
    
    protected EventManager eventManager;
    protected GameData gameData;
    protected Color color;
    protected Image unselected, selected;
    protected double mapX, mapY, scale;
    protected String name;
    
    public Sprite() {
        
    }
    
    public Sprite(Color color, String name, double x, double y) {

        this.color = color;
        this.name = name;
        mapX = x;
        mapY = y;
        scale = 1;
        setX(x);
        setY(y);
        initImages();
        setFocusTraversable(true);
        Tooltip t = new Tooltip(name);
        Tooltip.install(this, t);
        
        setOnMouseEntered(e->{
            
            setSelected(true);
            //System.out.println("Node Entered: " + name + " MapX: " + mapX + "MapY: " + mapY);
            
        });
        
        setOnMouseExited(e->{
        
            setSelected(false);
        
        });
        
    }
    
    abstract void initImages();
    
    public void center(double sourceOriginX, double sourceOriginY, double scale) {
        
        setTranslateX(0);
        setTranslateY(0);
        
        this.scale = scale;
        
        setX(sourceOriginX + (mapX * scale));
        setY(sourceOriginY + (mapY * scale));
        setScaleX(scale);
        setScaleY(scale);
        
        
        double shiftX = (getImage().getWidth() - (getImage().getWidth() * scale)) / 2;
        double shiftY = (getImage().getHeight() - (getImage().getHeight() * scale)) / 2;
        setX(getX() - shiftX);
        setY(getY() - shiftY);

        //System.out.println(toString());
        
    }
    
    public void setSelected(boolean isSelected) {
        
        if(isSelected)
            setImage(selected);
        else
            setImage(unselected);
        
    }
    
    public void setEventManager(EventManager eventManager) {
        
        this.eventManager = eventManager;
        
    }
    
    public void setGameData(GameData gameData) {
        
        this.gameData = gameData;
        
    }
    
    public void setName(String name) {
        
        this.name = name;
        
    }
    
    public double getMapX(){
        
        return mapX;
        
    }
    
    public double getMapY(){
        
        return mapY;
        
    }
    
    public String getName(){
        
        return name;
        
    }
    
    public void setMapX(double x) {
        
        mapX = x;
        
    }
    
    public void setMapY(double y) {
        
        mapY = y;
        
    }

    @Override
    public String toString() {
        
        String s = String.format("Sprite %s: MapX=%f MapY=%f | x=%f y=%f", 
            name, mapX, mapY, getX(), getY());
        return s;
        
    }
    
}
