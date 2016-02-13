package gcipher.buttons;


import gcipher.crackers.MonoalphabeticCracker;
import gcipher.crackers.TextScorer;
import javafx.scene.control.TextArea;

public class MonoalphabeticButton extends BaseButton {
	public MonoalphabeticButton(TextArea input, TextScorer textScorer) {
		super(input);
		this.cracker = new MonoalphabeticCracker(textScorer);
		this.setText("Monoalphabetic Substitution Cipher");
	}
}
