package neuralNetwork.activationFunctions;

public class LinearUnmodified implements Activation {

	@Override
	public Double activation(Double inp) {
		return inp.doubleValue();
	}

	@Override
	public double activationDifferential(Double input, Double outputResult) {
		return 1;
	}
	

}
