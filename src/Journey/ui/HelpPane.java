package Journey.ui;

import Journey.game.GameProperties;
import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import properties_manager.PropertiesManager;

/**
 * Class which displays the information about the Journey Through Europe game.
 * 
 * @author 
 *          Anthony G. Musco
 * <dt><b>Date Created</b><dd>
 *          Nov-09-2014
 * <dt><b>Class</b><dd>
 *          CSE 219
 */
public class HelpPane extends BorderPane implements JourneyPane {

    /**
     * Instance of the JourneyUI for which this GameHistoryPane is to be
     * associated.
     */
    JourneyUI ui;
    
    /**
     * Game manager for the current instance of JourneyUI for managing the game
     * history.
     */
    private WebView aboutInfo;    
    
    /**
     * Label for the title of this pane.
     */
    Label aboutText;
         
    /**
     * Container for the controls in the top of this pane.
     */
    private FlowPane bottomPane;
    
    /**
     * Button for returning the player back to the splash screen.
     */
    private Button backButton;
    
    /**
     * Constructor which initializes the JourneyUI and the GameManager for this
     * Game History Manager.
     */
    public HelpPane(JourneyUI ui) {
        
        this.ui = ui;
        
        initBackground();
        initComponents();
        setComponentBehaviors();
        setComponentLayouts();
        
    }

    @Override
    final public void initBackground() {
        
        BackgroundFill fill = new BackgroundFill(Color.rgb(253, 193, 37), 
          CornerRadii.EMPTY, Insets.EMPTY);
        setBackground(new Background(fill));
       
    }

    @Override
    final public void initComponents() {
        
        /* Get the properties manager. */
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        /* Pane Label. */
        String help = props.getProperty(GameProperties.HELP_TEXT);
        aboutText = new Label(help);
        setTop(aboutText);

        /* Game History Table. */
        aboutInfo = new WebView();
        
        /* Back Button */
        String backText = props.getProperty(GameProperties.BACK_TEXT);
        backButton = new Button(backText);
            
        try {
            WebEngine we = aboutInfo.getEngine();
            String aboutFile = props.getProperty(GameProperties.ABOUT_GAME_FILE);
            File file = new File("./data/" + aboutFile);
            we.load(file.toURI().toURL().toString());
            setCenter(aboutInfo);
        } catch (MalformedURLException ex) {
            Logger.getLogger(HelpPane.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        bottomPane = new FlowPane(backButton);
        setBottom(bottomPane);
        
    }

    @Override
    final public void setComponentBehaviors() {
       
        backButton.setOnAction(e -> {
        
            ui.getEventManager().respondToSwitchScreen(JourneyUI.GameScreen.HELP, ui.getEventManager().previous);
        
        });
        
    }

    @Override
    final public void setComponentLayouts() {
        
        setAlignment(aboutText, Pos.CENTER);
        
        /* Bottom Pane */
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.setPadding(new Insets(20));
        bottomPane.setHgap(30);
        
    }
    
}
