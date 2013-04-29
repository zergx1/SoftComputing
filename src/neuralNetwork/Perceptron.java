package neuralNetwork;

import java.text.DecimalFormat;
import java.util.Vector;

public class Perceptron {

	private int layersNumber;
	public Layer[] layers;
	private int[] unitsNumber;
	private double alfa;
	private double momentum;
	private double avgSquare;
	private double MaxAbsoluteError;
	private double AvgMaxAbsoluteError;
	private double percent;
	public Vector<double []> delta_w;

	/**
	 * @param alfa param for learning 
	 * @param momentum  param for learning
	 * @param unitsNumber array of integer, each element is number of neuron in every layer 
	 * @param isBias true/false
	 */
	public Perceptron(double alfa, double momentum, int[] unitsNumber,boolean isBias) { 
		this.layersNumber = unitsNumber.length; 
		this.alfa = alfa; 
		this.momentum = momentum;
		this.unitsNumber = unitsNumber;
		layers = new Layer[layersNumber];
		this.avgSquare = 0.0;
		delta_w = new Vector<double []>();
		
		for (int i = 0; i < layersNumber; i++) {
//			double[] temp = new double[unitsNumber[i]];
//			for(int j=0;j<temp.length;j++)
//				temp[j] = 0;
//			delta_w.add(i, temp);
//
//			
			if (i == 0)
				layers[i] = new Layer(NeuronType.Input, unitsNumber[i], 1, isBias);
			else if (i == (layersNumber - 1))
				layers[i] = new Layer(NeuronType.Output, unitsNumber[i], unitsNumber[i - 1], isBias);
			else {
				layers[i] = new Layer(NeuronType.Hidden, unitsNumber[i], unitsNumber[i - 1], isBias);
			}
		}
	}

	/**
	 * Method puts entrances for layer of given index from previous layer output. <br>
	 * WARNING: index can't be 0 because previous layer not exits <br>
	 * WARNING: this function doesn't calculate value, only copy value from output to entrance 
	 * @param index number of layer
	 */
	public void putLayerEntrences(int index) {
		double[] outputs = new double[unitsNumber[index - 1]];

		for (int i = 0; i < unitsNumber[index - 1]; i++)
		{ 
			outputs[i] = layers[index - 1].getNeuron(i).getOutput();
		}

		for (int i = 0; i < unitsNumber[index]; i++)
		{
			layers[index].getNeuron(i).setEntrence(outputs); 
		}

	}

	/**
	 * Method update output all neuron in given layer
	 * @param index number of layer
	 * @param nt type of neuron
	 * 
	 */
	public void updateLayerOutput(int index, NeuronType nt) {
		for (int i = 0; i < unitsNumber[index]; i++) {
			layers[index].getNeuron(i).updateOutput(nt);
		}
	}
	
	/**
	 * Method set entrances value for first layer.
	 * @param n array of input data
	 */
	public void setEntrencesValues(double ...n){
		for(int i = 0 ; i < n.length ;i++){
			layers[0].getNeuron(i).setEntrencesNeuronsInput(n[i]);
		}
	}
	
	/**
	 * Print current state of neural network
	 */
	public void printNetwork()
	{
		for(int i=0;i<this.layersNumber;i++)
		{
			Layer current_layer = this.layers[i]; 
			Neuron[] neurons = current_layer.getNeurons();

			System.out.println("######## LAYER: "+(i+1)+" ########("+neurons.length+")");
			
			for(int n=0;n<neurons.length;n++)
			{
				Neuron current_neuron = neurons[n];
				double[] weights = current_neuron.getWeights();
				double[] enters = current_neuron.getEntrence();
				System.out.println("# n = "+(n+1)+" | Bias:"+current_neuron.getBias()+" | Output: "+current_neuron.getOutput()+" | sValue: "+current_neuron.getsValue());
				System.out.println("=======");
				//if(i+1<this.layersNumber)
				//{
					System.out.println("--------");
					for(int c=0;c<weights.length;c++)
					{
						System.out.print((c+1)+" -> n "+"  Weight:"+weights[c]+"  Input: "+enters[c]+" || ");
					}
					System.out.println();
					System.out.println("--------");

				//}
				System.out.println("=======");
			}
			

		}
		System.out.println();
	}
	
	/**
	 * Return final output
	 * WARNING: method doesn't update value, only returning 
	 * @return
	 */
	double[] returnOutput()
	{
		Neuron[] neurons =this.layers[this.layers.length-1].getNeurons();
		double[] output = new double[neurons.length];
		for(int i=0;i<neurons.length;i++)
		{
			output[i] = neurons[i].getOutput();
		}
			return output;
	}
	
	/**
	 * Method return given neuron from given layer
	 * @param layer number of layer
	 * @param neuron number of neuron
	 * @return neuron
	 */
	Neuron returnNeuron(int layer,int neuron)
	{
		return this.layers[layer].getNeuron(neuron);
	}
	
	/**
	 * @param weights array of weights
	 * @param layer number of layer
	 * @param neuron number of neuron
	 */
	void setWeight(double[] weights, int layer, int neuron)
	{
		layers[layer].getNeuron(neuron).setWeights(weights);
	}

	
	/**
	 * Full cycle for neural network. Argument is input for first layer, and return is output of network.
	 * @param n array of input
	 * @return output
	 */
	public double[] epoc(double...n){
		setEntrencesValues(n);
		for(int i = 0 ; i < unitsNumber.length; i++)
		{
			if(i == 0)
				updateLayerOutput(i, NeuronType.Input);
			else if(i == unitsNumber.length-1)
				updateLayerOutput(i, NeuronType.Output);
			else 
				updateLayerOutput(i, NeuronType.Hidden);
			
			if(i != unitsNumber.length-1)
				putLayerEntrences(i+1);
		}
		return returnOutput();
	}
	/**
	 * Return input for neural network<br>
	 * WARNING: method doesn't update value, only returning 
	 * @return
	 */
	public double[] returnInput()
	{
		Neuron[] neurons =  this.layers[0].getNeurons();
		double[] input = new double[neurons.length];
		for(int i=0;i<neurons.length;i++)
			input[i] = neurons[i].getEntrence()[0];
		return input;
	}
	
	/**
	 * @param in array of input value
	 * @param out array of target-output value
	 */
	public void deltaRuleOnce(double[] in, double [] out)
	{
		int currentLayer = layersNumber - 1; // current layer
		
		double[] result = epoc(in);
		
		//**********OUTPUT************************
		for(int i = 0; i < result.length; i++)
		{
			double O = out[i];
			result[i] = O - result[i];
			layers[currentLayer].getNeuron(i).setError(result[i]);
			layers[currentLayer].getNeuron(i).updateWeights(alfa);
		}
		
		//**********HIDDEN************************
		
		currentLayer--;
		
		// for each hidden layer
		while(currentLayer > 0)
		{
			// for each neuron in layer
			for(int i = 0; i < layers[currentLayer].getNumUnits(); i++) 
			{
				double error = 0.0;
				// for each exit
				for(int j = 0; j < layers[currentLayer + 1].getNumUnits(); j++)
				{
					Neuron n = layers[currentLayer + 1].getNeuron(j);
					error += n.getEntrence()[i] * n.getError();
				}
				
				double O = layers[currentLayer].getNeuron(i).getOutput();
				error = O * ( 1.0 - O ) * error;
				
				layers[currentLayer].getNeuron(i).setError(error);
				layers[currentLayer].getNeuron(i).updateWeights(alfa);
			}
			
			currentLayer--;
		}
		
		//**********INPUT************************
		
		// input neurons haven't weights
		
		epoc(in);
	}

	public Layer[] getLayers() {
		return layers;
	}

	public void setLayers(Layer[] layers) {
		this.layers = layers;
	}

	
}

