package edu.rizza.ihmm;

public class Gen {
	private Integer[] startCodon;
	private Integer[] stopCodon;
	private Integer[] codingRegion;

	public Gen(Integer[] startCodon) {
		this.startCodon = startCodon;
	}

	public Complete() {

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

	public Integer[] getCodingRegion() {
		return codingRegion;
	}

	public void setCodingRegion(Integer[] codingRegion) {
		this.codingRegion = codingRegion;
	}

}
