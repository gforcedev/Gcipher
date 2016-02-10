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
			String thisDec = "";
			int ctLength = ct.length();
			for (int n = 0; n < ctLength; n++) {
				int thisChar = ct.charAt(n);
				thisChar += i;
				if (thisChar > 'Z') {
					thisChar -= 26;
				}
				thisDec += (char) thisChar;
			}
			decs.add(thisDec);
		}
		for (String thisDec : decs) {
			if (monogramScore(thisDec) > monogramScore(bestDec)) {
				bestDec = thisDec;
			}
		}
		return bestDec;
	}
}
