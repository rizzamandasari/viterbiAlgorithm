package edu.rizza.ihmm;

import java.util.ArrayList;
import java.util.List;

public class HMM {

	private final List<State> startCodons = new ArrayList<State>();
	private final List<State> stopCodons = new ArrayList<State>();
	private final List<State> codingRegionTypicals = new ArrayList<State>();
	private final List<State> codingRegionAtypicals = new ArrayList<State>();
	private final Transition transition = new Transition();

	public HMM() {
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
		stopCodons.add(new State(0.4, 0.05, 0.05, 0.4));

		transition.setStartAtypical(0.8);
		transition.setStartTypical(0.2);
		transition.setTypicalTypical(0.8);
		transition.setAtypicalAtypical(0.1);
		transition.setTypicalStop(0.2);
		transition.setAtypicalStop(0.9);

	}

	public void train(DNA dna) {
		double alphaTypical1, alphaTypical2, alphaTypical3;
		double alphaATypical1, alphaATypical2, alphaATypical3;
		double betaTypical1, betaTypical2, betaTypical3;
		double betaATypical1, betaATypical2, betaATypical3;
		double startTypical, startAtypical, typicalStop, atypicalStop;

		for (Gen gen : dna.getGenes()) {
			alphaTypical1 = alphaTypical2 = alphaTypical3 = 1;
			alphaATypical1 = alphaATypical2 = alphaATypical3 = 1;
			betaTypical1 = betaTypical2 = betaTypical3 = 1;
			betaATypical1 = betaATypical2 = betaATypical3 = 1;

			// hitung alpha1
			alphaTypical1 = startCodons.get(0).get(gen.getBasaStartCodon()[0])
					* startCodons.get(1).get(gen.getBasaStartCodon()[1])
					* startCodons.get(2).get(gen.getBasaStartCodon()[2]);
			alphaATypical1 = alphaTypical1;
			// end hitung alpha1

			// hitung beta3
			betaTypical3 = stopCodons.get(0).get(gen.getBasaStopCodon()[0])
					* stopCodons.get(1).get(gen.getBasaStopCodon()[1])
					* stopCodons.get(2).get(gen.getBasaStopCodon()[2])
					* transition.getTypicalStop();
			betaATypical3 = stopCodons.get(0).get(gen.getBasaStopCodon()[0])
					* stopCodons.get(1).get(gen.getBasaStopCodon()[1])
					* stopCodons.get(2).get(gen.getBasaStopCodon()[2])
					* transition.getAtypicalStop();
			// end hitung beta 3

			// hitung alpha 2 dan beta 2
			int index = 0;
			int j = 0;
			for (int i = 0; i < gen.getBasaCodingRegion().length; i++) {
				char basa = gen.getBasaCodingRegion()[i];
				char b_basa = gen.getBasaCodingRegion()[gen
						.getBasaCodingRegion().length
						- i - 1];
				if (index % 3 == 0) {
					// if (gen.isTypical()) {
					alphaTypical2 *= codingRegionTypicals.get(0).get(basa)
							* codingRegionTypicals.get(1).get(basa)
							* codingRegionTypicals.get(2).get(basa) * 100;

					betaTypical2 *= codingRegionTypicals.get(0).get(b_basa)
							* codingRegionTypicals.get(1).get(b_basa)
							* codingRegionTypicals.get(2).get(b_basa) * 100;
					// } else {
					alphaATypical2 *= codingRegionAtypicals.get(0).get(basa)
							* codingRegionAtypicals.get(1).get(basa)
							* codingRegionAtypicals.get(2).get(basa) * 100;
					betaATypical2 *= codingRegionAtypicals.get(0).get(b_basa)
							* codingRegionAtypicals.get(1).get(b_basa)
							* codingRegionAtypicals.get(2).get(b_basa) * 100;
					// }
				}
			}
			// double x = Math.pow(10, 308);

			alphaTypical2 *= transition.getStartTypical()
					* transition.getTypicalTypical() * alphaTypical1;
			alphaATypical2 *= transition.getStartAtypical()
					* transition.getAtypicalAtypical() * alphaATypical1;
			betaTypical2 *= transition.getTypicalTypical()
					* transition.getStartTypical() * betaTypical3;
			betaATypical2 *= transition.getAtypicalAtypical()
					* transition.getStartAtypical() * betaATypical3;
			// end hitung alpha2 dan beta 2

			// hitung alpha3

			alphaTypical3 = stopCodons.get(0).get(gen.getBasaStopCodon()[0])
					* stopCodons.get(1).get(gen.getBasaStopCodon()[1])
					* stopCodons.get(2).get(gen.getBasaStopCodon()[2])
					* transition.getTypicalStop() * alphaTypical2;
			alphaATypical3 = stopCodons.get(0).get(gen.getBasaStopCodon()[0])
					* stopCodons.get(1).get(gen.getBasaStopCodon()[1])
					* stopCodons.get(2).get(gen.getBasaStopCodon()[2])
					* transition.getAtypicalStop() * alphaATypical2;
			// end hitung alpha3

			// hitung beta1
			betaTypical1 = startCodons.get(0).get(gen.getBasaStartCodon()[0])
					* startCodons.get(1).get(gen.getBasaStartCodon()[1])
					* startCodons.get(2).get(gen.getBasaStartCodon()[2])
					* betaTypical2;
			betaATypical1 = startCodons.get(0).get(gen.getBasaStartCodon()[0])
					* startCodons.get(1).get(gen.getBasaStartCodon()[1])
					* startCodons.get(2).get(gen.getBasaStartCodon()[2])
					* betaATypical2;
			// end hitung beta1

			System.out.println("Alpha Typical 1: " + alphaTypical1);
			System.out.println("Alpha Typical 2: " + alphaTypical2);
			System.out.println("Alpha Typical 3: " + alphaTypical3);

			System.out.println("Alpha Atypical 1: " + alphaATypical1);
			System.out.println("Alpha Atypical 2: " + alphaATypical2);
			System.out.println("Alpha Atypical 3: " + alphaATypical3);

			System.out.println("Beta Typical 3: " + betaTypical3);
			System.out.println("Beta Typical 2: " + betaTypical2);
			System.out.println("Beta Typical 1: " + betaTypical1);

			System.out.println("Beta Atypical 3: " + betaATypical3);
			System.out.println("Beta Atypical 2: " + betaATypical2);
			System.out.println("Beta Atypical 1: " + betaATypical1);

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
			double typEnd = (alphaTypical2 / (alphaTypical3 + alphaATypical3))
					* (codingRegionTypicals.get(2).get(
							gen.getBasaCodingRegion()[2]) / (alphaTypical3 + alphaATypical3))
					* (codingRegionTypicals.get(0).get(
							gen.getBasaCodingRegion()[0]) / (alphaTypical3 + alphaATypical3))
					* (betaTypical2 / (alphaTypical3 + betaTypical1));
			double atypEnd = (alphaATypical2 / (alphaTypical3 + alphaATypical3))
					* (codingRegionTypicals.get(2).get(
							gen.getBasaCodingRegion()[2]) / (alphaTypical3 + alphaATypical3))
					* (codingRegionTypicals.get(0).get(
							gen.getBasaCodingRegion()[0]) / (alphaTypical3 + alphaATypical3))
					* (betaATypical2 / (alphaTypical3 + alphaATypical3));

			startTypical = sTyp / (sTyp + sATyp);
			startAtypical = sATyp / (sTyp + sATyp);
			typicalStop = typEnd / (typEnd + atypEnd);
			atypicalStop = atypEnd / (typEnd + atypEnd);
			double x = typicalStop + atypicalStop;

			// transition.setStartTypical(startTypical);
			// transition.setStartAtypical(startAtypical);
			// transition.setTypicalStop(atypicalStop);
			// transition.setAtypicalStop(atypicalStop);

			System.out.println("startTypical: " + startTypical);
			System.out.println("startATypical: " + startAtypical);
			System.out.println("typicalStop: " + typicalStop);
			System.out.println("atypStop: " + atypicalStop);
			System.out.println("===========");

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
