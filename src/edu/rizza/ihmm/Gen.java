package edu.rizza.ihmm;

import java.util.ArrayList;
import java.util.List;

public class Gen {
	private Integer[] startCodon;
	private Integer[] stopCodon;
	private String[] codingRegion;
	private String sequence;
	private char[] basaStartCodon;
	private char[] basaCodingRegion;
	private char[] basaStopCodon;

	private final List<Double> alphaTypical1 = new ArrayList<Double>();
	private final List<Double> alphaTypical2 = new ArrayList<Double>();
	private final List<Double> alphaTypical3 = new ArrayList<Double>();

	private final List<Double> betaTypical1 = new ArrayList<Double>();
	private final List<Double> betaTypical2 = new ArrayList<Double>();
	private final List<Double> betaTypical3 = new ArrayList<Double>();

	private final List<Double> alphaATypical1 = new ArrayList<Double>();
	private final List<Double> alphaATypical2 = new ArrayList<Double>();
	private final List<Double> alphaATypical3 = new ArrayList<Double>();

	private final List<Double> betaATypical1 = new ArrayList<Double>();
	private final List<Double> betaATypical2 = new ArrayList<Double>();
	private final List<Double> betaATypical3 = new ArrayList<Double>();

	/*
	 * public void isComplete() { Iterator<Gen> iterator = genes.iterator();
	 * while (iterator.hasNext()) { if(genes.equals(stopCodon)){
	 * 
	 * }
	 * 
	 * }
	 */

	public List<Double> getAlphaTypical1() {
		return alphaTypical1;
	}

	public List<Double> getAlphaTypical2() {
		return alphaTypical2;
	}

	public List<Double> getAlphaTypical3() {
		return alphaTypical3;
	}

	public List<Double> getBetaTypical1() {
		return betaTypical1;
	}

	public List<Double> getBetaTypical2() {
		return betaTypical2;
	}

	public List<Double> getBetaTypical3() {
		return betaTypical3;
	}

	public List<Double> getAlphaATypical1() {
		return alphaATypical1;
	}

	public List<Double> getAlphaATypical2() {
		return alphaATypical2;
	}

	public List<Double> getAlphaATypical3() {
		return alphaATypical3;
	}

	public List<Double> getBetaATypical1() {
		return betaATypical1;
	}

	public List<Double> getBetaATypical2() {
		return betaATypical2;
	}

	public List<Double> getBetaATypical3() {
		return betaATypical3;
	}

	public boolean isCompete() {
		return stopCodon.length != 0;
	}

	// geter setter
	public Integer[] getStartCodon() {
		return startCodon;
	}

	public void setStartCodon(Integer[] startCodon) {
		this.startCodon = startCodon;
	}

	public Integer[] getStopCodon() {
		return stopCodon;
	}

	public void setStopCodon(Integer[] stopCodon) {
		this.stopCodon = stopCodon;
	}

	public String[] getCodingRegion() {
		return codingRegion;
	}

	public void setCodingRegion(String[] codingRegion) {
		this.codingRegion = codingRegion;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getSequence() {
		return sequence;
	}

	public void setBasaStartCodon(char[] basaStartCodon) {
		this.basaStartCodon = basaStartCodon;
	}

	public char[] getBasaStartCodon() {
		if (basaStartCodon == null) {
			basaStartCodon = sequence.substring(0, 3).toCharArray();
		}
		return basaStartCodon;
	}

	public void setBasaCodingRegion(char[] basaCodingRegion) {
		this.basaCodingRegion = basaCodingRegion;
	}

	public char[] getBasaCodingRegion() {
		if (basaCodingRegion == null) {
			basaCodingRegion = sequence.substring(3, (sequence.length() - 3))
					.toCharArray();
		}
		return basaCodingRegion;
	}

	public void setBasaStopCodon(char[] basaStopCodon) {
		this.basaStopCodon = basaStopCodon;
	}

	public boolean isTypical() {
		return sequence.length() <= 1506;

	}

	public char[] getBasaStopCodon() {
		if (basaStopCodon == null) {
			basaStopCodon = sequence.substring((sequence.length() - 3),
					(sequence.length())).toCharArray();
		}
		return basaStopCodon;
	}

	public double sumStateTypical(int state) {
		double sumState = 0;

		int index = 0;
		for (double alpha : alphaTypical2) {
			if (index % 3 == state) {
				sumState += alpha
						* betaTypical2.get(alphaTypical2.size() - index - 1);
				// out of bound
			}
			index++;
		}
		return sumState;
	}

	public double sumStateATypical(int state) {
		double sumState = 0;

		int index = 0;

		for (double alpha : alphaATypical2) {
			if (index % 3 == state) {
				sumState += alpha
						* betaATypical2.get(alphaATypical2.size() - index - 1);
			}
			index++;
		}
		return sumState;

	}

	public double sumCharTypical(int state, char c) {

		double sumChar = 0;

		int index = 0;
		for (double alpha : alphaTypical2) {
			if (index % 3 == state && basaCodingRegion[index] == c) {
				sumChar += alpha
						* betaTypical2.get(alphaTypical2.size() - 1 - index);
			}
			index++;
		}
		return sumChar;

	}

	public double sumCharATypical(int state, char c) {
		double sumChar = 0;

		int index = 0;
		for (double alpha : alphaTypical2) {
			if (index % 3 == state && basaCodingRegion[index] == c) {
				sumChar += alpha
						* betaTypical2.get(alphaTypical2.size() - 1 - index);
			}
			index++;
		}
		return sumChar;
	}

}
