import java.io.Serializable;

class Transition implements Serializable {
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