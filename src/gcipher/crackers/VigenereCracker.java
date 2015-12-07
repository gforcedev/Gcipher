package gcipher.crackers;

import java.io.IOException;
import java.util.ArrayList;

public class VigenereCracker extends BaseCracker {
	public VigenereCracker() throws IOException {
		super();
	}
	
	@Override
	public String decrypt(String ct) {
		ct = ct.toUpperCase().replaceAll("[^A-Z]", "");
		ArrayList<String> decs = new ArrayList<String>();
		for (int i = 1; i < 20; i++) {
			decs.add(keyTest(i, ct));
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
	
	
	
	public String caesar(String ct) {
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
	public String keyTest(int length, String ct) {
		String[] seperated = new String[length];
		int ctlength = ct.length();
		for (int i = 0; i < length; i++) {
			seperated[i] = "";
		}
			
		
		for (int i = 0; i < ctlength; i++) {
			seperated[i % length] += ct.charAt(i);
		}
		for (int i = 0; i < length; i++) {
			seperated[i] = caesar(seperated[i]);
		}
		String toReturn = "";

		while (seperated[length - 1].length() > 0) {
			for (int i = 0; i < length; i++) {
				toReturn += seperated[i].charAt(0);
				seperated[i] = seperated[i].substring(1);
			}
		}
		
		
		return toReturn;
	}
}
