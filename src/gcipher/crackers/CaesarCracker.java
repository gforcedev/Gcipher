package gcipher.crackers;

import java.io.IOException;
import java.util.ArrayList;

public class CaesarCracker extends BaseCracker {
	public CaesarCracker(String str) throws IOException {
		this.enc = str.toUpperCase().replaceAll("[^A-Z]", "");
	}
	
	public String decrypt() {
		String bestDec = this.enc;
		ArrayList<String> decs = new ArrayList<String>();
		for (int i = 0; i < 26; i++) {
			String thisDec = "";
			int encLength = enc.length();
			for (int n = 0; n < encLength; n++) {
				int thisChar = enc.charAt(n);
				thisChar += i;
				if (thisChar > 'Z') {
					thisChar -= 26;
				}
				thisDec += (char)thisChar;
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
