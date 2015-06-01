package Journey.game;

import Journey.manager.EventManager;
import Journey.ui.PlayerCardsTray;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.util.Duration;

/**
 * Class which depicts a city card for the game. This class allows the card to
 * be displayed and moved across the screen, as well as flipped.
 * 
 * @author 
 *          Anthony G. Musco
 * <dt><b>Date Created</b><dd>
 *          Nov-22-2014
 * <dt><b>Class</b><dd>
 *          CSE 219
 */
public class CityCard extends Sprite{

    public void setOriginalX() {
        originalX = getTranslateX();
    }
    public void setOriginalY() {
        
        originalY = getTranslateY();
        
    }

    /**
     * Enumeration indicating the status of the card.
     *     IN_TRAY:     Indicates that the card is currently in the
     *                  PlayerCardsPane, face up.
     *     CENTER_UP:   Indicates that the card is currently in the center of
     *                  the screen, face up.
     *     CENTER_DOWN: Indicates that the card is currently in the center of
     *                  the screen, face down.
     */
    public enum DisplayStatus { IN_TRAY, CENTER_UP, CENTER_DOWN };
    
    /**
     * CityNode to which this card is attached.
     */
    private CityNode city;
    
    /**
     * Event manager which moves the card around the screen on command.
     */
    private final EventManager eventManager;
    
    /**
     * Current display status of the card (see DisplayStatus enumeration).
     */
    private DisplayStatus status;
    
    /**
     * Image for the front of the card.
     */
    private Image frontImage;
    
    /**
     * Image for the back of the card.
     */
    private Image backImage;
    
    /**
     * Flag used when constructing the edges to the city card.
     */
    private boolean constructed = false;
    
    private boolean removed = false;
    
    private double originalX = 0;
    private double originalY = 0;
    
    /**
     * Constructor of the CityCard object based on the indicated CityNode.
     * 
     * @param city
     *          CityNode to which this CityCard is attached.
     * @param eventManager
     *          EventManager which responds to click requests on the card.
     */
    public CityCard(CityNode city, EventManager eventManager) {
        
        super(city.getColor(), city.getName() + " Card", 0.0, 0.0);
        this.city = city;
        this.eventManager = eventManager;
        constructed = true;
        setBehaviors();
        initImages();
        
    }

    @Override
    void initImages() {
        
        if (constructed) {
            this.frontImage = city.getFrontImage();
            this.backImage = city.getBackImage();
            this.selected = city.getFrontImage();
            this.unselected = city.getFrontImage();
            status = DisplayStatus.IN_TRAY;
            setImage(frontImage);  
        }
        
    }

    /**
     * Flips the city card over. Rotates between the 'front' and 'back' images.
     */
    public void flip() {
        
        if(getImage() == frontImage) {
            setImage(backImage);
        } else {
            setImage(frontImage);
        }
    }
    
    /**
     * Sets the behavior for actions on this card.
     */
    private void setBehaviors(){
        
        setOnMousePressed(e->{
            
            if(!eventManager.getUI().getFreeze())
            eventManager.respondToClickCard(this);
            
        });
        
    }

    @Override
    public void setSelected(boolean isSelected) {
        
        if (isSelected) {
            
            if(status == DisplayStatus.IN_TRAY) {
                city.setSelected(true);
                city.setIndicated(true);
                setScaleX(PlayerCardsTray.scale * 1.05);
                setScaleY(PlayerCardsTray.scale * 1.05);
            }
            
        } else {
            
            if(status == DisplayStatus.IN_TRAY) {            
                city.setSelected(false);
                city.setIndicated(false);
                setScaleX(PlayerCardsTray.scale);
                setScaleY(PlayerCardsTray.scale);
            }
            
        }
        
    }
    
    
    public void toggleCardDisplay() {
        
        switch(status) {
            
            case IN_TRAY:
                
                /* Move card to the center of the screen. */
                TranslateTransition moveCardToCenter = new 
                TranslateTransition(Duration.millis(1000), this);
                moveCardToCenter.setAutoReverse(false);
                originalX = getTranslateX();
                originalY = getTranslateY();
                moveCardToCenter.setToX(0);
                moveCardToCenter.setToY(0);

                /* Enlarge the card. */
                ScaleTransition enlargeCard = new 
                    ScaleTransition(Duration.millis(1000), this);
                enlargeCard.setToX(0.65);
                enlargeCard.setToY(0.65);

                /* Play animations sequentially. */
                SequentialTransition playCardTransition = new 
                SequentialTransition(moveCardToCenter, enlargeCard);
                
                this.status = DisplayStatus.CENTER_UP;
                playCardTransition.play();
                
                break;
                
            case CENTER_UP:
                flip();
                this.status = DisplayStatus.CENTER_DOWN;
                break;
                
            case CENTER_DOWN:
                
                if(removed)
                    ((PlayerCardsTray) getParent()).removeCard(this);
                
                flip();
                
                /* Minimize the card. */
                ScaleTransition minimizeCard = new 
                    ScaleTransition(Duration.millis(1000), this);
                minimizeCard.setToX(0.40);
                minimizeCard.setToY(0.40);
                
                /* Move card to the tray. */
                TranslateTransition moveCardToTray = new 
                        TranslateTransition(Duration.millis(1000), this);
                moveCardToTray.setAutoReverse(false);
                moveCardToTray.setToX(originalX);
                moveCardToTray.setToY(originalY);

                /* Play animations sequentially. */
                SequentialTransition hideCardTransition = new 
                    SequentialTransition(minimizeCard, moveCardToTray);
                this.status = DisplayStatus.IN_TRAY;
                hideCardTransition.play();
                break;
            
        }
        
    }
    
    /**
     * Public getter method for the city which this card wraps.
     * 
     * @return 
     *          A reference to the CityNode which this card wraps.
     */
    public CityNode getCity() {
        
        return city;
        
    }
    
    /**
     * Public getter method for the display status of this card.
     * 
     * @return
     *          The DisplayStatus of this card.
     */
    public DisplayStatus getStatus() {
        
        return status;
        
    }
    
    /**
     * Public setter method for the display status of this card.
     * 
     * @param status 
     *          The new DisplayStatus of this card.
     */
    public void setStatus(DisplayStatus status) {
        
       this.status = status;
        
    }
    
    public boolean isRemoved() {
        
        return removed;
        
    }
    
    public void setRemoved(boolean removed) {
        
        this.removed = removed;
        
    }
    
}
