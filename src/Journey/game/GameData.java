package Journey.game;

import static Journey.game.CityNode.CityColor;
import Journey.manager.GameManager.GameState;
import Journey.ui.JourneyUI;
import Journey.ui.MessageBox;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Duration;

/**
 * Data for the game currently being played. Contains references to all the
 * Players and CityNodes of the game, and contains a counter which tracks the
 * current turn of the game.
 * 
 * Once the game has ended, the GameData is added to the game history of the 
 * JourneyUI. This keeps track of the winning player, the total number of turns,
 * and the time the game took.
 * 
 * @author 
 *          Anthony G. Musco
 * <dt><b>Date Created</b><dd>
 *          Oct-22-2014
 * <dt><b>Class</b><dd>
 *          CSE 219
 */
public class GameData implements Serializable {

    /**
     * Total number of cards a player is to be dealt to begin the game.
     */
    public static final int HAND_SIZE = 9;
    
    /**
     * Number of iterations of the Fisher-Yates shuffle.
     */
    public static final int SHUFFLE_TIMES = 5;
    
    /**
     * Collection of players currently playing the game.
     */
    public ArrayList<Player> players;
    
    /**
     * Collection of CityNode cards for the game. When the game begins, the
     * deck is shuffled and distributed to the players.
     */
    public ArrayList<CityNode> deck;
    HashMap<String, CityNode> cities;
    /**
     * Counter which keeps track of the current turn of the game.
     */
    public int currentTurn;
    
    /**
     * Player which is currently playing.
     */
    public Player currentPlayer;
    
    /**
     * Current state of the game. Can be NOT_STARTED, PLAYING, or OVER.
     */
    public GameState state;
    
    /**
     * Flag indicating whether the current player must roll the die before
     * moving to a new city.
     */
    public boolean dieRolled;
    
    /**
     * Start and end times of the game.
     */
    public GregorianCalendar startTime, endTime;
    
    /**
     * Once the game ends, the winning player is stored in this variable.
     */
    public Player winner;
    
    /**
     * List of turns to record.
     */
    private ObservableList<Turn> turns;
    
    volatile private JourneyUI ui;
    
    /**
     * Default constructor for the GameData. This initializes all variables and
     * sets the game up to be played.
     * 
     * @param ui
     *          JourneyUI instance to which this GameData is attached.
     * @param map
     *          Collection of CityNodes to be displayed on the game board. 
     *          These must be constructed by the GameManager before the game
     *          may begin.
     * 
     * @param players
     *          Collection of Players which will play the game. These must be
     *          constructed and assigned colors by the GameSetupPane before
     *          the game may begin.
     */
    public GameData(JourneyUI ui, HashMap<String, CityNode> map, ArrayList<Player> players) {
        
        /* Initialize the Game State to not started. */
        state = GameState.NOT_STARTED;
        
        /* Construct the list of city nodes from the map. */
        this.cities = map;
        deck = new ArrayList<>();
        for (Map.Entry<String, CityNode> entry : map.entrySet()) {
            CityNode node = entry.getValue();
            node.setGameData(this);
            deck.add(node);
        }
        
        /* Add the list of turns for the game. */
        turns = FXCollections.observableArrayList();
        
        /* Set the ui. */
        this.ui = ui;
        
        /* Set the players. */
        this.players = players;
        
        currentPlayer = null;
        
        /* Initialize die rolled flag to false. */
        dieRolled = false;
        
    }
    
    /**
     * Starts a new game. Sets the startTime to the current time, resets the 
     * currentTurn to 0, and assigns the players a new hand of cards.
     * 
     * @return 
     *          A Sequential Transition representing the dealing of cards.
     */
    public SequentialTransition startGame() {
        
        /* Set the turn to 0. */
        currentTurn = 0;
        
        /* Set the start time of the game. */
        startTime = new GregorianCalendar();
        
        /* Update the game state to 'playing'. */
        state = GameState.PLAYING;
        
        /* Deal the cards and return the animation of the deal. */
        return dealCards();
        
    }

    /**
     * Clears every player's hand to an empty HashMap of CityNode cards, and 
     * deals new CityNode cards from the top of the deck. Each Player will 
     * receive HAND_SIZE number of cards.
     */
    private SequentialTransition dealCards() {

        /* Shuffle the deck. */
        shuffle();
        
        /* Construct the sequential transition of dealing the hand. */
        SequentialTransition st = new SequentialTransition();
        st.setAutoReverse(false);
        for(int i = 0; i < players.size(); i++) {
            
            /* Construct the sequential transition of dealing the cards. */
            SequentialTransition st2 = new SequentialTransition();
            for(int j = 0; j < HAND_SIZE; j++) {
                
                /* While the card is the incorrect color, cycle card to bottom. */
                while(!deck.get(0).getColor().equals(
                        CityColor[j % CityColor.length])) {
                    
                    deck.add(deck.remove(0));
                    
                }
                
                /* Deal the next card. */
                st2.getChildren().add(players.get(i).dealCity(deck.remove(0)));
                
                /* Put the card tray to the back of the stack pane. */
                players.get(i).getCardTray().toBack();
                
                /* Add a delay to the animation. */
                st2.setDelay(Player.animationSpeed);
                
            }
            
            /* After the animation completes, put the card tray in the back. */
            final Player p = players.get(i);
            final Player p2 = (i < (players.size() - 1)) ? players.get(i + 1) : players.get(0);
            st2.setOnFinished(e->{
                p.getCardTray().toBack();  
                ui.getGamePlayScreen().setCurrentPlayer(p2);
            });
            
            st.getChildren().add(st2);
            st.getChildren().add(new PauseTransition(Duration.millis(750)));
            
        } 
        
        /* Return the animation */
        return st;
    }    
    
    /**
     * Shuffles the deck to a random ordering.
     */
    private void shuffle() {

        int count = SHUFFLE_TIMES;
        
        while(count > 0 ) {
            
            for(int i = 0; i < deck.size(); i++) {

                /* For each card at i, swap with random card. */
                int j = (int) (Math.random() * deck.size());
                CityNode temp = deck.get(j);
                deck.set(j, deck.get(i));
                deck.set(i, temp);

            }
            count--;
            
        }
        
    }
    
    /**
     * If the current players points are less than or equal to 0, this method
     * sets the value of current player to the next player in line to play the 
     * game. Otherwise the player remains the same.
     * 
     * @return
     *          A reference to the next player in line to play the game.
     */
    public Player getNextPlayer() {
        
        /* Get next player if current turn is over. */
        if(currentPlayer.getPoints() <= 0) {
            currentPlayer = players.get(++currentTurn % players.size());
            
            dieRolled = false;
        }
        
        /*Skip players with negative points.*/
        while(currentPlayer.getPoints() < 0) {
            currentPlayer.addPoints(1); /* Increment points (skip turn). */
            currentPlayer = players.get(++currentTurn % players.size());
        }
            
        /* Return the next player. */
        currentPlayer.getCardTray().toFront();
        return currentPlayer;
        
    }

    /**
     * Sets the winner of this game to the indicated player and sets it state 
     * to OVER. If there is no winner, (winner == null), then the game ends
     * with no winner.
     * 
     * @param winner
     *          Player to be set as the winner of this game.
     */
    public void endGame(Player winner) {
        
        this.winner = winner;
        state = GameState.OVER;
        MessageBox.showDialog(winner.getName() + " wins!");
        
    }

    /**
     * Public getter method for the current player of the turn.
     * 
     * @return 
     *          A reference to the current player of the turn.
     */
    public Player getCurrentPlayer() {
    
        return currentPlayer;
    
    }
    
    /**
     * Public setter method for the boolean flag indicating whether the die
     * has been rolled or not.
     * 
     * @param rolled
     *          Boolean flag indicating whether the die has been rolled.
     */
    public void setDieRolled(boolean rolled) {
        
        dieRolled = rolled;
        
    }
    
    /**
     * Public getter method for the boolean flag indicating whether the die
     * has been rolled or not.
     * 
     * @return 
     *          Boolean flag indicating whether the die has been rolled.
     */
    public boolean getDieRolled() {
        
       return dieRolled;
        
    }
    
    public void addTurn(Player player, CityNode fromCity, CityNode toCity, 
      String description){
        
        String playerName = (player == null) ? "N/a" : player.getName();
        String fromCityName = (fromCity == null) ? "N/a" : fromCity.getName();
        String toCityName = (toCity == null) ? "N/a" : toCity.getName();
        
        turns.add(new Turn(currentTurn, playerName, fromCityName, toCityName,
          description));
        
    }

    public ObservableList getTurns() {
    
        return turns;
    
    }

    public HashMap<String, CityNode> getCities() {
    
        return cities;
    
    }

    public ArrayList<Player> getPlayers() {
    
        return players;
    
    }

    public void setUI(JourneyUI ui) {
    
        this.ui = ui;
    
    }
    
}
