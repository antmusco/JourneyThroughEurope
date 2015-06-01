/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Journey.ui;

import Journey.game.CityCard;
import Journey.game.CityNode;
import Journey.game.Player;
import java.util.ArrayList;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Pane which contains all of the cards held by players during the game. Cards
 * are displayed as a splayed stack of city cards, and each one is clickable to 
 * display the information about the city.
 * 
 * @author 
 *          Anthony G. Musco
 * <dt><b>Date Created</b><dd>
 *          Nov-20-2014
 * <dt><b>Class</b><dd>
 *          CSE 219
 */
public class PlayerCardsTray extends Pane {
    
    /**
     * Scale factor for the cards.
     */
    public static final double scale = 0.40;
    
    public static final double PREF_WIDTH = 180;
    public static final double PREF_HEIGHT = 725;
    
    /**
     * Vertical displacement so that all the cards appear "stacked".
     */
    private double displacement = 0.0;
    
    /**
     * Padding for the card in the card tray.
     */
    public static final double paddingX = 10;
 
    /**
     * Padding for the card in the card tray.
     */
    public static final double paddingY = 10;
    
    /**
     * JourneyUI instance in which this Player Cards Tray is located..
     */
    private final JourneyUI ui;
    
    /**
     * The player for which this card pane serves.
     */
    private final Player player;
    
    /**
     * Variable indicating the horizontal and vertical offset for the cards in
     * the card pane.
     */
    private double cardWidth = 0;
    
    /**
     * Collection of cards for this player.
     */
    private ArrayList<CityCard> hand;
    
    /**
     * Default constructor.
     * 
     * @param player
     *          The player for which this card pane serves.
     * @param ui
     *          JourneyUI instance in which this player cards tray is located.
     */
    public PlayerCardsTray(Player player, JourneyUI ui) {
    
        this.player = player;
        this.ui = ui;
        hand = new ArrayList<>();
        initBackground();
        setPrefWidth(PREF_WIDTH);
        setPrefHeight(PREF_HEIGHT);
    
    }

    final public void initBackground() {
        
        BackgroundFill fill = new BackgroundFill(Color.WHEAT, 
          CornerRadii.EMPTY, Insets.EMPTY);
        setBackground(new Background(fill));
        
    }

    public TranslateTransition addCard(CityNode city) {
        
        /* If the cities is null, do nothing. */
        if(city == null)
            return null;
          
        /* Create the card. */
        final CityCard card = new CityCard(city, ui.getEventManager());

        /* Add front card image to the stack pane. */
        getChildren().add(card);
        card.toBack();
        hand.add(card);

        /* Place the card in the center of the screen. */
        double offsetX = city.getFrontImage().getWidth();
        offsetX = -(offsetX - offsetX * scale) / 2;
        offsetX += PREF_WIDTH;
        offsetX += ui.getGamePlayScreen().getCenterWidth()/2;
        offsetX -= (city.getFrontImage().getWidth() * scale /2);
        card.setLayoutX(offsetX);
        card.setLayoutY(0);
        card.setScaleX(scale);
        card.setScaleY(scale);
        
        /* Animate the card to its propert position */
        double finalX = card.getImage().getWidth();
        finalX = -(finalX - (finalX * scale))/2;
        finalX += paddingX;
        
        double finalY = card.getImage().getHeight();
        finalY = -(finalY - (finalY * scale))/2;
        finalY += paddingY;
        
        TranslateTransition t = new TranslateTransition(Player.animationSpeed, card);
        t.setAutoReverse(false);
        t.setToX(finalX - offsetX);
        t.setToY(finalY + displacement);
        t.setOnFinished(e->{card.toFront();});
        
        /* Increment the vertical offset. */
        displacement += 55;
        
        return t;
        
    }
    
    /**
     * Returns the width of the card for layout purposes.
     * 
     * @return 
     *          The double value of the scaled width of the card in pixels.
     */
    public double getCardWidth() {
        
        return cardWidth;
        
    }

    /**
     * Plays the card and removes it from the tray. If there are any special
     * instructions, they will be activated here and performed when applicable.
     * 
     * @param city 
     *          The city of the card to be played.
     */
    public SequentialTransition playCard(CityNode city) {
        
        CityCard card = null;
        
        /* Find the card wrapping this city. */
        for (CityCard c : hand) {
            
            if(c.getCity() == city) {
                card = c;
                break;
            }
            
        } 
        
        /* Do nothing if the card was not found. */
        if(card == null) return null;

        TranslateTransition moveCardToCenter = new 
        TranslateTransition(Duration.millis(500), card);
        moveCardToCenter.setAutoReverse(false);
        card.setOriginalX();
        moveCardToCenter.setToX(0);
        moveCardToCenter.setToY(0);

        /* Enlarge the card. */
        ScaleTransition enlargeCard = new 
        ScaleTransition(Duration.millis(500), card);
        enlargeCard.setToX(0.65);
        enlargeCard.setToY(0.65);

        /* Play animations sequentially. */
        SequentialTransition playCardTransition = new 
        SequentialTransition(moveCardToCenter, enlargeCard);
        
        playCardTransition.getChildren().add(removeCard(card));
        
        return playCardTransition;
        
    }

    public FadeTransition removeCard(CityCard card) {
        
        FadeTransition ft = new FadeTransition(Duration.millis(500), card);
        ft.setToValue(0);
        ft.setOnFinished(e->{        
            getChildren().remove(card);
            hand.remove(card);
            player.getHand().remove(card.getCity());
            card.setRemoved(true);
        });
        return ft;
            
    }
    
}
