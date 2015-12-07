package gcipher.buttons;

import java.io.IOException;

import gcipher.crackers.RailfenceCracker;
import javafx.scene.control.TextArea;

public class RailfenceButton extends BaseButton {

	public RailfenceButton(TextArea input) throws IOException {
		super(input);
		this.setText("Rail Fence");
		this.cracker = new RailfenceCracker();
	}

}
