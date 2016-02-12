package gcipher.crackers;

import java.io.IOException;
import java.util.ArrayList;

public class ColumnTransCracker extends TextScorer {
	public ColumnTransCracker() throws IOException {
		super();
	}

	public String decrypt(String ct) {
		ct = ct.toUpperCase().replaceAll("[^A-Z]", "");
		ArrayList<String> decs = new ArrayList<String>();
		for (int i = 2; i < Math.floor(ct.length() / 2); i++) {
			decs.add(keyTest(ct, i));
			System.out.println(keyTest(ct, i));
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


	public String keyTest(String ct, int key) {
		String[] seperated = new String[key];
		int ctlength = ct.length();
		for (int i = 0; i < key; i++) {
			seperated[i] = "";
		}


		for (int i = 0; i < ctlength; i++) {
			seperated[i % key] += ct.charAt(i);
		}

		String toReturn = "";

		for (int i = 0; i < key - 1; i++) {
			while (seperated[i].length() > 0) {
				toReturn += seperated[i].charAt(0);
				seperated[i] = seperated[i].substring(1);
			}
		}
		return toReturn;
	}
}
