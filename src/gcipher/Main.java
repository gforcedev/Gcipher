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
import javafx.scene.control.Alert;
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
	private Rectangle selectedRect;
	private Rectangle buttonRect;
	private TextScorer scorer;
	private Button solveButton;
	private Button reverseButton;
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
		buttonPanel.getStyleClass().add("buttonPanel");

		crackButton = new Button("crack");
		crackButton.getStyleClass().add("button-raised");
		crackButton.setOnAction(this);
		crackButton.getStyleClass().add("button-raised");
		buttonPanel.getChildren().add(crackButton);

		solveButton = new Button("Decrypt with key");
		solveButton.setId("solvebutton");
		solveButton.getStyleClass().add("row2button");
		solveButton.setOnAction(this);
		buttonPanel.getChildren().add(solveButton);

		reverseButton = new Button("Reverse output");
		reverseButton.setOnAction(this);
		buttonPanel.getChildren().add(reverseButton);

		keyField = new TextField();
		keyField.setId("keyField");
		keyField.setMinWidth(500.0);
		buttonPanel.getChildren().add(keyField);

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
		selectedRect
		 = new Rectangle();
		selectedRect.setX(cipherList.get(0).getTranslateX());
		selectedRect.setY(42); //46 down, minus the 4 height
		selectedRect.setWidth(cipherList.get(0).getWidth());
		selectedRect.setHeight(4);
		selectedRect.setId("rectangleTop");
		buttonRect = new Rectangle();
		buttonRect.setY(0);
		buttonRect.setHeight(46);
		buttonRect.setArcWidth(20);
		buttonRect.setArcHeight(300);
		buttonRect.setId("buttonRect");

		layout.getChildren().add(selectedRect);
		layout.getChildren().add(buttonRect);
		scene1 = new Scene(layout);
		scene1.getStylesheets().add("gcipher/master.css");
		window.setScene(scene1);
		window.show();

		//the startup animation
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().addAll(
				new KeyFrame(new Duration(0),
						new KeyValue(input.maxHeightProperty(), 0),
						new KeyValue(output.maxHeightProperty(), 0)
					),
				new KeyFrame(new Duration(300),
						new KeyValue(selectedRect.translateXProperty(), cipherList.get(0).getLayoutX() + 100),
						new KeyValue(selectedRect.widthProperty(), cipherList.get(0).getWidth() - 3),
						new KeyValue(input.maxHeightProperty(), 300),
						new KeyValue(output.maxHeightProperty(), 300)
				),
				new KeyFrame(new Duration(500),
						new KeyValue(selectedRect.translateXProperty(), cipherList.get(0).getLayoutX()),
						new KeyValue(selectedRect.widthProperty(), cipherList.get(0).getWidth())
				));
		// play animation
		timeline.play();
	}

	@Override
	public void handle(ActionEvent e) {
		if (e.getSource() instanceof BaseButton) {
			BaseButton source = (BaseButton) e.getSource();
			selectAnimation(source);


			cracker = source.cracker;
		} else if (e.getSource() == crackButton) {
			animation2((Button) e.getSource());

			if (input.getText().length() == 0) {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Gcipher information");
				alert.setHeaderText(null);
				alert.setContentText("No Ciphertext!");
				alert.showAndWait();
			} else {
				try {
					String key = cracker.getKey(input.getText());
					keyField.setText(key);
					output.setText(cracker.solveWithKey(input.getText(), key));
				} catch (Exception ex) {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Gcipher information");
					alert.setHeaderText(null);
					alert.setContentText("Error:\n" + ex.getLocalizedMessage());
					alert.showAndWait();
				}
			}
		} else if (e.getSource() == solveButton) {
			animation2((Button) e.getSource());
			try {
				output.setText(cracker.solveWithKey(input.getText(), keyField.getText()));
			} catch (Exception ex) {
				output.setText("An error occured.");
			}
		} else if (e.getSource() == reverseButton) {
			animation2((Button) e.getSource());

			String unreversed = output.getText();
			StringBuilder reversed = new StringBuilder();
			for (int i = unreversed.length() - 1; i >= 0; i--) {
				reversed.append(unreversed.charAt(i));
			}
			output.setText(reversed.toString());
		}
	}

	void selectAnimation(Button source) {
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().addAll(
				new KeyFrame(new Duration(450),
						new KeyValue(selectedRect.translateXProperty(), source.getLayoutX()),
						new KeyValue(selectedRect.widthProperty(), source.getWidth())
				));
		// play animation
		timeline.play();

		Timeline timeline2 = new Timeline();
		timeline2.getKeyFrames().addAll(
				new KeyFrame(new Duration(0),
						new KeyValue(buttonRect.translateXProperty(), source.getLayoutX() + source.getWidth() / 2),
						new KeyValue(buttonRect.translateYProperty(), 0),
						new KeyValue(buttonRect.widthProperty(), 0)
				),
				new KeyFrame(new Duration(250),
						new KeyValue(buttonRect.translateXProperty(), source.getLayoutX()),
						new KeyValue(buttonRect.translateYProperty(), 0),
						new KeyValue(buttonRect.widthProperty(), source.getWidth())
				),
				new KeyFrame(new Duration(251),
						new KeyValue(buttonRect.translateXProperty(), source.getLayoutX() + source.getWidth() / 2),
						new KeyValue(buttonRect.translateYProperty(), 0),
						new KeyValue(buttonRect.widthProperty(), 0)
				));
		// play animation
		timeline2.play();
	}

	void animation2(Button source) {
		Timeline timeline2 = new Timeline();
		timeline2.getKeyFrames().addAll(
				new KeyFrame(new Duration(0),
						new KeyValue(buttonRect.translateXProperty(), source.getLayoutX() + source.getWidth() / 2),
						new KeyValue(buttonRect.translateYProperty(), 47),
						new KeyValue(buttonRect.widthProperty(), 0)
				),
				new KeyFrame(new Duration(300),
						new KeyValue(buttonRect.translateXProperty(), source.getLayoutX()),
						new KeyValue(buttonRect.translateYProperty(), 47),
						new KeyValue(buttonRect.widthProperty(), source.getWidth())
				),
				new KeyFrame(new Duration(301),
						new KeyValue(buttonRect.translateXProperty(), source.getLayoutX() + source.getWidth() / 2),
						new KeyValue(buttonRect.translateYProperty(), 47),
						new KeyValue(buttonRect.widthProperty(), 0)
				));
		// play animation
		timeline2.play();
	}
}
