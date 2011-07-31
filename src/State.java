import java.io.Serializable;

class State implements Serializable {
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