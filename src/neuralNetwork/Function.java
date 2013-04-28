package neuralNetwork;

public class Function {

	public static final String STEP_FUNCTION = "STEP_FUNCTION";
	public static final String SIGMOID = "SIGMOID";
	public static final String SIGMOID_PRIM = "SIGMOID_PRIM";

	// TODO other activation function

	/**
	 * Step function
	 * 
	 * @param s
	 *            argument
	 * @return value of function
	 */
	static double stepFunction(double s) {
		if (s <= 0)
			return 0;
		else
			return 1.0;
	}

	/**
	 * Sigmoid
	 * 
	 * @param s
	 *            argument
	 * @return value of function
	 */
	static double sigmoid(double s) {
		return 1 / (1 + Math.exp(-s));
	}

	/**
	 * Derivative of sigmoid
	 * 
	 * @param s
	 *            argument
	 * @return value of function
	 */
	static double sigmoidPrim(double s) {
		return sigmoid(s) * (1 - sigmoid(s));
	}

	/**
	 * Function calculate value of activation function due to chosen variant and
	 * argument
	 * 
	 * @param variant
	 * @param s
	 *            argument
	 * @return value of chosen activation function
	 */
	static double calculate(String variant, Double s) {
		switch (variant) {
		case STEP_FUNCTION:
			return stepFunction(s);
		case SIGMOID:
			return sigmoid(s);
		case SIGMOID_PRIM:
			return sigmoidPrim(s);
		default:
			return 0;
		}
	}
}
