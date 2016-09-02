package neuralNetwork.activationFunctions;

public class SinFunction implements Activation {

	@Override
	public Double activation(Double inp) {
		return ((inp.doubleValue() <= (Math.PI / 2)) && (inp.doubleValue() >= -(Math.PI / 2)))
				? Math.sin(inp.doubleValue()) : ((inp.doubleValue() > 0) ? 1 : -1);

	}

}
