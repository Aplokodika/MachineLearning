/*
 * Copyright (c) 2016 K Sreram, All rights reserved
 */
package neuralNetwork;


public class NeuralNetworkQuick extends NeuralNetwork{

	NeuralNetworkQuick(ComputeError errFnc) {
		super(errFnc);
	}
	
	@Override
	public void computeNetworkResult(int startIndex) {
		computeNetworkResultQuick(startIndex);
	}

}