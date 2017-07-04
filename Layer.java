public class Layer {//A layer of neurons! I could have just used a Neuron[]
	//but this improves performance, readability, and space
	public Layer prev;
	public Layer next;
	public Neuron[] contents;
	public Layer (Layer p, Layer n, Neuron[] c) {
		prev = p;
		next = n;
		contents = c;
	}
}