public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// DNA dna = new DNA("file/seqNC_011812.txt",
		// "file/seqNC_011812-gene.txt");
		// System.out.println(dna.getSequence());
		// System.out.println(dna.getGenes());
		// HMM hmm = new HMM();
		// hmm.fwdBwd(dna);
		// hmm.train(dna);
		DNA prediction = new DNA("file/a.txt");
		// System.out.println(prediction.getPredictionGenes());
		System.out.println("===================");
		for (Gen gen : prediction.getPredictionGenes()) {

			System.out.println(gen.getSequence());
		}
	}
}
