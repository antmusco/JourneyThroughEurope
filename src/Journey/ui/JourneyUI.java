/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Journey.ui;

import Journey.game.GameBoard;
import Journey.manager.ErrorManager;
import Journey.manager.EventManager;
import Journey.manager.GameManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

/**
 * Primary user interface for the game application. All graphical interface
 * components are controlled/managed via the instance of JourneyUI for the
 * current game.
 * 
 * @author 
 *          Anthony G. Musco
 * <dt><b>Date Created</b><dd>
 *          Oct-22-2014
 * <dt><b>Class</b><dd>
 *          CSE 219
 */
public class JourneyUI extends StackPane {
    
    /**
     * Splash screen for the game.
     */
    private SplashScreenPane splashScreen;
    
    /**
     * Game History screen for the game.
     */
    private GameHistoryPane gameHistoryScreen;
    
    /**
     * Game Setup screen for the game.
     */
    private GameSetupPane gameSetupScreen;
    
    /**
     * Game play screen for the game.
     */
    private GamePlayPane gamePlayScreen;
    
    /**
     * About screen for the game.
     */
    private HelpPane helpScreen;
            
    /**
     * GameManager for this JourneyUI instance.
     */
    private GameManager gameManager;
    
    /**
     * EventManager for this JourneyUI instance.
     */
    private EventManager eventManager;
    
    /**
     * ErrorManager for this JourneyUI instance.
     */
    private ErrorManager errorManager;

    private boolean FREEZE_ANIMATION = true;
    
    public void freeze() {
    
        FREEZE_ANIMATION = true;
        
    }
    
    public void unfreeze() {
        
        FREEZE_ANIMATION = false;
        
    }

    public boolean getFreeze() {
    
        return FREEZE_ANIMATION;
    
    }

    /**
     * Enumeration describing the types of screens available to display.
     */
    public enum GameScreen {
        
        SPLASH, GAME_HISTORY, GAME_PLAY, GAME_SETUP, FLIGHT_PLAN, ABOUT, HELP
        
    }
 
    /**
     * Constructor which initializes the gameManager, eventManager, and
     * errorManager.
     */
    public JourneyUI() {
        
        /* Initialize the game manager, event manager, and error manager. */
        gameManager = new GameManager(this);
        eventManager = new EventManager(this);
        errorManager = new ErrorManager(this);
        
        /* Initialize the splash screen and game setup screen. */
        splashScreen = new SplashScreenPane(this);
        gameHistoryScreen = new GameHistoryPane(this);
        gameSetupScreen = new GameSetupPane(this);
        gamePlayScreen = new GamePlayPane(this);
        helpScreen = new HelpPane(this);
        
        getChildren().addAll(splashScreen, gameHistoryScreen, gameSetupScreen, 
          gamePlayScreen, helpScreen);
        initLayouts();        
        
        /* Set the splash screen visible. */
        eventManager.respondToSwitchScreen(GameScreen.HELP, GameScreen.SPLASH);
        
    }

    /**
     * Set the layouts of the splash screen, game setup screen, and game play
     * screen inside the JourneyUI.
     */
    private void initLayouts() {
        
        setAlignment(splashScreen, Pos.CENTER);
        setAlignment(gameHistoryScreen, Pos.CENTER);
        setAlignment(gameSetupScreen, Pos.CENTER);
        setAlignment(gamePlayScreen, Pos.CENTER);
        setAlignment(helpScreen, Pos.CENTER);
        
    }

    public void initControls() {
        
        Scene scene = this.getScene();
        GameBoard map = gamePlayScreen.getMap();
        gamePlayScreen.bindProperties(scene);
        
        scene.setOnKeyPressed(e->{
            if(!FREEZE_ANIMATION)
                map.handleKeyPress(e);
        });
        
        scene.widthProperty().addListener(e->{
            if(!FREEZE_ANIMATION)
            map.adjustZoom();
        });
        
        scene.heightProperty().addListener(e->{
            if(!FREEZE_ANIMATION)
            map.adjustZoom();
        });
        
                
        scene.setOnMouseDragged(e->{
            if(!FREEZE_ANIMATION)
            map.dragMap(e.getX(), e.getY());
         });
        
                
        scene.setOnMouseMoved(e->{
            if(!FREEZE_ANIMATION)
            map.getMouseCoordinates(e.getX(), e.getY());
        });
        
        scene.setOnMousePressed(e->{
            if(!FREEZE_ANIMATION)
            map.beginDrag(e.getX(), e.getY());
        });

        scene.setOnMouseReleased(e->{
            if(!FREEZE_ANIMATION)
            map.moveMap(e.getX() - map.dragStartX, e.getY() - map.dragStartY);
        });
        
        scene.setOnScroll(e->{
            if(!FREEZE_ANIMATION)
            map.zoomSource(e.getDeltaY());        
        });
        
    }
        
    /**
     * Public getter method for this JourneyUI's EventManager.
     * 
     * @return 
     *          The EventManager for this JourneyUI.
     */
    public EventManager getEventManager() {
        
        return eventManager;
        
    }
    
    /**
     * Public getter method for this JourneyUI's ErrorManager.
     * 
     * @return 
     *          The ErrorManager for this JourneyUI.
     */
    public ErrorManager getErrorManager() {
        
        return errorManager;
        
    }
    
    /**
     * Public getter method for this JourneyUI's GameManager.
     * 
     * @return 
     *          The GameManager for this JourneyUI.
     */
    public GameManager getGameManager() {
        
        return gameManager;
        
    }
  
    /**
     * Public getter method for the Splash Screen.
     * 
     * @return 
     *          The JourneyUI's splash screen.
     */
    public SplashScreenPane getSplashScreen() {
        
        return splashScreen;
        
    }
    
    /**
     * Public getter method for the Game History Screen.
     * 
     * @return 
     *          The JourneyUI's game history screen.
     */
    public GameHistoryPane getGameHistoryScreen() {
        
        return gameHistoryScreen;
        
    }   
    
    /**
     * Public getter method for the Game Setup Screen.
     * 
     * @return 
     *          The JourneyUI's game setup screen.
     */
    public GameSetupPane getGameSetupScreen() {
        
        return gameSetupScreen;
        
    }
    
    /**
     * Public getter method for the Game Play Screen.
     * 
     * @return 
     *          The JourneyUI's game play screen.
     */
    public GamePlayPane getGamePlayScreen() {
        
        return gamePlayScreen;
        
    }
    
    /**
     * Public getter method for the About Screen.
     * 
     * @return 
     *          The JourneyUI's about screen.
     */
    public HelpPane getHelpScreen() {
        
        return helpScreen;
        
    }
    
    
}
