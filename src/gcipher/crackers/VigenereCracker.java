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

		String bestKey = "";
		float bestScore = Float.NEGATIVE_INFINITY;

		for (int i = 2; i < 30; i++) {
			StringBuilder[] partials = new StringBuilder[i];

			for (int x = 0; x < i; x++) {
				partials[x] = new StringBuilder();
			}
			for (int x = 0; x < ct.length(); x++) {
				partials[x % i].append(ct.charAt(x));
			}


			StringBuilder key = new StringBuilder();
			for (int x = 0; x < i; x++) {
				key.append(alphabet.charAt((Integer.parseInt(caesar.getKey(partials[x].toString())) ) % 26));
			}
			String keyString = key.toString();
			float thisScore = scorer.quadgramScore(solveWithKey(ct, keyString));
			if (thisScore > bestScore) {
				bestKey = keyString;
				bestScore = thisScore;
			}
		}
		return bestKey;
	}

	@Override
	public String solveWithKey(String ct, String key) {
		ct = ct.toUpperCase().replaceAll("[^A-Z]", "");

		StringBuilder dec = new StringBuilder();
		for (int i = 0; i < ct.length(); i++) {
//			System.out.println(Math.floorMod((ct.charAt(i) - 'A') - (key.charAt(i % key.length()) - 'A'), 26));
			dec.append((char) (Math.floorMod((ct.charAt(i) - 'A') - (key.charAt(i % key.length()) - 'A'), 26) + 'A'));
		}
		return dec.toString();
	}

}
