package neuralNetwork;

import java.util.ArrayList;

public interface ComputeError <NeuronWithFnc extends Neuron>{
	Float computeError(ArrayList<Float> expectedOut, ArrayList<Float> obtainedOut);
}

