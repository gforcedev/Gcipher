package gcipher.buttons;

import java.io.IOException;

import gcipher.crackers.VigenereCracker;
import javafx.scene.control.TextArea;

public class VigenereButton extends BaseButton {	
	public VigenereButton(TextArea input) throws IOException {
		super(input);
		this.setText("Vigenere Cipher");
		this.cracker = new VigenereCracker();
	}
}
