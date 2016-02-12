package gcipher.crackers;

import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;

public class VigenereCracker  {
	private final TextScorer scorer;
	public VigenereCracker(TextScorer textScorer) {
		scorer = textScorer;
	}


	public String decrypt(String ct) {
		ct = ct.toUpperCase().replaceAll("[^A-Z]", "");
		ArrayList<String> decs = new ArrayList<>();
		for (int i = 1; i < 20; i++) {
			decs.add(keyTest(i, ct));
		}
		String bestDec = "";
		for (String dec : decs) {
			if (scorer.quadgramScore(dec) > scorer.quadgramScore(bestDec)) {
				bestDec = dec;
			}
		}
		return bestDec;
	}


	public String caesar(String ct) {
		String bestDec = null;
		float bestScore = Float.NEGATIVE_INFINITY;

		for (int i = 0; i < 26; i++) {
			int ctLength = ct.length();
			char[] characters = new char[ctLength];
			for (int n = 0; n < ctLength; n++) {
				int thisChar = ct.charAt(n);
				thisChar += i;
				if (thisChar > 'Z') {
					thisChar -= 26;
				}
				characters[i] = (char) thisChar;
			}

			String thisDec = new String(characters);
			float thisScore = scorer.monogramScore(thisDec);
			if (thisScore > bestScore) {
				bestDec = thisDec;
				bestScore = thisScore;
			}
		}

		return bestDec;
	}

	public String keyTest(int length, String ct) {
		String[] seperated = new String[length];
		int ctlength = ct.length();
		for (int i = 0; i < length; i++) {
			seperated[i] = "";
		}


		for (int i = 0; i < ctlength; i++) {
			seperated[i % length] += ct.charAt(i);
		}
		for (int i = 0; i < length; i++) {
			seperated[i] = caesar(seperated[i]);
		}
		String toReturn = "";

		while (seperated[length - 1].length() > 0) {
			for (int i = 0; i < length; i++) {
				toReturn += seperated[i].charAt(0);
				seperated[i] = seperated[i].substring(1);
			}
		}


		return toReturn;
	}


	public String solveWithKey(String ct, String key) {
		return "";
	}
}
