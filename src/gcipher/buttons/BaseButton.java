package gcipher.buttons;

import gcipher.crackers.Cracker;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class BaseButton extends Button {
	public final TextArea input;
	public Cracker cracker;

	public BaseButton(TextArea input) {
		this.input = input;
	}
}
