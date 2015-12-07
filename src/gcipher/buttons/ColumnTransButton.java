package gcipher.buttons;

import java.io.IOException;

import gcipher.crackers.ColumnTransCracker;
import javafx.scene.control.TextArea;

public class ColumnTransButton extends BaseButton {

	public ColumnTransButton(TextArea input) throws IOException {
		super(input);
		this.setText("Columnar transposition");
		this.cracker = new ColumnTransCracker();
	}
}
