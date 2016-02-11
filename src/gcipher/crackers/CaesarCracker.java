package gcipher.crackers;

import java.io.IOException;
import java.util.ArrayList;

public class CaesarCracker extends BaseCracker {
	public CaesarCracker() throws IOException {
		super();
	}

	@Override
	public String decrypt(String ct) {
		ct = ct.toUpperCase().replaceAll("[^A-Z]", "");
		String bestDec = ct;
		ArrayList<String> decs = new ArrayList<String>();
		for (int i = 0; i < 26; i++) {
			decs.add(solveWithKey(ct, Integer.toString(i)));
		}
		for (String thisDec : decs) {
			if (monogramScore(thisDec) > monogramScore(bestDec)) {
				bestDec = thisDec;
			}
		}
		return bestDec;
	}

	@Override
	public String solveWithKey(String ct, String key) {
		ct = ct.toUpperCase().replaceAll("[^A-Z]", "");
		try {
			int intKey = Integer.parseInt(key);
		} catch (Exception e) {
			return "key should be a number smaller that 26";
		}
		int intKey = Integer.parseInt(key);
		if (intKey > 26) {
			return "key should be a number smaller that 26";
		}

		int ctLength = ct.length();
		String dec = "";


		for (int n = 0; n < ctLength; n++) {
			int thisChar = ct.charAt(n);
			thisChar += intKey;
			if (thisChar > 'Z') {
				thisChar -= 26;
			}
			dec += (char) thisChar;
		}

		return dec;
	}
}
