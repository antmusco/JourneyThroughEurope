package Journey.ui;

import Journey.game.GameProperties;
import Journey.manager.EventManager;
import Journey.ui.JourneyUI.GameScreen;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import properties_manager.PropertiesManager;

/**
 * Class which manages the Splash Screen for the JourneyUI. 
 *
 * @author 
 *          Anthony G. Musco
 * <dt><b>Date Created</b><dd>
 *          Oct-26-2014
 * <dt><b>Class</b><dd>
 *          CSE 219
 */
public class SplashScreenPane extends StackPane implements JourneyPane {

    /**
     * JourneyUI to which this SplashScreenPane is assigned.
     */
    public JourneyUI ui;
    
    /**
     * Main control pane which contains all of the buttons for the Splash
     * Screen.
     */
    private VBox buttonBox;
    
    /**
     * Button which opens the game setup pane for a new game.
     */
    private Button newGameButton;
    
    /**
     * Button which opens the load game pane to resume a saved game.
     */
    private Button loadGameButton;
    
    /**
     * Button which displays the help pane to the user.
     */
    private Button helpButton;
    
    /**
     * Button which allows the user to exit the game.
     */
    private Button exitButton;
    
    /**
     * Constructor for the SpashScreenPane, which initializes the JourneyUI
     * instance in which this pane is situated. It also initializes all of the 
     * components of the SpashScreen.
     * 
     * @param ui 
     *          JourneyUI to which this pane is assigned.
     */
    public SplashScreenPane(JourneyUI ui) {
        
        this.ui = ui;
        initBackground();
        initComponents();
        setComponentBehaviors();
        setComponentLayouts();
        
    }

    @Override
    final public void initBackground() {
                
        /* Load splash screen image. */
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String data = props.getProperty(GameProperties.IMG_PATH);
        String path = props.getProperty(GameProperties.SPLASH_SCREEN_IMAGE);
        Image img = new Image(data + path);
        
        /* Create the background format specifiers */
        BackgroundSize bs = new BackgroundSize(getWidth(), getHeight(), false, 
            false, true, false);
        
        /* Construct the background */
        BackgroundFill[] fills = new BackgroundFill[1];
        fills[0] = new BackgroundFill(Color.rgb(253, 193, 37), CornerRadii.EMPTY, 
                Insets.EMPTY);
        BackgroundImage[] bkImgs = new BackgroundImage[1];
        bkImgs[0] = new BackgroundImage(img, 
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, 
            BackgroundPosition.CENTER, bs);
        
        /* Set the background of this pane */
        setBackground(new Background(fills, bkImgs));
        
    }

    @Override
    final public void initComponents() {
        
        /* Get the text for each button. */
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String newGameText = props.getProperty(GameProperties.NEW_GAME_TEXT);
        String loadGameText = props.getProperty(GameProperties.LOAD_GAME_TEXT);
        String helpText = props.getProperty(GameProperties.HELP_TEXT);
        String exitText = props.getProperty(GameProperties.EXIT_TEXT);
        
        /* Create each button */
        newGameButton = new Button(newGameText);
        loadGameButton = new Button(loadGameText);
        helpButton = new Button(helpText);
        exitButton = new Button(exitText);
        
        /* Add buttons to buttonBox; buttonBox to the StackPane */
        buttonBox = new VBox(newGameButton, loadGameButton, helpButton, exitButton);
        this.getChildren().add(buttonBox);
        
    }

    @Override
    final public void setComponentBehaviors() {
        
        EventManager eventManager = ui.getEventManager();
        
        /* New Game Button */
        newGameButton.setOnAction(e -> {
            eventManager.respondToSwitchScreen(GameScreen.SPLASH, GameScreen.GAME_SETUP);
        });
        
        /* Load Game Button */
        loadGameButton.setOnAction(e ->{
            ui.getGameManager().loadGame();
        });
        
        /* Help Button */
        helpButton.setOnAction(e -> {
            eventManager.respondToSwitchScreen(GameScreen.SPLASH, GameScreen.HELP);
        });
        
        /* Exit Button*/
        exitButton.setOnAction(e -> {
            eventManager.respondToExit();
        });
        
    }

    @Override
    final public void setComponentLayouts() {
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        double h = Double.parseDouble(props.getProperty(GameProperties.BUTTON_HEIGHT));
        double w = Double.parseDouble(props.getProperty(GameProperties.BUTTON_WIDTH));
        
        /* Spacing and Alignment. */
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        /* Button sizes. */
        newGameButton.setPrefSize(w, h);
        loadGameButton.setPrefSize(w, h);
        helpButton.setPrefSize(w, h);
        exitButton.setPrefSize(w, h);
        
    }
    
}
