package edu.rizza.ihmm;

public class Gen {
	private Integer[] startCodon;
	private Integer[] stopCodon;
	private String[] codingRegion;
	private String sequence;
	private char[] basaStartCodon;
	private char[] basaCodingRegion;
	private char[] basaStopCodon;

	/*
	 * public void isComplete() { Iterator<Gen> iterator = genes.iterator();
	 * while (iterator.hasNext()) { if(genes.equals(stopCodon)){
	 * 
	 * }
	 * 
	 * }
	 */

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

}
