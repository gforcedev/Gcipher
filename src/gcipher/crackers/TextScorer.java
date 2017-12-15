package gcipher.crackers;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TextScorer {
	private final float[] quadgrams;
	private final HashMap<String, Float> monograms;

	private String[] oneWords;
	private double[] oneWordProbs;
	private List<String> oneWordList;

	private String[] twoWords;
	private double[] twoWordProbs;
	private List<String> twoWordList;

	private double[] uWordProbs; //u is unseen
	private double count = 1024908267229l;

	public TextScorer() {
		try {
			quadgrams = loadQuadgrams();
			monograms = loadMonograms();
			loadOneWords();
//			loadTwoWords();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public float quadgramScore(String str) {
		str = str.toUpperCase().replaceAll("[^A-Z]", "");
		float fitness = 0;
		int length = str.length();
		for (int i = 0; i < length - 3; i++) {
			fitness += quadgrams[offset(str, i, 4)];
		}
		return fitness;
	}

	public float monogramScore(String str) {
		str = str.toUpperCase().replaceAll("[^A-Z]", "");
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

	public String wordGuess(String str) {
		str = str.toLowerCase().replaceAll("[^a-z]", "");
		StringBuilder ret = new StringBuilder();

		int maxWordLength = Math.min(20, str.length());

		String[][] segs = segment(str, maxWordLength);
		for (int i = 0; i < segs.length; i++) {

		}

		return ret.toString();
	}

	private String[][] segment(String str, int max) {
		String[][] ret = new String[max][2];
		for (int i = 0; i < max; i++) {
			ret[i][0] = str.substring(0, i);
			ret[i][1] = str.substring(i);
		}
		return ret;
	}

	private double getProbs(String[][] segs) {
		double[] ret = new double[segs.length];
		for (int i = 0; i < segs.length; i++) {
			double first;
			double second;
			try {
				first = oneWordProbs[oneWordList.lastIndexOf(segs[i][0])];
			} catch(ArrayIndexOutOfBoundsException e) {
				first = uWordProbs[segs[i][0].length()];
			}
			return first;
		}
		return 0d;
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
		URL url = TextScorer.class.getResource("count_1w.txt");
		File file = new File(url.getPath());

		List<String> lines = Files.readAllLines(Paths.get("count_1w.txt"));

		int linelength = lines.size();
		String[] words = new String[linelength / 2];
		double[] probabilities = new double[linelength / 2];

		for (int i = 0; i < (linelength / 2); i++) {
			words[i] = lines.get(i * 2);
			probabilities[i] = Math.log10(Long.parseLong(lines.get(i * 2 + 1)));
		}

		oneWords = words;
		oneWordProbs = probabilities;
		oneWordList = Arrays.asList(oneWords);

		uWordProbs = new double[50];
		for (int i = 0; i < 50; i++) {
			uWordProbs[i] = (10 / count * Math.pow(10, i));
		}
	}

	public void loadTwoWords() throws IOException {
		URL url = TextScorer.class.getResource("2WordScores.txt");
		File file = new File(url.getPath());

		List<String> lines = Files.readAllLines(Paths.get("2WordScores.txt"));

		int linelength = lines.size();
		String[] words = new String[linelength / 2];
		double[] probabilities = new double[linelength / 2];

		for (int i = 0; i < (linelength / 2); i++) {
			words[i] = lines.get(i * 2);
			probabilities[i] = Math.log10(Long.parseLong(lines.get(i * 2 + 1)));
		}

		twoWords = words;
		twoWordProbs = probabilities;
		twoWordList = Arrays.asList(twoWords);
	}
}
