package edu.rizza.ihmm;

public class State {

	private double startState;
	private double codState;
	private double codAtState;
	private double stopState;

	// bikin single ton

	State() {

	}

	private State instance;

	public State getInstance() {
		return instance = instance != null ? new State() : instance;
	}

	public void setStartState(double startState) {
		this.startState = startState;
		double a = 0.8;

	}

	public double getStartState() {
		return startState;
	}

	public void setCodState(double codState) {
		this.codState = codState;
	}

	public double getCodState() {
		return codState;
	}

	public void setStopState(double stopState) {
		this.stopState = stopState;
	}

	public double getStopState() {
		return stopState;
	}

	public void setCodAtState(double codAtState) {
		this.codAtState = codAtState;
	}

	public double getCodAtState() {
		return codAtState;
	}

}
