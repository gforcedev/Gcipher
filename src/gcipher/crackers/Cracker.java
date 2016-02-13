package gcipher.crackers;

public abstract class Cracker {
	public TextScorer scorer;

	public Cracker(TextScorer textscorer) {
		scorer = textscorer;
	}

	public abstract String getKey(String ct);

	public abstract String solveWithKey(String key, String ct);
}
