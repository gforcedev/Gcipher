package gcipher;

import gcipher.buttons.*;
import gcipher.crackers.Cracker;
import gcipher.crackers.TextScorer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.util.ArrayList;


public class Main extends Application implements EventHandler<ActionEvent> {

	private Stage window;
	private Scene scene1;
	private Cracker cracker;
	private Button crackButton;
	private TextArea input;
	private TextArea output;
	private ArrayList<BaseButton> cipherList = new ArrayList<>();
	private TextScorer scorer;
	private Button solveButton;
	private TextField keyField;


	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		window.setTitle("Gcipher");

		BorderPane layout = new BorderPane();
		BorderPane mainPanel = new BorderPane();

		HBox ioPanel = new HBox();
		input = new TextArea();
		input.setWrapText(true);
		output = new TextArea();
		output.setWrapText(true);
		HBox.setHgrow(input, Priority.ALWAYS);
		HBox.setHgrow(output, Priority.ALWAYS);
		input.setMaxWidth(Double.MAX_VALUE);
		output.setMaxWidth(Double.MAX_VALUE);
		ioPanel.getChildren().addAll(input, output);
		mainPanel.setCenter(ioPanel);

		HBox buttonPanel = new HBox(3);
		buttonPanel.setAlignment(Pos.CENTER);
		crackButton = new Button("crack");
		crackButton.getStyleClass().add("row2button");
		crackButton.setOnAction(this);
		crackButton.getStyleClass().add("crackButton");
		buttonPanel.getChildren().add(crackButton);
		keyField = new TextField();
		keyField.setId("keyField");
		solveButton = new Button("Decrypt with key");
		solveButton.setId("solvebutton");
		solveButton.getStyleClass().add("row2button");
		solveButton.setOnAction(this);
		buttonPanel.getChildren().addAll(solveButton, keyField);
		mainPanel.setTop(buttonPanel);

		HBox cipherPanel = new HBox(7);
		cipherPanel.setId("cipherpanel");
		cipherPanel.setAlignment(Pos.BASELINE_CENTER);
		try {
			scorer = new TextScorer();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		cipherList.add(new CaesarButton(input, scorer));
		cipherList.get(0).getStyleClass().add("selected");
		cracker = cipherList.get(0).cracker;
		cipherList.add(new MonoalphabeticButton(input, scorer));
		cipherList.add(new VigenereButton(input, scorer));
		cipherList.add(new ColumnTransButton(input, scorer));
		cipherList.add(new RailfenceButton(input, scorer));

		for (Button button : cipherList) {
			button.setOnAction(this);
			cipherPanel.getChildren().add(button);
		}



		layout.setTop(cipherPanel);
		layout.setCenter(mainPanel);
		scene1 = new Scene(layout);
		scene1.getStylesheets().add("gcipher/master.css");
		window.setScene(scene1);
		window.show();
	}

	@Override
	public void handle(ActionEvent e) {
		if (e.getSource() instanceof BaseButton) {
			BaseButton source = (BaseButton) e.getSource();
			for (Button button : cipherList) {
				button.getStyleClass().remove("selected");
			}
			cracker = source.cracker;
			source.getStyleClass().add("selected");
		} else if (e.getSource() == crackButton) {
			String key = cracker.getKey(input.getText());
			keyField.setText(key);
			output.setText(cracker.solveWithKey(input.getText(), key));
		} else if (e.getSource() == solveButton) {
			output.setText(cracker.solveWithKey(keyField.getText(), input.getText()));
		}
	}
}
