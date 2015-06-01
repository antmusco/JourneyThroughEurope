/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Journey.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javax.swing.plaf.synth.SynthLookAndFeel;

/**
 *
 * @author Anthony
 */
public class MessageBox extends Stage{

    public static final int WIDTH = 200;
    public static final int HEIGHT = 100;
    
    public MessageBox(String message) {
        
        setTitle("Message");
        
        BorderPane bp = new BorderPane();
        Label text = new Label(message);
        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(10));
        Button okayButton = new Button("Okay");
        okayButton.setOnAction(e->{
            close();
        });
        buttonBox.getChildren().addAll(okayButton);
        buttonBox.setAlignment(Pos.CENTER);
        bp.setCenter(text);
        bp.setBottom(buttonBox);
        
        Scene messageScene = new Scene(bp, WIDTH, HEIGHT);
        messageScene.getStylesheets().add("Journey/style/messageBoxStyle.css");
        setScene(messageScene);
        
    }
    
    public static void showDialog(String message){
    
        MessageBox msg = new MessageBox(message);
        msg.show();
        
    }
    
}
