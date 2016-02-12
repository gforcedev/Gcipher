package gcipher.buttons;

import java.io.IOException;

import gcipher.crackers.TextScorer;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class BaseButton extends Button {
	public final TextArea input;
	public TextScorer cracker;

	public BaseButton(TextArea input) throws IOException {
		this.input = input;
		this.cracker = new TextScorer();
	}
}
