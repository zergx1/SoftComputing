package neuralNetwork;

import java.util.Arrays;

public class Main {

	public static void main(String[] args) {
		
		
			double[][] correct = {{3.0},{1.0}};
			
			double[][] entrance = {{2.0,3.0},{4.0,4.0}};
			Delta d = new Delta(0.1, 0.005);
			d.learn(entrance,correct);
			
//		int [] structure = {2,1};
//		double alfa = 0.2;
		
//		double[][] in={{1.0,0.0},{1.0,1.0},{0.0,0.0},{0.0,1.0}};	// OR gate
//		double[][] out={{1.0},{1.0},{0.0},{1.0}};
		double[][] in={{1.0,0.0},{1.0,1.0},{0.0,0.0},{0.0,1.0}};	// AND gate
		double[][] out={{0.0},{1.0},{0.0},{0.0}};

//		double[][] in={{1.0,0.0},{1.0,1.0},{0.0,0.0},{0.0,1.0}};	// XOR gate
//		double[][] out={{1.0},{0.0},{0.0},{1.0}};					//struktura {2,2,1}
		
//		Perceptron p = new Perceptron(alfa, 0, structure, false);
//		p.printNetwork();
//		
//		for(int i = 0 ; i < 100000; i++)
//			for(int j = 0 ; j < in.length; j++)
//				p.deltaRuleOnce(in[j], out[j]);
//		
//		
//		p.printNetwork();
//		
//		for(int i = 0; i < in.length; i++)
//		{
//			System.out.println(Arrays.toString(in[i]));
//			double result = p.epoc(in[i])[0];
//			if(result > 0.5 )
//				System.out.println(1);
//			else
//				System.out.println(0);
			
//			System.out.println(result);
//				
//		}
//		
	}

}
