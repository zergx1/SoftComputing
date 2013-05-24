package neuralNetwork;

import java.lang.reflect.Array;
import java.util.Arrays;

public class Delta {
	private double learning_rate;
	private double precision;
	private Neuron[] neurons;
	
	Delta(double learning_rate, double precision)
	{
		this.learning_rate = learning_rate;
		this.precision = precision;
		this.neurons = new Neuron[1];
	}
	
	/**
	 * @param x
	 * @param y
	 */
	public void learn(double[][] x, double[][] y)
	{
		int iteration = 0;
		int numberOfSample = x.length;
		this.neurons[0] = new Neuron(x[0].length, NeuronType.Hidden,false);
		Neuron n = this.neurons[0];
		double error;
		double totalError = 0.0;
		double[] w = new double[n.getWeights().length];
		do
		// till output is greater then precision
		{
			totalError = 0.0;
			for(int j = 0; j < numberOfSample; j++)
			{
				n.setEntrence(x[j]);
				n.updateOutput(NeuronType.Hidden);
				error = y[j][0] - n.getOutput();
				for(int i = 0; i < w.length; i++)
				{
					w[i] = n.getWeights()[i] + learning_rate* n.getEntrence()[i]*error;
				}
				n.setWeights(w);
				totalError+=Math.abs((n.getOutput() - y[j][0]));
			iteration++;
			}
			if(iteration%100 == 0)
			{
				System.out.println("Epoc: " + iteration + " Actual weights: " + Arrays.toString(n.getWeights()) + " Actual error: " + totalError);
//				for(int i = 0; i < w.length; i++)
//				{
//					n.setEntrence(x[i]);
//					n.updateOutput(NeuronType.Hidden);
//					System.out.println("Entance[" + Arrays.toString(x[i]) + "]" + " Output[" + n.getOutput() + "]" + " Correct[" + Arrays.toString(y[i]) + "]");
//				}
			}
		}while( Math.abs(totalError) > this.precision );
		System.out.println("Number of epocs = " + iteration);
		System.out.println("Total error = " + totalError);
		System.out.println("Actual weights = " +  Arrays.toString(n.getWeights()));
		for(int i = 0; i < w.length; i++)
		{
			n.setEntrence(x[i]);
			n.updateOutput(NeuronType.Hidden);
			System.out.println("Entance[" + Arrays.toString(x[i]) + "]" + " Output[" + n.getOutput() + "]" + " Correct[" + Arrays.toString(y[i]) + "]");
		}

	}
	
	
	

}
