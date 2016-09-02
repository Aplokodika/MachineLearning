package neuralNetwork.activationFunctions;

public class LinearUnmodified implements Activation {

	@Override
	public Double activation(Double inp) {
		return inp.doubleValue();
	}

}
