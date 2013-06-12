package neuralNetwork;

import java.util.Arrays;

import patternTools.ConvertPattern;

public class Main {

	public static void main(String[] args) {

		// double[][] correct = { { 3.0 }, { 1.0 } };
		//
		// double[][] entrance = { { 2.0, 3.0 }, { 4.0, 4.0 } };
		// Adaline d = new Adaline(0.1, 0.005);
		// d.learn(entrance, correct);

//		int[] structure = { 2, 1 };
		double alfa = 0.5;
		boolean isBias = false;

		// double[][] in={{1.0,0.0},{1.0,1.0},{0.0,0.0},{0.0,1.0}}; // OR gate
		// double[][] out={{1.0},{1.0},{0.0},{1.0}};

//		double[][] in = { { 1.0, 0.0 }, { 1.0, 1.0 }, { 0.0, 0.0 },	{ 0.0, 1.0 } }; // AND gate
//		double[][] out = { { 0.0 }, { 1.0 }, { 0.0 }, { 0.0 } };

		// double[][] in={{1.0,0.0},{1.0,1.0},{0.0,0.0},{0.0,1.0}}; // XOR gate
		// double[][] out={{1.0},{0.0},{0.0},{1.0}}; //struktura {2,2,1}

		ConvertPattern cp = new ConvertPattern("C:\\Users\\Adam\\git\\SoftComputing\\letters");
		double[][] in = cp.convertIntToDouble(cp.getInput());
		double[][] out = cp.convertIntToDouble(cp.getOutput());
		int[] structure = cp.getStructure();
		
		Madaline m = new Madaline(structure, alfa, isBias);
		
		double[][][] weights = new double[1][26][16];
		for(int i = 0; i < 26; i++)
		{
			for(int j = 0; j < 16; j++)
			{
				weights[0][i][j] = in[i][j] / 16d;
			}
		}
		m.setAllWeights(weights, null);
		
		System.out.println(Arrays.toString(m.epoc(in[conv('C')]))); // A
		
//		m.learn(in, out);
		

	}
	
	public static int conv(char a)
	{
		if( a <= 90)
			return a - 65;
		else
			return a - 97;
			
	}

}
