package gcipher.buttons;

import gcipher.crackers.TextScorer;
import gcipher.crackers.VigenereCracker;
import javafx.scene.control.TextArea;

public class VigenereButton extends BaseButton {

	public VigenereButton(TextArea input, TextScorer textScorer) {
		super(input);
		this.setText("Vigenere Cipher");
		this.cracker = new VigenereCracker(textScorer);
	}
}
