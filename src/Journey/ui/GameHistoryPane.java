/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Journey.ui;

import Journey.game.GameData;
import Journey.game.Turn;
import Journey.manager.GameManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * Class which displays the game history of the Journey Framework.
 * 
 * @author 
 *          Anthony G. Musco
 * <dt><b>Date Created</b><dd>
 *          Nov-09-2014
 * <dt><b>Class</b><dd>
 *          CSE 219
 */
public class GameHistoryPane extends BorderPane implements JourneyPane {

    /**
     * Instance of the JourneyUI for which this GameHistoryPane is to be
     * associated.
     */
    JourneyUI ui;
    
    /**
     * Game manager for the current instance of JourneyUI for managing the game
     * history.
     */
    private GameManager gameManager;    
    
    /**
     * Label for the title of this pane.
     */
    Label gameHistoryText;
    
    /**
     * Table for displaying the games history.
     */
    private TableView historyTable;
    
    /**
     * Constructor which initializes the JourneyUI and the GameManager for this
     * Game History Manager.
     */
    public GameHistoryPane(JourneyUI ui) {
        
        this.ui = ui;
        gameManager = ui.getGameManager();
        
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
        
        /* Pane Label. */
        HBox topBox = new HBox();
        topBox.setAlignment(Pos.CENTER);
        gameHistoryText = new Label("Game History");
        Button backButton = new Button("Back");
        backButton.setOnAction(e->{
            ui.getEventManager().respondToSwitchScreen(JourneyUI.GameScreen.GAME_HISTORY, ui.getEventManager().previous);
        });
        topBox.getChildren().addAll(gameHistoryText, backButton);
        setTop(topBox);
        
        /* Game History Table. */
        historyTable = new TableView();
        TableColumn turnNo = new TableColumn("Turn");
        turnNo.setMinWidth(175);
        turnNo.setCellValueFactory(new PropertyValueFactory<Turn, Integer>("turnNumber"));
        TableColumn playerName = new TableColumn("Player");
        playerName.setMinWidth(200);
        playerName.setCellValueFactory(new PropertyValueFactory<Turn, String>("playerName"));
        TableColumn toCityName = new TableColumn("To City");
        toCityName.setMinWidth(200);
        TableColumn fromCityName = new TableColumn("From City");
        fromCityName.setCellValueFactory(new PropertyValueFactory<Turn, String>("fromCityName"));
        TableColumn description = new TableColumn("Description");
        description.setMinWidth(500);
        toCityName.setCellValueFactory(new PropertyValueFactory<Turn, String>("toCityName"));
        fromCityName.setMinWidth(200);
        description.setCellValueFactory(new PropertyValueFactory<Turn, String>("description"));
        historyTable.getColumns().addAll(turnNo, playerName, fromCityName, 
            toCityName, description);
        setCenter(historyTable);
        
    }

    @Override
    final public void setComponentBehaviors() {
       
        /* Implement. */
        
    }

    @Override
    final public void setComponentLayouts() {
        
        setAlignment(gameHistoryText, Pos.CENTER);
        
    }

    public void setGameData(GameData currentGame) {
        
        historyTable.setItems(currentGame.getTurns());
    
    }
    
}
