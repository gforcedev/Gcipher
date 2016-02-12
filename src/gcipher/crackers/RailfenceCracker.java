package gcipher.crackers;

import java.io.IOException;
import java.util.ArrayList;

public class RailfenceCracker extends TextScorer {
	public RailfenceCracker() throws IOException {
		super();

	}

	public String decrypt(String ct) {
		ct = ct.toUpperCase().replaceAll("[^A-Z]", "");
		ArrayList<String> decs = new ArrayList<String>();
		int ctlength = ct.length();
		for (int i = 0; i < ctlength / 2; i++) {
			decs.add(solveWithKey(ct, Integer.toString(i)));
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

	@Override
	public String solveWithKey(String ct, String key) {
		key = key.toUpperCase().replaceAll("\\D", "");
		if (key.length() == 0) {
			return "Key must be a number";
		}
		int intKey = Integer.parseInt(key);
		if (intKey < 2) {
			return ct;
		}
		int ctlength = ct.length();

		char[][] deca = new char[intKey][ctlength];
		for (int i = 0; i < intKey; i++) {
			for (int n = 0; n < deca[i].length; n++) {
				deca[i][n] = '.';
			}
		}
		int row = 0;
		int col = 0;
		int dir = 1; //dir is direction

		while (col < ctlength) {

			deca[row][col] = '_';

			col++;
			row += dir;
			dir *= (((row % (intKey - 1)) == 0) ? -1 : 1);
		}

		for (row = 0; row < intKey; row++) {
			for (col = 0; col < ctlength; col++) {
				if (deca[row][col] == '_') {
					deca[row][col] = ct.charAt(0);
					ct = ct.substring(1);
				}
			}
		}


		row = 0;
		col = 0;
		dir = 1; //dir is direction

		String dec = "";

		while (col < ctlength) {
			if (deca[row][col] != '.') {
				dec += deca[row][col];
			}
			col++;
			row += dir;
			dir *= (((row % (intKey - 1)) == 0) ? -1 : 1);
		}

		return dec;
	}
}
