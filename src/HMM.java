import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HMM implements Serializable {

	private final List<State> startCodons = new ArrayList<State>();
	private final List<State> stopCodons = new ArrayList<State>();
	private final List<State> codingRegionTypicals = new ArrayList<State>();
	private final List<State> codingRegionAtypicals = new ArrayList<State>();
	private final Transition transition = new Transition();

	public HMM(boolean probabilitySet) {
		if (probabilitySet) {
			probability();
		}
	}

	public HMM() {
		startCodons.add(new State(0.8, 0.01, 0.01, 0.18));
		startCodons.add(new State(0.01, 0.97, 0.01, 0.01));
		startCodons.add(new State(0.01, 0.01, 0.01, 0.97));

		codingRegionTypicals.add(new State(0.25, 0.25, 0.25, 0.25));
		codingRegionTypicals.add(new State(0.25, 0.25, 0.25, 0.25));
		codingRegionTypicals.add(new State(0.25, 0.25, 0.25, 0.25));

		codingRegionAtypicals.add(new State(0.3, 0.2, 0.25, 0.25));
		codingRegionAtypicals.add(new State(0.25, 0.3, 0.2, 0.25));
		codingRegionAtypicals.add(new State(0.3, 0.2, 0.3, 0.2));

		stopCodons.add(new State(0.01, 0.97, 0.01, 0.01));
		stopCodons.add(new State(0.97, 0.01, 0.01, 0.01));
		stopCodons.add(new State(0.3, 0.15, 0.15, 0.3));

		transition.setStartAtypical(0.36);
		transition.setStartTypical(0.63);
		transition.setTypicalTypical(0.98);
		transition.setAtypicalAtypical(0.02);
		transition.setTypicalStop(0.02);
		transition.setAtypicalStop(0.98);

	}

	public void probability() {

		startCodons.add(new State(0.8, 0.01, 0.01, 0.18));
		startCodons.add(new State(0.01, 0.97, 0.01, 0.01));
		startCodons.add(new State(0.01, 0.01, 0.01, 0.97));

		codingRegionTypicals.add(new State(0.25, 0.25, 0.25, 0.25));
		codingRegionTypicals.add(new State(0.25, 0.25, 0.25, 0.25));
		codingRegionTypicals.add(new State(0.25, 0.25, 0.25, 0.25));

		codingRegionAtypicals.add(new State(0.25, 0.25, 0.25, 0.25));
		codingRegionAtypicals.add(new State(0.25, 0.25, 0.25, 0.25));
		codingRegionAtypicals.add(new State(0.25, 0.25, 0.25, 0.25));

		stopCodons.add(new State(0.01, 0.97, 0.01, 0.01));
		stopCodons.add(new State(0.97, 0.01, 0.01, 0.01));
		stopCodons.add(new State(0.3, 0.15, 0.15, 0.3));

		transition.setStartAtypical(0.2);
		transition.setStartTypical(0.8);
		transition.setTypicalTypical(0.5);
		transition.setAtypicalAtypical(0.5);
		transition.setTypicalStop(0.5);
		transition.setAtypicalStop(0.5);
	}

	public void prediction(DNA dna) {
		double deltaTypical1, deltaTypical2, deltaTypical3;
		double deltaATypical1, deltaATypical2, deltaATypical3;
		int indx = 0;

		List<Gen> pgenes = new ArrayList<Gen>();
		Collections.copy(pgenes, dna.getPredictionGenes());
		for (Gen pgen : dna.getPredictionGenes()) {
			deltaTypical1 = deltaTypical2 = deltaTypical3 = 1;
			deltaATypical1 = deltaATypical2 = deltaATypical3 = 1;

			// hitung delta1
			deltaTypical1 = startCodons.get(0).get(pgen.getBasaStartCodon()[0])
					* startCodons.get(1).get(pgen.getBasaStartCodon()[1])
					* startCodons.get(2).get(pgen.getBasaStartCodon()[2]);
			deltaATypical1 = startCodons.get(0)
					.get(pgen.getBasaStartCodon()[0])
					* startCodons.get(1).get(pgen.getBasaStartCodon()[1])
					* startCodons.get(2).get(pgen.getBasaStartCodon()[2]);

			// delta2
			deltaTypical2 *= transition.getStartTypical()
					* transition.getTypicalTypical() * deltaTypical1;
			deltaATypical2 *= transition.getStartAtypical()
					* transition.getAtypicalAtypical() * deltaATypical1;
			for (int i = 0; i < pgen.getBasaCodingRegion().length; i++) {
				char b = pgen.getBasaCodingRegion()[i];
				int index = 0;
				if (index % 3 == 0) {
					deltaTypical2 *= codingRegionTypicals.get(0).get(b)
							* codingRegionTypicals.get(1).get(b)
							* codingRegionTypicals.get(2).get(b) * 100;
					deltaATypical2 *= codingRegionAtypicals.get(0).get(b)
							* codingRegionAtypicals.get(1).get(b)
							* codingRegionAtypicals.get(2).get(b) * 100;
				}
				index++;
			}

			deltaTypical3 *= deltaTypical2 * transition.getTypicalStop()
					* stopCodons.get(0).get(pgen.getBasaStopCodon()[0])
					* stopCodons.get(1).get(pgen.getBasaStopCodon()[1])
					* stopCodons.get(2).get(pgen.getBasaStopCodon()[2]);
			deltaATypical3 *= deltaATypical2 * transition.getAtypicalStop()
					* stopCodons.get(0).get(pgen.getBasaStopCodon()[0])
					* stopCodons.get(1).get(pgen.getBasaStopCodon()[1])
					* stopCodons.get(2).get(pgen.getBasaStopCodon()[2]);

			if (deltaTypical3 > deltaATypical3) {
				System.out.println("Gen Typical");
				// if (pgen.getSequence().length() > 1500) {
				// dna.getPredictionGenes().remove(indx);
				// }
			} else {
				System.out.println("Gen ATypical");
				// if (pgen.getSequence().length() < 1500) {
				// dna.getPredictionGenes().remove(indx);
				// }
			}
		}

	}

	public void train(DNA dna) {
		double alphaTypical1, alphaTypical2, alphaTypical3;
		double alphaATypical1, alphaATypical2, alphaATypical3;
		double betaTypical1, betaTypical2, betaTypical3;
		double betaATypical1, betaATypical2, betaATypical3;
		double startTypical, startAtypical, typicalStop, atypicalStop, typicalTypical, atypicalAtypical;

		for (Gen gen : dna.getGenes()) {
			alphaTypical1 = alphaTypical2 = alphaTypical3 = 1;
			alphaATypical1 = alphaATypical2 = alphaATypical3 = 1;
			betaTypical1 = betaTypical2 = betaTypical3 = 1;
			betaATypical1 = betaATypical2 = betaATypical3 = 1;

			int[][][] typicalStates = {
					{ { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } },
					{ { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } },
					{ { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } } };
			int[][][] atypicalState = {
					{ { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } },
					{ { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } },
					{ { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } } };
			System.out.println(gen.getSequence());
			System.out.println(gen.getBasaStartCodon());
			System.out.println(gen.getBasaStopCodon());
			// hitung alpha1
			alphaTypical1 = startCodons.get(0).get(gen.getBasaStartCodon()[0])
					* startCodons.get(1).get(gen.getBasaStartCodon()[1])
					* startCodons.get(2).get(gen.getBasaStartCodon()[2]);
			alphaATypical1 = alphaTypical1;
			gen.getAlphaTypical1().add(0,
					startCodons.get(0).get(gen.getBasaStartCodon()[0]));
			gen.getAlphaTypical1().add(
					1,
					startCodons.get(0).get(gen.getBasaStartCodon()[0])
							* startCodons.get(1)
									.get(gen.getBasaStartCodon()[1]));
			gen.getAlphaTypical1().add(
					2,
					startCodons.get(0).get(gen.getBasaStartCodon()[0])
							* startCodons.get(1)
									.get(gen.getBasaStartCodon()[1])
							* startCodons.get(2)
									.get(gen.getBasaStartCodon()[2]));

			gen.getAlphaATypical1().add(0,
					startCodons.get(0).get(gen.getBasaStartCodon()[0]));
			gen.getAlphaATypical1().add(
					1,
					startCodons.get(0).get(gen.getBasaStartCodon()[0])
							* startCodons.get(1)
									.get(gen.getBasaStartCodon()[1]));
			gen.getAlphaATypical1().add(
					2,
					startCodons.get(0).get(gen.getBasaStartCodon()[0])
							* startCodons.get(1)
									.get(gen.getBasaStartCodon()[1])
							* startCodons.get(2)
									.get(gen.getBasaStartCodon()[2]));

			// end hitung alpha1

			// hitung beta3
			betaTypical3 = stopCodons.get(0).get(gen.getBasaStopCodon()[0])
					* stopCodons.get(1).get(gen.getBasaStopCodon()[1])
					* stopCodons.get(2).get(gen.getBasaStopCodon()[2])
					* transition.getTypicalStop();
			// *set beta*
			gen.getBetaTypical3().add(
					0,
					stopCodons.get(0).get(gen.getBasaStopCodon()[0])
							* transition.getTypicalStop());
			gen.getBetaTypical3().add(
					1,
					stopCodons.get(0).get(gen.getBasaStopCodon()[0])
							* stopCodons.get(1).get(gen.getBasaStopCodon()[1])
							* transition.getTypicalStop());
			gen.getBetaTypical3().add(
					2,
					stopCodons.get(0).get(gen.getBasaStopCodon()[0])
							* stopCodons.get(1).get(gen.getBasaStopCodon()[1])
							* stopCodons.get(2).get(gen.getBasaStopCodon()[2])
							* transition.getTypicalStop());

			betaATypical3 = stopCodons.get(0).get(gen.getBasaStopCodon()[0])
					* stopCodons.get(1).get(gen.getBasaStopCodon()[1])
					* stopCodons.get(2).get(gen.getBasaStopCodon()[2])
					* transition.getAtypicalStop();

			gen.getBetaATypical3().add(
					0,
					stopCodons.get(0).get(gen.getBasaStopCodon()[0])
							* transition.getAtypicalStop());
			gen.getBetaATypical3().add(
					1,
					stopCodons.get(0).get(gen.getBasaStopCodon()[0])
							* stopCodons.get(1).get(gen.getBasaStopCodon()[1])
							* transition.getAtypicalStop());
			gen.getBetaATypical3().add(
					2,
					stopCodons.get(0).get(gen.getBasaStopCodon()[0])
							* stopCodons.get(1).get(gen.getBasaStopCodon()[1])
							* stopCodons.get(2).get(gen.getBasaStopCodon()[2])
							* transition.getAtypicalStop());

			// end hitung beta 3

			// hitung alpha 2 dan beta 2
			alphaTypical2 *= transition.getStartTypical()
					* transition.getTypicalTypical() * alphaTypical1;
			alphaATypical2 *= transition.getStartAtypical()
					* transition.getAtypicalAtypical() * alphaATypical1;
			betaTypical2 *= transition.getTypicalTypical()
					* transition.getStartTypical() * betaTypical3;
			betaATypical2 *= transition.getAtypicalAtypical()
					* transition.getStartAtypical() * betaATypical3;

			int index = 0;
			int j = 0;
			for (int i = 0; i < gen.getBasaCodingRegion().length; i++) {
				char basa = gen.getBasaCodingRegion()[i];
				char b_basa = gen.getBasaCodingRegion()[gen
						.getBasaCodingRegion().length
						- i - 1];
				if (index % 3 == 0) {
					if (gen.isTypical()) {
						alphaTypical2 *= codingRegionTypicals.get(0).get(basa)
								* codingRegionTypicals.get(1).get(basa)
								* codingRegionTypicals.get(2).get(basa) * 100;
						// set
						gen.getAlphaTypical2().add(index, alphaTypical2);

						betaTypical2 *= codingRegionTypicals.get(0).get(b_basa)
								* codingRegionTypicals.get(1).get(b_basa)
								* codingRegionTypicals.get(2).get(b_basa) * 100;
						// set
						gen.getBetaTypical2().add(index, betaTypical2);
						alphaATypical2 *= codingRegionAtypicals.get(0)
								.get(basa)
								* codingRegionAtypicals.get(1).get(basa)
								* codingRegionAtypicals.get(2).get(basa) * 100;
						// set
						gen.getAlphaATypical2().add(index, alphaATypical2);
						betaATypical2 *= codingRegionAtypicals.get(0).get(
								b_basa)
								* codingRegionAtypicals.get(1).get(b_basa)
								* codingRegionAtypicals.get(2).get(b_basa)
								* 100;
						// set
						gen.getBetaATypical2().add(index, betaATypical2);
						// System.out.println("urutan :" + i);
						// System.out.println("Alpha Typical 2: " +
						// alphaTypical2
						// + "Alpha ATypical 2: " + alphaATypical2
						// + "Beta Typical 2 :" + betaTypical2
						// + "Beta Atypical 2 :" + betaATypical2);

					} else {
						alphaTypical2 *= codingRegionTypicals.get(0).get(basa)
								* codingRegionTypicals.get(1).get(basa)
								* codingRegionTypicals.get(2).get(basa) * 100;
						// set
						gen.getAlphaTypical2().add(index, alphaTypical2);

						betaTypical2 *= codingRegionTypicals.get(0).get(b_basa)
								* codingRegionTypicals.get(1).get(b_basa)
								* codingRegionTypicals.get(2).get(b_basa) * 100;
						// set
						gen.getBetaTypical2().add(index, betaTypical2);
						alphaATypical2 *= codingRegionAtypicals.get(0)
								.get(basa)
								* codingRegionAtypicals.get(1).get(basa)
								* codingRegionAtypicals.get(2).get(basa) * 100;
						// set
						gen.getAlphaATypical2().add(index, alphaATypical2);
						betaATypical2 *= codingRegionAtypicals.get(0).get(
								b_basa)
								* codingRegionAtypicals.get(1).get(b_basa)
								* codingRegionAtypicals.get(2).get(b_basa)
								* 100;
						// set
						gen.getBetaATypical2().add(index, betaATypical2);

						if (i % 6 == 0) {
							alphaTypical2 *= 0.1;
							alphaATypical2 *= 0.1;
							betaTypical2 *= 0.1;
							betaATypical2 *= 0.1;
						}

						// System.out.println("urutan :" + i);
						// System.out.println("Alpha Typical 2: " +
						// alphaTypical2);
						// System.out.println("Alpha ATypical 2: "+
						// alphaATypical2)
					}

					/*
					 * double typ_typ = alphaTypical2
					 * codingRegionAtypicals.get(0).get(basa)
					 * codingRegionAtypicals.get(0).get(b_basa) betaTypical2;
					 */
				}
				switch (basa) {
				case 'a':
					typicalStates[1][index % 3][0]++;
					break;
				case 't':
					typicalStates[1][index % 3][1]++;
					break;
				case 'c':
					typicalStates[1][index % 3][2]++;
					break;
				case 'g':
					typicalStates[1][index % 3][3]++;
					break;
				}
			}
			// double x = Math.pow(10, 308);

			// end hitung alpha2 dan beta 2

			// hitung alpha3

			alphaTypical3 = stopCodons.get(0).get(gen.getBasaStopCodon()[0])
					* stopCodons.get(1).get(gen.getBasaStopCodon()[1])
					* stopCodons.get(2).get(gen.getBasaStopCodon()[2])
					* transition.getTypicalStop() * alphaTypical2;
			// set
			gen.getAlphaTypical3().add(
					0,
					stopCodons.get(0).get(gen.getBasaStopCodon()[0])
							* transition.getTypicalStop() * alphaTypical2);
			gen.getAlphaTypical3().add(
					1,
					stopCodons.get(0).get(gen.getBasaStopCodon()[0])
							* stopCodons.get(1).get(gen.getBasaStopCodon()[1])
							* transition.getTypicalStop() * alphaTypical2);
			gen.getAlphaTypical3().add(
					2,
					stopCodons.get(0).get(gen.getBasaStopCodon()[0])
							* stopCodons.get(1).get(gen.getBasaStopCodon()[1])
							* stopCodons.get(2).get(gen.getBasaStopCodon()[2])
							* transition.getTypicalStop() * alphaTypical2);

			alphaATypical3 = stopCodons.get(0).get(gen.getBasaStopCodon()[0])
					* stopCodons.get(1).get(gen.getBasaStopCodon()[1])
					* stopCodons.get(2).get(gen.getBasaStopCodon()[2])
					* transition.getAtypicalStop() * alphaATypical2;
			// set
			gen.getAlphaATypical3().add(
					0,
					stopCodons.get(0).get(gen.getBasaStopCodon()[0])
							* transition.getAtypicalStop() * alphaATypical2);
			gen.getAlphaATypical3().add(
					1,
					stopCodons.get(0).get(gen.getBasaStopCodon()[0])
							* stopCodons.get(1).get(gen.getBasaStopCodon()[1])
							* transition.getAtypicalStop() * alphaATypical2);
			gen.getAlphaATypical3().add(
					2,
					stopCodons.get(0).get(gen.getBasaStopCodon()[0])
							* stopCodons.get(1).get(gen.getBasaStopCodon()[1])
							* stopCodons.get(2).get(gen.getBasaStopCodon()[2])
							* transition.getAtypicalStop() * alphaATypical2);
			// end hitung alpha3

			// hitung beta1 startCodon
			betaTypical1 = startCodons.get(0).get(gen.getBasaStartCodon()[0])
					* startCodons.get(1).get(gen.getBasaStartCodon()[1])
					* startCodons.get(2).get(gen.getBasaStartCodon()[2])
					* betaTypical2;
			gen.getBetaTypical1().add(
					0,
					startCodons.get(0).get(gen.getBasaStartCodon()[0])
							* betaTypical2);
			gen.getBetaTypical1().add(
					1,
					startCodons.get(0).get(gen.getBasaStartCodon()[0])
							* startCodons.get(1)
									.get(gen.getBasaStartCodon()[1])
							* betaTypical2);
			gen.getBetaTypical1().add(
					2,
					startCodons.get(0).get(gen.getBasaStartCodon()[0])
							* startCodons.get(1)
									.get(gen.getBasaStartCodon()[1])
							* startCodons.get(2)
									.get(gen.getBasaStartCodon()[2])
							* betaTypical2);

			betaATypical1 = startCodons.get(0).get(gen.getBasaStartCodon()[0])
					* startCodons.get(1).get(gen.getBasaStartCodon()[1])
					* startCodons.get(2).get(gen.getBasaStartCodon()[2])
					* betaATypical2;
			gen.getBetaATypical1().add(
					0,
					startCodons.get(0).get(gen.getBasaStartCodon()[0])
							* betaATypical2);
			gen.getBetaATypical1().add(
					1,
					startCodons.get(0).get(gen.getBasaStartCodon()[0])
							* startCodons.get(1)
									.get(gen.getBasaStartCodon()[1])
							* betaATypical2);
			gen.getBetaATypical1().add(
					2,
					startCodons.get(0).get(gen.getBasaStartCodon()[0])
							* startCodons.get(1)
									.get(gen.getBasaStartCodon()[1])
							* startCodons.get(2)
									.get(gen.getBasaStartCodon()[2])
							* betaATypical2);

			double fwdbwd = alphaTypical3 + alphaATypical3;
			// end hitung beta1

			// System.out.println("Alpha Typical 1: " + alphaTypical1);
			// System.out.println("Alpha Typical 2: " + alphaTypical2);
			// System.out.println("Alpha Typical 3: " + alphaTypical3);

			// System.out.println("Alpha Atypical 1: " + alphaATypical1);
			// System.out.println("Alpha Atypical 2: " + alphaATypical2);
			// System.out.println("Alpha Atypical 3: " + alphaATypical3);

			// System.out.println("Beta Typical 3: " + betaTypical3);
			// System.out.println("Beta Typical 2: " + betaTypical2);
			// System.out.println("Beta Typical 1: " + betaTypical1);

			// System.out.println("Beta Atypical 3: " + betaATypical3);
			// System.out.println("Beta Atypical 2: " + betaATypical2);
			// System.out.println("Beta Atypical 1: " + betaATypical1);
			// System.out.println("------------------------------------------- ");

			// -------------------------------------------------------------

			// hitung transition probability
			double sTyp = alphaTypical1
					* startCodons.get(2).get(gen.getBasaStartCodon()[2])
					* codingRegionTypicals.get(0).get(
							gen.getBasaCodingRegion()[0]) * betaTypical3
					/ (alphaTypical3 + alphaATypical3);
			double sATyp = alphaATypical1
					* startCodons.get(2).get(gen.getBasaStartCodon()[2])
					* codingRegionTypicals.get(0).get(
							gen.getBasaCodingRegion()[2]) * betaATypical3
					/ (alphaTypical3 + alphaATypical3);
			double typEnd = (alphaTypical2
					* codingRegionTypicals.get(2).get(
							gen.getBasaCodingRegion()[2]) / (alphaTypical3 + alphaATypical3))
					* (codingRegionTypicals.get(0).get(
							gen.getBasaCodingRegion()[0])
							* betaTypical2 / (alphaTypical3 + betaTypical1));
			double atypEnd = (alphaATypical2
					* codingRegionTypicals.get(2).get(
							gen.getBasaCodingRegion()[2]) / (alphaTypical3 + alphaATypical3))
					* (codingRegionTypicals.get(0).get(
							gen.getBasaCodingRegion()[0])
							* betaATypical2 / (alphaTypical3 + alphaATypical3));

			startTypical = sTyp / (sTyp + sATyp);
			startAtypical = sATyp / (sTyp + sATyp);
			typicalStop = typEnd / (typEnd + atypEnd);
			atypicalStop = atypEnd / (typEnd + atypEnd);
			typicalTypical = (1 - typicalStop);
			atypicalAtypical = (1 - atypicalStop);

			if (atypicalStop < 0.01) {
				atypicalStop = atypicalStop + 0.01;
				typicalStop = typicalStop - 0.01;
				typicalTypical = typicalTypical + 0.01;
				atypicalAtypical = atypicalAtypical - 0.01;
			} else if (typicalStop < 0.01) {
				typicalStop = typicalStop + 0.01;
				atypicalStop = atypicalStop - 0.01;
				atypicalAtypical = atypicalAtypical + 0.01;
				typicalTypical = typicalTypical - 0.01;
			}

			double x = typicalStop + atypicalStop;

			transition.setStartTypical(startTypical);
			transition.setStartAtypical(startAtypical);
			transition.setTypicalStop(atypicalStop);
			transition.setAtypicalStop(atypicalStop);
			transition.setTypicalTypical(typicalTypical);
			transition.setAtypicalAtypical(atypicalAtypical);

			// System.out.println("startTypical: " + startTypical);
			// System.out.println("startATypical: " + startAtypical);
			// System.out.println("typcalTypical" + typicalTypical);
			// System.out.println("atypAtyp" + atypicalAtypical);
			// System.out.println("typicalStop: " + typicalStop);
			// System.out.println("atypStop: " + atypicalStop);

			// ================================================================
			// emition probability

			// sum_state += (alpha char . beta char) / fwdbwd -> yg ini u/state

			// start

			double a_StartTyp1 = ((gen.sumCharStartTyp(0, 'a') / gen
					.sumStateStartTyp(0)) + 0.01)
					/ ((gen.sumStateStartTyp(0) / gen.sumStateStartTyp(0)) + 0.04);
			double t_StartTyp1 = ((gen.sumCharStartTyp(0, 't') / gen
					.sumStateStartTyp(0)) + 0.01)
					/ ((gen.sumStateStartTyp(0) / gen.sumStateStartTyp(0)) + 0.04);
			double c_StartTyp1 = ((gen.sumCharStartTyp(0, 'c') / gen
					.sumStateStartTyp(0)) + 0.01)
					/ ((gen.sumStateStartTyp(0) / gen.sumStateStartTyp(0)) + 0.04);
			double g_StartTyp1 = ((gen.sumCharStartTyp(0, 'g') / gen
					.sumStateStartTyp(0)) + 0.01)
					/ ((gen.sumStateStartTyp(0) / gen.sumStateStartTyp(0)) + 0.04);

			double a_StartTyp2 = ((gen.sumCharStartTyp(1, 'a') / gen
					.sumStateStartTyp(1)) + 0.01)
					/ ((gen.sumStateStartTyp(1) / gen.sumStateStartTyp(1)) + 0.04);
			double t_StartTyp2 = ((gen.sumCharStartTyp(1, 't') / gen
					.sumStateStartTyp(1)) + 0.01)
					/ ((gen.sumStateStartTyp(1) / gen.sumStateStartTyp(1)) + 0.04);
			double c_StartTyp2 = ((gen.sumCharStartTyp(1, 'c') / gen
					.sumStateStartTyp(1)) + 0.01)
					/ ((gen.sumStateStartTyp(1) / gen.sumStateStartTyp(1)) + 0.04);
			double g_StartTyp2 = ((gen.sumCharStartTyp(1, 'g') / gen
					.sumStateStartTyp(1)) + 0.01)
					/ ((gen.sumStateStartTyp(1) / gen.sumStateStartTyp(1)) + 0.04);

			double a_StartTyp3 = ((gen.sumCharStartTyp(2, 'a') / gen
					.sumStateStartTyp(2)) + 0.01)
					/ ((gen.sumStateStartTyp(2) / gen.sumStateStartTyp(2)) + 0.04);
			double t_StartTyp3 = ((gen.sumCharStartTyp(2, 't') / gen
					.sumStateStartTyp(2)) + 0.01)
					/ ((gen.sumStateStartTyp(2) / gen.sumStateStartTyp(2)) + 0.04);
			double c_StartTyp3 = ((gen.sumCharStartTyp(2, 'c') / gen
					.sumStateStartTyp(2)) + 0.01)
					/ ((gen.sumStateStartTyp(2) / gen.sumStateStartTyp(2)) + 0.04);
			double g_StartTyp3 = ((gen.sumCharStartTyp(2, 'g') / gen
					.sumStateStartTyp(2)) + 0.01)
					/ ((gen.sumStateStartTyp(2) / gen.sumStateStartTyp(2)) + 0.04);

			startCodons.get(0).set('a', a_StartTyp1);
			startCodons.get(0).set('t', t_StartTyp1);
			startCodons.get(0).set('c', c_StartTyp1);
			startCodons.get(0).set('g', g_StartTyp1);

			startCodons.get(1).set('a', a_StartTyp2);
			startCodons.get(1).set('t', t_StartTyp2);
			startCodons.get(1).set('c', c_StartTyp2);
			startCodons.get(1).set('g', g_StartTyp2);

			startCodons.get(2).set('a', a_StartTyp3);
			startCodons.get(2).set('t', t_StartTyp3);
			startCodons.get(2).set('c', c_StartTyp3);
			startCodons.get(2).set('g', g_StartTyp3);

			// start atypical
			double a_StartATyp1 = ((gen.sumCharStartATyp(0, 'a') / gen
					.sumStateStartATyp(0)) + 0.01)
					/ ((gen.sumStateStartATyp(0) / gen.sumStateStartATyp(0)) + 0.04);
			double t_StartATyp1 = ((gen.sumCharStartATyp(0, 't') / gen
					.sumStateStartATyp(0)) + 0.01)
					/ ((gen.sumStateStartATyp(0) / gen.sumStateStartATyp(0)) + 0.04);
			double c_StartATyp1 = ((gen.sumCharStartATyp(0, 'c') / gen
					.sumStateStartATyp(0)) + 0.01)
					/ ((gen.sumStateStartATyp(0) / gen.sumStateStartATyp(0)) + 0.04);
			double g_StartATyp1 = ((gen.sumCharStartATyp(0, 'g') / gen
					.sumStateStartATyp(0)) + 0.01)
					/ ((gen.sumStateStartATyp(0) / gen.sumStateStartATyp(0)) + 0.04);

			double a_StartATyp2 = ((gen.sumCharStartATyp(1, 'a') / gen
					.sumStateStartATyp(1)) + 0.01)
					/ ((gen.sumStateStartATyp(1) / gen.sumStateStartATyp(1)) + 0.04);
			double t_StartATyp2 = ((gen.sumCharStartATyp(1, 't') / gen
					.sumStateStartATyp(1)) + 0.01)
					/ ((gen.sumStateStartATyp(1) / gen.sumStateStartATyp(1)) + 0.04);
			double c_StartATyp2 = ((gen.sumCharStartATyp(1, 'c') / gen
					.sumStateStartATyp(1)) + 0.01)
					/ ((gen.sumStateStartATyp(1) / gen.sumStateStartATyp(1)) + 0.04);
			double g_StartATyp2 = ((gen.sumCharStartATyp(1, 'g') / gen
					.sumStateStartATyp(1)) + 0.01)
					/ ((gen.sumStateStartATyp(1) / gen.sumStateStartATyp(1)) + 0.04);

			double a_StartATyp3 = ((gen.sumCharStartATyp(2, 'a') / gen
					.sumStateStartATyp(2)) + 0.01)
					/ ((gen.sumStateStartATyp(2) / gen.sumStateStartATyp(2)) + 0.04);
			double t_StartATyp3 = ((gen.sumCharStartATyp(2, 't') / gen
					.sumStateStartATyp(2)) + 0.01)
					/ ((gen.sumStateStartATyp(2) / gen.sumStateStartATyp(2)) + 0.04);
			double c_StartATyp3 = ((gen.sumCharStartATyp(2, 'c') / gen
					.sumStateStartATyp(2)) + 0.01)
					/ ((gen.sumStateStartATyp(2) / gen.sumStateStartATyp(2)) + 0.04);
			double g_StartATyp3 = ((gen.sumCharStartATyp(2, 'g') / gen
					.sumStateStartATyp(2)) + 0.01)
					/ ((gen.sumStateStartATyp(2) / gen.sumStateStartATyp(2)) + 0.04);

			System.out.println("a" + a_StartTyp1 + " t" + t_StartTyp1 + " c"
					+ c_StartTyp1 + " g" + g_StartTyp1);
			System.out.println("a" + a_StartTyp2 + " t" + t_StartTyp2 + " c"
					+ c_StartTyp2 + " g" + g_StartTyp2);
			System.out.println("a" + a_StartTyp3 + " t" + t_StartTyp3 + " c"
					+ c_StartTyp3 + " g" + g_StartTyp3);

			System.out.println("Atypical ---> a" + a_StartATyp1 + " t"
					+ t_StartATyp1 + " c" + c_StartATyp1 + " g" + g_StartATyp1);
			System.out.println("Atypical ---> a" + a_StartATyp2 + " t"
					+ t_StartATyp2 + " c" + c_StartATyp2 + " g" + g_StartATyp2);
			System.out.println("Atypical ---> a" + a_StartATyp3 + " t"
					+ t_StartATyp3 + " c" + c_StartATyp3 + " g" + g_StartATyp3);

			double sumStateTyp0 = gen.sumStateTypical(0);
			double sumStateTyp1 = gen.sumStateTypical(1);
			double sumStateTyp2 = gen.sumStateTypical(2);
			double sumStateAtyp0 = gen.sumStateATypical(0);
			double sumStateAtyp1 = gen.sumStateATypical(1);
			double sumStateAtyp2 = gen.sumStateATypical(2);

			// System.out.println(sumStateAtyp0);

			// coding region
			// sum_char += (alpha char . beta char) / fwdbwd ---> yg ini untuk
			// for (int z = 1; z <= 3; z++) {
			double a_Typ1 = gen.sumCharTypical(0, 'a') / sumStateTyp0;
			double t_Typ1 = gen.sumCharTypical(0, 't') / sumStateTyp0;
			double c_Typ1 = gen.sumCharTypical(0, 'c') / sumStateTyp0;
			double g_Typ1 = gen.sumCharTypical(0, 'g') / sumStateTyp0;

			double a_Typ2 = gen.sumCharTypical(1, 'a') / sumStateTyp1;
			double t_Typ2 = gen.sumCharTypical(1, 't') / sumStateTyp1;
			double c_Typ2 = gen.sumCharTypical(1, 'c') / sumStateTyp1;
			double g_Typ2 = gen.sumCharTypical(1, 'g') / sumStateTyp1;

			double a_Typ3 = gen.sumCharTypical(2, 'a') / sumStateTyp2;
			double t_Typ3 = gen.sumCharTypical(2, 't') / sumStateTyp2;
			double c_Typ3 = gen.sumCharTypical(2, 'c') / sumStateTyp2;
			double g_Typ3 = gen.sumCharTypical(2, 'g') / sumStateTyp2;

			codingRegionTypicals.get(0).set(gen.getBasaCodingRegion()[0],
					a_Typ1);
			codingRegionTypicals.get(0).set(gen.getBasaCodingRegion()[1],
					t_Typ1);
			codingRegionTypicals.get(0).set(gen.getBasaCodingRegion()[2],
					c_Typ1);
			codingRegionTypicals.get(0).set(gen.getBasaCodingRegion()[3],
					g_Typ1);
			codingRegionTypicals.get(1).set(gen.getBasaCodingRegion()[0],
					a_Typ2);
			codingRegionTypicals.get(1).set(gen.getBasaCodingRegion()[1],
					t_Typ2);
			codingRegionTypicals.get(1).set(gen.getBasaCodingRegion()[2],
					c_Typ2);
			codingRegionTypicals.get(1).set(gen.getBasaCodingRegion()[3],
					g_Typ2);
			codingRegionTypicals.get(2).set(gen.getBasaCodingRegion()[0],
					a_Typ3);
			codingRegionTypicals.get(2).set(gen.getBasaCodingRegion()[1],
					t_Typ3);
			codingRegionTypicals.get(2).set(gen.getBasaCodingRegion()[2],
					c_Typ3);
			codingRegionTypicals.get(2).set(gen.getBasaCodingRegion()[3],
					g_Typ3);

			double a_ATyp1 = gen.sumCharATypical(0, 'a') / sumStateTyp0;
			double t_ATyp1 = gen.sumCharATypical(0, 't') / sumStateTyp0;
			double c_ATyp1 = gen.sumCharATypical(0, 'c') / sumStateTyp0;
			double g_ATyp1 = gen.sumCharATypical(0, 'g') / sumStateTyp0;

			double a_ATyp2 = gen.sumCharATypical(1, 'a') / sumStateTyp1;
			double t_ATyp2 = gen.sumCharATypical(1, 't') / sumStateTyp1;
			double c_ATyp2 = gen.sumCharATypical(1, 'c') / sumStateTyp1;
			double g_ATyp2 = gen.sumCharATypical(1, 'g') / sumStateTyp1;

			double a_ATyp3 = gen.sumCharATypical(2, 'a') / sumStateTyp2;
			double t_ATyp3 = gen.sumCharATypical(2, 't') / sumStateTyp2;
			double c_ATyp3 = gen.sumCharATypical(2, 'c') / sumStateTyp2;
			double g_ATyp3 = gen.sumCharATypical(2, 'g') / sumStateTyp2;

			codingRegionAtypicals.get(0).set(gen.getBasaCodingRegion()[0],
					a_ATyp1);
			codingRegionAtypicals.get(0).set(gen.getBasaCodingRegion()[1],
					t_ATyp1);
			codingRegionAtypicals.get(0).set(gen.getBasaCodingRegion()[2],
					c_ATyp1);
			codingRegionAtypicals.get(0).set(gen.getBasaCodingRegion()[3],
					g_ATyp1);
			codingRegionAtypicals.get(1).set(gen.getBasaCodingRegion()[0],
					a_ATyp2);
			codingRegionAtypicals.get(1).set(gen.getBasaCodingRegion()[1],
					t_ATyp2);
			codingRegionAtypicals.get(1).set(gen.getBasaCodingRegion()[2],
					c_ATyp2);
			codingRegionAtypicals.get(1).set(gen.getBasaCodingRegion()[3],
					g_ATyp2);
			codingRegionAtypicals.get(2).set(gen.getBasaCodingRegion()[0],
					a_ATyp3);
			codingRegionAtypicals.get(2).set(gen.getBasaCodingRegion()[1],
					t_ATyp3);
			codingRegionAtypicals.get(2).set(gen.getBasaCodingRegion()[2],
					c_ATyp3);
			codingRegionAtypicals.get(2).set(gen.getBasaCodingRegion()[3],
					g_ATyp3);

			// stop
			double a_StopTyp1 = (gen.sumCharStopTyp(0, 'a')
					/ gen.sumStateStopTyp(0) + 0.01)
					/ (gen.sumStateStopTyp(0) / gen.sumStateStopTyp(0) + 0.04);
			double t_StopTyp1 = (gen.sumCharStopTyp(0, 't')
					/ gen.sumStateStopTyp(0) + 0.01)
					/ (gen.sumStateStopTyp(0) / gen.sumStateStopTyp(0) + 0.04);
			double c_StopTyp1 = (gen.sumCharStopTyp(0, 'c')
					/ gen.sumStateStopTyp(0) + 0.01)
					/ (gen.sumStateStopTyp(0) / gen.sumStateStopTyp(0) + 0.04);
			double g_StopTyp1 = (gen.sumCharStopTyp(0, 'g')
					/ gen.sumStateStopTyp(0) + 0.01)
					/ (gen.sumStateStopTyp(0) / gen.sumStateStopTyp(0) + 0.04);

			double a_StopTyp2 = (gen.sumCharStopTyp(1, 'a')
					/ gen.sumStateStopTyp(1) + 0.01)
					/ (gen.sumStateStopTyp(1) / gen.sumStateStopTyp(1) + 0.04);
			double t_StopTyp2 = (gen.sumCharStopTyp(1, 't')
					/ gen.sumStateStopTyp(1) + 0.01)
					/ (gen.sumStateStopTyp(1) / gen.sumStateStopTyp(1) + 0.04);
			double c_StopTyp2 = (gen.sumCharStopTyp(1, 'c')
					/ gen.sumStateStopTyp(1) + 0.01)
					/ (gen.sumStateStopTyp(1) / gen.sumStateStopTyp(1) + 0.04);
			double g_StopTyp2 = (gen.sumCharStopTyp(1, 'g')
					/ gen.sumStateStopTyp(1) + 0.01)
					/ (gen.sumStateStopTyp(1) / gen.sumStateStopTyp(1) + 0.04);

			double a_StopTyp3 = (gen.sumCharStopTyp(2, 'a')
					/ gen.sumStateStopTyp(2) + 0.01)
					/ (gen.sumStateStopTyp(2) / gen.sumStateStopTyp(2) + 0.04);
			double t_StopTyp3 = (gen.sumCharStopTyp(2, 't')
					/ gen.sumStateStopTyp(2) + 0.01)
					/ (gen.sumStateStopTyp(2) / gen.sumStateStopTyp(2) + 0.04);
			double c_StopTyp3 = (gen.sumCharStopTyp(2, 'c')
					/ gen.sumStateStopTyp(2) + 0.01)
					/ (gen.sumStateStopTyp(2) / gen.sumStateStopTyp(2) + 0.04);
			double g_StopTyp3 = (gen.sumCharStopTyp(2, 'g')
					/ gen.sumStateStopTyp(2) + 0.01)
					/ (gen.sumStateStopTyp(2) / gen.sumStateStopTyp(2) + 0.04);

			stopCodons.get(0).set('a', a_StopTyp1);
			stopCodons.get(0).set('t', t_StopTyp1);
			stopCodons.get(0).set('c', c_StopTyp1);
			stopCodons.get(0).set('g', g_StopTyp1);

			stopCodons.get(1).set('a', a_StopTyp2);
			stopCodons.get(1).set('t', t_StopTyp2);
			stopCodons.get(1).set('c', c_StopTyp2);
			stopCodons.get(1).set('g', g_StopTyp2);

			stopCodons.get(2).set('a', a_StopTyp3);
			stopCodons.get(2).set('t', t_StopTyp3);
			stopCodons.get(2).set('c', c_StopTyp3);
			stopCodons.get(2).set('g', g_StopTyp3);

			System.out.println("Typical State 1 --> a : " + a_Typ1 + ",t : "
					+ t_Typ1 + ",c : " + c_Typ1 + ",g :" + g_Typ1);
			System.out.println("Typical State 2 --> a : " + a_Typ2 + ",t : "
					+ t_Typ2 + ",c : " + c_Typ2 + ",g :" + g_Typ2);
			System.out.println("Typical State 3 --> a : " + a_Typ3 + ",t : "
					+ t_Typ3 + ",c : " + c_Typ3 + ",g :" + g_Typ3);

			// atyp
			System.out.println("Atypical State 1 --> a : " + a_ATyp1 + ",t : "
					+ t_ATyp1 + ",c : " + c_ATyp1 + ",g :" + g_ATyp1);
			System.out.println("Atypical State 1 --> a : " + a_ATyp2 + ",t : "
					+ t_ATyp2 + ",c : " + c_ATyp2 + ",g :" + g_ATyp2);
			System.out.println("Atypical State 1 --> a : " + a_ATyp3 + ",t : "
					+ t_ATyp3 + ",c : " + c_ATyp3 + ",g :" + g_ATyp3);
			System.out.println("========================================");
			// }

			// perchar
			// sum = sum_char / sum_state nilai e_prob

			System.out.println("Typical Stop 1 --> a : " + a_StopTyp1 + ",t : "
					+ t_StopTyp1 + ",c : " + c_StopTyp1 + ",g :" + g_StopTyp1);
			System.out.println("Typical Stop 2 --> a : " + a_StopTyp2 + ",t : "
					+ t_StopTyp2 + ",c : " + c_StopTyp2 + ",g :" + g_StopTyp2);
			System.out.println("Typical Stop 3 --> a : " + a_StopTyp3 + ",t : "
					+ t_StopTyp3 + ",c : " + c_StopTyp3 + ",g :" + g_StopTyp3);

		}
	}

	public List<State> getStartCodons() {
		return startCodons;
	}

	public List<State> getStopCodons() {
		return stopCodons;
	}

	public List<State> getCodingRegionTypicals() {
		return codingRegionTypicals;
	}

	public List<State> getCodingRegionAtypicals() {
		return codingRegionAtypicals;
	}

}
