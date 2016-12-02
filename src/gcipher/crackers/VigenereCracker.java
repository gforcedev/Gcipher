package gcipher.crackers;

public class VigenereCracker extends Cracker {
	private CaesarCracker caesar;
	private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public VigenereCracker(TextScorer textScorer) {
		super(textScorer);
		caesar = new CaesarCracker(textScorer);
	}


	public String getKey (String ct) {
		ct = ct.toUpperCase().replaceAll("[^A-Z]", "");
		if (ct.length() > 500) {
			ct = ct.substring(0,500);
		}
		String bestKey = "";
		float bestScore = Float.NEGATIVE_INFINITY;
		for (int i = 0; i < 5; i++) {
			for (int keyLength = 2; keyLength < 20; keyLength++) {
				String thisKey = keyTest(keyLength, ct);
				String thisDec = solveWithKey(ct, thisKey);
				float thisScore = scorer.quadgramScore(thisDec);
				if (thisScore > bestScore) {
					bestScore = thisScore;
					bestKey = thisKey;
				}
			}
		}

		return bestKey;
	}

	@Override
	public String solveWithKey(String key, String ct) {
		return solveInternal(key, ct.toUpperCase().replaceAll("[^A-Z]", ""));
	}


	public String keyTest(int length, String ct) {
		String[] separated = new String[length];
		char[] key = new char[length];
		int ctLength = ct.length();
		for (int i = 0; i < length; i++) {
			separated[i] = "";
		}


		for (int i = 0; i < ctLength; i++) {
			separated[i % length] += ct.charAt(i);
		}
		for (int i = 0; i < length; i++) {
			String keyi = caesar.getKey(separated[i]);
			key[i] = alphabet.charAt(Integer.parseInt(keyi));
		}
		StringBuilder toReturn = new StringBuilder();

		for (int i = 0; i < length; i++) {
			toReturn.append(key[i]);
		}


		return toReturn.toString();
	}


	private String solveInternal(String ct, String key) {
		key = key.toUpperCase().replaceAll("[^A-Z]", "");
		ct = ct.toUpperCase().replaceAll("[^A-Z]", "");

		char[] ctArray = ct.toCharArray();
		char[] keyArray = key.toCharArray();
		StringBuilder dec = new StringBuilder();

		for (int i = 0; i < ctArray.length; i++) {
			char original = ctArray[i];
			ctArray[i] -= keyArray[i % keyArray.length] - 'A';
			if (ctArray[i] < 'A') {
				ctArray[i] += 26;
			}
			if(!Character.isAlphabetic(ctArray[i])) {
				throw new RuntimeException("Poot!");
			}
			dec.append(ctArray[i]);
		}
		return dec.toString();
	}
}
