package gcipher.buttons;

import java.io.IOException;

import gcipher.crackers.MonoalphabeticCracker;
import javafx.scene.control.TextArea;

public class MonoalphabeticButton extends BaseButton {
	public MonoalphabeticButton(TextArea input) throws IOException {
		super(input);
		this.cracker = new MonoalphabeticCracker();
		this.setText("Monoalphabetic Substitution Cipher");
	}
}
