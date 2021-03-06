package neuralNetwork;

import java.security.acl.LastOwnerException;
import java.util.Vector;

public class Madaline {

	final double globalError = 0.1;
	private double alfa; // learning rate
	private Layer[] layers;
	public boolean isBias;

	public Madaline(int[] structure, double alfa, boolean isBias) {
		this.alfa = alfa;
		layers = new Layer[structure.length];

		for (int i = 0; i < structure.length; i++) {
			if (i == 0)
				layers[i] = new Layer(NeuronType.Input, structure[i], 1, isBias);
			else if (i == (structure.length - 1))
				layers[i] = new Layer(NeuronType.Output, structure[i], structure[i - 1], isBias);
			else {
				layers[i] = new Layer(NeuronType.Hidden, structure[i], structure[i - 1], isBias);
			}
		}
	}

	/**
	 * weights[layer][neuron][ weights]
	 * @param weights
	 * @param bias
	 */
	public void setAllWeights(double[][][] weights, double[][] bias) {
		for (int i = 1; i < layers.length; i++) {
			for (int j = 0; j < weights[i - 1].length; j++) {
				layers[i].getNeuron(j).setWeights(weights[i - 1][j]);
				if (bias != null)
					layers[i].getNeuron(j).setBias(bias[i - 1][j]);

			}
		}
	}

	/**
	 * Method puts entrances for layer of given index from previous layer
	 * output. <br>
	 * WARNING: index can't be 0 because previous layer not exits <br>
	 * WARNING: this function doesn't calculate value, only copy value from
	 * output to entrance
	 * 
	 * @param index
	 *            number of layer
	 */
	private void putLayerEntrences(int index) {
		double[] outputs = new double[layers[index - 1].getNumUnits()];

		for (int i = 0; i < layers[index - 1].getNumUnits(); i++) {
			outputs[i] = layers[index - 1].getNeuron(i).getOutput();
		}

		for (int i = 0; i < layers[index].getNumUnits(); i++) {
			layers[index].getNeuron(i).setEntrence(outputs);
		}

	}

	/**
	 * Method update output all neuron in given layer
	 * 
	 * @param index
	 *            number of layer
	 * @param nt
	 *            type of neuron
	 * 
	 */
	private void updateLayerOutput(int index, NeuronType nt) {
		for (int i = 0; i < layers[index].getNumUnits(); i++) {
			layers[index].getNeuron(i).updateOutput(nt);
		}
	}

	/**
	 * Method set entrances value for first layer.
	 * 
	 * @param n
	 *            array of input data
	 */
	private void setEntrencesValues(double... n) {
		for (int i = 0; i < n.length; i++) {
			layers[0].getNeuron(i).setEntrencesNeuronsInput(n[i]);
		}
	}

	/**
	 * Return final output WARNING: method doesn't update value, only returning
	 * 
	 * @return
	 */
	public double[] returnOutput() {
		Neuron[] neurons = this.layers[this.layers.length - 1].getNeurons();
		double[] output = new double[neurons.length];
		for (int i = 0; i < neurons.length; i++) {
			output[i] = neurons[i].getOutput();
		}
		return output;
	}

	/**
	 * Method return given neuron from given layer
	 * 
	 * @param layer
	 *            number of layer
	 * @param neuron
	 *            number of neuron
	 * @return neuron
	 */
	public Neuron returnNeuron(int layer, int neuron) {
		return this.layers[layer].getNeuron(neuron);
	}

	/**
	 * @param weights
	 *            array of weights
	 * @param layer
	 *            number of layer
	 * @param neuron
	 *            number of neuron
	 */
	void setWeight(double[] weights, int layer, int neuron) {
		layers[layer].getNeuron(neuron).setWeights(weights);
	}

	/**
	 * Full cycle for neural network. Argument is input for first layer, and
	 * return is output of network.
	 * 
	 * @param n
	 *            array of input
	 * @return output
	 */
	public double[] epoc(double... n) {
		// put inptut to the first layer
		setEntrencesValues(n);

		for (int i = 0; i < layers.length; i++) {
			if (i == 0)
				updateLayerOutput(i, NeuronType.Input);
			else if (i == layers.length - 1)
				updateLayerOutput(i, NeuronType.Output);
			else
				updateLayerOutput(i, NeuronType.Hidden);

			if (i != layers.length - 1)
				putLayerEntrences(i + 1);
		}

		return returnOutput();
	}

	/**
	 * Return input for neural network<br>
	 * WARNING: method doesn't update value, only returning
	 * 
	 * @return
	 */
	public double[] returnInput() {
		Neuron[] neurons = this.layers[0].getNeurons();
		double[] input = new double[neurons.length];
		for (int i = 0; i < neurons.length; i++)
			input[i] = neurons[i].getEntrence()[0];
		return input;
	}

	/**
	 * @param in
	 *            array of input value
	 * @param out
	 *            array of target-output value
	 */
	public void learn(double[] in, double[] out) {
		epoc(in);

		for (int i = 0; i < layers[1].getNumUnits(); i++) {
			Neuron n = layers[1].getNeuron(i);
			if (Math.abs(n.getOutput() - out[i]) <= globalError)
				continue;

			double error = out[i] - n.getOutput();
			error *= alfa;

			double[] weights = n.getWeights();
			for (int w = 0; w < weights.length; w++) {
				double deltaW = error * n.getEntrence()[w];
				weights[w] += deltaW;
			}

			n.setWeights(weights);
		}
	}

	public void learn(double[][] in, double[][] out) {
		int exit = 0;
		while (++exit < 10000) {
			double totalError = 0d;
			for (int i = 0; i < in.length; i++) {
				learn(in[i], out[i]);
				double error = 0;
				double[] output = returnOutput();
				for (int e = 0; e < out[i].length; e++)
					error += Math.abs(output[e] - out[i][e]);

				totalError += error;
			}

			if (totalError < globalError)
				break;

			if (exit % 10 == 0)
				System.out.println("Epoc: " + exit + " Error: " + totalError);
		}

		System.out.println("EPOC: " + exit);
	}

	public Layer[] getLayers() {
		return layers;
	}

	public void setLayers(Layer[] layers) {
		this.layers = layers;
	}

	/**
	 * Print current state of neural network
	 */
	public void printNetwork() {
		for (int i = 0; i < layers.length; i++) {
			Layer current_layer = this.layers[i];
			Neuron[] neurons = current_layer.getNeurons();

			System.out.println("######## LAYER: " + (i + 1) + " ########(" + neurons.length + ")");

			for (int n = 0; n < neurons.length; n++) {
				Neuron current_neuron = neurons[n];
				double[] weights = current_neuron.getWeights();
				double[] enters = current_neuron.getEntrence();
				System.out.println("# n = " + (n + 1) + " | Bias:" + current_neuron.getBias() + " | Output: " + current_neuron.getOutput() + " | sValue: "
						+ current_neuron.getsValue());
				System.out.println("=======");
				// if(i+1<this.layersNumber)
				// {
				System.out.println("--------");
				for (int c = 0; c < weights.length; c++) {
					System.out.print((c + 1) + " -> n " + "  Weight:" + weights[c] + "  Input: " + enters[c] + " || ");
				}
				System.out.println();
				System.out.println("--------");

				// }
				System.out.println("=======");
			}

		}
		System.out.println();
	}

}
