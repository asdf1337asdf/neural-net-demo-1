import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class PolynomialTester{//Tests a Neural Net to classify points as above or below a cubic
	double learningRate;
	public PolynomialTester(double learn){
		learningRate=learn;
	}
	public void netRun() throws IOException{
		NeuralNet n = new NeuralNet (new int[]{2,9,9,1}, learningRate);
		FileWriter p = new FileWriter (new File ("percentages.txt"));
		double[][] cases = new double[100000][];//there are 100000 cases
		Random r = new Random();
		for (int i = 0; i < cases.length; i++) {//If the point is above
			//x^3 - x^2 - x + 1, the target output is 1
			//Otherwise, the target output is 0
			double d1 = r.nextDouble() * 4 - 2;
			double d2 = r.nextDouble() * 3 - 1;
			double d3;
			if (d1*d1*d1 - d1*d1 - d1 + 1 > d2)
				d3 = 1.0;
			else
				d3 = 0.0;
			cases[i] = new double[]{d1,d2,d3};
		}
		int[] didRight = new int[cases.length];//Tests if the NN got it right or not
		for (int i = 0; i < cases.length; i++) {
			n.setInputs(new double[]{cases[i][0], cases[i][1]});//Sets the inputs, feeds forward
			if (cases[i][2] == 0) {//checks correctness
				if (n.layers[n.layers.length - 1].contents[0].value < 0.5)
					didRight[i] = 1;
				else
					didRight[i] = 0;
			}
			else {//checks correctness
				if (n.layers[n.layers.length - 1].contents[0].value > 0.5)
					didRight[i] = 1;
				else
					didRight[i] = 0;
			}
			n.backprop(new double[]{cases[i][2]});//learns
		}
		for (int i = 0; i < cases.length; i += 50) {//This nested loop prints the percentage
			//it got correct in each group of 50 cases
			int sum = 0;
			for (int j = 0; j < 50; j++) {
				sum += didRight[i + j];
			}
			p.write(((Integer)(sum * 2)).toString() + "\r\n");//and writes it to percentages.txt
		}
		p.close();
	}
}