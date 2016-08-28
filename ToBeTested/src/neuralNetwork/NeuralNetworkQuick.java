package neuralNetwork;

public class NeuralNetworkQuick extends NeuralNetwork{

	NeuralNetworkQuick(ComputeError errFnc) {
		super(errFnc);
	}
	
	@Override
	public void computeNetworkResult(int startIndex) throws Exception {
		computeNetworkResultQuick(startIndex);
	}

}
