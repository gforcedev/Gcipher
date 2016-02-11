package gcipher.crackers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class MonoalphabeticCracker extends BaseCracker {
	public MonoalphabeticCracker() throws IOException {
		super();
	}

	@Override
	public String decrypt(String ct) {
		ct = ct.toUpperCase().replaceAll("[^A-Z]", "");
		ArrayList<String> decs = new ArrayList<String>();
		for (int i = 0; i < 5; i++) {
			decs.add(monoSolveGen(ct));
		}
		String bestDec = "";
		int decLength = decs.size();
		for (int i = 0; i < decLength; i++) {
			if (quadgramScore(decs.get(i)) > quadgramScore(bestDec)) {
				bestDec = decs.get(i);
			}
		}
		return bestDec;
	}

	public String monoSolveGen(String ct) {
		String parentKey = shuffleString(alphabet);

		int count = 0;

		while (true) {
			float fitness = quadgramScore(solveWithKey(ct, parentKey));
			String newKey = swap2(parentKey);

			float newFitness = quadgramScore(solveWithKey(ct, newKey));

			if (newFitness > fitness) {
				count = 0;
				fitness = quadgramScore(solveWithKey(ct, newKey));
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
		String solved = "";
		int ctLength = ct.length();

		for (int i = 0; i < ctLength; i++) {
			solved += key.charAt(alphabet.indexOf(ct.charAt(i)));
		}

		return solved;
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
