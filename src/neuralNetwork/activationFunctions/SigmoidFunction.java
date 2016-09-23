package neuralNetwork.activationFunctions;

public class SigmoidFunction implements Activation {

	@Override
	public Double activation(Double inp) {
		return 1/(1+Math.pow(Math.E, -(inp.doubleValue())));
	}

	@Override
	public double activationDifferential(Double input, Double outputResult) {
		return outputResult.doubleValue() * ( 1 - outputResult.doubleValue());
	}

}
