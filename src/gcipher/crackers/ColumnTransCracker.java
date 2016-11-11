package gcipher.crackers;

import java.util.ArrayList;

public class ColumnTransCracker extends Cracker {
	public ColumnTransCracker(TextScorer textScorer) {
		super(textScorer);
	}

	public String getKey(String ct) {
		ct = ct.toUpperCase().replaceAll("[^A-Z]", "");
		ArrayList<String> decs = new ArrayList<>();
		for (int i = 2; i < Math.floor(ct.length() / 2); i++) {
			decs.add(keyTest(ct, i));
			System.out.println(keyTest(ct, i));
		}
		String bestDec = "";
		for (String dec : decs) {
			if (scorer.quadgramScore(dec) > scorer.quadgramScore(bestDec)) {
				bestDec = dec;
			}
		}
		return bestDec;
	}

	@Override
	public String solveWithKey(String ct, String key) {
		return "";
	}


	public int[] keyTest(String ct, int key) {
		String[] seperated = new String[key];
		int ctlength = ct.length();
		for (int i = 0; i < key; i++) {
			seperated[i] = "";
		}


		for (int i = 0; i < ctlength; i++) {
			seperated[i % key] += ct.charAt(i);
		}

		int[] toReturn = new int[key];
		for (int i = 0; i < key; i++) {
			toReturn[i] = i;
		}


		return null;
	}
}
