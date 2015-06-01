package Journey.ui;

import Journey.game.GameProperties;
import Journey.game.Player;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import properties_manager.PropertiesManager;


/**
 * Class which manages the Game Setup Screen for the JourneyUI. 
 *
 * @author 
 *          Anthony G. Musco
 * <dt><b>Date Created</b><dd>
 *          Oct-26-2014
 * <dt><b>Class</b><dd>
 *          CSE 219
 */
public class GameSetupPane extends BorderPane implements JourneyPane {
    
    public static final int DEFAULT_PLAYER_NUM = 3;
    
    /**
     * JourneyUI instance to which this GameSetupPane is associated.
     */
    public JourneyUI ui;
    
    /**
     * Container for the controls in the top of this pane.
     */
    private FlowPane bottomPane;
    
    /**
     * Container for the controls in the bottom of this pane.
     */
    private FlowPane topPane;
    
    /**
     * ComboBox which sets the number of players for the game.
     */
    private ComboBox playerCountComboBox;
    
    /**
     * GridPane to contain the PlayerEditPanes.
     */
    private GridPane editPanesGrid;
    
    /**
     * Players being edited for the game.
     */
    private PlayerEditPane[] playerEditPanes;
    
    /**
     * Button for returning the player back to the splash screen.
     */
    private Button backButton;
    
    /**
     * Button which starts the game and displays the game board to the user.
     */
    private Button playButton;
    
    /**
     * Constructor which initializes the JourneyUI instance to which this
     * Game Setup Pane is associated.
     * 
     * @param ui 
     *          JourneyUI for this GameSetupPane.
     */
    public GameSetupPane(JourneyUI ui) {
        
        this.ui = ui;
        initBackground();
        initComponents();
        setComponentBehaviors();
        setComponentLayouts();
        
        /* Set default to 3 players. */
        playerCountComboBox.setValue(playerCountComboBox.getItems().get(2));
        updateNumberOfPlayers(DEFAULT_PLAYER_NUM);
        
    }

    @Override
    final public void initBackground() {

        /* Get property manager. */
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        /* Build the image. */
        String imgPath = props.getProperty(GameProperties.IMG_PATH);
        String bkImgPath = props.getProperty(GameProperties.BACKGROUND_IMAGE);
        Image img = new Image(imgPath + bkImgPath);
        
        /* Set the background. */
        BackgroundImage bkImg = new BackgroundImage(img, BackgroundRepeat.REPEAT, 
          BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        setBackground(new Background(bkImg));
        
    }
    
    @Override
    final public void initComponents() {
        
        /* Get property manager. */
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        /* Pane Title */
        Label selectPlayersLabel = new Label("Select the Travelers:");
        
        /* Player Count ComboBox */
        int maxPlayers = Integer.parseInt(
            props.getProperty(GameProperties.MAX_PLAYERS));
        playerCountComboBox = new ComboBox();
        ObservableList list = playerCountComboBox.getItems();
        for(int i = 1; i <= maxPlayers; i++)
            list.add(i);
        
        /* Back Button */
        String backText = props.getProperty(GameProperties.BACK_TEXT);
        backButton = new Button(backText);
        /* Play Button */
        String playText = props.getProperty(GameProperties.PLAY_TEXT);
        playButton = new Button(playText);
        
        /* Top Pane. */
        topPane = new FlowPane();
        topPane.getChildren().addAll(selectPlayersLabel, playerCountComboBox);
        
        /* Bottom Pane */
        bottomPane = new FlowPane();
        bottomPane.getChildren().addAll(backButton, playButton);
        
        setTop(topPane);
        setBottom(bottomPane);
        
    }

    @Override
    final public void setComponentLayouts() {
        
        /* Top Pane */
        topPane.setAlignment(Pos.CENTER);
        topPane.setPadding(new Insets(20));
        topPane.setHgap(30);
        
        /* Bottom Pane */
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.setPadding(new Insets(20));
        bottomPane.setHgap(30);
        
    }

    @Override
    final public void setComponentBehaviors() {
        
        playerCountComboBox.setOnAction(e -> {
        
            int numberOfPlayers = (Integer) playerCountComboBox.getValue();
            updateNumberOfPlayers(numberOfPlayers);
        
        });
        
        backButton.setOnAction(e -> {
        
            ui.getEventManager().respondToSwitchScreen(JourneyUI.GameScreen.GAME_SETUP, ui.getEventManager().previous);
        
        });
        
        playButton.setOnAction(e ->{
        
            ArrayList<Player> players = getPlayers();
            ui.getEventManager().respondToNewGame(players);
        
        });
        
    }

    /**
     * Sets the number of players for the game, and updates the number of
     * PlayerEditPanes in the Pane.
     * 
     * @param numberOfPlayers
     *          The number of players to play the game.
     */
    final public void updateNumberOfPlayers(int numberOfPlayers) {        
        
        /* Player Edit Panes*/
        playerEditPanes = new PlayerEditPane[numberOfPlayers];
        editPanesGrid = new GridPane();
        editPanesGrid.setAlignment(Pos.CENTER);
        Player.countOfPlayers = 0;
        for(int i = 0; i < numberOfPlayers; i++) {
            playerEditPanes[i] = new PlayerEditPane(ui);
            if (i < 3) 
                editPanesGrid.add(playerEditPanes[i], i, 0, 1, 1);
            else
                editPanesGrid.add(playerEditPanes[i], i - 3, 1, 1, 1);
        }
        
        setCenter(editPanesGrid);
        
    }
    
    /**
     * Loops through the current PlayerEditPanes on the Game Setup Pane and
     * constructs players based on the selections chosen by the user.
     * 
     * @return 
     *          An ArrayList of Players which will play the game.
     */
    private ArrayList<Player> getPlayers() {
        
        ArrayList<Player> players = new ArrayList();
        
        /* For each PlayerEditPane, construct player and add to list. */
        for(PlayerEditPane pep : playerEditPanes) {
            
            Player p = pep.getPlayer();
            Tooltip t = new Tooltip(p.getName());
            Tooltip.install(p, t);
            players.add(p);
            
        }
        
        return players;
        
    }
    
}
