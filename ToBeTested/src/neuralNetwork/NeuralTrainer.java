/*
 * Author: K Sreram. 
 * copyright (c) 2016 K Sreram, all rights reserved.
 */
package neuralNetwork;

import java.util.ArrayList;

import neuralNetwork.NeuralNetwork.MoveOrder;

public class NeuralTrainer {

	public enum ChangeWeight {
		increaseWeight, decreaseWeight, noChange
	}

	public NeuralNetwork NNetwork;

	public void setNNetwork(NeuralNetwork Nnet) {
		NNetwork = Nnet;
	}

	private ChangeWeight checkChange(Float changeErrorAdd, Float changeErrorSub, Float neuronError) {
		if (changeErrorAdd.floatValue() < neuronError.floatValue()) {
			if (changeErrorSub.floatValue() < neuronError.floatValue()) {
				if (changeErrorAdd.floatValue() < changeErrorSub.floatValue()) {
					return ChangeWeight.increaseWeight;
				} else {
					return ChangeWeight.decreaseWeight;
				}
			} else {
				return ChangeWeight.increaseWeight;
			}
		} else if (changeErrorSub.floatValue() < neuronError.floatValue()) {
			return ChangeWeight.decreaseWeight;
		}
		return ChangeWeight.noChange;
	}

	/**
	 * This method is called right after compute network result is called.
	 * @param
	 * 	neuron - the neuron's connecting weight to be modified
	 * @param 
	 *  neuronLayer - the previous layer to which the current neuron is present.
	 *  			  This value cannot exceed the maximum size of the hidden layer 
	 * @param
	 * 	expectedOutput- the expected output of the neural network system
	 * @throws Exception 
	 */
	public void trainNeuron(Neuron neuron, Integer neuronLayer,
							ArrayList<Float> expectedOutput) throws Exception {
		Neuron preNeuron;
		ArrayList <Float> annOutput = NLayerToArray
										.obtainLayerOutputInArray(NNetwork.networkData.outputNeurons); 
		Float neuronError = NNetwork.errorFunction.computeError(expectedOutput, annOutput);
		
		Float changeErrorAdd, changeErrorSub;
		Float change;
		
		for (int i = 0; i < neuron.parentNeurons.size(); i++) {
			if (neuron.getPreWeight(neuron.neuronIndex) != null) {
				preNeuron = neuron.parentNeurons.get(i);
				change = new Float(neuron.learningRate.floatValue() * neuronError.floatValue()
						+ neuron.momentum * (preNeuron.getPreWeight(neuron.neuronIndex))
						- preNeuron.getWeight(neuron.neuronIndex));
			} else {
				preNeuron = neuron.parentNeurons.get(i);
				change = new Float(neuron.learningRate.floatValue() * neuronError.floatValue());
			}
			preNeuron.updateWeight(neuron.neuronIndex, change);
			NNetwork.computeNetworkResult(neuronLayer, MoveOrder.moveForward);// finds the result 
			annOutput = NLayerToArray.obtainLayerOutputInArray(NNetwork.networkData.outputNeurons);
			changeErrorAdd = NNetwork.errorFunction.computeError(expectedOutput, annOutput);

			preNeuron.reverseUpdate();
			
			preNeuron.updateWeight(neuron.neuronIndex,  - change.floatValue());
			NNetwork.computeNetworkResult(neuronLayer, MoveOrder.moveForward);// finds the result 
			annOutput = NLayerToArray.obtainLayerOutputInArray(NNetwork.networkData.outputNeurons);
			changeErrorSub = NNetwork.errorFunction.computeError(expectedOutput, annOutput);

			
			ChangeWeight changeWeight = checkChange(changeErrorAdd, changeErrorSub, neuronError);
			if(changeWeight.equals(ChangeWeight.increaseWeight)){
				preNeuron.updateWeight(neuron.neuronIndex, change);
			}
			else if(changeWeight.equals(ChangeWeight.decreaseWeight)){
				preNeuron.updateWeight(neuron.neuronIndex,  - change.floatValue());
			}
			// else ignore
		}
	}
	
	

}
// To do:
// Need to add initialization methods for initializing the momentum and the
// learning rate of
// each neurons.