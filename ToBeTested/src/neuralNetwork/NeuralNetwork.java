/*
 * Author: K Sreram. 
 * copyright (c) 2016 K Sreram, all rights reserved.
 * 
 * Developer License Agreement.
 * ..............................
 * 
 * Members of Aplokodika may update the following source and may claim the ownership of
 * copyright to the source they contribute. The members of Aplokodika may modify, 
 * create or alter a whole or a part of the following code with consent from the 
 * majority of the copyright holders of the source code. Each modification must be 
 * documented and notified, with appropriate identity information of the author who helped 
 * modify the source; this includes the author's name, personal email address.
 * 
 * By contributing to this code, you agree to grant Aplokodika free license to store, modify, share, 
 * sell, republish and grant such license to third parties without any cost or conditions. 
 * 
 * Authors contributing to this project owns the piece of code they write. That is, Aplokodika does
 * not claim to own the copyright to the content contributed by an author unless the rights are 
 * explicitly transfered. By modifying/creating/contributing to this project, the authors agree 
 * to grant Aplokodika organization free license to store, modify, share, 
 * sell, republish and grant such license to third parties without any cost or conditions.      
 */

package neuralNetwork;

import java.util.*;

public class NeuralNetwork<NeuronWithFnc extends Neuron> {

	enum MoveOrder {
		moveForward, moveBackward
	};

	public boolean constructStatus = false;

	public NetworkData<NeuronWithFnc> networkData;

	// public Float learningRate;

	// public Float momentum;

	ComputeError<NeuronWithFnc> errorFunction;

	NeuralNetwork(Factory<NeuronWithFnc> fact, ComputeError<NeuronWithFnc> errFnc) {
		networkData = new NetworkData<NeuronWithFnc>(fact);
		errorFunction = errFnc;
	}

	/**
	 * This computes the final output value based on the inputs from the
	 * previous neurons, iterating through each layers. This method is called
	 * after the `setInput` method gets called.
	 * 
	 * @throws Exception
	 */
	public void computeNetworkResult(int startIndex, MoveOrder direction) throws Exception {
		if (constructStatus == false) {
			Exception e = new Exception("error : Network not constructed");
			throw e;
		}
		if (networkData.initializedInputs == false) {
			Exception e = new Exception("error : Inputs not initialized");
			throw e;
		}
		Queue<Neuron> neuronQueue = new LinkedList<Neuron>();

		// The following adds the first hidden layer's neurons into the queue
		// after pulling
		// the input from the input layers.
		for (int indexNeuron = 0; indexNeuron < networkData.hiddenLayers.get(startIndex).size(); indexNeuron++) {
			networkData.hiddenLayers.get(startIndex).get(indexNeuron).pullInput();
			networkData.hiddenLayers.get(startIndex).get(indexNeuron).computeOutput();
			neuronQueue.add(networkData.hiddenLayers.get(startIndex).get(indexNeuron));
		}

		// the following loop continues until the queue becomes empty. If the
		// queue becomes empty,
		// it signifies that the output has reached the output neurons.
		while (neuronQueue.isEmpty() == false) {
			Neuron neuronTemp;
			Neuron neuron = neuronQueue.poll();
			for (int i = 0; i < ((direction == MoveOrder.moveForward) ? neuron.childNeurons.size()
					: neuron.parentNeurons.size()); i++) {
				if (direction == MoveOrder.moveForward) {
					neuronTemp = neuron.childNeurons.get(i);
				} else {
					neuronTemp = neuron.parentNeurons.get(i);
				}
				if (neuronTemp != null) {
					neuronTemp.pullInput();
					neuronTemp.computeOutput();
					neuronQueue.add(neuronTemp);
				}
			}
		}

	}

	/*
	 * public void trainDataSet(ArrayList<Float>inputValues, ArrayList<Float>
	 * expectedOutput) throws Exception{
	 * 
	 * networkData.setInput(inputValues); computeNetworkResult();
	 * ArrayList<Float> outputs =
	 * NLayerToArray.obtainLayerOutputInArray(networkData.outputNeurons);
	 * 
	 * }
	 */

}
