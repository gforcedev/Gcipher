package gcipher.buttons;

import gcipher.crackers.HillCracker;
import gcipher.crackers.TextScorer;
import javafx.scene.control.TextArea;

/**
 * Created by gforcedev on 26/11/2016.
 */
public class HillButton extends BaseButton {
	public HillButton(TextArea input, TextScorer textScorer) {
		super(input);
		this.cracker = new HillCracker(textScorer);
		this.setText("Hill Cipher");
	}
}
