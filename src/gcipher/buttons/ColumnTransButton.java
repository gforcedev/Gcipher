package gcipher.buttons;


import gcipher.crackers.ColumnTransCracker;
import gcipher.crackers.TextScorer;
import javafx.scene.control.TextArea;

public class ColumnTransButton extends BaseButton {

	public ColumnTransButton(TextArea input, TextScorer textScorer) {
		super(input);
		this.setText("Columnar transposition");
		this.cracker = new ColumnTransCracker(textScorer);
	}
}
