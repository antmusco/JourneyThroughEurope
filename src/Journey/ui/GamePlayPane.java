package Journey.ui;

import Journey.game.CityNode;
import Journey.game.Edge;
import Journey.game.GameBoard;
import Journey.game.GameData;
import Journey.game.GameProperties;
import Journey.game.Player;
import Journey.manager.EventManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import properties_manager.PropertiesManager;

/**
 * Class which depicts the gameplay screen for the JourneyUI.
 * 
 * @author 
 *          Anthony G. Musco
 * <dt><b>Date Created</b><dd>
 *          Nov-08-2014
 * <dt><b>Class</b><dd>
 *          CSE 219
 */
public class GamePlayPane extends BorderPane implements JourneyPane {
    
    /**
     * JourneyUI instance to which this GameSetupPane is associated.
     */
    JourneyUI ui;
    
    /**
     * Pane which contains the Game Board in the center of the screen.
     */
    Pane centerBox;
    
    /**
     * Pane which contains the contents of the top section of this BorderPane.
     */
    HBox topBox;
    
    /**
     * Pane which contains the contents of the bottom section of this BorderPane.
     */
    HBox bottomBox;
    
    /**
     * Pane which contains the contents of the left section of this BorderPane.
     */
    VBox leftBox;
    
    /**
     * Pane which contains the contents of the right section of this BorderPane.
     */
    VBox rightBox;
    
    /**
     * Collection of PlayerCardsPanes.
     */
    StackPane playerCardTrays;
    
    /**
     * Event manager for the Journey game.
     */
    EventManager eventManager;
    
    /**
     * Game board for the current game.
     */
    GameBoard gameBoard;    
    
    /**
     * Label indicating the name of the current player.
     */
    Label currentPlayerLabel;
    
    /**
     * ImageView indicating the current player.
     */
    ImageView currentPlayerImage;
    
    /**
     * StackPane which contains the dieValues imageViews.
     */
    StackPane dieBox;
    
    /**
     * Collection of the die images.
     */
    ImageView[] dieValues;
    
    /**
     * Message to the player.
     */
    Label moveMessageLabel;
    
    /**
     * Label which indicates the status of the game.
     */
    Label statusMessageLabel;
    
    /**
     * Flight plan for the game.
     */
    FlightPlanPane flightPlan;
    
    Image[] musicSelector;
    
    ImageView musicToggle;
    
    MediaPlayer songTrack;
    
    Button rollButton;
    Button portButton;
    Button flightButton;
    
    boolean muted;
    
    /**
     * Constructor which initializes the JouneyUI instance for this
     * GamePlayPane.
     * 
     * @param ui 
     *          JourneyUI instance for this GamePlayPane.
     */
    public GamePlayPane(JourneyUI ui) {
        
        this.ui = ui;
        eventManager = ui.getEventManager();
        initBackground();
        initComponents();
        setComponentBehaviors();
        setComponentLayouts();
        
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
    
    /**
     * Initializes the game board with the game map and adds it to the pane. 
     */
    @Override
    final public void initComponents() {
        
        double buttonWidth = 150;
        
        
        /* Get the game map image. */
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(GameProperties.IMG_PATH);
        String mapPath = imgPath + props.getProperty(GameProperties.MAP_GLOBAL_IMAGE);        

        /* Create the game board. */
        gameBoard = new GameBoard(new Image(mapPath), this);
        
        /* Center Pane. */
        centerBox = new Pane();
        centerBox.getChildren().add(gameBoard);
        setCenter(centerBox);
        
        /* Top Pane. */
        topBox = new HBox();
        Label journeyTitleLabel = new Label("Journey Through Europe");
        imgPath = props.getProperty(GameProperties.IMG_PATH);
        String musicOnPath =  imgPath + props.getProperty(GameProperties.MUSIC_ON_IMAGE);
        String musicOffPath = imgPath + props.getProperty(GameProperties.MUSIC_OFF_IMAGE);
        musicSelector = new Image[2];
        musicSelector[0] = new Image(musicOffPath);
        musicSelector[1] = new Image(musicOnPath);
        musicToggle = new ImageView(musicSelector[1]);
        musicToggle.setFitHeight(60);
        musicToggle.setFitWidth(60);
        musicToggle.setOnMouseClicked(e->{
            toggleMusic();
        });
        topBox.getChildren().addAll(journeyTitleLabel, musicToggle);
        setTop(topBox);
        
        /* Right Pane. */
        rightBox = new VBox();
        rightBox.setSpacing(5);
        dieBox = new StackPane();
        dieValues = new ImageView[6];
        for(int i = 0; i < 6; i++) {
            dieValues[i] = new ImageView(new Image("file:./images/die_" + (i + 1) + ".jpg"));
            dieBox.getChildren().add(dieValues[i]);
        }
        rollButton = new Button("Roll!");
        rollButton.setPrefWidth(buttonWidth);
        rollButton.setOnAction(e->{
            eventManager.respondToRoll();
        });
        portButton = new Button("Take Ferry");
        portButton.setPrefWidth(buttonWidth);
        portButton.setOnAction(e->{
            eventManager.respondToFerry();
        });
        flightButton = new Button("Take Plane");
        flightButton.setPrefWidth(buttonWidth);
        flightButton.setOnAction(e->{
            eventManager.respondToFly(flightPlan);
        });
        Button gameHistoryButton = new Button("Game History");
        gameHistoryButton.setPrefWidth(buttonWidth);
        gameHistoryButton.setOnAction(e->{
            eventManager.respondToSwitchScreen(JourneyUI.GameScreen.GAME_PLAY, 
            JourneyUI.GameScreen.GAME_HISTORY);
        });
        Button aboutButton = new Button("About");
        aboutButton.setPrefWidth(buttonWidth);
        aboutButton.setOnAction(e->{
            ui.getEventManager().respondToSwitchScreen(JourneyUI.GameScreen.GAME_PLAY, JourneyUI.GameScreen.HELP);
        });
        Button findPathButton = new Button("Find Path");
        findPathButton.setPrefWidth(buttonWidth);
        findPathButton.setOnAction(e->{
            ui.getEventManager().respondToFindPath(ui.getGameManager().getCurrentPlayer());
        });
        Button saveGameButton = new Button("Save Game");
        saveGameButton.setPrefWidth(buttonWidth);
        saveGameButton.setOnAction(e->{
            ui.getEventManager().respondToSaveGame();
        });
        rightBox.setPrefWidth(200);
        moveMessageLabel = new Label();
        moveMessageLabel.setStyle("-fx-font-size: 14; -fx-font-family: Arial;");
        rightBox.getChildren().addAll(moveMessageLabel,dieBox, rollButton, aboutButton, portButton, flightButton, gameHistoryButton, saveGameButton, findPathButton);
        setRight(rightBox);
        
        /* Left Pane. */
        leftBox = new VBox();
        HBox playerHBox = new HBox();
        leftBox.setSpacing(5);
        leftBox.setPrefWidth(PlayerCardsTray.PREF_WIDTH + 20);
        currentPlayerLabel = new Label();
        currentPlayerLabel.setStyle("-fx-font-size:14;");
        currentPlayerImage = new ImageView();
        currentPlayerImage.setFitHeight(75);
        currentPlayerImage.setFitWidth(75);
        playerHBox.getChildren().addAll(currentPlayerLabel, currentPlayerImage);
        playerCardTrays = new StackPane();
        BackgroundFill fill = new BackgroundFill(Color.WHEAT, CornerRadii.EMPTY, Insets.EMPTY);
        playerCardTrays.setBackground(new Background(fill));
        playerCardTrays.prefWidthProperty().bind(leftBox.widthProperty());
        leftBox.getChildren().addAll(playerHBox, playerCardTrays);
        setLeft(leftBox);
        
        /* Bottom Pane. */
        bottomBox = new HBox();
        statusMessageLabel = new Label("Status: Game Setup.");
        bottomBox.getChildren().add(statusMessageLabel);
        setBottom(bottomBox);
        
        /* Start game music. 
        muted = false;
        String audioPath = props.getProperty(GameProperties.AUDIO_PATH);
        String themePath = props.getProperty(GameProperties.THEME_MUSIC_FILE);
        Media media = new Media("file:./audio/theme_music.mp3");
        songTrack = new MediaPlayer(media);
        songTrack.setAutoPlay(true);
        songTrack.play();
        */
    }

    @Override
    final public void setComponentBehaviors() {
        
        /* No behaviors as of yet. */
        
    }

    @Override
    final public void setComponentLayouts() {
        
        BackgroundFill fill = new BackgroundFill(Color.rgb(253, 193, 37), 
                CornerRadii.EMPTY, Insets.EMPTY);
        
        /* Top Container */
        topBox.setAlignment(Pos.CENTER);
        topBox.setBackground(new Background(fill));
        
        /* Right Container */
        rightBox.setAlignment(Pos.CENTER);
        rightBox.setBackground(new Background(fill));
        
        /* Left Container */
        leftBox.setAlignment(Pos.CENTER);
        leftBox.setBackground(new Background(fill));
        
        /* Bottom Container */
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setBackground(new Background(fill));
        
    }
    
    /**
     * Sets the players of this GamePlayPane as well as the game board.
     * 
     * @param players 
     *          Players to add to the pane and the game board.
     */
    public void setPlayers(ArrayList<Player> players) {
        
        /* Loop through players. */
        for (Player p : players) {
            
            getChildren().add(p);   /* Add to this pane. */
            gameBoard.addPlayer(p); /* Add to the game board. */
            playerCardTrays.getChildren().add(p.getCardTray()); /* Add card tray to this pane */
            p.setEventManager(eventManager);    
            
        }
        
    }
    
    /**
     * Sets the cities of this GamePlayPane as well as the game board.
     * 
     * @param cities 
     *          Cities to add to the pane and the game board.
     */
    public void setCities(HashMap<String, CityNode> cities) {
        
        /* Loop through CityNodes */
        for (Map.Entry<String, CityNode> entry : cities.entrySet()) {
            
            CityNode c = entry.getValue();
            getChildren().add(c); /* Add to this pane. */
            gameBoard.addCity(c); /* Add to the game board. */
            c.setEventManager(eventManager);
            
            /* Add all edges to this pane. */
            for(Edge e : c.getEdges())
                getChildren().add(e);
            
        }
        
        /* Add the indicator to this pane. */
        getChildren().add(CityNode.indicator);
        buildFlightPath();
        
    }

    /**
     * Public getter method for the game board in this game play pane.
     * 
     * @return 
     *          A reference to the game board in this game play pane.
     */
    public GameBoard getMap() {
        
        return gameBoard;
        
    }

    /**
     * Zooms out the map maximally within the pane.
     */
    public void maxZoomOut() {
        
        gameBoard.zoomSource(-1000);
        
    }

    public void drawPanes() {
        
        if (topBox != null) {
            
            setTop(null);
            topBox.toFront();
            setTop(topBox);
            
        }
    
        if(rightBox != null) {
            
            setRight(null);
            rightBox.toFront();
            setRight(rightBox);
            
        }
        
        if(leftBox != null) {
            
            setLeft(null);
            leftBox.toFront();
            setLeft(leftBox);
            
        }
        
        if(bottomBox != null) {
        
            setBottom(null);
            bottomBox.toFront();
            setBottom(bottomBox);
        
        }
        
    }

    
    public double getCenterWidth() {
        
        if(centerBox == null)
            return 0;
        else
            return centerBox.widthProperty().doubleValue();
        
    }
    
    public double getCenterHeight() {
        
        if(centerBox == null)
            return 0;
        else
            return centerBox.heightProperty().doubleValue();
        
    }
    
    public double getLeftWidth() {
        
        if(leftBox == null)
            return 0;
        else
            return leftBox.widthProperty().doubleValue();
    
    }
    
    public double getRightWidth() {
        
        if(rightBox == null)
            return 0;
        else
            return rightBox.widthProperty().doubleValue();
        
    }

    public double getTopHeight() {
        
        if(topBox == null)
            return 0;
        else
            return topBox.heightProperty().doubleValue();
    
    }
    
    public double getBottomHeight() {
        
        if(bottomBox == null) 
            return 0;
        else
            return bottomBox.heightProperty().doubleValue();
        
    }

    void bindProperties(Scene scene) {
        
        prefWidthProperty().bind(scene.widthProperty());
        prefHeightProperty().bind(scene.heightProperty());       
        gameBoard.widthProperty().bind(scene.widthProperty());
        gameBoard.heightProperty().bind(scene.heightProperty());
        topBox.prefWidthProperty().bind(scene.widthProperty());
        bottomBox.prefWidthProperty().bind(scene.widthProperty());
        leftBox.prefHeightProperty().bind(scene.heightProperty());
        rightBox.prefHeightProperty().bind(scene.heightProperty());
        
    }

    public void addPlayerCardPane(PlayerCardsTray pcp) {

        playerCardTrays.getChildren().add(pcp);
        
    }

    public GameBoard getGameBoard() {
    
        return gameBoard;
    
    }

    /**
     * Rolls the die on the screen to determine the number of cities a player
     * can move.
     * 
     * @return 
     *          The value of the resulting die roll.
     */
    public SequentialTransition rollDice(Player p, GameData d) {
        
        if (p == null) return null;
        
        /* Clear the message label. */
        moveMessageLabel.setText("");

        /* Declare and initialize the roll value. */
        int rollValue = 0;
        
        /* Create the sequential animation that will simulate a die roll. */
        SequentialTransition rollAnimation = new SequentialTransition();
        
        /* Add a series of fade transitions to simulate the roll. */
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < dieValues.length; j++) {
                rollValue = (int)(Math.random()*6);
                final ImageView val = dieValues[rollValue];
                FadeTransition fade = new FadeTransition(Duration.millis(100), val);
                fade.setAutoReverse(false);
                fade.setFromValue(1);
                fade.setToValue(1);
                fade.setOnFinished(e->{val.toFront();});
                rollAnimation.getChildren().add(fade);
            }    
        }
        
        /* Get the resultof the roll. */
        final int rollResult = rollValue + 1;
        rollAnimation.setOnFinished(e->{
            updateMoveMessage(rollResult);
        });

        d.addTurn(p, null, null, "Rolled a " + rollResult);
        p.addPoints(rollResult);
        
        /* Return the roll result. */
        return rollAnimation;
        
    }

    public void updateMoveMessage(int rollValue) {
        
        String text;
        
        if(rollValue == 0) {
            
            text = ui.getGameManager().getCurrentPlayer().getName() +
                " must roll. ";
            
        } else {
            
            text = ui.getGameManager().getCurrentPlayer().getName() 
              + " move " + rollValue;
            if(rollValue == 1) text += " city."; else text += " cities.";
        
        }
        
        moveMessageLabel.setText(text);
    
    }
    
    public void updateStatusMessage(String message) {
        
        statusMessageLabel.setText(message);
        
    }
    
    public void setCurrentPlayer(Player player) {
        
        if (player == null) return;
        
        currentPlayerImage.setImage(player.getImage());
        currentPlayerLabel.setText(player.getName());
        player.getCardTray().toFront();
        
        
    }

    private void toggleMusic() {
        
        if(muted) {
            
            musicToggle.setImage(musicSelector[1]);
            //songTrack.play();
            muted = false;
            
        } else {
            
            musicToggle.setImage(musicSelector[0]);
            //songTrack.pause();
            muted = true;
            
        }      
        
    }

    public void disableRollButton(boolean b) {
        
        rollButton.setDisable(b);
        
    }
    
    public void disablePortButton(boolean b) {
        
        portButton.setDisable(b);
        
    }

    public void disableFlightButton(boolean b) {
    
        flightButton.setDisable(b);
    
    }

    public void buildFlightPath() {
        
        if(flightPlan != null) return;
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(GameProperties.IMG_PATH);
        String flightPath = imgPath + props.getProperty(GameProperties.FLIGHT_MAP_FILE);
                
        Image flightImg = new Image(flightPath);
        flightPlan = new FlightPlanPane(ui.getGameManager(), flightImg);
        
    }
    
}
