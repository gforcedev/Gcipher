package gcipher.crackers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;


public class BaseCracker {
	private final HashMap<String, Float> quadgrams;
	private final HashMap<String, Float> monograms;
	private float quadDefault;
	
	public String enc;
	public String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public BaseCracker() throws IOException {
		this.quadgrams = loadQuadgrams();
		this.monograms = loadMonograms();
	}
	public String decrypt(String ct) {
		return "";
	}

	public float quadgramScore(String str) {
		float fitness = 0;
		int length = str.length();
		for (int i = 0; i < length - 3; i++) {
			String thisQuad = str.substring(i, i + 4);
			Float score = quadgrams.get(thisQuad);
			fitness += score == null ? quadDefault : score.floatValue();
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

	private HashMap<String, Float> loadQuadgrams() throws IOException {
		URL url = getClass().getResource("english_quadgrams.txt");
		File file = new File(url.getPath());

		double sum = 0;
		List<String> lines = Files.readAllLines(Paths.get(file.getAbsolutePath()));

		HashMap<String, Float> quadgrams = new HashMap<String, Float>();
		int linelength = lines.size();
		for (int i = 0; i < linelength; i += 2) {
			float single = Float.parseFloat(lines.get(i + 1));
			quadgrams.put(lines.get(i), (float) Math.log10(single));
			sum += single;
		}

		quadDefault = (float)Math.log10(0.01 / sum);
		return quadgrams;
	}

	public HashMap<String, Float> loadMonograms() throws IOException {
		URL url = getClass().getResource("english_monograms.txt");
		File file = new File(url.getPath());

		List<String> lines = Files.readAllLines(Paths.get(file.getAbsolutePath()));

		HashMap<String, Float> monograms = new HashMap<String, Float>();
		int linelength = lines.size();
		for (int i = 0; i < linelength; i += 2) {
			monograms.put(lines.get(i), (float) Math.log10(Float.parseFloat(lines.get(i + 1))));
		}

		return monograms;
	}
}