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
		stopCodons.add(new State(0.1, 0.2, 0.3, 0.4));

		transition.setStartAtypical(0.8);
		transition.setStartTypical(0.2);
		transition.setTypicalTypical(0.8);
		transition.setAtypicalAtypical(0.1);
		transition.setTypicalStop(0.2);
		transition.setAtypicalStop(0.9);

	}

	public void fwdBwd(DNA dna) {
		for (Gen gen : dna.getGenes()) {
			// untuk mengambil probabilitas startCodon
			double alpha1 = startCodons.get(0).get(gen.getBasaStartCodon()[0])
					* startCodons.get(1).get(gen.getBasaStartCodon()[1])
					* startCodons.get(2).get(gen.getBasaStartCodon()[2]);

			double atyp = 1;
			double typ = 1;
			int index = 0;
			for (char basa : gen.getBasaCodingRegion()) {
				if (index % 3 == 0) {
					if (gen.isTypical()) {

						typ = codingRegionTypicals.get(0).get(basa)
								* codingRegionTypicals.get(1).get(basa)
								* codingRegionTypicals.get(2).get(basa);
					} else {
						atyp = codingRegionAtypicals.get(0).get(basa)
								* codingRegionAtypicals.get(1).get(basa)
								* codingRegionAtypicals.get(2).get(basa);
					}
				}

				index++;

			}

			// double alpha21;
			// double alpha22;

			double alpha21 = typ * alpha1 * transition.getStartTypical()
					* transition.getTypicalTypical(); // error
			double alpha22 = atyp * alpha1 * transition.getStartAtypical()
					* transition.getAtypicalAtypical(); // error

			double alpha31 = stopCodons.get(0).get(gen.getBasaStopCodon()[0])
					* stopCodons.get(1).get(gen.getBasaStopCodon()[1])
					* stopCodons.get(2).get(gen.getBasaStopCodon()[2])
					* transition.getTypicalStop() * alpha21;

			double alpha32 = stopCodons.get(0).get(gen.getBasaStopCodon()[0])
					* stopCodons.get(1).get(gen.getBasaStopCodon()[1])
					* stopCodons.get(2).get(gen.getBasaStopCodon()[2])
					* transition.getAtypicalStop() * alpha22;

			System.out.println("Alpha Typical: " + alpha31);
			System.out.println("Alpha Atypical: " + alpha32);

		}

		for (Gen gen : dna.getGenes()) {
			// stop codon
			double beta4 = 1;

			// coding region
			double beta31 = stopCodons.get(0).get(gen.getBasaStopCodon()[0])
					* stopCodons.get(1).get(gen.getBasaStopCodon()[1])
					* stopCodons.get(2).get(gen.getBasaStopCodon()[2])
					* transition.getTypicalStop();
			double beta32 = stopCodons.get(0).get(gen.getBasaStopCodon()[0])
					* stopCodons.get(1).get(gen.getBasaStopCodon()[1])
					* stopCodons.get(2).get(gen.getBasaStopCodon()[2])
					* transition.getAtypicalStop();

			// start codon
			double b_typ = 1;
			double b_atyp = 1;

			int index = 0;
			for (char basa : gen.getBasaCodingRegion()) {
				if (index % 3 == 0) {
					if (gen.isTypical()) {

						b_typ = codingRegionTypicals.get(0).get(basa)
								* codingRegionTypicals.get(1).get(basa)
								* codingRegionTypicals.get(2).get(basa);
					} else {

						b_atyp = codingRegionAtypicals.get(0).get(basa)
								* codingRegionAtypicals.get(1).get(basa)
								* codingRegionAtypicals.get(2).get(basa);
					}
				}

				index++;
			}
			// start
			double beta21 = b_typ * transition.getTypicalTypical()
					* transition.getStartTypical() * beta31;
			double beta22 = b_atyp * transition.getAtypicalAtypical()
					* transition.getStartAtypical() * beta32;
			// begin
			double beta11 = startCodons.get(0).get(gen.getBasaStartCodon()[0])
					* startCodons.get(1).get(gen.getBasaStartCodon()[1])
					* startCodons.get(2).get(gen.getBasaStartCodon()[2])
					* beta21;
			double beta12 = startCodons.get(0).get(gen.getBasaStartCodon()[0])
					* startCodons.get(1).get(gen.getBasaStartCodon()[1])
					* startCodons.get(2).get(gen.getBasaStartCodon()[2])
					* beta22;
			System.out.println("beta typical: " + beta11);
			System.out.println("beta atypical: " + beta12);
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
