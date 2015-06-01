package Journey.application;

import Journey.ui.JourneyUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import xml_utilities.InvalidXMLFileFormatException;

/**
 * Main application of the Journey Through Europe game. This class starts the 
 * application and initializes all of the game-wide instance variables.
 * 
 * @author 
 *          Anthony G. Musco
 * <dt><b>Date Created</b><dd>
 *          Oct-22-2014
 * <dt><b>Class</b><dd>
 *          CSE 219
 */
public class Main extends Application {

    static String PROPERTIES_FILE_NAME = "properties.xml";
    static String SCHEMA_FILE_NAME = "properties_schema.xsd";
    static String DATA_PATH = "./data/";
    
    public static void main(String[] args) {
        
        launch(args);
        
    }
    
    /**
     * Begins the application.
     * 
     * @param primaryStage
     *          Main stage (window) in which the game scenes will operate.
     * @throws Exception 
     *          Exception thrown if something goes wrong.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        /* Initialize property manager and grab reference.*/
        PropertiesManager props = initPropertiesManager();
        
        /* Initialize the title of the primary stage. */
        String title = props.getProperty(Journey.game.GameProperties.GAME_TITLE_TEXT);
        primaryStage.setTitle(title);
        
        /* Initialize the JourneyUI */
        JourneyUI root = new JourneyUI();
        double width = Double.parseDouble(props.getProperty(Journey.game.GameProperties.WINDOW_WIDTH));
        double height = Double.parseDouble(props.getProperty(Journey.game.GameProperties.WINDOW_HEIGHT));
        Scene primaryScene = new Scene(root, width, height);        
        primaryScene.getStylesheets().add("Journey/style/journeyStyle.css");
        primaryStage.setScene(primaryScene);
        root.initControls();

        /* Set on close request */
        primaryStage.setOnCloseRequest(e-> {
            root.getEventManager().respondToExit();
            e.consume();
        });

        
        primaryStage.maximizedProperty().addListener(e->{
            root.getEventManager().respondToMaximize();
        });
        
        /* Show the stage, begin the game*/
        primaryStage.show();
        
    }
    
    /**
     * Initializes the properties manager singleton. This method creates the
     * first (and only) instance of the properties manager object and sets the
     * properties xml file and the properties_schema xsd file (both located in
     * the "./data/" folder). It then loads the properties from those files
     * into it's own hash table.
     * 
     * @return
     *          A reference to the initialized property manager.
     * @throws InvalidXMLFileFormatException 
     *          Exception is thrown if the XML file is of invalid format.
     */
    public PropertiesManager initPropertiesManager() 
        throws InvalidXMLFileFormatException {
        
        /* Create a new properties manager for this instance of the game. */
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        /* Initialize the properties file name */
        props.addProperty(Journey.game.GameProperties.PROPERTIES_FILE_NAME,
            PROPERTIES_FILE_NAME);
        
        /* Initialize the properties schema file name */
        props.addProperty(Journey.game.GameProperties.SCHEMA_FILE_NAME,
            SCHEMA_FILE_NAME);
        
        /* Initialize the properties data path (required for loadProperties
           call) */
        props.addProperty(Journey.game.GameProperties.DATA_PATH,
                DATA_PATH);
        
        /* Load the properties into the property manager */
        props.loadProperties(PROPERTIES_FILE_NAME,
            SCHEMA_FILE_NAME);
        
        return props;
        
    }
    
}
