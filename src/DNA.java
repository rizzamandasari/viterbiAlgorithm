import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DNA {

	private String sequence;
	private char[] sequenceArray;
	private String filename;
	private String genFilename;
	private List<Gen> genes = new ArrayList<Gen>();
	private final List<Gen> predictionGenes = new ArrayList<Gen>();

	public DNA(String filename, String genFilename) {
		this.filename = filename;
		this.genFilename = genFilename;
		preprocessing();

	}

	public DNA(String filename) {
		this.filename = filename;
		preprocessing();
	}

	private void preprocessing() {
		FileInputStream finput = null;

		// membuka file
		try {
			finput = new FileInputStream(filename);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(finput));
			String line = null;
			sequence = "";

			while ((line = br.readLine()) != null) {
				sequence += line.replaceAll("[\n0-9 ]", "");

			}
			// System.out.println(dna);
			finput.close();

			sequenceArray = sequence.toCharArray();

		} catch (FileNotFoundException fnfe) {
			System.out.println("File not found");
			return;
		} catch (IOException ioe) {
			System.out.println("Error IO");
			return;
		}
	}

	public void loadGenes(String filename) {

	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public char[] getSequenceArray() {
		return sequenceArray;
	}

	public void setSequenceArray(char[] sequenceArray) {
		this.sequenceArray = sequenceArray;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setGenes(List<Gen> genes) {
		this.genes = genes;
	}

	public List<Gen> getGenes() {
		if (genes.size() != 0) {
			return genes;
		}
		FileInputStream finput = null;
		try {
			finput = new FileInputStream(genFilename);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(finput));
			String line = null;

			while ((line = br.readLine()) != null) {
				Gen gen = new Gen();
				String[] str = line.split("-", 2);
				int startCodonIndex = Integer.parseInt(str[0]);
				int stopCodonIndex = Integer.parseInt(str[1]);
				if (stopCodonIndex > startCodonIndex) {
					Integer[] startCodon = { new Integer(startCodonIndex),
							new Integer(startCodonIndex + 1),
							new Integer(startCodonIndex + 2) };
					gen.setStartCodon(startCodon);
					Integer[] stopCodon = { new Integer(stopCodonIndex - 2),
							new Integer(stopCodonIndex - 1),
							new Integer(stopCodonIndex) };
					gen.setStopCodon(stopCodon);
					String[] codingRegion = new String[1000000];
					int j = 0;
					for (int i = startCodonIndex + 3; i < stopCodonIndex - 2; i = i + 3) {
						codingRegion[j] = sequence.substring(i, i + 2);
						j++;
					}
					// System.out.println(startCodonIndex);
					gen.setCodingRegion(codingRegion);
					gen.setSequence(sequence.substring(startCodonIndex,
							stopCodonIndex));

				} else {

					Integer[] startCodon = { new Integer(startCodonIndex),
							new Integer(startCodonIndex + 1),
							new Integer(startCodonIndex + 2) };
					gen.setStartCodon(startCodon);
					Integer[] stopCodon = { new Integer(stopCodonIndex - 2),
							new Integer(stopCodonIndex - 1),
							new Integer(stopCodonIndex) };
					gen.setStopCodon(stopCodon);
					gen.setSequence(sequence.substring(stopCodonIndex,
							startCodonIndex));

				}
				genes.add(gen);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return genes;
	}

	public List<Gen> getPredictionGenes() {
		// cari startcodon
		StringBuilder sb = null;
		for (int i = 0; i < sequenceArray.length; i++) {
			if (i < sequenceArray.length - 2) {
				sb = new StringBuilder();
				sb.append(sequenceArray[i]).append(sequenceArray[i + 1])
						.append(sequenceArray[i + 2]);
				if (sb.toString().equalsIgnoreCase("atg")
						&& i < sequenceArray.length - 180) {
					Integer[] startCodon = { new Integer(i),
							new Integer(i + 1), new Integer(i + 2) };
					for (int j = i + 180; (j < i + 2999)
							&& (j < sequenceArray.length); j++) {
						sb = new StringBuilder();
						sb.append(sequenceArray[j - 2]).append(
								sequenceArray[j - 1]).append(sequenceArray[j]);
						if (sb.toString().equalsIgnoreCase("tga")
								|| sb.toString().equalsIgnoreCase("taa")
								|| sb.toString().equalsIgnoreCase("tag")) {
							Gen gen = new Gen();
							gen.setStartCodon(startCodon);
							Integer[] stopCodon = { new Integer(j - 2),
									new Integer(j - 1), new Integer(j) };
							gen.setStopCodon(stopCodon);
							// coding region
							int x = 0;
							String[] codingRegion = new String[5000];
							for (int k = i + 3; k < j - 2; k = k + 3) {

								codingRegion[x] = sequence.substring(k, k + 2);
								x++;
							}
							gen.setSequence(sequence.substring(i, j + 1));
							predictionGenes.add(gen);

						}
					}

				}

			}

		}
		return predictionGenes;
	}
	/*
	 * public void stopCodon() { StringBuilder sb = null; for (int i = 0; i <
	 * (sequenceArray.length - 2); i++) { sb = new
	 * StringBuilder(sequenceArray[i]); String str = sb.append(sequenceArray[i +
	 * 1]).append(i + 2) .toString(); if (str.equalsIgnoreCase("tag") ||
	 * str.equalsIgnoreCase("tga") || str.equalsIgnoreCase("taa")) { Integer[]
	 * startCodon = { new Integer(i), new Integer(i + 1), new Integer(i + 2) };
	 * genes.add(new Gen(stopCodon)); // error dsni } return; }
	 */
}
