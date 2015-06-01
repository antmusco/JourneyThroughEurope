/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Journey.game;

import java.io.Serializable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Anthony
 */
public class Turn implements Serializable {
    
    private SimpleIntegerProperty turnNumber;
    private SimpleStringProperty playerName;
    private SimpleStringProperty fromCityName;
    private SimpleStringProperty toCityName;
    private SimpleStringProperty description;
    
    public Turn() {
        
    }
    
    public Turn(int turnNumber, String playerName, String fromCityName, 
       String toCityName, String description){
        
        this.turnNumber = new SimpleIntegerProperty(turnNumber);
        this.playerName = new SimpleStringProperty(playerName);
        this.fromCityName = new SimpleStringProperty(fromCityName);
        this.toCityName = new SimpleStringProperty(toCityName);
        this.description = new SimpleStringProperty(description);
        
    }
    
    public Integer getTurnNumber(){
        
        return turnNumber.get() + 1;
        
    }
    
    public String getPlayerName(){
        
        return playerName.get();
        
    }
    
    public String getFromCityName(){
        
        return fromCityName.get();
        
    }
    
    public String getToCityName(){
        
        return toCityName.get();
        
    }
    
    public String getDescription(){
        
        return description.get();
        
    }
    
}
