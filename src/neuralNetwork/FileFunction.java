package neuralNetwork;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class FileFunction {
	
	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	public void setTestPath(String testPath) {
		this.testPath = testPath;
	}

	private int [] struktura;
	private boolean bias;	//czy mamy u¿ywaæ biasu
	private double alfa;	//wspó³czynnik nauki
	private double momentum;	//wspó³czynnik momentum
	private int epocs;		//ilosc epok do nauki
	private double stopValue;	//wartoœæ b³edu œredniokwadratowego dla którego koñczymy proces nauki
	private String netPathFrom;	//œcie¿ka do pliku z zapisan¹ sieci¹
	private String netPathTo;	//œcie¿ka do pliku gdzie zapisaæ sieæ
	private String dataPath;	//œcie¿ka do pliku z zapisanym zbiorem uczacym sie
	private boolean ifRandomData ;	//losowanie wzorców do nauki
	private String testPath;	//œcie¿ka ze wzorcami testowymi
	private String logsPath;	//œcie¿ka gdzie zapisaæ logi z nauki
	private boolean ifShow;	//czy wyswietlac na wyjsciu postepy nauki
	private int stepLog;	//krok wyswietlania/zapisywania logow
	private boolean flagLog;	//Opcja w³¹czaj¹c logowanie sieci (co s)
	private boolean flagLogAdv; //Parametr okreœlaj¹cy, czy sieæ ma logowaæ szczegó³owe wyniki testowania po zakoñczeniu procesu nauki
	private boolean flagLogAfterLearn; //Parametr okreœlaj¹cy, czy sieæ ma zostaæ opisana w logach po zakoñczeniu procesu nauki.
	private boolean firstPart=false;	//potrzebne do logow backpropagation
	private double[][] in;
	private double[][] out;
	private double[][] inTest;
	private double[][] outTest;
	private ArrayList<double []> inTemp=new ArrayList<double []>();
	private ArrayList<double []> outTemp=new ArrayList<double []>();
	private String logsLearnProgress;
	private boolean progresclear=true;
	private String logsTest;
	
	public FileFunction(int[] struktura, boolean bias, double alfa,
			double momentum, int epocs, double stopValue, String netPathFrom,
			String netPathTo, String dataPath, boolean ifRandomData,
			String testPath, String logsPath, String logsTest, boolean ifShow, int stepLog,
			boolean flagLog, boolean flagLogAdv,
			boolean flagLogAfterLearn,String logsLearnProgress) {
		super();
		this.struktura = struktura;
		this.bias = bias;
		this.alfa = alfa;
		this.momentum = momentum;
		this.epocs = epocs;
		this.stopValue = stopValue;
		this.netPathFrom = netPathFrom;
		this.netPathTo = netPathTo;
		this.dataPath = dataPath;
		this.ifRandomData = ifRandomData;
		this.testPath = testPath;
		this.logsPath = logsPath;
		this.ifShow = ifShow;
		this.stepLog = stepLog;
		this.flagLog = flagLog;
		this.flagLogAdv = flagLogAdv;
		this.flagLogAfterLearn = flagLogAfterLearn;
		this.logsLearnProgress=logsLearnProgress;
		this.logsTest=logsTest;
	}

	public void networkToFile(Perceptron p)
	{
//		Format pliku
//		
//		Ilosc wartstw J
//			ilosc neuronow N w warstwie 1
//				bias[1],wagi tego neuronu z poprzednimi
//				bias[2],wagi tego neuronu z poprzednimi
//				.......................................
//				bias[n],wagi tego neuronu z poprzednimi
//			ilosc neuronow N w warstwie 2
//				bias[1],wagi tego neuronu z poprzednimi
//				bias[2],wagi tego neuronu z poprzednimi
//				.......................................
//				bias[n],wagi tego neuronu z poprzednimi
//				
//			ilosc neuronow N w warstwie J
//				bias[1],wagi tego neuronu z poprzednimi
//				bias[2],wagi tego neuronu z poprzednimi
//				.......................................
//				bias[n],wagi tego neuronu z poprzednimi
//		alfa(wspolczynnik uczenia),momentum
			
		PrintWriter save=null;
		try {
			save = new PrintWriter(new FileWriter(new File(this.netPathTo),false));
			save.println(p.getLayers().length);	//ilosc warstw
			for(int i=0;i<p.getLayers().length;i++)
			{
				Layer current_layer = p.getLayers()[i]; 
				Neuron[] neurons = current_layer.getNeurons();
				save.println(current_layer.getNumUnits());	//ilosc neuronow w warstwie
				
				for(int j=0;j<neurons.length;j++)
				{
					Neuron current_neuron = neurons[j];
					double[] weights = current_neuron.getWeights();
					save.print(current_neuron.getBias());
					
						for(int c=0;c<weights.length;c++)
						{
							save.print(","+weights[c]);
						}
						save.println();
				}
				
				
			}
//			save.println(p.getAlfa()+","+p.getMomentum());
			save.close();
			//System.out.println("tu");
		} catch (IOException e) {
			System.out.println("Blad zapisu do pliku");
			e.printStackTrace();
		}  
		
	}

	public Perceptron fileToNetwork()
	{
		Perceptron p = null;

		FileReader fileReader;
		try {
			fileReader = new FileReader(this.netPathFrom);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = bufferedReader.readLine();
			String splite[];
			Vector <Integer> struktura=new Vector<Integer>();
			double alfa;
			double momentum;
			int neuronSize;
			int size;
			int numOfEntrance=0;
			size=Integer.parseInt(line);
			Layer ls[]=new Layer[size];
			for(int a=0;a<size;a++)
			{
				line=bufferedReader.readLine();
				struktura.add(Integer.parseInt(line));	//ilosc neuronow
				
				neuronSize=struktura.lastElement();
				
				Neuron ns[]=new Neuron[neuronSize];

				for(int i = 0;i<neuronSize;i++)
				{
					line=bufferedReader.readLine();
					splite=line.split(",");
					double wag[]=new double[splite.length-1];
					for(int j=1;j<splite.length;j++)
					{
						wag[j-1]=Double.parseDouble(splite[j]);
					}
					if(numOfEntrance==0)
						ns[i]=new Neuron(wag,Double.parseDouble(splite[0]),1,NeuronType.Input);
					else
					{
						if(a!=size-1)
							ns[i]=new Neuron(wag,Double.parseDouble(splite[0]),numOfEntrance,NeuronType.Hidden);
						else
							ns[i]=new Neuron(wag,Double.parseDouble(splite[0]),numOfEntrance,NeuronType.Output);
					}
					
				}
				if(a==0)
					ls[a]=new Layer(ns,1);
				else
					ls[a]=new Layer(ns,ls[a-1].getNumUnits());
				
				numOfEntrance=neuronSize;
			}
			line=bufferedReader.readLine();
			splite=line.split(",");
			alfa=Double.parseDouble(splite[0]);
			momentum=Double.parseDouble(splite[1]);
			
			int[] trueCriteria = new int[struktura.size()];
			for(int i=0;i<trueCriteria.length;i++)
				trueCriteria[i]=struktura.elementAt(i);
			p=new Perceptron(alfa, momentum, trueCriteria,false);
			p.setLayers(ls);

			  bufferedReader.close();
			  return p;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Blad odczytu sieci z pliku-brak pliku");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Blad odczytu sieci z pliku");
			e.printStackTrace();
		}
		  	  
		return p;

		
		
	}

	public void setInOut()
	{
		/*
		 *  PLik wejsciowy musi miec formie in:out    np 1.0,1.0:2.0,2.0 
		 *  nowe dane w nowej linijce
		 *  czyli
		 *  in:out
		 *  in:out
		 *  in:out
		 */
		try{


			  FileInputStream fstream = new FileInputStream(this.dataPath);
			  
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  String[] tmp;
			  Vector<String []> S_in = new Vector<String []>();
			  Vector<String []> S_out = new Vector<String []>();
			  
			  String[] in_tmp;
			  String[] out_tmp;

			  int count =0;
			  while ((strLine = br.readLine()) != null)   
			  {

				  tmp = strLine.split(":");

				  in_tmp = tmp[0].split(",");
				  out_tmp = tmp[1].split(",");
				  
				  S_in.add(in_tmp.clone());
				  S_out.add(out_tmp.clone());

			  }
			  this.in = new double[S_in.size()][S_in.elementAt(0).length];
			  this.out = new double[S_out.size()][S_out.elementAt(0).length];
			  for(int i=0;i<S_in.size();i++)
			  {
				  for(int j=0;j<S_in.elementAt(i).length;j++)
				  {
					  this.in[i][j] = Double.valueOf( S_in.elementAt(i)[j] );
				  }
				  for(int j=0;j<S_out.elementAt(i).length;j++)
				  {
					  this.out[i][j] = Double.valueOf( S_out.elementAt(i)[j] );

				  }


			  }


			  in.close();
		}
		catch (Exception e)
		{
			  System.err.println("Error: " + e.getMessage());
			  e.printStackTrace();
		}
	}
	public void setInTest()
	{
		/*
		 *  PLik wejsciowy musi miec formie in:out    np 1.0,1.0:2.0,2.0 
		 *  nowe dane w nowej linijce
		 *  czyli
		 *  in:out
		 *  in:out
		 *  in:out
		 */
		try{


			  FileInputStream fstream = new FileInputStream(this.testPath);
			  
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  String[] tmp;
			  Vector<String []> S_in = new Vector<String []>();
			  Vector<String []> S_out = new Vector<String []>();
			  
			  String[] in_tmp;
			  String[] out_tmp;

			  int count =0;
			  while ((strLine = br.readLine()) != null)   
			  {

				  tmp = strLine.split(":");

				  in_tmp = tmp[0].split(",");
				  out_tmp = tmp[1].split(",");
				  
				  S_in.add(in_tmp.clone());
				  S_out.add(out_tmp.clone());

			  }
			  
			  
			  this.inTest = new double[S_in.size()][S_in.elementAt(0).length];
			  this.outTest = new double[S_out.size()][S_out.elementAt(0).length];
			  for(int i=0;i<S_in.size();i++)
			  {
				  for(int j=0;j<S_in.elementAt(i).length;j++)
				  {
					  this.inTest[i][j] = Double.valueOf( S_in.elementAt(i)[j] );

				  }
				  for(int j=0;j<S_out.elementAt(i).length;j++)
				  {
					  this.outTest[i][j] = Double.valueOf( S_out.elementAt(i)[j] );

				  }


			  }


			  in.close();
		}
		catch (Exception e)
		{
			  System.err.println("Error: " + e.getMessage());
			  e.printStackTrace();
		}
	}

		
	public void randomData(int learnAmount, int testAmount, String to,String to2)
	{
		if(this.inTemp.size()+this.outTemp.size()<learnAmount+testAmount)
			return;
		
		Random rand=new Random();
		 this.in = new double[learnAmount][inTemp.get(0).length];
		 this.out = new double[learnAmount][outTemp.get(0).length];
		
		 this.inTest = new double[testAmount][inTemp.get(0).length];
		 this.outTest = new double[testAmount][outTemp.get(0).length];
		 int los;
		 for(int i=0;i<learnAmount;i++)
		 {
			 los=rand.nextInt(inTemp.size());
			 this.in[i]=this.inTemp.get(los);
			 inTemp.remove(los);
			 this.out[i]=this.outTemp.get(los);
			 outTemp.remove(los);
		 }
		 for(int i=0;i<testAmount;i++)
		 {
			 los=rand.nextInt(inTemp.size());
			 this.inTest[i]=this.inTemp.get(los);
			 inTemp.remove(los);
			 this.outTest[i]=this.outTemp.get(los);
			 outTemp.remove(los);
		 }
		 PrintWriter save=null;
		 String temp="";
			try {
				save = new PrintWriter(new FileWriter(new File(to),false));

				for(int i=0;i<learnAmount;i++)
				{
					for(int j=0;j<in[0].length;j++)
					{
						temp+=in[i][j]+",";
					}
					temp=temp.substring(0, temp.length()-1);
					temp+=":";
					save.print(temp);
					temp="";
					for(int j=0;j<out[0].length;j++)
					{
						temp+=out[i][j]+",";
					}
					temp=temp.substring(0, temp.length()-1);
					save.println(temp);
					temp="";
				}

				
				save.close();
			} catch (IOException e) {
				System.out.println("Blad zapisu do pliku");
				e.printStackTrace();
			}  
			
				try {
					PrintWriter save2=null;
					save2 = new PrintWriter(new FileWriter(new File(to2),false));

					
					for(int i=0;i<testAmount;i++)
					{
						for(int j=0;j<this.inTest[0].length;j++)
						{
							temp+=this.inTest[i][j]+",";
						}
						temp=temp.substring(0, temp.length()-1);
						temp+=":";
						save2.print(temp);
						temp="";
						for(int j=0;j<outTest[0].length;j++)
						{
							temp+=outTest[i][j]+",";
						}
						temp=temp.substring(0, temp.length()-1);
						save2.println(temp);
						temp="";
					}

					
					save2.close();
				} catch (IOException e) {
					System.out.println("Blad zapisu do pliku");
					e.printStackTrace();
				}  
		
	}

	public void tempData(String from)
	{
		try{
			  FileInputStream fstream = new FileInputStream(from);
			  
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  String[] tmp;
			  Vector<String []> S_in = new Vector<String []>();
			  Vector<String []> S_out = new Vector<String []>();
			  
			  String[] in_tmp;
			  String[] out_tmp;

			  while ((strLine = br.readLine()) != null)   
			  {

				  tmp = strLine.split(":");

				  in_tmp = tmp[0].split(",");
				  out_tmp = tmp[1].split(",");
				  
				  S_in.add(in_tmp.clone());
				  S_out.add(out_tmp.clone());

			  }
			  int inL=S_in.elementAt(0).length;
			  int outL=S_out.elementAt(0).length;
			  for(int i=0;i<S_in.size();i++)
			  {
				  double[] inArray = new double[inL];
				  double[] outArray = new double[outL];
				  
				  for(int j=0;j<inL;j++)
				  {
					  inArray[j]=Double.valueOf(S_in.elementAt(i)[j]);
				  }
				  for(int j=0;j<outL;j++)
				  {
					  outArray[j]=Double.valueOf(S_out.elementAt(i)[j]);

				  }
				  this.inTemp.add(inArray);
				  this.outTemp.add(outArray);
			  }
			  in.close();
		}
		catch (Exception e)
		{
			  System.err.println("Error: " + e.getMessage());
			  e.printStackTrace();
		}
	}
	
	/*Format logow uczenia sie
	 * 
	 * #Logi procesu nauki#
	 * alfa								=
	 * momentum							=
	 * ilosc epok						=
	 * uzycie biasu 					[true/false]
	 * progowy blad sredniokwadratowy 	=
	 * losowanie wzorcow do nauki		[true/false]
	 * krok logowania postepow			=
	 * #################################
	 * Stan poczatkowy	B³ad=...
	 * 		Warstwa 2	(wyjsciowa)
	 * 			Neuron 1 bias=...
	 * 				waga 1=...	wejscie 1=...	waga1*wejscie1=...
	 * 				waga 2=...	wejscie 2=...	waga2*wejscie2=...
	 * 				.........................
	 * 			suma=...	f(s)=...	wyjscie=...	t(spodziewane)=...	E=... 	deltaBias=...		
	 * 			
	 * 			Neruon 2 bias=...
	 *				waga 1=...	wejscie 1=...	waga1*wejscie1=...
	 * 				waga 2=...	wejscie 2=...	waga2*wejscie2=...
	 * 				.........................
	 *			suma=...	f(s)=...	wyjscie=...	t(spodziewane)=...	E=...	deltaBias=...
	 *
	 * 		Warstwa 1	(wejsciowa)
	 * 			Neruon 1 bias=...
	 * 				waga 1=...	wejscie 1=...	waga1*wejscie1=...
	 * 				.........................
	 *			suma=...	f(s)=...	wyjscie=...	deltaBias=...	E=...
	 * Epoka 1	B³ad=...
	 * 		Warstwa 2	(wyjsciowa)
	 * 			Neuron 1 bias=...
	 * 				waga 1=...	wejscie 1=...	waga1*wejscie1=...
	 * 					lastdeltaW=...
	 * 				waga 2=...	wejscie 2=...	waga2*wejscie2=...
	 * 					lastdeltaW=...
	 * 				.........................
	 * 			suma=...	f(s)=...	wyjscie=...	t(spodziewane)=...	E=... 	deltaBias=...		
	 * 			
	 * 			Neruon 2 bias=...
	 *				waga 1=...	wejscie 1=...	waga1*wejscie1=...
	 *					lastdeltaW=...
	 * 				waga 2=...	wejscie 2=...	waga2*wejscie2=...
	 * 					lastdeltaW=...
	 * 				.........................
	 *			suma=...	f(s)=...	wyjscie=...	t(spodziewane)=...	E=...	deltaBias=...
	 *
	 * 		Warstwa 1	(wejsciowa)
	 * 			Neruon 1 bias=...
	 * 				waga 1=...	wejscie 1=...	waga1*wejscie1=...
	 * 				.........................
	 *			suma=...	f(s)=...	wyjscie=...	deltaBias=...	E=...
	 *
	 * Epoka 2	B³ad=...
	 * ....
	 * .... 
	 *##################################
	 *Blad sredni =
	 * 
	 * 
	 */


	public double[][] getIn() {
		return in;
	}

	public void setIn(double[][] in) {
		this.in = in;
	}

	public double[][] getOut() {
		return out;
	}

	public void setOut(double[][] out) {
		this.out = out;
	}

	public void networkAfterLearn(Perceptron p)
	{
		PrintWriter save=null;
		String temp="";
		try {
			save = new PrintWriter(new FileWriter(new File(this.logsPath),true));
			save.println("Siec po nauce\n");
			int layers;
			Layer currentLayer;
			Neuron currentNeuron;
			layers=p.getLayers().length;
			for(int l=layers-1;l>=0;l--)
			{
			currentLayer=p.getLayers()[l];
			temp+="\tWarstwa "+(l+1);
			if(l==layers-1)
				temp+="\t(wyjsciowa)\n";
			else if(l==0)temp+="\t(wejsciowa)\n"; else temp+="\t(ukryta)\n";
			save.print(temp);
				for(int n=0;n<currentLayer.getNumUnits();n++)
				{
					currentNeuron=currentLayer.getNeuron(n);
					temp="\t\tNeuron "+(n+1)+"\tbias= "+currentNeuron.getBias();
					save.println(temp);
					double[] wag=currentNeuron.getWeights();
					temp="\t\t";
					for(int w=0;w<wag.length;w++)
					{
						temp+="\twaga "+(w+1)+"\t= "+wag[w];
						
					}
					temp+="\n";
					save.print(temp);

					temp="";
				}
			}
			

			save.close();
		} catch (IOException e) {
			System.out.println("Blad zapisu do pliku");
			e.printStackTrace();
		}  
	}

	public void learnProgress(int epo,double error)
	{
		PrintWriter save=null;
		if(progresclear==true)
		{
			try {
				save = new PrintWriter(new FileWriter(new File(this.logsLearnProgress),false));
				save.print("");
				save.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			progresclear=false;
		}
		
		String temp;
		try {
			
			save = new PrintWriter(new FileWriter(new File(this.logsLearnProgress),true));
			temp=(epo+1)+" "+error;
			save.println(temp);
			save.close();
		} catch (IOException e) {
			System.out.println("Blad zapisu do pliku");
			e.printStackTrace();
		}  
	}
	
	public void learnProgress2(int epo,double error,String to)
	{
		PrintWriter save=null;
	
		String temp;
		try {
			
			save = new PrintWriter(new FileWriter(new File(to),true));
			temp=(epo+1)+" "+error;
			save.println(temp);
			save.close();
		} catch (IOException e) {
			System.out.println("Blad zapisu do pliku");
			e.printStackTrace();
		}  
	}
	
	public void MsgtoFile(String msg,String to)
	{	
		PrintWriter save;
		try {
			
			save = new PrintWriter(new FileWriter(new File(to),true));
			save.println(msg);
			save.close();
		} catch (IOException e) {
			System.out.println("Blad zapisu do pliku");
			e.printStackTrace();
		}  
	}

	public void testLog(double []in,double []out1,double []out2)
	{
		PrintWriter save=null;
		
		String temp="";
		try {
			save = new PrintWriter(new FileWriter(new File(this.logsTest),true));
			for(int i=0;i<in.length;i++)
			temp+="Wejscie "+(i+1)+"\t="+in[i]+"\t";
			save.println(temp);
			temp="";
			for(int i=0;i<out2.length;i++)
				temp+="Wyjscie "+(i+1)+"\t="+ Math.round(out2[i]*1000.0)/1000.0+"\t";
			save.println(temp);
			temp="";
			for(int i=0;i<out2.length;i++)
				temp+="Spodziewane wyjscie "+(i+1)+"\t="+out1[i]+"\t";
			save.println(temp);
			temp="";
			save.close();
		} catch (IOException e) {
			System.out.println("Blad zapisu do pliku");
			e.printStackTrace();
		}  
	}
	public double[][] getInTest() {
		return inTest;
	}

	public double[][] getOutTest() {
		return outTest;
	}

	public String getLogsLearnProgress() {
		return logsLearnProgress;
	}
}
