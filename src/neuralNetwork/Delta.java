package neuralNetwork;

public class Delta {
	private double[] correct;
	private double learnig_factor = 0.5;
	private double precision = 0.1;
	private Neuron[] neurons;
	
	Delta(double[] correct, double learning_factor, double precision)
	{
		this.correct = correct;
		this.learnig_factor = learning_factor;
		this.precision = precision;
		this.neurons = new Neuron[1];
		this.neurons[0] = new Neuron(1, NeuronType.Output,false);
	}
	
	public void learn()
	{
		Neuron n = this.neurons[0];
		double[] entrance = new double[1];
		entrance[0] = 0.8;
		n.setEntrence(entrance);
		double error;
		double[] w = new double[1];
		do
		// till output is greater then precision
		{
			n.updateOutput(NeuronType.Output);
			error = this.correct[0] - n.getOutput();
			w[0] = n.getWeights()[0] + n.getEntrence()[0]*error;
			n.setWeights(w);
			
		}while( Math.abs(n.getOutput() - this.correct[0] ) > this.precision );
		System.out.print(n.getOutput());

	}
	
	
	

}
