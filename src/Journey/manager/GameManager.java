package Journey.manager;

import Journey.file.FileLoader;
import Journey.game.CityNode;
import Journey.game.Edge;
import Journey.game.Edge.EdgeType;
import Journey.game.GameData;
import Journey.game.GameProperties;
import Journey.game.Player;
import Journey.game.Player.PlayerType;
import Journey.ui.JourneyUI;
import Journey.ui.MessageBox;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.util.Duration;
import properties_manager.PropertiesManager;

/**
 * Manages the data of the current game, and stores the data of past games.
 * 
 * @author 
 *          Anthony G. Musco
 * <dt><b>Date Created</b><dd>
 *          Oct-25-2014
 * <dt><b>Class</b><dd>
 *          CSE 219
 */
public class GameManager {
    
    /**
     * JourneyUI instance to which this GameManager is bound.
     */
    JourneyUI ui;
    
    /**
     * Data for the game currently being played.
     */
    GameData currentGame;
    
    /**
     * History of games played.
     */
    ArrayList<GameData> gameHistory;
    
    /**
     * List of players currently playing the game.
     */
    ArrayList<Player> players;    
    
    HashMap<String, CityNode> cities;

    /**
     * Provides an animation to move the players to their respective home
     * cities. Initializes their playing positions.
     * 
     * @return 
     *          A Sequential Transition of players moving to their home cities.
     */
    public SequentialTransition movePlayersHome() {
        
        /* Construct a sequential animation of players moving to their
         * respective home cities. */
        SequentialTransition moveHomeAnimation = new SequentialTransition();        
        moveHomeAnimation.setAutoReverse(false);
        for (Player p : players) {
            
            /* Initialize the players' position */
            ui.getGamePlayScreen().getGameBoard().initPlayerPositions();
            
            /* Add the transition of each player moving home. */
            moveHomeAnimation.getChildren().add(p.moveToCity(p.getHomeCity()));
            moveHomeAnimation.getChildren().add(new PauseTransition(Duration.millis(250)));
                        
        }
        
        moveHomeAnimation.setOnFinished(e->{
            ui.unfreeze();
            nextTurn();        
        });
        
        /* Return the animation. */
        return moveHomeAnimation;
        
    }

    /**
     * Public getter method for the current player of the game. If the current
     * game or current player is null, null is returned.
     * 
     * @return
     *          The current player of the game.
     */
    public Player getCurrentPlayer() {
       
        if(currentGame == null) 
            return null;
        else
            return currentGame.getCurrentPlayer();
                
    }
    
    /**
     * Public getter method for the next player of the game. If the current
     * game or current player is null, null is returned. The current games
     * turn counter is incremented as a result of this method call.
     * 
     * @return
     *          The next player of the game.
     */   
    public Player getNextPlayer() {
        
        if(currentGame == null) 
            return null;
        else if (currentGame.currentPlayer == null){
            
            currentGame.currentPlayer = currentGame.players.get(0);
            ui.getGamePlayScreen().setCurrentPlayer(currentGame.currentPlayer);
            return currentGame.currentPlayer;
            
        } else {
        
            Player c = currentGame.getCurrentPlayer();
            Player p = currentGame.getNextPlayer();
            if(c != p) {
                
                ui.getGamePlayScreen().setCurrentPlayer(p);
                ui.getGamePlayScreen().disableRollButton(false);
                
            }

            return p;
            
        }
    }

    public void playTurn(Player player, CityNode city) {
        
        /* If the current game is null, do nothing. */
        if(currentGame == null || player == null) return;
        
        /* If the die needs to be rolled, do nothing. */
        if(!player.isAtPort() && !currentGame.getDieRolled()) {
            
            MessageBox.showDialog("Must first roll die.");
            return;
            
        }
        
        /* Assume the player cannot move until a match is found. */
        boolean canMove = false;
        
        /* Check to see if the player can move to the indicated city. */
        for(Edge e : player.getCurrentCity().getEdges()) {
            
            /* If a match is found, the player can move to the city. */
            if(e.getNeighbor() == city) {
                
                /* Player can take the ship. */
                if(e.getEdgeType() == EdgeType.SEA && player.isAtPort()) {
                    
                    canMove = true;
                    player.setAtPort(false);
                    player.addPoints(-player.getPoints()); 
                    
                } 
                /* Player cannot take ship. */
                else if(e.getEdgeType() == EdgeType.SEA){
                    
                    MessageBox.showDialog("Cannot move accross port!");
                    
                } 
                /* Player must take ship. */
                else if(player.isAtPort()){
                    
                    MessageBox.showDialog("Must select a port city!");
                    
                } 
                /* Player can move to city. */
                else {
                    
                    canMove = true;
                    
                }
                
                break;
                
            }
            
        }
        
        /* Move the player to the city. */
        if(canMove) {
            
            currentGame.addTurn(player, player.getCurrentCity(), city, "Move to City.");
            /* Get and play the motion animation. */
            ParallelTransition st = player.moveToCity(city);    
                      
            /* Check to see if the player is at a destination. */
            boolean atDestination = false;
            for(CityNode c : player.getHand()) {
                
                if(c == city) {
      
                    /* Play the card. */
                    if(city == player.getHomeCity()) {
                        
                        /* Player has won. */
                        if(player.getHand().size() == 1) {
                            
                            st.getChildren().add(player.getCardTray().playCard(city));
                            atDestination = true;
                            currentGame.endGame(player);
                            
                        } else {
                            
                            MessageBox.showDialog("You must visit all cities before returning home.");
                            
                        }
                        
                    } else {
                    
                        /* Set points to zero. */
                        st.getChildren().add(player.getCardTray().playCard(city));
                        player.addPoints(-player.getPoints());                        
                        player.getCardTray().playCard(city);
                        atDestination = true;
                        
                    }
                    
                    break;
                    
                }
                
            }
            
            if(atDestination) {
                ui.getGamePlayScreen().updateStatusMessage("Status: " 
                  + player.getName() + " is at destination " + city.getName());
            } else {
                ui.getGamePlayScreen().updateStatusMessage("Status: Moving " 
                  + player.getName() + " to city " + city.getName());                
            }
            st.setOnFinished(e->{
            
                player.alignToCity(city);
                nextTurn();
                ui.unfreeze();
            
            });
            
            ui.freeze();
            st.play();
            
            
        }else{
            
            ui.getGamePlayScreen().updateStatusMessage("Status: Cannot move " 
              + player.getName() + " to city " + city.getName());
            
        }
            
    }

    /**
     * Rolls the die for the current player.
     */
    public SequentialTransition rollDice(Player player) {
        
        /* If the die has already been rolled, do nothing. */
        if(currentGame.getDieRolled()) return null;
        
        /* Set the die rolled flag to true. */
        currentGame.setDieRolled(true);
        ui.getGamePlayScreen().disableRollButton(true);
        
        /* If the player is not null, add the points from the die roll. */ 
        return ui.getGamePlayScreen().rollDice(player, currentGame);
        
    }

    public void waitAtPort() {
        
        if (currentGame == null) return;
        Player p = currentGame.getCurrentPlayer();
        if(p == null) return;
        currentGame.addTurn(p, p.getCurrentCity(), null, "Waiting for ferry.");
        
    }

    private void playTurnAI(Player player) {
        
        ArrayList<CityNode> path = player.getShortestPath();
        
        if(player.isAtPort()) {
            
            SequentialTransition st = new SequentialTransition();
            st.setAutoReverse(false);
            CityNode origin = player.getCurrentCity();
            CityNode dest = path.remove(0);
            ParallelTransition pt = player.moveToCity(dest);
            st.getChildren().add(pt);
            player.setAtPort(false);
            player.addPoints(-player.getPoints());
            boolean atDestination = false;
            for(CityNode c : player.getHand()) {

                if(c == dest) {

                    /* Play the card. */
                    if(dest == player.getHomeCity()) {

                        /* Player has won. */
                        if(player.getHand().size() == 1) {

                            st.getChildren().add(player.getCardTray().playCard(dest));
                            atDestination = true;
                            st.setOnFinished(e->{

                                currentGame.endGame(player);

                            });
                            st.play();
                            return;

                        }

                    } else {

                        /* Set points to zero. */
                        st.getChildren().add(player.getCardTray().playCard(dest));
                        atDestination = true;

                    }

                    break;

                }

            }

            if(atDestination) {
                ui.getGamePlayScreen().updateStatusMessage("Status: " 
                  + player.getName() + " is at destination " + dest.getName());
            } else {
                ui.getGamePlayScreen().updateStatusMessage("Status: Moving " 
                  + player.getName() + " to city " + dest.getName());                
            }

            st.setOnFinished(e->{

                player.alignToCity(dest);
                nextTurn();
                ui.unfreeze();

            });
            currentGame.addTurn(player, origin, player.getCurrentCity(), "Move to City.");

            ui.freeze();
            st.play();
            
        } else {
            
            SequentialTransition st = new SequentialTransition();
            st.setAutoReverse(false);
            
            if(!currentGame.getDieRolled()) {
                SequentialTransition dieRollAnimation = rollDice(player);
                if(dieRollAnimation == null) return;
                st.getChildren().add(dieRollAnimation);
            }
            
            final CityNode origin = player.getCurrentCity();
            final CityNode dest = (path.isEmpty()) ? null : path.get(0);
            
            if(player.getPoints() > 0) {
                
                if(origin == null || dest == null) 
                    return;
                
                Edge edge = origin.getEdge(dest);
                if(edge == null)
                    return;
                
                if(edge.getEdgeType() == EdgeType.SEA) {
                    
                    player.setAtPort(true);
                    player.addPoints(-player.getPoints());
                    waitAtPort();
                                    
                    st.setOnFinished(e->{

                        nextTurn();
                        ui.unfreeze();
                        
                    });
                
                } else {
                    
                    CityNode city = path.remove(0);
                    st.getChildren().add(player.moveToCity(city));
                    currentGame.addTurn(player, origin, player.getCurrentCity(), "Move to City.");
                    boolean atDestination = false;
                    for(CityNode c : player.getHand()) {

                        if(c == city) {

                            /* Play the card. */
                            if(city == player.getHomeCity()) {

                                /* Player has won. */
                                if(player.getHand().size() == 1) {

                                    st.getChildren().add(player.getCardTray().playCard(city));
                                    atDestination = true;
                                    st.setOnFinished(e->{
                                    
                                        currentGame.endGame(player);
                                    
                                    });
                                    st.play();
                                    return;

                                }

                            } else {

                                /* Set points to zero. */
                                player.addPoints(-player.getPoints());
                                st.getChildren().add(player.getCardTray().playCard(city));
                                atDestination = true;

                            }

                            break;

                        }

                    }

                    if(atDestination) {
                        ui.getGamePlayScreen().updateStatusMessage("Status: " 
                          + player.getName() + " is at destination " + city.getName());
                    } else {
                        ui.getGamePlayScreen().updateStatusMessage("Status: Moving " 
                          + player.getName() + " to city " + city.getName());                
                    }
                
                    st.setOnFinished(e->{

                        player.alignToCity(dest);
                        nextTurn();
                        ui.unfreeze();

                    });

                }
                
            }        
            
            ui.freeze();
            st.play();
        }
        
    }

    public void nextTurn() {

        Player p = ui.getGameManager().getCurrentPlayer();
        if(p != null)
            System.out.println("Current Player: " + p.getName() + " - " + p.getPoints() + " points");
        /* Get the next player, if necessary. */
        Player player = getNextPlayer();
        System.out.println("New Player: " + player.getName() + " - " + player.getPoints() + " points");

        if(player.getPlayerType() == PlayerType.COMPUTER) {
            
            playTurnAI(player);

        } else{

            if(player.isAtPort()) {

                MessageBox.showDialog("Please select a port city to move to.");

            }
      
            boolean portCity = false;
            for(Edge e: player.getCurrentCity().getEdges()) {
                
                if(e.getEdgeType() == Edge.EdgeType.SEA) {

                    portCity = true;
                    break;

                }

            }
            
            ui.getGamePlayScreen().disablePortButton(!portCity);
            if(player.getCurrentCity() != null)
                ui.getGamePlayScreen().disableFlightButton(!player.getCurrentCity().isFlightCity());
            ui.getGamePlayScreen().disableRollButton(false);
            /* Update the messages. */
            ui.getGamePlayScreen().updateMoveMessage(player.getPoints());
    
        }
        
    }

    public Object getCurrentGame() {
        return currentGame;
    }

    public void flyToCity(CityNode city) {
        
        /* Implement. */  
        Player player = currentGame.getCurrentPlayer();
        int points = player.getPoints();
     
        int section = player.getCurrentCity().getSection();
        int destSection = city.getSection();
        int cost = 0;

        switch(section) {

            case 1:
                if(destSection == 1)
                    cost = 2;
                else if (destSection == 2 || destSection == 4)
                    cost = 4;
                break;
            case 2:
                if(destSection == 2)
                    cost = 2;
                else if (destSection == 1 || destSection == 3)
                    cost = 4;
                break;
            case 3:
                if(destSection == 3)
                    cost = 2;
                else if (destSection == 2 || destSection == 4 || destSection == 6)
                    cost = 4;
                break;
            case 4:
                if(destSection == 4)
                    cost = 2;
                else if (destSection == 1 || destSection == 5 || destSection == 3)
                    cost = 4;
                break;
            case 5:
                if(destSection == 5)
                    cost = 2;
                else if (destSection == 4 || destSection == 6)
                    cost = 4;
                break;
            case 6:
                if(destSection == 6)
                    cost = 2;
                else if (destSection == 3 || destSection == 5)
                    cost = 4;
                break;

        }
        
        if(city != player.getCurrentCity() && points >= cost && player.getCurrentCity().isFlightCity()) {
            
            player.addPoints(-(cost - 1));
            SequentialTransition st = new SequentialTransition();
            st.setAutoReverse(false);
            CityNode origin = player.getCurrentCity();
            ParallelTransition pt = player.moveToCity(city);
            st.getChildren().add(pt);
            player.setAtPort(false);
            player.addPoints(-player.getPoints());
            boolean atDestination = false;
            for(CityNode c : player.getHand()) {

                if(c == city) {

                    /* Play the card. */
                    if(city == player.getHomeCity()) {

                        /* Player has won. */
                        if(player.getHand().size() == 1) {

                            st.getChildren().add(player.getCardTray().playCard(city));
                            atDestination = true;
                            st.setOnFinished(e->{

                                currentGame.endGame(player);

                            });
                            st.play();
                            return;

                        }

                    } else {

                        /* Set points to zero. */
                        st.getChildren().add(player.getCardTray().playCard(city));
                        atDestination = true;

                    }

                    break;

                }

            }

            if(atDestination) {
                ui.getGamePlayScreen().updateStatusMessage("Status: " 
                  + player.getName() + " is at destination " + city.getName());
            } else {
                ui.getGamePlayScreen().updateStatusMessage("Status: Moving " 
                  + player.getName() + " to city " + city.getName());                
            }

            st.setOnFinished(e->{

                player.alignToCity(city);
                nextTurn();
                ui.unfreeze();
                ui.getEventManager().resetFlightStage();

            });
            currentGame.addTurn(player, origin, player.getCurrentCity(), "Fly to City.");

            ui.freeze();
            st.play();
            
            
        } else {
            
            MessageBox.showDialog("Cannot fly from here.");
            
        }
        
    }

    public HashMap<String, CityNode> getCities() {
        
        return cities;
    
    }

    public void saveGame() {
    
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String loadName = props.getProperty(GameProperties.LOAD_FILE_NAME);
        File saveFile = new File("data/" + loadName);
        try {
            FileOutputStream fos = new FileOutputStream(saveFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            
            oos.writeObject(currentGame);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }      
    
    }
    
    public void loadGame() {
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String historyName = props.getProperty(GameProperties.LOAD_FILE_NAME);
        File loadFile = new File("data/" + historyName);
        if (!loadFile.exists()) {
            System.out.println("File Not Found.");
            return;
        }
        try {
            FileInputStream fis = new FileInputStream(loadFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            
            currentGame = (GameData) ois.readObject();
            if (currentGame == null) {
                System.out.println("Error Occurred.");
            }
            currentGame.setUI(ui);
            cities = currentGame.getCities();
            players = currentGame.getPlayers();
            ui.getGamePlayScreen().setCities(cities);
            ui.getGamePlayScreen().setPlayers(players);
            ui.getGamePlayScreen().setCurrentPlayer(currentGame.currentPlayer);
            ui.getEventManager().respondToSwitchScreen(JourneyUI.GameScreen.SPLASH, JourneyUI.GameScreen.GAME_PLAY);
            nextTurn();
            
        } catch (Exception e) {
            
            System.out.println(e.getMessage());
            
        }
        
    }
   
    /**
     * Enumeration listing the possible states of the game.
     */
    public enum GameState {
        
        NOT_STARTED, PLAYING, OVER;
        
    }
    
    /**
     * Constructor which initializes the JourneyUI for this GameManager
     * instance.
     * 
     * @param ui 
     *          JourneyUI for this particular GameManager instance.
     */
    public GameManager(JourneyUI ui) {
        
        this.ui = ui;
        
    }
    
    
    /**
     * Creates a new game using the players currently playing and the cards
     * constructed using the data currently on file.
     */
    public void setupNewGame() {
        
        /* If there are no players, don't begin new game */
        if(players == null) {
            
            ui.getErrorManager().respondToNoPlayers();
            return;
            
        }
        
        /* 
         * A game was played previously and has NOT been ended. End the game
         * with no winner. If there was a winning player, it would have already
         *  been ended and added to the game history. 
         */
        if(currentGame != null && currentGame.state != GameState.OVER) {
                
            currentGame.endGame(null);
            gameHistory.add(currentGame);
            
        }
        
        /* Construct the deck from the data on file */
        cities = FileLoader.loadMap();
        currentGame = new GameData(ui, cities, players);
        ui.getGameHistoryScreen().setGameData(currentGame);
        
        ui.getGamePlayScreen().setCities(cities);
        ui.getGamePlayScreen().setPlayers(players);
        ui.getGamePlayScreen().setCurrentPlayer(currentGame.currentPlayer);
        
    }
    
    /**
     * Begins the game of Journey. This step deals the cards to each player 
     * (animating the dealing process) and then moves each of the players to
     * their home city.
     * 
     * @return 
     *          The sequential animation of dealing cards.
     */
    public SequentialTransition startNewGame() {
        
        if (currentGame == null)
            return null;
        
        return currentGame.startGame();
        
    }
    
    /**
     * Sets the players of the game to a new collection.
     * 
     * @param players 
     *          ArrayList of players to play the game.
     */
    public void setPlayers(ArrayList<Player> players) {
        
        this.players = players;
        
    }
    
    /**
     * POblic getter method for the complete history of games for the JourneyUI.
     * 
     * @return
     *          The list of games that have been played.
     */
    public ArrayList<GameData> getGameHistory() {
        
        return gameHistory;
        
    }
    
}
