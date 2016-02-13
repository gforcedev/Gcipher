package gcipher.buttons;

import gcipher.crackers.RailfenceCracker;
import gcipher.crackers.TextScorer;
import javafx.scene.control.TextArea;

public class RailfenceButton extends BaseButton {

	public RailfenceButton(TextArea input, TextScorer textScorer) {
		super(input);
		this.setText("Rail Fence");
		this.cracker = new RailfenceCracker(textScorer);
	}

}
