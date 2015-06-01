package Journey.ui;

import Journey.game.GameProperties;
import Journey.game.Player;
import Journey.game.Player.PlayerType;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import properties_manager.PropertiesManager;

/**
 * Mini-Pane which will be used to edit a Player's properties. Properties will
 * include the name, color, and type of player (Human vs. Computer).
 * 
 * @author 
 *          Anthony G. Musco
 * <dt><b>Date Created</b><dd>
 *          Oct-26-2014
 * <dt><b>Class</b><dd>
 *          CSE 219
 */
public class PlayerEditPane extends FlowPane implements JourneyPane {
    
    /**
     * Default array of Colors for the game setup pane.
     */
    public static final Color[] playerColors = {Color.RED, Color.BLUE, 
        Color.BLACK, Color.YELLOW, Color.WHITE, Color.GREEN};
    
    /**
     * Player object which this EditPane will wrap.
     */
    public Player player;
    
    /**
     * TextField used to prompt the user for a name for the Player.
     */
    public TextField nameTextField;

    /**
     * ToggleGroup used to select the type of Player.
     */
    public ToggleGroup typeToggle;
    
    /**
     * Image icon representing the color of the flag for the Player.
     */
    public ImageView icon;
    
    /**
     * Constructor which initializes the Player and the components of the Pane.
     */
    public PlayerEditPane(JourneyUI ui) {
        
        String name = "Player " + (Player.countOfPlayers + 1);
        
        player = new Player(playerColors[Player.countOfPlayers], PlayerType.HUMAN, name, ui);
        player.toBack();
        initBackground();
        initComponents();
        setComponentBehaviors();
        setComponentLayouts();
        
    }

    @Override
    final public void initBackground() {
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(GameProperties.IMG_PATH);
        String backImg = props.getProperty(GameProperties.GAME_EDIT_PANE_BG);
        Image img = new Image(imgPath + backImg);
        BackgroundImage bgImg = new BackgroundImage(img, 
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, 
            BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        
        setBackground(new Background(bgImg));
        
    }

    @Override
    final public void initComponents() {
        
        /* Name TextField */
        nameTextField = new TextField(player.getName());
        
        /* Type Toggle Group */
        typeToggle = new ToggleGroup();
        for (PlayerType pt : PlayerType.values()) {
            
            /* Create the Radio Button */
            RadioButton rb = new RadioButton(pt.toString());
            if(pt == PlayerType.HUMAN) rb.setSelected(true);            
            
            /* Set its behavior and add it to the ToggleGroup */
            rb.setOnAction(e -> {
            
                player.setType(pt);
            
            });
            typeToggle.getToggles().add(rb);
            
        }
        
        /* Icon ImageView */
        icon = new ImageView(player.getImage());
        
    }

    @Override
    final public void setComponentBehaviors() {
        
        /* Name TextField - Lambda Expression change listener. */
        nameTextField.textProperty().addListener(
          (ObservableValue<? extends String> observable, String oldValue, 
          String newValue) -> {
              
              player.setName(newValue);
              
        });
        
        /* ToggleGroup behavior is set in initComponents() */
        
    }

    @Override
    final public void setComponentLayouts() {

        setAlignment(Pos.CENTER);
        
        /* Get the width of the EditPane. */
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        double side = Double.parseDouble(
                props.getProperty(GameProperties.EDIT_PANE_WIDTH));

        /* Set the preferred size of the pane and the name Text Field */
        setPrefSize(side, side);
        
        /* Place RadioButtons into a VBox. */
        VBox radioButtonBox = new VBox();
        for(int i = 0; i < PlayerType.values().length; i++) {
            radioButtonBox.getChildren().add(
                (RadioButton) typeToggle.getToggles().get(i));
        }
        
        /* Place text field and radio buttons in a VBox. */
        VBox controlsBox = new VBox();
        controlsBox.getChildren().addAll(nameTextField, radioButtonBox);
        controlsBox.setAlignment(Pos.CENTER);
        controlsBox.setPadding(new Insets(10));
        
        String css = "-fx-border-color: black;\n" +
                "-fx-border-insets: 0;\n" +
                "-fx-border-width: 3;\n" +
                "-border-style: double;\n";
        setStyle(css);
        
        /* Place icon and additional controls in a HBox. */
        getChildren().addAll(icon, controlsBox);
        
    }
     
    /**
     * Public getter method for the Player this EditPane edits.
     * 
     * @return 
     *          The Player which this EditPane has been editing.
     */
    public Player getPlayer() {
        
        return player;
        
    }
    
}
