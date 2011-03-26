package edu.rizza.ihmm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class DNA {

	private String sequence;
	private char[] sequenceArray;
	private String filename;
	private List<Gen> genes = null;

	public DNA(String filename) {
		preprocessing(filename);
	}

	private void preprocessing(String filename) {
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
		if (genes != null) {
			return genes;
		}
		StringBuilder sb = null;

		for (int i = 0; i < (sequenceArray.length - 2); i++) {
			sb = new StringBuilder(sequenceArray[i]);
			String str = sb.append(sequenceArray[i + 1]).append(i + 2)
					.toString();
			// cari startcodon
			if (str.equalsIgnoreCase("atg") || str.equalsIgnoreCase("gtg")) {
				Integer[] startCodon = { new Integer(i), new Integer(i + 1),
						new Integer(i + 2) };
				genes.add(new Gen(startCodon));
			}

		}

		return genes;
	}

	public stopCodon() {
		StringBuilder sb = null;
		for (int i = 0; i < (sequenceArray.length - 2); i++) {
			sb = new StringBuilder(sequenceArray[i]);
			String str = sb.append(sequenceArray[i + 1]).append(i + 2)
					.toString();
			if (str.equalsIgnoreCase("tag") || str.equalsIgnoreCase("tga")
					|| str.equalsIgnoreCase("taa")) {
				Integer[] startCodon = { new Integer(i), new Integer(i + 1),
						new Integer(i + 2) };
				genes.add(new Gen(stopCodon));
			}
			return;
		}
	}

}
