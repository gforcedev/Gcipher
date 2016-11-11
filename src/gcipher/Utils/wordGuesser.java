package gcipher.Utils;

import gcipher.crackers.Cracker;
import gcipher.crackers.TextScorer;

/**
 * Created by 14ClarTh on 11/11/2016.
 */
public class WordGuesser extends Cracker {
	public WordGuesser(TextScorer textScorer) {
		super(textScorer);
	}

	@Override
	public String getKey(String ct) {
		return null;
	}

	@Override
	public String solveWithKey(String key, String ct) {
		return null;
	}

	public String guess(String text) {

		return null;
	}
}
