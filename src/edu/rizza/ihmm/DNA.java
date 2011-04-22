package edu.rizza.ihmm;

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
	private final String genFilename;
	private List<Gen> genes = new ArrayList<Gen>();

	public DNA(String filename, String genFilename) {
		this.filename = filename;
		this.genFilename = genFilename;
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
					String[] codingRegion = new String[700000];
					int j = 0;
					for (int i = startCodonIndex + 3; i < stopCodonIndex - 2; i = i + 3) {
						codingRegion[j] = sequence.substring(i, i + 2);
						j++;
					}
					// System.out.println(startCodonIndex);
					gen.setCodingRegion(codingRegion);

				} else {

					Integer[] startCodon = { new Integer(startCodonIndex),
							new Integer(startCodonIndex + 1),
							new Integer(startCodonIndex + 2) };
					gen.setStartCodon(startCodon);
					Integer[] stopCodon = { new Integer(stopCodonIndex - 2),
							new Integer(stopCodonIndex - 1),
							new Integer(stopCodonIndex) };
					gen.setStopCodon(stopCodon);
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
