package neuralNetwork;

import java.util.Arrays;

public class Main {

	public static void main(String[] args) {

		// double[][] correct = { { 3.0 }, { 1.0 } };
		//
		// double[][] entrance = { { 2.0, 3.0 }, { 4.0, 4.0 } };
		// Adaline d = new Adaline(0.1, 0.005);
		// d.learn(entrance, correct);

		int[] structure = { 2, 1 };
		double alfa = 0.2;
		boolean isBias = true;

		// double[][] in={{1.0,0.0},{1.0,1.0},{0.0,0.0},{0.0,1.0}}; // OR gate
		// double[][] out={{1.0},{1.0},{0.0},{1.0}};

		double[][] in = { { 1.0, 0.0 }, { 1.0, 1.0 }, { 0.0, 0.0 },	{ 0.0, 1.0 } }; // AND gate
		double[][] out = { { 0.0 }, { 1.0 }, { 0.0 }, { 0.0 } };

		// double[][] in={{1.0,0.0},{1.0,1.0},{0.0,0.0},{0.0,1.0}}; // XOR gate
		// double[][] out={{1.0},{0.0},{0.0},{1.0}}; //struktura {2,2,1}

		double[] oneIn = {1.0, 1.0};
		Madaline m = new Madaline(structure, alfa, isBias);
		double[][][] weights = new double[][][]{ { {1.0, 1.0 } } };
		double[][] bias = new double[][]{ {0.5}, {0.5} };
		m.setAllWeights(weights, bias);
		System.out.println(Arrays.toString(m.epoc(oneIn)));
		System.out.println("---");
		m.printNetwork();
		

	}

}
