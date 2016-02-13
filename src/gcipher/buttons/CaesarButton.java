package gcipher.buttons;


import gcipher.crackers.CaesarCracker;
import gcipher.crackers.TextScorer;
import javafx.scene.control.TextArea;

public class CaesarButton extends BaseButton {
	public CaesarButton(TextArea input, TextScorer textScorer) {
		super(input);
		this.cracker = new CaesarCracker(textScorer);
		this.setText("Caesar Shift Cipher");
	}
}
