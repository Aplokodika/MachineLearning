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

	private Float positiveValue(Float val){
		if(val.floatValue() < 0)
			return new Float(-val.floatValue());
		return val;
	}
	
	/**This is a part of the traditional network. 
	 * 
	 * This method is called right after compute network result is called. 
	 * This trains by modifying the connections to a single neuron from a neuron 
	 * from the previous layer. 
	 * 
	 * The following algorithm involves a lot of computation process. It is not 
	 * possible to speculate which gradient descend operation might be optimal because, 
	 * this network's algorithm accepts various kinds of activation functions 
	 * to be used for each neuron. 
	 * 
	 * @param
	 * 	neuron - the neuron's connecting weight to be modified
	 * @param 
	 *  neuronLayer - the layer in which the current neuron is present.
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
			preNeuron = neuron.parentNeurons.get(i);
			if (preNeuron.getPreWeight(neuron.neuronIndex) != null) {
				change = new Float((neuron.learningRate.floatValue() * neuronError.floatValue()
							/positiveValue(preNeuron.getWeight(neuron.neuronIndex).floatValue())
						+ neuron.momentum.floatValue() 
						* (preNeuron.getPreWeight(neuron.neuronIndex).floatValue()))
						- preNeuron.getWeight(neuron.neuronIndex)).floatValue();
			} else {
				preNeuron = neuron.parentNeurons.get(i);
				change = new Float(neuron.learningRate.floatValue() * neuronError.floatValue()
						/ positiveValue(preNeuron.getWeight(neuron.neuronIndex)));
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
	
	/**
	 * This is the traditional train-network method
	 * @param expectedOutput - 
	 * 			this it the set of all outputs that correlate with the given input. This is the 
	 * 			output that we expect to obtain. 
	 * @throws Exception
	 */
	
	public void trainNetwork(ArrayList<Float> expectedOutput) throws Exception{
		Neuron tempNeuron;
		for(int layerIndex = 0; layerIndex < NNetwork.networkData.hiddenLayers.size();
				layerIndex++){
			for(int nIndex = 0; nIndex < NNetwork.networkData.hiddenLayers.get(layerIndex).size();
					nIndex++){
				tempNeuron = NNetwork.networkData.hiddenLayers.get(layerIndex).get(nIndex);
				trainNeuron(tempNeuron, layerIndex, expectedOutput );
			}
		}
		
		for(int nIndex = 0; nIndex < NNetwork.networkData.outputNeurons.size();
				nIndex++){
			tempNeuron = NNetwork.networkData.outputNeurons.get(nIndex);
			trainNeuron(tempNeuron,  NNetwork.networkData.hiddenLayers.size() -1, expectedOutput);
		}
	}
	
}
// To do:
// Need to add initialization methods for initializing the momentum and the
// learning rate of
// each neurons.