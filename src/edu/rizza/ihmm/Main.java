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

		// System.out.println(hmm);
		// hmm.getStartCodons().get(0).set('a', 0.4);
		// hmm.getStartCodons().get(0).setA(0.4);

		System.out.println(hmm.getStartCodons().get(0).get('a'));
		System.out.println(hmm.getStartCodons().get(0).get('t'));
		System.out.println(hmm.getStartCodons().get(0).get('c'));
		System.out.println(hmm.getStartCodons().get(0).get('g'));

		
		
		

		/*
		 * startcodon index1 if( .equals('a')){ double p =
		 * hmm.getStartCodons().get(0).get('a'); } else if( .equals('t')){
		 * double p = hmm.getStartCodons().get(0).get('t'); } else if(
		 * .equals('c')){ double p = hmm.getStartCodons().get(0).get('c'); }
		 * else if( .equals('g')){ double p =
		 * hmm.getStartCodons().get(0).get('g'); } // startcodon index2 if(
		 * .equals('a')){ double q = hmm.getStartCodons().get(1).get('a'); }
		 * else if( .equals('t')){ double q =
		 * hmm.getStartCodons().get(1).get('t'); } else if( .equals('c')){
		 * double q = hmm.getStartCodons().get(1).get('c'); } else if(
		 * .equals('g')){ double q = hmm.getStartCodons().get(1).get('g'); }
		 * 
		 * // startcodon index3
		 * 
		 * if( .equals('a')){ double r = hmm.getStartCodons().get(2).get('a');
		 * }else if( .equals('t')){ double r =
		 * hmm.getStartCodons().get(2).get('t'); }else if( .equals('c')){ double
		 * r = hmm.getStartCodons().get(2).get('c'); }else if( .equals('g')){
		 * double r = hmm.getStartCodons().get(2).get('g'); }
		 * 
		 * double p, q, r; double alpha1 = p * q * r;
		 */
		
		  for (Gen gen : dna.getGenes()) { 
			  System.out.println(gen.getSequence().substring(int startCodonIndex,int stopCodonIndex));
		  }
		 

		// System.out.println("====================");

	}
}
