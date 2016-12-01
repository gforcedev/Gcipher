package gcipher.crackers;

import javafx.scene.control.TextInputDialog;

import java.util.Optional;

/**
 * Created by 14ClarTh on 21/11/2016.
 */
public class HillCracker extends Cracker {
	private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public HillCracker(TextScorer textScorer) {
		super(textScorer);
	}

	@Override
	public String getKey(String ct) {
		TextInputDialog dialog = new TextInputDialog("harry");
		dialog.setTitle("Input");
		dialog.setHeaderText("Crib Request");
		dialog.setContentText("Please enter suspected crib");
		Optional<String> result = dialog.showAndWait();

		// The Java 8 way to get the response value (with lambda expression).
		result.ifPresent(name -> System.out.println("Your name: " + name));

		String crib = result.get().toUpperCase();

		if (crib.length() > 4) {
			crib = crib.substring(0, 4);
		}



		return null;
	}

	@Override
	public String solveWithKey(String key, String ct) {
		return null;
	}
}
