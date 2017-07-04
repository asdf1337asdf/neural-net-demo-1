import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TesterXOr {//tests the neural net to see if it can learn xor
	public static void main (String[] args) throws IOException {
		NeuralNet n = new NeuralNet (new int[]{2,2,1}, 0.1);//learns xor perfectly after 34150 cases
		//a 2,2,1 network is the smallest neural net that can learn xor
		FileWriter p = new FileWriter (new File ("percentages.txt"));//the output gets written to this
		ArrayList<double[]> cases = new ArrayList<double[]>();//training data
		for (int i = 0; i < 100000; i++) {//data is added 4 at a time to cover all cases
			double[] d;
			if (i%4 == 0)
				d = new double[]{0,0,0};
			else if (i%4 == 1)
				d = new double[]{0,1,1};
			else if (i%4 == 2)
				d = new double[]{1,0,1};
			else
				d = new double[]{1,1,1};
			cases.add(d);
		}
		int[] didRight = new int[cases.size()];//if it got the answer right or not on each case
		for (int i = 0; i < cases.size(); i++) {
			n.setInputs (new double[]{cases.get(i)[0], cases.get(i)[1]});//sets the inputs, feeds forward
			if (cases.get(i)[2] < 0.5) {//checks correctness
				if (n.layers[n.layers.length - 1].contents[0].value < 0.5)
					didRight[i] = 1;
				else {
					didRight[i] = 0;
				}
			}
			else {//checks correctness
				if (n.layers[n.layers.length - 1].contents[0].value > 0.5)
					didRight[i] = 1;
				else {
					didRight[i] = 0;
				}
			}
			n.backprop (new double[]{cases.get(i)[2]});//The net "learns" in this step
		}
		for (int i = 0; i < 100000; i += 50) {//This nested loop prints the percentage it got correct each 50 cases
			int sum = 0;
			for (int j = 0; j < 50; j++) {
				sum += didRight[i + j];
			}
			p.write(((Integer)(sum * 2)).toString() + "\n");//and writes it to percentages.txt
		}
		p.close();
	}
}