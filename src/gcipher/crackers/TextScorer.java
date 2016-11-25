package gcipher.crackers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class TextScorer {
	private final float[] quadgrams;
	private final HashMap<String, Float> monograms;
	private String[] oneWords;
	private long[] oneWordProbs;
	private String[] twoWords;
	private long[] twoWordProbs;

	public TextScorer() {
		try {
			quadgrams = loadQuadgrams();
			monograms = loadMonograms();
			loadOneWords();
			loadTwoWords();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	public String decrypt(String ct) {
		return "";
	}

	public String solveWithKey(String ct, String key) {
		return "";
	}

	public float quadgramScore(String str) {
		float fitness = 0;
		int length = str.length();
		for (int i = 0; i < length - 3; i++) {
			fitness += quadgrams[offset(str, i, 4)];
		}
		return fitness;
	}

	public float monogramScore(String str) {
		float fitness = 0;
		int length = str.length();
		for (int i = 0; i < length; i++) {
			String thisMono = str.substring(i, i + 1);
			fitness += monograms.get(thisMono);
		}

		return fitness;
	}

	private int offset(String text, int offset, int length) {
		int sum = 0;
		for (int i = 0; i < length; i++) {
			sum *= 26;
			sum += text.charAt(offset + i) - 'A';
		}

		return sum;
	}

	private float[] loadQuadgrams() throws IOException {
		URL url = TextScorer.class.getResource("english_quadgrams.txt");
		File file = new File(url.getPath());

		double sum = 0;
		List<String> lines = Files.readAllLines(Paths.get("english_quadgrams.txt"));
		float[] quadgrams = new float[26 * 26 * 26 * 26];

		int linelength = lines.size();
		for (int i = 0; i < linelength; i += 2) {
			float single = Float.parseFloat(lines.get(i + 1));
			quadgrams[offset(lines.get(i), 0, 4)] = (float) Math.log10(single);
			sum += single;
		}

		float quadDefault = (float) Math.log10(0.01 / sum);

		for (int i = 0; i < quadgrams.length; i += 2) {
			if (quadgrams[i] == 0) quadgrams[i] = quadDefault;
		}

		return quadgrams;
	}

	public HashMap<String, Float> loadMonograms() throws IOException {
		URL url = TextScorer.class.getResource("english_monograms.txt");
		File file = new File(url.getPath());

		List<String> lines = Files.readAllLines(Paths.get("english_monograms.txt"));

		HashMap<String, Float> monograms = new HashMap<>();
		int linelength = lines.size();
		for (int i = 0; i < linelength; i += 2) {
			monograms.put(lines.get(i), (float) Math.log10(Float.parseFloat(lines.get(i + 1))));
		}

		return monograms;
	}

	public void loadOneWords() throws IOException {
		URL url = TextScorer.class.getResource("1WordScores.txt");
		File file = new File(url.getPath());

		List<String> lines = Files.readAllLines(Paths.get("1WordScores.txt"));

		int linelength = lines.size();
		String[] words = new String[linelength / 2];
		long[] probabilities = new long[linelength / 2];

		for (int i = 0; i < (linelength / 2); i++) {
			words[i] = lines.get(i * 2);
			probabilities[i] = Long.parseLong(lines.get(i * 2 + 1));
		}

		oneWords = words;
		oneWordProbs = probabilities;
	}

	public void loadTwoWords() throws IOException {
		URL url = TextScorer.class.getResource("2WordScores.txt");
		File file = new File(url.getPath());

		List<String> lines = Files.readAllLines(Paths.get("2WordScores.txt"));

		int linelength = lines.size();
		String[] words = new String[linelength / 2];
		long[] probabilities = new long[linelength / 2];

		for (int i = 0; i < (linelength / 2); i++) {
			words[i] = lines.get(i * 2);
			System.out.println(i);
			probabilities[i] = Long.parseLong(lines.get(i * 2 + 1));
		}

		twoWords = words;
		twoWordProbs = probabilities;
	}
}
