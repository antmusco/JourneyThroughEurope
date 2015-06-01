package Journey.ui;

import Journey.game.CityNode;
import Journey.game.GameProperties;
import Journey.manager.GameManager;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import properties_manager.PropertiesManager;
/**
 * Class which depicts the flight screen for the JourneyUI.
 * 
 * @author 
 *          Anthony G. Musco
 * <dt><b>Date Created</b><dd>
 *          Dec-08-2014
 * <dt><b>Class</b><dd>
 *          CSE 219
 */
public class FlightPlanPane extends Pane {

    public static final double SCALE = 0.50;
    GameManager manager;
    
    public FlightPlanPane(GameManager manager, Image img) {
        
        this.manager = manager;
        
        setPrefWidth(img.getWidth() * SCALE);
        setPrefHeight(img.getHeight() * SCALE);
        this.setBackground(new Background(new BackgroundImage(img, 
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, 
            BackgroundPosition.DEFAULT, 
            new BackgroundSize(img.getWidth() * SCALE, img.getHeight() * SCALE, 
                    false, false, true, true))));
        
        HashMap<String, CityNode> cities = manager.getCities();
        
        for(Map.Entry<String, CityNode> entry : cities.entrySet()) {
            
            CityNode city = entry.getValue();
            if(city.isFlightCity()) {
                
                FlightCityNode node = new FlightCityNode(this, city);
                getChildren().add(node);
                
            }
            
        }
        
    }

    public void respondToFlight(CityNode city) {
        
        manager.flyToCity(city);
        
    }

}

class FlightCityNode extends ImageView {
    
    public CityNode city;
    private FlightPlanPane pane;
    private Image unselected, selected;
    double scale = FlightPlanPane.SCALE - 0.005;
    
    public FlightCityNode(FlightPlanPane pane, CityNode city) {
        
        this.pane = pane;
        this.city = city;
        initImages();
        setBehavior();
        
        double xValue = city.getFlightX() * scale;
        xValue -= (unselected.getWidth() - (unselected.getWidth() * scale)) / 2;
        
        double yValue = city.getFlightY() * scale;
        yValue -= (unselected.getHeight() - (unselected.getHeight() * scale)) / 2;
        
        setX(xValue);
        setY(yValue);
        setScaleX(scale);
        setScaleY(scale);
        
    }

    private void initImages() {
           
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(GameProperties.IMG_PATH);
        
        String selStr = null, unselStr = null;
            
        selStr = props.getProperty(GameProperties.RED_CITY_UNSELECTED);
        unselStr = props.getProperty(GameProperties.RED_CITY_SELECTED);
      
        unselected = new Image(imgPath + selStr);
        selected = new Image(imgPath + unselStr);
        
        setImage(unselected);
    
    }

    private void setBehavior() {
    
        setOnMouseEntered(e->{
        
            setSelected(true);
            System.out.println(city.getName());
            
        });
        
        setOnMouseExited(e->{
        
            setSelected(false);
        
        });
        
        setOnMouseClicked(e->{
        
            pane.respondToFlight(city);
            
        });
        
    }

    private void setSelected(boolean b) {
    
        if(b)
            setImage(selected);
        else
            setImage(unselected);
    
    }
       
}
