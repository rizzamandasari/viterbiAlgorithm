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

			int[][][] typicalStates = {
					{ { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } },
					{ { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } },
					{ { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } } };
			int[][][] atypicalState = {
					{ { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } },
					{ { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } },
					{ { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } } };

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
					stopCodons.get(0).get(gen.getBasaStartCodon()[0])
							* transition.getTypicalStop());
			gen.getBetaTypical3().add(
					1,
					stopCodons.get(0).get(gen.getBasaStartCodon()[0])
							* stopCodons.get(1).get(gen.getBasaStartCodon()[1])
							* transition.getTypicalStop());
			gen.getBetaTypical3().add(
					2,
					stopCodons.get(0).get(gen.getBasaStartCodon()[0])
							* stopCodons.get(1).get(gen.getBasaStartCodon()[1])
							* stopCodons.get(2).get(gen.getBasaStartCodon()[2])
							* transition.getTypicalStop());

			betaATypical3 = stopCodons.get(0).get(gen.getBasaStopCodon()[0])
					* stopCodons.get(1).get(gen.getBasaStopCodon()[1])
					* stopCodons.get(2).get(gen.getBasaStopCodon()[2])
					* transition.getAtypicalStop();

			gen.getBetaATypical3().add(
					0,
					stopCodons.get(0).get(gen.getBasaStartCodon()[0])
							* transition.getAtypicalStop());
			gen.getBetaATypical3().add(
					1,
					stopCodons.get(0).get(gen.getBasaStartCodon()[0])
							* stopCodons.get(1).get(gen.getBasaStartCodon()[1])
							* transition.getAtypicalStop());
			gen.getBetaATypical3().add(
					2,
					stopCodons.get(0).get(gen.getBasaStartCodon()[0])
							* stopCodons.get(1).get(gen.getBasaStartCodon()[1])
							* stopCodons.get(2).get(gen.getBasaStartCodon()[2])
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
						// alphaTypical2);
						// System.out.println("Alpha ATypical 2: "
						// + alphaATypical2);
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
						// alphaATypical2);
					}
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

			// hitung beta1
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

			// ================================================================
			// emition probability

			// sum_state += (alpha char . beta char) / fwdbwd -> yg ini u/state
			double sumStateTyp0 = gen.sumStateTypical(0);
			double sumStateTyp1 = gen.sumStateTypical(1);
			double sumStateTyp2 = gen.sumStateTypical(2);
			double sumStateAtyp0 = gen.sumStateATypical(0);
			double sumStateAtyp1 = gen.sumStateATypical(1);
			double sumStateAtyp2 = gen.sumStateATypical(2);

			System.out.println(sumStateAtyp0);

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

			// }

			// perchar
			// sum = sum_char / sum_state nilai e_prob

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
