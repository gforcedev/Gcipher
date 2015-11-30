package gcipher.buttons;

import java.io.IOException;

import gcipher.crackers.BaseCracker;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class BaseButton extends Button {
	public final TextArea input;
	public BaseCracker cracker;
	
	public BaseButton(TextArea input) throws IOException {
		this.input = input;
		this.cracker = new BaseCracker();
	}
}
