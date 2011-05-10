package edu.rizza.ihmm;

import java.util.ArrayList;
import java.util.List;

public class HMM {
	class State {
		private double a;
		private double t;
		private double c;
		private double g;

		public State(double a, double t, double c, double g) {
			this.a = a;
			this.t = t;
			this.c = c;
			this.g = g;
		}

		public double getA() {
			return a;
		}

		public void setA(double a) {
			this.a = a;
		}

		public double getT() {
			return t;
		}

		public void setT(double t) {
			this.t = t;
		}

		public double getC() {
			return c;
		}

		public void setC(double c) {
			this.c = c;
		}

		public double getG() {
			return g;
		}

		public void setG(double g) {
			this.g = g;
		}

		public void set(char name, double value) {
			switch (name) {
			case 'a':
				setA(value);
				break;
			case 't':
				setT(value);
				break;
			case 'c':
				setC(value);
				break;
			case 'g':
				setG(value);
				break;
			}
		}

		public double get(char name) {

			double value;
			// kondisional
			switch (name) {
			case 'a':
				return getA();
			case 't':
				return getT();
			case 'c':
				return getC();
			case 'g':
				return getG();
			}
			return 0;

		}
	}

	class Transition {
		private double startAtypical;
		private double startTypical;
		private double typicalTypical;
		private double atypicalAtypical;
		private double typicalStop;
		private double atypicalStop;

		public double getStartTypical() {
			return startTypical;
		}

		public void setStartTypical(double startTypical) {
			this.startTypical = startTypical;
		}

		public double getStartAtypical() {
			return startAtypical;
		}

		public void setStartAtypical(double startAtypical) {
			this.startAtypical = startAtypical;
		}

		public double getTypicalTypical() {
			return typicalTypical;
		}

		public void setTypicalTypical(double typicalTypical) {
			this.typicalTypical = typicalTypical;
		}

		public double getAtypicalAtypical() {
			return atypicalAtypical;
		}

		public void setAtypicalAtypical(double atypicalAtypical) {
			this.atypicalAtypical = atypicalAtypical;
		}

		public double getTypicalStop() {
			return typicalStop;
		}

		public void setTypicalStop(double typicalStop) {
			this.typicalStop = typicalStop;
		}

		public double getAtypicalStop() {
			return atypicalStop;
		}

		public void setAtypicalStop(double atypicalStop) {
			this.atypicalStop = atypicalStop;
		}

	}

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

	public void train(DNA dna) {
		for (Gen gen : dna.getGenes()) {
			// untuk mengambil probabilitas startCodon
			double p = startCodons.get(0).get(gen.getBasaStartCodon()[0]);
			double q = startCodons.get(1).get(gen.getBasaStartCodon()[1]);
			double r = startCodons.get(2).get(gen.getBasaStartCodon()[2]);

			double alpha1;
			alpha1 = p * q * r;

			{
				int index = 0;
				for (char basa : gen.getBasaCodingRegion()) {
					if (index % 3 == 0) {
						if (gen.isTypical()) {
							codingRegionTypicals.get(0).get(basa);
							codingRegionTypicals.get(1).get(basa);
							codingRegionTypicals.get(2).get(basa);

							double typ;
							typ = codingRegionTypicals.get(0).get(basa)
									* codingRegionTypicals.get(1).get(basa)
									* codingRegionTypicals.get(2).get(basa);
						} else {
							codingRegionAtypicals.get(0).get(basa);
							codingRegionAtypicals.get(1).get(basa);
							codingRegionAtypicals.get(2).get(basa);

							double atyp;
							atyp = codingRegionAtypicals.get(0).get(basa)
									* codingRegionAtypicals.get(1).get(basa)
									* codingRegionAtypicals.get(2).get(basa);
						}
					}

					index++;

				}

				double alpha21;
				double alpha22;

				alpha21 = typ * alpha1 * transition.getStartTypical()
						* transition.getTypicalTypical();
				alpha22 = atyp * alpha1 * transition.getStartAtypical()
						* transition.getAtypicalAtypical();
			}

			stopCodons.get(0).get(gen.getBasaStopCodon()[0]);
			stopCodons.get(1).get(gen.getBasaStopCodon()[1]);
			stopCodons.get(2).get(gen.getBasaStopCodon()[2]);

			double alpha3 = stopCodons.get(0).get(gen.getBasaStopCodon()[0])
					* stopCodons.get(1).get(gen.getBasaStopCodon()[1])
					* stopCodons.get(2).get(gen.getBasaStopCodon()[2]);

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
