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
		System.out.println(hmm.getStartCodons().get(2).get('a'));
		// hmm.getStartCodons().get(0).set('a', 0.4);
		// hmm.getStartCodons().get(0).setA(0.4);

		for (Gen gen : dna.getGenes()) {

			System.out.println("====================");

		}

	}
}
