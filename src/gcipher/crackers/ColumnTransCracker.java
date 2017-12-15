package gcipher.crackers;

import java.util.ArrayList;

public class ColumnTransCracker extends Cracker {
	public ColumnTransCracker(TextScorer textScorer) {
		super(textScorer);
	}

	public String getKey(String ct) {
		ct = ct.toUpperCase().replaceAll("[^A-Z_]", "");
		String[] decs = new String[(int) Math.floor(ct.length() / 2)];
		for (int i = 2; i < Math.floor(ct.length() / 2); i++) {
			decs[i] = keyTest(ct, i);
		}
		String bestDec = "";
		int bestI = 2;
		for (int i = 2; i < decs.length; i++) {
			if (scorer.quadgramScore(decs[i]) > scorer.quadgramScore(bestDec)) {
				bestDec = decs[i];
				bestI = i;
			}
		}
		return Integer.toString(bestI);
	}

	@Override
	public String solveWithKey(String ct, String key) {
		ct = ct.toUpperCase().replaceAll("[^A-Z_]", "");
		return keyTest(ct, Integer.parseInt(key));
	}


	public String keyTest(String ct, int key) {
		ct = ct.toUpperCase().replaceAll("[^A-Z_]", "");
		String[] seperated = new String[key];
		int ctlength = ct.length();
		for (int i = 0; i < key; i++) {
			seperated[i] = "";
		}

		for (int i = 0; i < ctlength; i++) {
			seperated[i % key] += ct.charAt(i);
		}

		StringBuilder toReturn = new StringBuilder();
		for (int i = 0; i < key; i++) {
			toReturn.append(seperated[i]);
		}
		return toReturn.toString();
	}
}
