package neuralNetwork;

import java.util.Arrays;

public class Delta {
	private double correct;
	private double learning_rate;
	private double precision;
	private Neuron[] neurons;
	private double[] entrances;
	
	Delta(double[] entrances, double correct, double learning_rate, double precision)
	{
		this.correct = correct;
		this.learning_rate = learning_rate;
		this.precision = precision;
		this.entrances = entrances;
		this.neurons = new Neuron[1];
		this.neurons[0] = new Neuron(entrances.length, NeuronType.Hidden,false);
	}
	
	public void learn()
	{
		int iteration = 0;
		Neuron n = this.neurons[0];
		n.setEntrence(entrances);
		double error;
		double[] w = new double[n.getWeights().length];
		do
		// till output is greater then precision
		{
			n.updateOutput(NeuronType.Hidden);
			error = this.correct - n.getOutput();
			for(int i = 0; i < w.length; i++)
			{
				w[i] = n.getWeights()[i] + learning_rate* n.getEntrence()[i]*error;
				n.setWeights(w);
			}
			iteration++;
		}while( Math.abs(n.getOutput() - this.correct ) > this.precision );
		System.out.println("Number of epocs = " + iteration);
		System.out.println("Actual entrances = " + Arrays.toString(n.getEntrence()));
		System.out.println("Actual weights = " +  Arrays.toString(n.getWeights()));
		System.out.print("Actual output = " + n.getOutput());

	}
	
	
	

}
