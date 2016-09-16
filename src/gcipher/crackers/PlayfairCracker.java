package gcipher.crackers;

import java.util.ArrayList;

/**
 * Created by 14ClarTh on 12/09/2016.
 */
public class PlayfairCracker extends Cracker {

	private final static String alphabet = "ABCDEFGHIKLMNOPQRSTUVWXYZ";

	public PlayfairCracker(TextScorer textScorer) {super(textScorer);} {
	}

	public static String replaceLast(String text, String regex, String replacement) {
		return text.replaceFirst("(?s)"+regex+"(?!.*?"+regex+")", replacement);
	}

	private int[] gridFind(char[][] grid, char toFind) {
		for (int x = 0; x < 5; x++){
			for (int y = 0; y < 5; y++) {
				if (grid[x][y] == toFind) {
					return(new int[] {x,y});
				}
			}
		}
		return new int[] {0, 0};
	}

	@Override
	public String getKey(String ct) {
		return null;
	}

	@Override
	public String solveWithKey(String key, String ct) {
		// all for the key
		char[][] keySquare = new char[5][5];

		key = key.toUpperCase().replaceAll("[^A-Z]", "").replaceAll("J","I");
		ct = ct.toUpperCase().replaceAll("[^A-Z]", "").replaceAll("J","I");
		char[] keyArray = key.toCharArray();

		for (char c : keyArray) {
			if (replaceLast(key, "" + c, "").contains("" + c)) {
				key = replaceLast(key, "" + c, "");
			}
		}

		for (char c : alphabet.toCharArray()) {
			if (!key.contains("" + c)) {
				key = key + c;
			}
		}
		keyArray = key.toCharArray();

		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 5; y++){
				keySquare[x][y] = keyArray[x * 5 + y];
			}
		}


		// all for ciphertext bigrams
		for (char c : ct.toCharArray()) {
			key = key.replaceAll("" + c + c, "" + c + "X" + c);
		}

		if (ct.length() %2 != 0) {
			ct = ct + "X";
		}
		String[] ctArray = new String[ct.length() / 2];
		String current = "";
		for (int i = 0; i < ct.length(); i++) {
			current = current + ct.charAt(i);
			if (current.length() == 2) {
				ctArray[(i - 1) / 2] = current;
				current = "";
			}
		}

		int[] a = new int[2];
		int[] b = new int[2];

		for (String s : ctArray) {
			a = gridFind(keySquare, s.charAt(1));
		}



		return null;
	}
}
