package gcipher.buttons;


import java.io.IOException;

import gcipher.crackers.CaesarCracker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class CaesarButton extends Button implements EventHandler<ActionEvent> {
	CaesarCracker cracker;
	TextArea input;
	
	public CaesarButton(TextArea input) {
		this.setText("Caesar Shift Cipher");
		
		this.setOnAction(this);
		
		this.input = input;
	}
	
	@Override
	public void handle(ActionEvent event) {
		try {
			this.cracker = new CaesarCracker(this.input.getText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.input.setText(this.cracker.decrypt());
	}
}
