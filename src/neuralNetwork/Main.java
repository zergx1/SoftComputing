package neuralNetwork;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
			double[] correct = new double[1];
			correct[0] = 0.6;
			Delta d = new Delta(correct, 0.5, 0.05);
			d.learn();
	}

}
