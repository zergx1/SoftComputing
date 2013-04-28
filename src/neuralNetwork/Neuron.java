package neuralNetwork;

public class Neuron {
	private double[] entrence;
	private double[] weights;
	private double sValue;
	private double output;
	private double bias;
	public NeuronType type;

	public static final String ACTIVATION_FUNCTION = Function.SIGMOID;
	
	/**
	 * constructor with unknown weights
	 * @param numOfEntrences number of entrances, every neuron has this same number of input
	 * @param nt type of neuron (Input, Output, Hidden)
	 * @param isBias false - network without bias, true - network has bias
	 */
	public Neuron(int numOfEntrences, NeuronType nt,boolean isBias) {
		type = nt;
		switch (nt) {
		case Input:
			weights = new double[1];
			weights[0]= 1.0;
			entrence = new double[1];
			sValue = 0.0;
			output = 0.0;
			bias=0.0;

			break;
		case Output:
			weights = new double[numOfEntrences];
			entrence = new double[numOfEntrences];
			sValue = 0.0;
			output = 0.0;
			bias=0.0;
			
			for(int i = 0 ; i < weights.length;i++)
				weights[i]=Math.random()-0.5;

			break;
		case Hidden:
			weights = new double[numOfEntrences];
			entrence = new double[numOfEntrences];
			sValue = 0.0;
			output = 0.0;
			bias=0.0;
			
			for(int i = 0 ; i < weights.length;i++)
				weights[i]=Math.random()-0.5;

			break;
		default:

		}
		
		// if neruon has bias and isn't in input layer than bias should has some weight
		if(isBias && nt!=NeuronType.Input)
			bias=Math.random()-0.5;
		
	}
	
	
	/**
	 * constructor with known weights
	 * @param weights array with weights
	 * @param bi weight of bias
	 * @param numOfEntrences number of entrances
	 * @param nt type of neuron (Input, Output, Hidden)
	 */
	public Neuron(double []weights,double bi,int numOfEntrences, NeuronType nt) {
		
		type = nt;
		switch (nt) {
		case Input:
			this.weights = new double[1];
			this.weights[0]= 1.0;
			entrence = new double[1];
			sValue = 0.0;
			output = 0.0;

			break;
		case Output:
			this.weights = new double[numOfEntrences];
			entrence = new double[numOfEntrences];
			sValue = 0.0;
			output = 0.0;

			break;
		case Hidden:
			this.weights = new double[numOfEntrences];
			entrence = new double[numOfEntrences];
			sValue = 0.0;
			output = 0.0;
		break;
		default:

		}

		this.bias=bi;
		this.weights=weights;
	}
	

	/**
	 * Function calculate new sValue:
	 * SUM(x*w)
	 * @param nt neuron type
	 */
	public void sValue(NeuronType nt) { // aka update output
		sValue=0.0;
		for (int i = 0; i < weights.length; i++)
			sValue += weights[i] * entrence[i];
		
		switch(nt){
		case Input:
			output = sValue;
			break;
		case Hidden:
			sValue+=this.bias;	// if bias is off, than nothing happens becouse bias is equal zero
			output = Function.calculate(ACTIVATION_FUNCTION, sValue);
			break;
		case Output:
			sValue+=this.bias;	// if bias is off, than nothing happens becouse bias is equal zero
			output = Function.calculate(ACTIVATION_FUNCTION, sValue);
			break;
		}
	}
	
	public double[] getEntrence() {
		return entrence;
	}

	public void setEntrence(double[] entrence) {
		this.entrence = entrence;
	}
	public void setEntrencesNeuronsInput(double e){
		this.entrence[0]=e;
	}

	public double[] getWeights() {
		return weights;
	}

	public void setWages(double[] wages) {
		this.weights = wages;
	}

	public double getsValue() {
		return sValue;
	}

	public void setsValue(double sValue) {
		this.sValue = sValue;
	}

	public double getOutput() {
		return output;
	}

	public void setOutput(int output) {
		this.output = output;
	}

	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}

	
	/**
	 * @return only sValue without function
	 */
	public double return_net()
	{
		double[] weights = this.getWeights();
		double[] enters = this.getEntrence();
		double net=0;
		for(int i=0;i<weights.length;i++)
		{
			net += weights[i]*enters[i];
		}
		return net;
	}
	
}
