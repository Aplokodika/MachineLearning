package neuralNetwork.activationFunctions;

public class TanhFunction implements Activation {

	@Override
	public Double activation(Double inp) {
		return Math.tanh(inp.doubleValue());
	}
}
