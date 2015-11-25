package gcipher;

import java.util.ArrayList;
import gcipher.buttons.CaesarButton;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;





public class Main extends Application {
	
	Stage window;
	Scene scene1;
	
	

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Gcipher");
        
        
        
        VBox masterPanel = new VBox(20);
        
        VBox cipherPanel = new VBox(20);
        
        
        TextArea input =  new TextArea();
        input.getStyleClass().add("inputArea");
        input.setWrapText(true);
        
        VBox outputPanel = new VBox(20);
        TextArea output = new TextArea();
        outputPanel.getChildren().add(output);
        
      //cipher panel
        ArrayList<Button> cipherList = new ArrayList<Button>();
        cipherList.add(new CaesarButton(input));
        
        
        
        masterPanel.getChildren().addAll(input);
        for (Button cipherButton : cipherList) {
        	cipherPanel.getChildren().add(cipherButton);
        }
        
        

        
        BorderPane layout = new BorderPane();
        layout.setCenter(masterPanel);
        layout.setLeft(cipherPanel);
        layout.setRight(outputPanel);
        
        
        scene1 = new Scene(layout);
        scene1.getStylesheets().add("gcipher/master.css");
        window.setScene(scene1);
        window.show();
    }
}
