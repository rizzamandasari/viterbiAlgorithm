import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DNA dna = new DNA("file/seqNC005327.txt", "file/seqNC005327-gene.txt");
		// DNA prediction = new DNA("file/a.txt");
		// System.out.println(dna.getSequence());
		// System.out.println(dna.getGenes());

		// use buffering

		HMM hmm = null;
		try {
			InputStream file = new FileInputStream("save.txt");
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);
			hmm = (HMM) input.readObject();
		} catch (FileNotFoundException e1) {
			hmm = new HMM();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// HMM hmm = new HMM();
		hmm.train(dna);
		// hmm.prediction(prediction);
		// System.out.println(prediction.getPredictionGenes());
		// System.out.println("===================");
		// for (Gen gen : prediction.getPredictionGenes()) {

		// System.out.println(gen.getSequence());
		// }

		try {
			FileOutputStream fos = new FileOutputStream("file/save.txt");
			ObjectOutputStream obj_out = new ObjectOutputStream(fos);
			obj_out.writeObject(hmm);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
