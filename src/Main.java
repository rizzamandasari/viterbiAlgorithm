public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DNA dna = new DNA("file/a.txt", "file/a-gene.txt");
		// DNA prediction = new DNA("file/a.txt");
		// System.out.println(dna.getSequence());
		// System.out.println(dna.getGenes());

		HMM hmm = new HMM();
		hmm.train(dna);
		// hmm.prediction(prediction);
		// System.out.println(prediction.getPredictionGenes());
		// System.out.println("===================");
		// for (Gen gen : prediction.getPredictionGenes()) {

		// System.out.println(gen.getSequence());
		// }
	}
}
