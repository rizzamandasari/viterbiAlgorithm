package edu.rizza.ihmm;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DNA dna = new DNA("src/Sequence.txt", "src/seqNC_011812-gene.txt");

		System.out.println(dna.getSequence());
		System.out.println(dna.getGenes());

	}
}