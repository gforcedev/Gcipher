package gcipher.crackers;

import java.util.ArrayList;
import java.util.Collections;

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

	public String shuffleString(String str) {
		ArrayList<Character> list = new ArrayList<>();
		for (char c : str.toCharArray()) {
			list.add(c);
		}
		Collections.shuffle(list);
		String shuffled = "";
		for (char c : list) {
			shuffled += c;
		}
		return shuffled;
	}

	int myRand(int min, int max) {
		return (int) Math.floor(Math.random() * (max - min + 1) + min);
	}

	String swap2(String str) {
		char[] c = str.toCharArray();
		int a = myRand(0, c.length - 1);
		int b = myRand(0, c.length - 1);

		char temp = c[a];
		c[a] = c[b];
		c[b] = temp;

		String toReturn = "";
		for (char thisChar : c) {
			toReturn += thisChar;
		}

		return toReturn;
	}

	@Override
	public String getKey(String ct) {
		ct = ct.toUpperCase().replaceAll("[^A-Z]", "").replaceAll("J","I");

		String parentKey = shuffleString(alphabet);

		int count = 0;
		float fitness = scorer.quadgramScore(solveWithKey(ct, parentKey));

		while (true) {
			String newKey = swap2(parentKey);
			float newFitness = scorer.quadgramScore(solveWithKey(ct, newKey));
			for(int TEMP = 10; TEMP >= 0; TEMP = TEMP - 1) {

			}

			if (newFitness > fitness) {
				count = 0;
				fitness = newFitness;
				parentKey = newKey;
			} else {
				count++;
			}
			if (count > 1000) {
				break;
			}
		}
		return parentKey;
	}

	@Override
	public String solveWithKey(String ct, String key) {
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
				keySquare[y][x] = keyArray[x * 5 + y];
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

		StringBuilder answer = new StringBuilder();

		for (String s : ctArray) {
			a = gridFind(keySquare, s.charAt(0));
			b = gridFind(keySquare, s.charAt(1));

			if (a[0] == b[0]) {
				a[1] = (a[1] - 1) % 5;
				b[1] = (b[1] - 1) % 5;
			} else if (a[1] == b[1]) {
				a[0] = (a[0] - 1) % 5;
				b[0] = (b[0] - 1) % 5;
			} else {
				int tempX = a[0];
				a[0] = b[0];
				b[0] = tempX;
			}

			answer.append(keySquare[a[0]][a[1]]);
			answer.append(keySquare[b[0]][b[1]]);
		}

		return answer.toString();
	}
}
