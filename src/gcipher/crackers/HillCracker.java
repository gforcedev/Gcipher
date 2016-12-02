package gcipher.crackers;

import javafx.scene.control.TextInputDialog;
import org.la4j.LinearAlgebra;
import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;

import java.util.Optional;

/**
 * Created by 14ClarTh on 21/11/2016.
 */
public class HillCracker extends Cracker {
	private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public HillCracker(TextScorer textScorer) {
		super(textScorer);
	}

	@Override
	public String getKey(String ct) {
		TextInputDialog dialog = new TextInputDialog("harry");
		dialog.setTitle("Input");
		dialog.setHeaderText("Crib Request");
		dialog.setContentText("Please enter suspected crib");
		Optional<String> result = dialog.showAndWait();

		// The Java 8 way to get the response value (with lambda expression).
		result.ifPresent(name -> System.out.println("Your name: " + name));

		String crib = result.get().toUpperCase();

		if (crib.length() > 5) {
			crib = crib.substring(0, 5);
		}

		ct = ct.toUpperCase().replaceAll("[^A-Z]", "");

		if (ct.length() % 2 != 0) {
			ct += "Z";
		}
		String[] ctArray = new String[ct.length() / 2];
		for (int i = 0; i < ct.length(); i += 2) {
			ctArray[i / 2] = "" + ct.charAt(i) + ct.charAt(i + 1);
		}

		Matrix bestKey = null;
		float bestScore = Float.NEGATIVE_INFINITY;
		String bestDec = "";

		for (int i = 0; i < ct.length() - 5; i++) {
			Matrix key = null;
			if (i % 2 == 0) {
				Matrix matrixA = new Basic2DMatrix(new double[][]{
						{alphabet.indexOf(crib.charAt(0)), alphabet.indexOf(crib.charAt(2))},
						{alphabet.indexOf(crib.charAt(1)), alphabet.indexOf(crib.charAt(3))}
				});

				Matrix matrixB = new Basic2DMatrix(new double[][]{
						{alphabet.indexOf(ctArray[i / 2].charAt(0)), alphabet.indexOf(ctArray[i / 2 + 1].charAt(0))},
						{alphabet.indexOf(ctArray[i / 2].charAt(1)), alphabet.indexOf(ctArray[i / 2 + 1].charAt(1))}
				});

				double d = (matrixB.get(0, 0) * matrixB.get(1, 1) - matrixB.get(0, 1) * matrixB.get(1, 0)) % 26;
				d = Math.pow(d, -1);

				Matrix adj = new Basic2DMatrix(new double[][]{
						{matrixB.get(1, 1), matrixB.get(0, 1) * -1},
						{matrixB.get(1, 0) * -1, matrixB.get(0, 0)}
				});

				Matrix inverse = adj.multiply(d);

				for (int x = 0; x < 2; x++) {
					for (int y = 0; y < 2; y++) {
						if (inverse.get(x, y) < 0) {
							inverse.set(x, y, inverse.get(x, y) + 26);
						}
					}
				}

				Matrix needsMod = matrixA.multiply(inverse);

				key = new Basic2DMatrix(new double[][]{
						{needsMod.get(0, 0) % 26, needsMod.get(0, 1) % 26},
						{needsMod.get(1, 0) % 26, needsMod.get(1, 1) % 26}
				});

				for (int x = 0; x < 2; x++) {
					for (int y = 0; y < 2; y++) {
						if (key.get(x, y) < 0) {
							key.set(x, y, key.get(x, y) + 26);
						}
					}
				}
				///////////////////////////////////////////////////////////////////////////////////////////////////////
			} else {
				///////////////////////////////////////////////////////////////////////////////////////////////////////
				Matrix matrixA = new Basic2DMatrix(new double[][]{
						{alphabet.indexOf(crib.charAt(1)), alphabet.indexOf(crib.charAt(3))},
						{alphabet.indexOf(crib.charAt(2)), alphabet.indexOf(crib.charAt(4))}
				});

				Matrix matrixB = new Basic2DMatrix(new double[][]{
						{alphabet.indexOf(ctArray[(i + 1) / 2].charAt(0)), alphabet.indexOf(ctArray[(i + 1) / 2 + 1].charAt(0))},
						{alphabet.indexOf(ctArray[(i + 1) / 2].charAt(1)), alphabet.indexOf(ctArray[(i + 1) / 2 + 1].charAt(1))}
				});

				double d = (matrixB.get(0, 0) * matrixB.get(1, 1) - matrixB.get(0, 1) * matrixB.get(1, 0)) % 26;
				d = Math.pow(d, -1);

				Matrix adj = new Basic2DMatrix(new double[][]{
						{matrixB.get(1, 1), matrixB.get(0, 1) * -1},
						{matrixB.get(1, 0) * -1, matrixB.get(0, 0)}
				});

				Matrix inverse = adj.multiply(d);

				for (int x = 0; x < 2; x++) {
					for (int y = 0; y < 2; y++) {
						if (inverse.get(x, y) < 0) {
							inverse.set(x, y, inverse.get(x, y) + 26);
						}
					}
				}

				Matrix needsMod = matrixA.multiply(inverse);

				key = new Basic2DMatrix(new double[][]{
						{needsMod.get(0, 0) % 26, needsMod.get(0, 1) % 26},
						{needsMod.get(1, 0) % 26, needsMod.get(1, 1) % 26}
				});

				for (int x = 0; x < 2; x++) {
					for (int y = 0; y < 2; y++) {
						if (key.get(x, y) < 0) {
							key.set(x, y, key.get(x, y) + 26);
						}
					}
				}
			}

			String thisDec = withKey(key, ct);
			float thisScore = scorer.quadgramScore(thisDec);
			if (thisScore > bestScore) {
				bestScore = thisScore;
				bestKey = key;
				bestDec = thisDec;
			}
		}


		return Double.toString(bestKey.get(0, 0)) + "," + Double.toString(bestKey.get(0, 1)) +
				"," + Double.toString(bestKey.get(1, 0)) + "," + Double.toString(bestKey.get(1, 0)) + ",";
	}

	@Override
	public String solveWithKey(String ct, String key) {
		String[] mt = key.split(",");
		Matrix m = new Basic2DMatrix(new double[][] {
				{Double.parseDouble(mt[0]), Double.parseDouble(mt[1])},
				{Double.parseDouble(mt[2]), Double.parseDouble(mt[3])}
		});

		return withKey(m, ct);
	}

	private String withKey(Matrix key, String ct) {
		ct = ct.toUpperCase().replaceAll("[^A-Z]", "");

		if (ct.length() % 2 != 0) {
			ct += "Z";
		}
		String[] ctArray = new String[ct.length() / 2];
		for (int i = 0; i < ct.length(); i += 2) {
			ctArray[i / 2] = "" + ct.charAt(i) + ct.charAt(i + 1);
		}

		StringBuilder answer = new StringBuilder();
		for (int i = 0; i < ctArray.length; i++) {
			Matrix a = new Basic2DMatrix(new double[][]{
					{alphabet.indexOf(ctArray[i].charAt(0))},
					{alphabet.indexOf(ctArray[i].charAt(1))}
			});
			Matrix b = key.multiply(a);

			Matrix c = new Basic2DMatrix(new double[][]{
					{Math.abs(b.get(0, 0) % 26)},
					{Math.abs(b.get(1, 0) % 26)}
			});
			if (c.get(0, 0) < 0) {
				c.set(0, 0, c.get(0, 0) + 26);
			}
			if (c.get(1, 0) < 0) {
				c.set(1, 0, c.get(0, 0) + 26);
			}

			answer.append(alphabet.charAt((int) c.get(0, 0)));
			answer.append(alphabet.charAt((int) c.get(1, 0)));
		}


		return answer.toString();
	}
}
