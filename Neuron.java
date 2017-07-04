import java.lang.Math;

public class Neuron {
	public Layer prev;//The neural network is structured as a Layer[].
	public Layer next;
	public double[] weights;//one for each neuron in the next layer
	public double delta;//the delta value for each neuron.
	//This is the derivative of the error with respect to the weights of this neuron.
	public double value;//the value of the neuron
	public Neuron (Layer p, Layer c, int v) {
		prev = p;
		next = c;
		if (prev == null)
			weights = new double[0];
		else
			weights = new double[prev.contents.length];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = Math.random() * 2 - 1;
		}
		delta = 0.0;
		value = 0.0;
	}
	public static double sigmoid (double in) {
		return 1.0 / (1.0 + Math.pow (2.718281828, -in));
	}
	public double getNet() {//net is the weighted sum of the inputs.
		//This is before the sigmoid is applied
		double sum = 0.0;
		for (int i = 0; i < weights.length; i++) {
			sum += weights[i] * prev.contents[i].value;
		}
		return sum;
	}
	public void getValue() {
		value = sigmoid (getNet());
	}
}