package edu.rizza.ihmm;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DNA dna = new DNA("src/Sequence.txt");

		System.out.println(dna.getSequence());

	}

}
