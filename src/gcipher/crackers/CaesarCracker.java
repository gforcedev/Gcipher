package gcipher.crackers;

public class CaesarCracker extends Cracker {
	public CaesarCracker(TextScorer textScorer) {
		super(textScorer);
	}

	public String getKey(String ct) {
		ct = ct.toUpperCase().replaceAll("[^A-Z]", "");
		int bestKey = 0;
		float bestScore = Float.NEGATIVE_INFINITY;
		for (int i = 0; i < 26; i++) {
			String thisDec = solveWithKey(ct, Integer.toString(i));
			float thisScore = scorer.monogramScore(thisDec);
			if (thisScore > bestScore) {
				bestScore = thisScore;
				bestKey = i;
			}
		}

		return Integer.toString(bestKey);
	}

	public String solveWithKey(String ct, String key) {
		ct = ct.toUpperCase().replaceAll("[^A-Z]", "");
		try {
			int i = Integer.parseInt(key);
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
			thisChar -= intKey;
			if (thisChar < 'A') {
				thisChar += 26;
			}
			dec += (char) thisChar;
		}

		return dec;
	}
}
