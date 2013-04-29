package neuralNetwork;

import java.util.Arrays;

/**
 * This class representive the layer in neural network
 * @author Adam
 *
 */
public class Layer {

	private Neuron neurons[];	// array with all neurons in this layer
	private double[] errors; // error for all neurons in this layer
	// private int numOutput;
	private int numInput;	// number of input
	// private int numLayers;
	private int numUnits; // number of neuron (neurons.lenght)

	/**
	 * Constructor of layer
	 * @param nt type of neuron (Input, Output, Hidden)
	 * @param numUnits amount of neuron in this layer
	 * @param numOfEntrences number of entrance to neuron
	 * @param isBias 
	 */
	public Layer(NeuronType nt, int numUnits, int numOfEntrences, boolean isBias) {

		neurons = new Neuron[numUnits];
		numInput = numOfEntrences;
		this.numUnits=numUnits;
		errors = new double[numUnits];
		Arrays.fill(this.errors, 0.0);
		switch (nt) {
		case Input:
			for (int i = 0; i < numUnits; i++) {
				neurons[i] = new Neuron(1, nt,isBias);
			}
			break;
		case Output:
			for (int i = 0; i < numUnits; i++) {
				neurons[i] = new Neuron(numOfEntrences, nt, isBias);
			}
			break;
		case Hidden:
			for (int i = 0; i < numUnits; i++) {
				neurons[i] = new Neuron(numOfEntrences, nt, isBias);
			}
			break;

		}

	}
	/** Shorter constructor for layer
	 * @param neu array of neurons
	 * @param num number of input 
	 */
	public Layer(Neuron neu[],int num) {

		this.neurons =neu;
		this.numUnits=neu.length;
		this.numInput=num;
		Arrays.fill(this.errors, 0.0);
	}

	public Neuron[] getNeurons() {
		return neurons;
	}
	public Neuron getNeuron(int i) {
		return neurons[i];
	}
	public void setNeurons(Neuron[] neurons) {
		this.neurons = neurons;
	}

	public int getNumInput() {
		return numInput;
	}

	public void setNumInput(int numInput) {
		this.numInput = numInput;
	}

	public int getNumUnits() {
		return numUnits;
	}

	public void setNumUnits(int numUnits) {
		this.numUnits = numUnits;
	}
	
	/**
	 * 
	 * @param from index of neuron
	 * @return array of double with all weights
	 */
	public double[] getWeightsFromNeuron(int from)
	{
		double []result = new double [this.numUnits];
		
		for(int i=0;i<this.numUnits;i++)
			result[i]=this.neurons[i].getWeights()[from];
		return result;
		
	}
	public double[] getErrors() {
		if(errors == null)
		{
			errors = new double[numUnits];
			for(int i = 0; i < neurons.length; i++)
				errors[i] = neurons[i].getError();
		}
		return errors;
	}
	public void setErrors(double[] errors) {
		this.errors = errors;
	}
}
