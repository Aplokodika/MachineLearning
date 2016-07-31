/*
 * Author: K Sreram. 
 * copyright (c) 2016 K Sreram, all rights reserved.
 */
package neuralNetwork;

import java.util.*;

public class NeuralNetwork {

	public static enum MoveOrder {
		moveForward, moveBackward
	};

	public boolean constructStatus = false;

	public NetworkData networkData;

	// public Float learningRate;

	// public Float momentum;

	ComputeError errorFunction;

	NeuralNetwork(ComputeError errFnc) {
		networkData = new NetworkData();
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
		// the input from the input layers. ( ! ) 
		
	
		
		for (int indexNeuron = 0; 
				indexNeuron < networkData.hiddenLayers.get(startIndex).size(); 
																		indexNeuron++) {
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
					neuronTemp = neuron.childNeurons.get(i);// this is a map!!!! not an ArrayList!!!
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
