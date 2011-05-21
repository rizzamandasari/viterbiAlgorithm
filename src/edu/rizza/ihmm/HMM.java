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
		double alpha1 = 1, alpha21 = 1, alpha22 = 1, alpha31 = 1, alpha32 = 1, beta4 = 1, beta31, beta32, beta21 = 1, beta22 = 1, beta11 = 1, beta12 = 1;
		Gen gene = new Gen();
		for (Gen gen : dna.getGenes()) {
			// untuk mengambil probabilitas startCodon
			alpha1 = startCodons.get(0).get(gen.getBasaStartCodon()[0])
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

			alpha21 = typ * alpha1 * transition.getStartTypical()
					* transition.getTypicalTypical(); // error
			alpha22 = atyp * alpha1 * transition.getStartAtypical()
					* transition.getAtypicalAtypical(); // error

			alpha31 = stopCodons.get(0).get(gen.getBasaStopCodon()[0])
					* stopCodons.get(1).get(gen.getBasaStopCodon()[1])
					* stopCodons.get(2).get(gen.getBasaStopCodon()[2])
					* transition.getTypicalStop() * alpha21;

			alpha32 = stopCodons.get(0).get(gen.getBasaStopCodon()[0])
					* stopCodons.get(1).get(gen.getBasaStopCodon()[1])
					* stopCodons.get(2).get(gen.getBasaStopCodon()[2])
					* transition.getAtypicalStop() * alpha22;

			System.out.println("Alpha Typical: " + alpha31);
			System.out.println("Alpha Atypical: " + alpha32);

		}

		for (Gen gen : dna.getGenes()) {
			// coding region
			beta31 = stopCodons.get(0).get(gen.getBasaStopCodon()[0])
					* stopCodons.get(1).get(gen.getBasaStopCodon()[1])
					* stopCodons.get(2).get(gen.getBasaStopCodon()[2])
					* transition.getTypicalStop();
			beta32 = stopCodons.get(0).get(gen.getBasaStopCodon()[0])
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
			beta21 = b_typ * transition.getTypicalTypical()
					* transition.getStartTypical() * beta31;
			beta22 = b_atyp * transition.getAtypicalAtypical()
					* transition.getStartAtypical() * beta32;
			// begin
			beta11 = startCodons.get(0).get(gen.getBasaStartCodon()[0])
					* startCodons.get(1).get(gen.getBasaStartCodon()[1])
					* startCodons.get(2).get(gen.getBasaStartCodon()[2])
					* beta21;
			beta12 = startCodons.get(0).get(gen.getBasaStartCodon()[0])
					* startCodons.get(1).get(gen.getBasaStartCodon()[1])
					* startCodons.get(2).get(gen.getBasaStartCodon()[2])
					* beta22;
			System.out.println("beta typical: " + beta11);
			System.out.println("beta atypical: " + beta12);

			// ================>step 5 tansition prob
		}
		double fwd_bwd_typ = alpha31 + beta11;
		double fwd_bwd_atyp = alpha32 + beta12;
		double prob_s2typ = (alpha1
				* startCodons.get(0).get(gene.getBasaStartCodon()[2])
				* codingRegionTypicals.get(0)
						.get(gene.getBasaCodingRegion()[0]) * beta21)
				/ (fwd_bwd_typ);
		double prob_s2atyp = (alpha1
				* startCodons.get(0).get(gene.getBasaStartCodon()[0])
				* codingRegionAtypicals.get(0).get(
						gene.getBasaCodingRegion()[1]) * beta22)
				/ (fwd_bwd_typ);

		double prob_typ2typ = (alpha21
				* codingRegionTypicals.get(0)
						.get(gene.getBasaCodingRegion()[2])
				* codingRegionTypicals.get(0)
						.get(gene.getBasaCodingRegion()[0]) * beta22)
				/ (fwd_bwd_typ);

		double prob_atyp2atyp = (alpha21
				* codingRegionTypicals.get(0)
						.get(gene.getBasaCodingRegion()[2])
				* codingRegionTypicals.get(0)
						.get(gene.getBasaCodingRegion()[0]) * beta22)
				/ (fwd_bwd_typ);

		double prob_typ2end = (alpha21
				* codingRegionTypicals.get(0)
						.get(gene.getBasaCodingRegion()[2])
				* codingRegionTypicals.get(0)
						.get(gene.getBasaCodingRegion()[0]) * beta22)
				/ (fwd_bwd_typ);

		double prob_atyp2end = (alpha22
				* codingRegionTypicals.get(0)
						.get(gene.getBasaCodingRegion()[2])
				* codingRegionTypicals.get(0)
						.get(gene.getBasaCodingRegion()[0]) * beta22)
				/ (fwd_bwd_typ);

		double t_prob_s2typ = prob_s2typ / (prob_s2typ + prob_s2atyp);
		double t_prob_s2atyp = prob_s2atyp / (prob_s2typ + prob_s2atyp);
		double t_prob_ty2typ = prob_typ2typ / (prob_typ2typ + prob_typ2end);
		double t_prob_ty2end = prob_typ2end / (prob_typ2typ + prob_typ2end);
		double t_prob_aty2atyp = prob_atyp2atyp
				/ (prob_atyp2atyp + prob_atyp2end);
		double t_prob_atyp2end = prob_atyp2end
				/ (prob_atyp2atyp + prob_atyp2end);
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
