package gcipher.crackers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class MonoalphabeticCracker extends TextScorer {
	public MonoalphabeticCracker() throws IOException {
		super();
	}

	@Override
	public String decrypt(String ct) {
		ct = ct.toUpperCase().replaceAll("[^A-Z]", "");
		String bestDec = "";
		float bestScore = Float.NEGATIVE_INFINITY;
		for (int i = 0; i < 5; i++) {
			String thisDec = monoSolveGen(ct);
			float thisScore = quadgramScore(thisDec);
			if (thisScore > bestScore) {
				bestDec = thisDec;
				bestScore = thisScore;
			}
		}

		return bestDec;
	}

	public String monoSolveGen(String ct) {
		String parentKey = shuffleString(alphabet);

		int count = 0;
		float fitness = quadgramScore(solveWithKey(ct, parentKey));

		while (true) {
			String newKey = swap2(parentKey);
			float newFitness = quadgramScore(solveWithKey(ct, newKey));

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
		String dec = solveWithKey(ct, parentKey);
		return dec;
	}

	@Override
	public String solveWithKey(String ct, String key) { //ct is ciphertext
		ct = ct.toUpperCase().replaceAll("[^A-Z]", "");
		key = key.toUpperCase().replaceAll("[^A-Z]", "");
		if (key.length() < 26) {
			for (int i = 0; i < 26; i++) {
				if (!key.contains(alphabet.substring(i, i + 1))) {
					key += alphabet.substring(i, i + 1);
				}
			}
		}
		StringBuilder solved = new StringBuilder(ct.length());
		int ctLength = ct.length();

		for (int i = 0; i < ctLength; i++) {
			solved.append(key.charAt(ct.charAt(i) - 'A'));
		}

		return solved.toString();
	}

	public String shuffleString(String str) {
		ArrayList<Character> list = new ArrayList<Character>();
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
}
