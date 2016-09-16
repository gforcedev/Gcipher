package gcipher.buttons;

import gcipher.crackers.PlayfairCracker;
import gcipher.crackers.TextScorer;
import javafx.scene.control.TextArea;

/**
 * Created by 14ClarTh on 13/09/2016.
 */
public class PlayfairButton extends BaseButton {
	public PlayfairButton(TextArea input, TextScorer textScorer) {
		super(input);
		this.cracker = new PlayfairCracker(textScorer);
		this.setText("Playfair Cipher");
	}
}
