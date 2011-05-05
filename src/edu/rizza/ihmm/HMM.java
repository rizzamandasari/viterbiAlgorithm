package edu.rizza.ihmm;

import java.util.ArrayList;
import java.util.List;

public class HMM {
	class State {
		private double a;
		private double t;
		private double c;
		private double g;

		class Transition {
			private double startatypical;
			private double typicaltypical;
			private double atypicalatypical;
			private double typicalstop;
			private double atypicalstop;

			private double starttypical;

			public double getStarttypical() {
				return starttypical;
			}

			public void setStarttypical(double starttypical) {
				this.starttypical = starttypical;
			}

			public double getStartatypical() {
				return startatypical;
			}

			public void setStartatypical(double startatypical) {
				this.startatypical = startatypical;
			}

			public double getTypicaltypical() {
				return typicaltypical;
			}

			public void setTypicaltypical(double typicaltypical) {
				this.typicaltypical = typicaltypical;
			}

			public double getAtypicalatypical() {
				return atypicalatypical;
			}

			public void setAtypicalatypical(double atypicalatypical) {
				this.atypicalatypical = atypicalatypical;
			}

			public double getTypicalstop() {
				return typicalstop;
			}

			public void setTypicalstop(double typicalstop) {
				this.typicalstop = typicalstop;
			}

			public double getAtypicalstop() {
				return atypicalstop;
			}

			public void setAtypicalstop(double atypicalstop) {
				this.atypicalstop = atypicalstop;
			}

		}

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

	private final List<State> startCodons = new ArrayList<State>();
	private final List<State> stopCodons = new ArrayList<State>();
	private final List<State> codingRegionTypicals = new ArrayList<State>();
	private final List<State> codingRegionAtypicals = new ArrayList<State>();
	private tp = new Transition();

	public HMM() {
		startCodons.add(new State(0.8, 0.01, 0.01, 0.18));
		startCodons.add(new State(0.01, 0.97, 0.01, 0.01));
		startCodons.add(new State(0.01, 0.01, 0.01, 0.97));

		codingRegionTypicals.add(new State(0.1, 0.2, 0.3, 0.4));
		codingRegionTypicals.add(new State(0.1, 0.2, 0.3, 0.4));
		codingRegionTypicals.add(new State(0.1, 0.2, 0.3, 0.4));

		codingRegionAtypicals.add(new State(0.1, 0.2, 0.3, 0.4));
		codingRegionAtypicals.add(new State(0.1, 0.2, 0.3, 0.4));
		codingRegionAtypicals.add(new State(0.1, 0.2, 0.3, 0.4));

		stopCodons.add(new State(0.1, 0.2, 0.3, 0.4));
		stopCodons.add(new State(0.1, 0.2, 0.3, 0.4));
		stopCodons.add(new State(0.1, 0.2, 0.3, 0.4));

	}

	public void train(DNA dna) {
		for (Gen gen : dna.getGenes()) {
			// untuk mengambil probabilitas startCodon
			startCodons.get(0).get(gen.getBasaStartCodon()[0]);
			startCodons.get(1).get(gen.getBasaStartCodon()[1]);
			startCodons.get(2).get(gen.getBasaStartCodon()[2]);

			double alpha1 = startCodons.get(0).get(gen.getBasaStartCodon()[0])
					* startCodons.get(1).get(gen.getBasaStartCodon()[1])
					* startCodons.get(2).get(gen.getBasaStartCodon()[2]);

			int index = 0;
			for (char neuclotida : gen.getBasaCodingRegion()) {
				if (index % 3 == 0) {
					if (gen.isTypical()) {
						codingRegionTypicals.get(0).get(neuclotida);
						codingRegionTypicals.get(1).get(neuclotida);
						codingRegionTypicals.get(2).get(neuclotida);
					} else { // if atypical
						codingRegionTypicals.get(0).get(neuclotida);
						codingRegionTypicals.get(1).get(neuclotida);
						codingRegionTypicals.get(2).get(neuclotida);
					}
				}
				index++;
			}
			// untuk mengambil
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
