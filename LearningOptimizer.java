import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class LearningOptimizer {//Runs tests to find the optimal learning rate for a given data set
	public static void main(String[] args) throws IOException {
		double optL=0;
		double maxAvg=0;
		for (int i=1;i<26;i++){//increments from 0 to 0.5 by 0.02.
			double[] avg = new double[5];//runs 5 times for every learning rate
			for (int k=0;k<5;k++){
				PolynomialTester ptest=new PolynomialTester(i/50.0);
				ptest.netRun();
				Scanner p = new Scanner (new File ("percentages.txt"));
				int[] percent = new int[2000];
				for (int j=0;j<2000;j++){percent[j]=p.nextInt();}
				double res = 0;
				for (int j=0;j<20;j++){res+=percent[1999-j];}//this considers the final 1000 neural net runs
				//out of 100000, which shows how accurate it ended up learning.
				res/=20;
				avg[k]=res;
			}
			double fin = 0;
			for (int j=0;j<5;j++){fin+=avg[j];}
			fin/=5;
			System.out.println(i/50.0+" "+fin);
			if (fin>maxAvg){maxAvg=fin; optL=i/50.0;}//checking if this learning rate was optimal
		}
		System.out.println(optL+" "+maxAvg);
	}
}
