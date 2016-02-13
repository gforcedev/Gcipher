package gcipher.crackers;

public class VigenereCracker extends Cracker {
	private CaesarCracker caesar;
	private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public VigenereCracker(TextScorer textScorer) {
		super(textScorer);
		caesar = new CaesarCracker(textScorer);
	}


	public String getKey(String ct) {
		ct = ct.toUpperCase().replaceAll("[^A-Z]", "");
		String bestKey = "";
		float bestScore = Float.NEGATIVE_INFINITY;
		for (int i = 2; i < 20; i++) {
			String thisKey = keyTest(i, ct);
			String thisDec = solveWithKey(ct, thisKey);
			float thisScore = scorer.quadgramScore(thisDec);

			if (thisScore > bestScore) {
				bestScore = thisScore;
				bestKey = thisKey;
			}
		}
		return bestKey;
	}


	public String keyTest(int length, String ct) {
		String[] seperated = new String[length];
		char[] key = new char[length];
		int ctlength = ct.length();
		for (int i = 0; i < length; i++) {
			seperated[i] = "";
		}


		for (int i = 0; i < ctlength; i++) {
			seperated[i % length] += ct.charAt(i);
		}
		for (int i = 0; i < length; i++) {
			String keyi = caesar.getKey(seperated[i]);
			key[i] = alphabet.charAt(Integer.parseInt(keyi));
		}
		StringBuilder toReturn = new StringBuilder();

		for (int i = 0; i < length; i++) {
			toReturn.append(key[i]);
		}


		return toReturn.toString();
	}


	public String solveWithKey(String ct, String key) {
		key = key.toUpperCase().replaceAll("[^A-Z]", "");
		char[] ctArray = ct.toCharArray();
		char[] keyArray = key.toCharArray();
		StringBuilder dec = new StringBuilder();

		for (int i = 0; i < ctArray.length; i++) {
			ctArray[i] -= keyArray[i % keyArray.length] - 'A';
			if (ctArray[i] < 'A') {
				ctArray[i] += 26;
			}
			dec.append(ctArray[i]);
		}
		return dec.toString();
	}
}
