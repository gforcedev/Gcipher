package gcipher;

import gcipher.buttons.*;
import gcipher.crackers.Cracker;
import gcipher.crackers.TextScorer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;


public class Main extends Application implements EventHandler<ActionEvent> {

	private Stage window;
	private Scene scene1;
	private Cracker cracker;
	private Button crackButton;
	private TextArea input;
	private TextArea output;
	private ArrayList<BaseButton> cipherList = new ArrayList<>();
	private Rectangle rect;
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
		buttonPanel.setAlignment(Pos.TOP_LEFT);
		buttonPanel.getStyleClass().add("card");

		crackButton = new Button("crack");
		crackButton.getStyleClass().add("button-raised");
		crackButton.setOnAction(this);
		crackButton.getStyleClass().add("button-raised");
		buttonPanel.getChildren().add(crackButton);

		solveButton = new Button("Decrypt with key");
		solveButton.setId("solvebutton");
		solveButton.getStyleClass().add("row2button");
		solveButton.setOnAction(this);

		keyField = new TextField();
		keyField.setId("keyField");
		keyField.setMinWidth(500.0);

		buttonPanel.getChildren().addAll(solveButton, keyField);
		mainPanel.setTop(buttonPanel);

		HBox cipherPanel = new HBox(7);
		cipherPanel.getStyleClass().add("card");
		cipherPanel.setAlignment(Pos.TOP_LEFT);
		try {
			scorer = new TextScorer();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		cipherList.add(new CaesarButton(input, scorer));
		cracker = cipherList.get(0).cracker;
		cipherList.add(new MonoalphabeticButton(input, scorer));
		cipherList.add(new VigenereButton(input, scorer));
		cipherList.add(new ColumnTransButton(input, scorer));
		cipherList.add(new RailfenceButton(input, scorer));
		cipherList.add(new PlayfairButton(input, scorer));

		for (Button button : cipherList) {
			button.setOnAction(this);
			cipherPanel.getChildren().add(button);
		}

		layout.setTop(cipherPanel);
		layout.setCenter(mainPanel);

		rect = new Rectangle();
		rect.setX(cipherList.get(0).getTranslateX());
		rect.setY(42); //46 down, minus the 4 height
		rect.setWidth(cipherList.get(0).getWidth());
		rect.setHeight(4);
		rect.setId("rectangleTop");

		layout.getChildren().add(rect);
		scene1 = new Scene(layout);
		scene1.getStylesheets().add("gcipher/master.css");
		window.setScene(scene1);
		window.show();

		Timeline timeline = new Timeline();
		timeline.getKeyFrames().addAll(
				new KeyFrame(new Duration(350),
						new KeyValue(rect.translateXProperty(), cipherList.get(0).getLayoutX() + 100),
						new KeyValue(rect.widthProperty(), cipherList.get(0).getWidth() - 40)
				),
				new KeyFrame(new Duration(700),
						new KeyValue(rect.translateXProperty(), cipherList.get(0).getLayoutX()),
						new KeyValue(rect.widthProperty(), cipherList.get(0).getWidth())
				));
		// play animation
		timeline.play();
	}

	@Override
	public void handle(ActionEvent e) {
		if (e.getSource() instanceof BaseButton) {
			BaseButton source = (BaseButton) e.getSource();

			Timeline timeline = new Timeline();
			timeline.getKeyFrames().addAll(
			new KeyFrame(new Duration(400),
					new KeyValue(rect.translateXProperty(), source.getLayoutX()),
					new KeyValue(rect.widthProperty(), source.getWidth())
			));
			// play animation
			timeline.play();

			cracker = source.cracker;
		} else if (e.getSource() == crackButton) {
			String key = cracker.getKey(input.getText());
			keyField.setText(key);
			output.setText(cracker.solveWithKey(input.getText(), key));
		} else if (e.getSource() == solveButton) {
			output.setText(cracker.solveWithKey(input.getText(), keyField.getText()));
		}
	}

	void setCss(String css) {
		scene1.getStylesheets().add(css);
	}
}
