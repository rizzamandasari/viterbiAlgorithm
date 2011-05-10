package edu.rizza.ihmm;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DNA dna = new DNA("src/seqNC_011812.txt", "src/seqNC_011812-gene.txt");
		// System.out.println(dna.getSequence());
		// System.out.println(dna.getGenes());
		HMM hmm = new HMM();
		hmm.train(dna);
	}
}
