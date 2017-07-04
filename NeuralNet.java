import java.lang.Math;

/*
 * Explanation:
 * A Neural Network is a type of artificial intelligence which uses supervised learning to learn
 * different patterns in data. More complex data requires more layers and more neurons per layer,
 * as well as a lower learning rate.
 * 
 * General algorithm:
 * The output of the neural net is calculated using feed-forward. Feed-forward is when an array
 * of ordered inputs is sent to a layer of neurons, which weights each input, then applies a
 * sigmoid on the sum. This new value is the output (or simply value) of the neuron.
 * The next layer uses the values of the first layer as inputs to each neuron.
 * The process continues until the final layer, whose outputs are the outputs of the NN.
 * 
 * A Neural Network learns through backpropagation. If we have an error function which
 * represents the "error" of the outputs of the NN with respect to a particular case
 * (usually this is (1/2) times the sum of the differences between the target value and the
 * output value), then we would like to adjust the weights in order to minimize that error.
 * Each weight can be adjusted so that the error function decreases.
 * This usues backpropagation. Backpropagation is a method to calculate the partial derivative
 * of the error with respect to each weight.
 * Basically, by using the chain rule approximately 2.64 billion (exaggeration) times, you get
 * the formula for the deltas of each neuron to minimize the error function on a single case.
 * 
 * Another important term is stochastic gradient descent. Once we have used backpropagation to
 * calculate the derivative of the error with respect to each weight (remember, this is for
 * a single target output), we change the weights so that the error goes down. But since the
 * error is for a single case of the data, we need to be careful with how much we change the
 * weights. The general idea of SGD is that there are certain patterns within the data, so
 * the weights should tend to converge to a certain value. If we change the weights by, say,
 * 0.1 of the total change required for one specific case (this is the learning rate), then
 * given enough cases, the weights will gradually shift to their final values in a sort of
 * biased random walk.
 */

public class NeuralNet {
	public Layer[] layers;//the layers of neurons in the NN. layers[0] is an input (only the value matters)
	//and layers[layers.length - 1] is the output layer
	double learningRate;
	public NeuralNet (int[] sizeOfEachLayer, double l) {//constructor; constructs any size neural net with any learning rate
		layers = new Layer[sizeOfEachLayer.length];
		//Initialize the 1st layer
		layers[0] = new Layer (null, null, new Neuron[sizeOfEachLayer[0]]);
		for (int i = 0; i < layers[0].contents.length; i++) {
			layers[0].contents[i] = new Neuron (null, null, 0);
		}
		//Initialize the next layers, making sure to not have null pointer exceptions
		for (int i = 1; i < layers.length; i++) {
			layers[i] = new Layer (layers[i-1], null, new Neuron[sizeOfEachLayer[i]]);
			for (int j = 0; j < layers[i].contents.length; j++) {
				layers[i].contents[j] = new Neuron (layers[i-1], null, 0);
			}
			layers[i-1].next = layers[i];
			for (int j = 0; j < layers[i-1].contents.length; j++) {
				layers[i-1].contents[j].next = layers[i];
			}
		}
		learningRate = l;//set the learning rate
	}
	public void setInputs (double[] in) {//sets the inputs to new values and updates the output of the NN (feeds forward)
		for (int i = 0; i < layers[0].contents.length; i++) {
			layers[0].contents[i].value = in[i];
		}
		for (int i = 1; i < layers.length; i++) {
			for (int j = 0; j < layers[i].contents.length; j++) {
				layers[i].contents[j].getValue();
			}
		}
	}
	public double getError(double[] targets) {//Gets the total error of the net given an array of target outputs
		//this is never used, but is included for completeness
		double sum = 0;
		for (int i = 0; i < layers[layers.length - 1].contents.length; i++) {
			sum += 0.5 * Math.pow((targets[i] - layers[layers.length - 1].contents[i].value), 2);
		}
		return sum;
	}
	public void getDeltas (Layer lay, double[] target) {//backpropagation of deltas
		if (lay == layers[layers.length - 1]) {//if output layer
			for (int j = 0; j < lay.contents.length; j++) {
				Neuron current = lay.contents[j];
				current.delta = (current.value - target[j]) * current.value * (1.0 - current.value);
			}
			return;
		}
		getDeltas (lay.next, target);//why backpropagation is named as such (1)
		for (int j = 0; j < lay.contents.length; j++) {
			Neuron current = lay.contents[j];
			double sum = 0.0;
			for (int x = 0; x < lay.next.contents.length; x++) {//see (1)
				sum += lay.next.contents[x].delta * lay.next.contents[x].weights[j];
			}
			for (int i = 0; i < lay.prev.contents.length; i++) {
				current.delta = sum * current.value * (1.0 - current.value);
			}
		}
	}
	public void backprop (double[] target) {//runs getDeltas on the correct layer and updates weights
		getDeltas (layers[1], target);
		for (int lay = 1; lay < layers.length; lay++) {
			for (Neuron n : layers[lay].contents) {
				for (int i = 0; i < n.weights.length; i++) {
					n.weights[i] -= learningRate * n.delta * n.prev.contents[i].value;
				}
			}
		}
	}
	public String toString() {//temporary toString() used for testing
		String ret = "";
		for (Layer l : layers) {
			for (Neuron n : l.contents) {
				for (double d : n.weights) {
					ret = ret + (double)(int)(d * 1000)/1000 + " ";
				}
				ret = ret + "\t";
			}
			ret = ret + "\n";
		}
		return ret;
	}
}