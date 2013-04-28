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

	public Perceptron(double alfa, double momentum, int[] unitsNumber,boolean isBias) { // tworzenie
																					// perceptronu
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

	public void updateLayerEntrences(int index) {
		double[] temp_val;
		temp_val = new double[unitsNumber[index - 1]];

		for (int i = 0; i < unitsNumber[index - 1]; i++) { // pobierz output z
														// poziomu nizej.
			temp_val[i] = layers[index - 1].getNeuron(i).getOutput();
		}

		for (int i = 0; i < unitsNumber[index]; i++) {
			layers[index].getNeuron(i).setEntrence(temp_val); // podstaw
																// wartosci
																// wyjsc z
																// poziomu index
																// - 1 na
																// wejscia
																// index.
		}

	}

	public void updateLayerOutput(int index, NeuronType nt) { // obliczenie
																// wyjscia
																// neuron w
																// zaleznosci od
																// jego typu
		for (int i = 0; i < unitsNumber[index]; i++) {
			layers[index].getNeuron(i).sValue(nt);
		}
	}
	public void setEntrencesValues(double ...n){
		for(int i = 0 ; i < n.length ;i++){
			layers[0].getNeuron(i).setEntrencesNeuronsInput(n[i]);
		}
	}
	
	public void showNetwork()
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
				System.out.println("# n = "+(n+1)+" | Bias:"+current_neuron.getBias()+" | Wyjscie: "+current_neuron.getOutput()+" | sValue: "+current_neuron.getsValue());
				System.out.println("=======");
				//if(i+1<this.layersNumber)
				//{
					System.out.println("--------");
					for(int c=0;c<weights.length;c++)
					{
						System.out.print((c+1)+" -> n "+"  Waga:"+weights[c]+"  Wejscie: "+enters[c]+" || ");
					}
					System.out.println();
					System.out.println("--------");

				//}
				System.out.println("=======");
			}
			

		}
		System.out.println();
	}
	
	double[] return_output()
	{
		Neuron[] neurons =this.layers[this.layers.length-1].getNeurons();
		double[] output = new double[neurons.length];
		for(int i=0;i<neurons.length;i++)
		{
			output[i] = neurons[i].getOutput();
		}
			return output;
	}
	
	double[] return_output_hide()	//do eksperymentu 4
	{
		Neuron[] neurons =this.layers[this.layers.length-2].getNeurons();
		int size=this.layers[this.layers.length-1].getNumInput();
		//System.out.println(size);
		double[] output = new double[size];
		for(int i=0;i<size;i++)
		{
			
			output[i] = neurons[i].getOutput();
			//System.out.println(output[i]);
		}
			return output;
	}
	
	
	Neuron return_neuron(int layer,int neuron)
	{
		return this.layers[layer].getNeuron(neuron);
	}

	public double[] epoc(double...n){ // wejscia dla epoki
		setEntrencesValues(n);
		for(int i = 0 ; i < unitsNumber.length;i++){
			if(i == 0)updateLayerOutput(i, NeuronType.Input);
			else if(i == unitsNumber.length-1)updateLayerOutput(i, NeuronType.Output);
			else updateLayerOutput(i, NeuronType.Hidden);
			if(i != unitsNumber.length-1)updateLayerEntrences(i+1);
		}
		return return_output();
	}
	
	public void set_test_env()
	{
		this.setEntrencesValues(0.4,-0.7);
		double tmp[] = new double[2];
		tmp[0] = 0.1; tmp[1] = -0.2;
		this.layers[1].getNeuron(0).setWages(tmp.clone());
		tmp[0] = 0.4; tmp[1] = 0.2;
		this.layers[1].getNeuron(1).setWages(tmp.clone());
		tmp[0] = 0.2; tmp[1] = -0.5;
		this.layers[2].getNeuron(0).setWages(tmp.clone());
		
	}
	
	public double[] get_input()
	{
		Neuron[] neurons =  this.layers[0].getNeurons();
		double[] input = new double[neurons.length];
		for(int i=0;i<neurons.length;i++)
			input[i] = neurons[i].getEntrence()[0];
		return input;
	}
	
	public static void print_array(double[] ar)
	{
		for(int i=0;i<ar.length;i++)
			System.out.print(ar[i]+", ");
		System.out.println();
	}
	
//	public void back_propagation(double[] answer)
//	{
//		double[] tmp = null;
//		double[] input = this.get_input(); // Pobranie danych wejsciowych
//		System.out.println("Step 1 (Input)");
//		this.print_array(input);
//		//Step 3
//		Neuron[] hidden_neurons = null;
//		for(int l=1;l<this.layers.length;l++) //
//		{
//			hidden_neurons = this.layers[l].getNeurons();
//
//
//			for(int i=0;i<hidden_neurons.length;i++)
//			{
//	//			tmp = new double[this.layers[0].getNeurons().length];
//	//			for(int j=0;j<tmp.length;j++)
//	//				tmp[j] = hidden_neurons[i]
//				hidden_neurons[i].setsValue( hidden_neurons[i].return_net());
//				//System.out.println("N: "+hidden_neurons[i].getsValue());
//				hidden_neurons[i].sValue(hidden_neurons[i].type);
//				//System.out.println("O:"+ hidden_neurons[i].getOutput());
//	
//			}
//		}
//		//OUTPUT
//		
//		//hidden_neurons[i].setsValue( hidden_neurons[i].return_net());
//		System.out.println("Step 1 (Output Hidden)");
//		double[] output = this.return_output();
//		double error = 0;
//		for(int i=0;i<output.length;i++){
//			error += Math.pow((answer[i]-output[i]),2.0);
//		}
//		System.out.println("Error: "+error);
//		double[] d = new double[output.length];
//		for(int i=0;i<output.length;i++){
//			d[i] = (answer[i]-output[i])*output[i]*(1-output[i]);
//		}
//		this.print_array(d);
//		
//		Vector<double []> Y = new Vector<double []>();
//		Y.setSize(this.layers.length);
//		
//
//		for(int i=this.layers.length-2;i>=0;i--)
//		{
//			
//			System.out.println("=======Layer "+i);
//			Neuron[] neurons = this.layers[i].getNeurons();
//			tmp = new double[neurons.length];
//			for(int j=0;j<tmp.length;j++)
//			{
//				System.out.println("Neur: "+neurons[j].getOutput()+" Er: "+d[0]);
//				tmp[j] = neurons[j].getOutput()*d[0];
//				
//			}	
//			this.print_array(tmp);
//			Y.add(i,tmp.clone());
//			tmp = new double[ delta_w.elementAt(i).length ];
//			for(int j=0;j<delta_w.elementAt(i).length;j++)
//			{
//				System.out.println("delta: "+delta_w.elementAt(i)[j]+" Y: "+Y.elementAt(i)[j]*this.alfa);
//				tmp[j] = delta_w.elementAt(i)[j] + Y.elementAt(i)[j]*this.alfa;
//			}
//			this.print_array(tmp);
//			delta_w.setElementAt(tmp, i);
//			Vector< double[] > e = new Vector<double[]>();
//			Layer next_layer = this.layers[i+1];
//			Neuron[] next_neurons = next_layer.getNeurons();
//			for(int n=0;n<next_neurons.length;n++)
//			{
//				tmp = new double[this.layers[i].getNeurons().length];
//				double[] weights = next_neurons[n].getWeights();
//				for(int c=0;c<tmp.length;c++)
//				{
//					tmp[c] = weights[c]*d[0];
//					System.out.println(tmp[c]);
//				}
//				d = tmp;
//				
//				
//			}
//
//			//OD KONCA BEZ POCZATKU
//			
//		}
//
//
//
//	}

	public Layer[] getLayers() {
		return layers;
	}

	public void setLayers(Layer[] layers) {
		this.layers = layers;
	}
	//d=1/2* E(Ti-Yi)^2
	//gdzie yi-wartoœci otrzymane, ti-wartosci spodziewane na wyjœciu i
	public double getError(double [][] input,double [][] output)
	{
		double result=0;
		double temp=0;
		for(int i=0;i<input.length;i++)
		{
			this.epoc(input[i]);
			double []a=this.return_output();
			temp=0.0;
			for(int j=0;j<a.length;j++)
			{
				temp+=Math.pow(output[i][j]-a[j],2);
			}
			
			temp=temp/2.0;
			result+=temp;
		}
		result=result/input.length;
		this.avgSquare=result;
		return result;

	}
	public double getError(double [] input,double []output)
	{
		double result=0;
		double temp=0;

			this.epoc(input);
			double []a=this.return_output();
			temp=0.0;
			for(int j=0;j<a.length;j++)
			{
				temp+=Math.pow(output[j]-a[j],2);
			}
			
			temp=temp/2.0;
			result=temp;

		return result;

	}
//	
//	public void back_propagationSetWeg(double[] input,double[] answer,boolean isBias)
//	{
//		epoc2(input);
//		Layer l=this.layers[this.layersNumber-1];	//dostep do ostatniej warstwy;
//		l.backpropForOutputLayer(answer, this.alfa, this.momentum,isBias);
//		for(int i=this.layersNumber-2;i>0;i--)
//		{
//			l=this.layers[i];
//			l.backpropForHideLayer(this.layers[i+1],this.alfa, this.momentum,isBias);
//		}
//		
//		
//	}
//	
//	public void back_propagationChangeWeg(boolean isBias)
//	{
//		Layer l;
//		for(int i=0;i<this.layersNumber;i++)
//		{
//			l=this.layers[i];
//			l.backpropChangeWeg(isBias);
//		}
//		
//	}

	public double getGlobalError() {
		return avgSquare;
	}
	
	/*
	 *  X = x - x0
	 *  x - najwieksza roznica z danego przedzialu od x0
	 *  x0 - srednia z danych, jezeli nie jest dane
	 *  Maksymalny blad bezwzgledny
	 * 
	 */
	
	public double maxAbsoluteError(double[][] input,double[][] output)
	{
		
		double temp=0;
		double max = 0.0;

		for(int i=0;i<input.length;i++)
		{
			this.epoc(input[i]);
			double []a=this.return_output();
			temp=0.0;
			for(int j=0;j<a.length;j++)
			{
				temp = a[j] - output[i][j];
				if ( Math.abs(temp) > Math.abs(max))
				{
					//System.out.println( a[j]+" "+output[i][j]);
					max = temp;
				}
			}
			
		}
		this.MaxAbsoluteError=Math.abs(max);
		return Math.abs(max);
		
	}
	
	
	/*
	 * Srednia makysmalnego bledy bezwzglednego
	 */
	public double avgMaxAbsoluteError(double [][]input,double [][]output)
	{
		double avg = 0.0;
		double temp=0;
		double max = 0.0;

		for(int i=0;i<input.length;i++)
		{
			this.epoc(input[i]);
			double []a=this.return_output();
			temp=0.0;
			for(int j=0;j<a.length;j++)
			{
				temp += Math.abs(a[j] - output[i][j]);
			}
			avg += temp/a.length;
			
		}
		avg = avg/input.length;
		this.AvgMaxAbsoluteError=avg;
		return avg;
	}
	
	/*
	 * Maksymalny blad 
	 */
	public double MaxError(double [][]input,double [][]output)
	{
		double avg = 0.0;
		double temp=0;
		double max = 0.0;

		for(int i=0;i<input.length;i++)
		{
			this.epoc(input[i]);
			double []a=this.return_output();
			temp=0.0;
			for(int j=0;j<a.length;j++)
			{
				temp += Math.abs(a[j] - output[i][j]);
			}
			avg += temp;
			
		}
	
		return avg;
	}
	
	public void AllError(double [][]input,double [][]output)
	{
		double avg = 0.0;
		double temp=0,temp2,temp3,result=0.0;
		double max = 0.0;
		int procent=0;

		for(int i=0;i<input.length;i++)
		{
			this.epoc(input[i]);
			double []a=this.return_output();
			temp=0.0;
			temp2=-2.0;
			temp3=0.0;
			for(int j=0;j<a.length;j++)
			{
				temp3+=Math.pow(output[i][j]-a[j],2);
				temp += Math.abs(a[j] - output[i][j]);
				temp2 = a[j] - output[i][j];
				if ( Math.abs(temp2) > Math.abs(max))
				{
					max = temp2;
					
				}
				//if(Math.abs(output[i][j]-Math.round(a[j]*100.0)/100.0)<=0.0)	//tu zaokraglenie do drugiego miejsca i blad rowny 0
				if(output[i][j]==Math.round(a[j]))	//tu zaokraglenie do jednosci wszystkie eksperymenty procz 1
					procent++;
				
				
					
			}
			//temp3=temp3/2.0;
			result+=temp3;
			avg += temp/a.length;
			
		}

		avg = avg/input.length;
		this.AvgMaxAbsoluteError=avg;
		result=result/input.length;
		this.avgSquare=result;
		this.MaxAbsoluteError=Math.abs(max);
		this.percent=(double)procent/(input.length*output[0].length)*100.0;
		//System.out.println(this.percent);

	}

	public double getAvgSquare() {
		return avgSquare;
	}

	public double getMaxAbsoluteError() {
		return MaxAbsoluteError;
	}

	public double getAvgMaxAbsoluteError() {
		return AvgMaxAbsoluteError;
	}
	public double getPercent() {
		return percent;
	}


}

