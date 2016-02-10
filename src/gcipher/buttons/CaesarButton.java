package gcipher.buttons;


import java.io.IOException;

import gcipher.crackers.CaesarCracker;
import javafx.scene.control.TextArea;

public class CaesarButton extends BaseButton {
	public CaesarButton(TextArea input) throws IOException {
		super(input);
		this.cracker = new CaesarCracker();
		this.setText("Caesar Shift Cipher");
	}
}
