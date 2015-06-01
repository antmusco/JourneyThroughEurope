package Journey.manager;

import Journey.game.CityCard;
import Journey.game.CityNode;
import Journey.game.Edge;
import Journey.game.Edge.EdgeType;
import Journey.game.PathFinder;
import Journey.game.Player;
import Journey.ui.FlightPlanPane;
import Journey.ui.JourneyUI;
import Journey.ui.JourneyUI.GameScreen;
import Journey.ui.MessageBox;
import java.util.ArrayList;
import javafx.animation.SequentialTransition;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Manages all events for the Journey Framework. Any event which occurs during
 * the game passes through an instance of this class to be routed to the 
 * appropriate handling method.
 * 
 * @author 
 *          Anthony G. Musco
 * <dt><b>Date Created</b><dd>
 *          Oct-25-2014
 * <dt><b>Class</b><dd>
 *          CSE 219
 */
public class EventManager {
    
    /**
     * JourneyUI instance to which this EventManager is bound.
     */
    JourneyUI ui;
    
    /**
     * Previous game screen for back button.
     */
    public GameScreen previous;
    
    public boolean togglePath = false;
    
    Stage flightStage;
    
    public EventManager(JourneyUI ui) {
        
        this.ui = ui;
        
    }
    
    public void respondToClickPlayer(Player player) {
        
    }
    
    public void respondToDragPlayer(Player player) {
        
    }
    
    public void respondToClickCity(CityNode city) {
        
        if(ui.getGameManager().getCurrentPlayer().getPlayerType().equals(Player.PlayerType.HUMAN))
            ui.getGameManager().playTurn(ui.getGameManager().getCurrentPlayer(), city);
        
    }
    
    public void respondToSwitchScreen(GameScreen prev, GameScreen next) {
        
        switch(next) {
            
            case SPLASH:
                ui.getSplashScreen().toFront();
                break;
                
            case GAME_HISTORY:
                
                previous = prev;
                ui.getGameHistoryScreen().toFront();
                break;
            
            case HELP:
                
                previous = prev;
                ui.getHelpScreen().toFront();
                break;
                
            case GAME_PLAY:
                ui.getGamePlayScreen().maxZoomOut();
                ui.getGamePlayScreen().toFront();
                break;
            
            case GAME_SETUP:
                ui.getGameSetupScreen().toFront();
                break;
                
            case FLIGHT_PLAN:
                
                previous = prev;
                
                break;
        }
        
    }
    
    public void respondToNewGame(ArrayList<Player> players) {
        
        /* Grab a reference to the GameManager of the JourneyUI. */
        GameManager gameManager = ui.getGameManager();
        
        /* Set the players and begin the game. */
        gameManager.setPlayers(players);
        gameManager.setupNewGame();
        
        /* Show the game map. */
        respondToSwitchScreen(GameScreen.GAME_SETUP, GameScreen.GAME_PLAY);
        
        /* Create sequential transition. This will hold a sequence of 
         * animations, which includes dealing the cards into the players card
         * tray, and moving the players to their home cities.*/
        SequentialTransition st = gameManager.startNewGame();
        st.getChildren().add(gameManager.movePlayersHome());
        
        st.play();
        
    }
    
    public void respondToSaveGame() {
        
        ui.getGameManager().saveGame();
        
    }

    public void respondToExit() {
        
        System.exit(0);
        
    }

    public void respondToClickCard(CityCard card) {
               
        card.toggleCardDisplay();
        
    }

    public void respondToRoll() {
        
        ui.getGameManager().rollDice(ui.getGameManager().getCurrentPlayer()).play();
        
    }

    public void respondToFerry() {
    
        Player p = ui.getGameManager().getCurrentPlayer();
        if(p == null) return;
        
        CityNode cn = p.getCurrentCity();
        if(cn == null) return;
        
        boolean hasSeaRoute = false;
        for(Edge e : cn.getEdges()) {
            
            if(e.getEdgeType() == EdgeType.SEA) {
                
                hasSeaRoute = true;
                break;
                
            }
            
        }
        
        if(hasSeaRoute) {
            
            p.addPoints(-p.getPoints());
            p.setAtPort(true);
            ui.getGameManager().waitAtPort();
            ui.getGameManager().nextTurn();
            
        } else {
            
            MessageBox.showDialog("Cannot take ferry here.");
            
        }       
        
    }

    public void respondToMaximize() {
    
        ui.getGamePlayScreen().getGameBoard().zoomSource(0.01);
    
    }

    public void respondToFindPath(Player currentPlayer) {
        
        togglePath = !togglePath;
        
        ArrayList<CityNode> path = PathFinder.findShortestPath(
                currentPlayer.getCurrentCity(), currentPlayer.getHand());
        
        if(path != null && togglePath) {

            for(int j = 1; j < path.size(); j++) {
                //path.get(0).setHighlighted(togglePath);
                Edge edge = path.get(j-1).getEdge(path.get(j));
                if(edge != null) 
                    edge.activate(true);
            }

        } else {
            
            for(Object o : ui.getGamePlayScreen().getChildren())
                if(o instanceof Edge)
                    ((Edge) o).activate(false);
            
        }
        
    }

    public void respondToFly(FlightPlanPane flightPlanPane) {

        if (flightPlanPane == null) return;
        if(flightStage == null) {
            flightStage= new Stage();
            double width = flightPlanPane.getPrefWidth();
            double height = flightPlanPane.getPrefHeight();
            Scene scene = new Scene(flightPlanPane,width, height);
            flightStage.setScene(scene);
            flightStage.sizeToScene();
            flightStage.setResizable(false);
            flightStage.setOnCloseRequest(e->{
            
               flightStage.hide();
               e.consume();
            
            });
        }
        
        flightStage.show();
    
    }

    public JourneyUI getUI() {
        
        return ui;
        
    }

    public void resetFlightStage() {
    
        flightStage.hide();
    
    }
    
}
