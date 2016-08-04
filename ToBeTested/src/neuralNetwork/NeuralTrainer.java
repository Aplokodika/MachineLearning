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

	private ChangeWeight checkChange(Double changeErrorAdd, Double changeErrorSub, Double neuronError) {

		if (changeErrorAdd.doubleValue() <= neuronError.doubleValue()) {
			if (changeErrorSub.doubleValue() <= neuronError.doubleValue()) {
				if (changeErrorAdd.doubleValue() < changeErrorSub.doubleValue()) {
					return ChangeWeight.increaseWeight;
				} else {
					return ChangeWeight.decreaseWeight;
				}
			} else {
				return ChangeWeight.increaseWeight;
			}
		} else if (changeErrorSub.doubleValue() <= neuronError.doubleValue()) {
			return ChangeWeight.decreaseWeight;
		}
		return ChangeWeight.noChange;
	}

	private Double positiveValue(Double val) {
		if (val.doubleValue() < 0)
			return new Double(-val.doubleValue());
		return val;
	}

	/**
	 * This is a part of the traditional network.
	 * 
	 * This method is called right after compute network result is called. This
	 * trains by modifying the connections to a single neuron from a neuron from
	 * the previous layer.
	 * 
	 * The following algorithm involves a lot of computation process. It is not
	 * possible to speculate which gradient descend operation might be optimal
	 * because, this network's algorithm accepts various kinds of activation
	 * functions to be used for each neuron.
	 * 
	 * @param neuron
	 *            - the neuron's connecting weight to be modified
	 * @param neuronLayer
	 *            - the layer in which the current neuron is present. This value
	 *            cannot exceed the maximum size of the hidden layer
	 * @param expectedOutput-
	 *            the expected output of the neural network system
	 * @throws Exception
	 */
	public void trainNeuron(Neuron neuron, ArrayList<Double> expectedOutput) throws Exception {
		Neuron preNeuron;
		NNetwork.computeNetworkResult(0, MoveOrder.moveForward);
		
		ArrayList<Double> annOutput
					= NLayerToArray.obtainLayerOutputInArray(NNetwork.networkData.outputNeurons);
		
		Double neuronError = NNetwork.errorFunction.computeError(expectedOutput, annOutput);
		
		
		Double changeErrorAdd = new Double(Double.MAX_VALUE);
		Double changeErrorSub = changeErrorAdd;
		Double change;

		for (int i = 0; i < neuron.parentNeurons.size(); i++) {
			preNeuron = neuron.parentNeurons.get(i);
			if ( (preNeuron.previousChangeValues.get(neuron.getNeuronIndex()) != null) &&
				 (preNeuron.previousError.get(neuron.getNeuronIndex()) != null)) {
				change = positiveValue(
						(
								neuron.learningRate.doubleValue() 
								*positiveValue ( neuronError.doubleValue() 
										       - preNeuron.previousError.get(neuron.getNeuronIndex()))
						 )		
						+ (
								neuron.momentum.doubleValue() 
								* (
										preNeuron.previousChangeValues.get(neuron.getNeuronIndex())
								   )
						  )
						);
				
			} else if (preNeuron.previousError.get(neuron.getNeuronIndex()) != null){
				preNeuron = neuron.parentNeurons.get(i);
				change = positiveValue(neuron.learningRate.doubleValue() 
						* positiveValue ( neuronError.doubleValue() 
					       - preNeuron.previousError.get(neuron.getNeuronIndex()))
						);
					}
				else {
					change = neuron.learningRate.doubleValue();
				}
			if (preNeuron.checkFeasibility(neuron.getNeuronIndex(), change)) {
				preNeuron.updateWeight(neuron.getNeuronIndex(), change);
				NNetwork.computeNetworkResult(0, MoveOrder.moveForward);// finds
																					// the
																					// result
				annOutput = NLayerToArray.obtainLayerOutputInArray(NNetwork.networkData.outputNeurons);
				changeErrorAdd = NNetwork.errorFunction.computeError(expectedOutput, annOutput);
				preNeuron.updateWeight(neuron.getNeuronIndex(), -change.doubleValue());
			}

			if (preNeuron.checkFeasibility(neuron.getNeuronIndex(), -change.doubleValue())) {
				preNeuron.updateWeight(neuron.getNeuronIndex(), -change.doubleValue());
				NNetwork.computeNetworkResult(0, MoveOrder.moveForward);// finds
																					// the
																					// result
				annOutput = NLayerToArray.obtainLayerOutputInArray(NNetwork.networkData.outputNeurons);
				changeErrorSub = NNetwork.errorFunction.computeError(expectedOutput, annOutput);

				preNeuron.updateWeight(neuron.getNeuronIndex(), change);
			}

			ChangeWeight changeWeight = checkChange(changeErrorAdd, changeErrorSub, neuronError);
			if (changeWeight.equals(ChangeWeight.increaseWeight)) {
				//System.out.println("err ::: " + changeErrorAdd);
				preNeuron.previousChangeValues.put(neuron.getNeuronIndex(), change);
				preNeuron.previousError.put(neuron.getNeuronIndex(), neuronError);
				preNeuron.updateWeight(neuron.getNeuronIndex(), change);
			
			} else if (changeWeight.equals(ChangeWeight.decreaseWeight)) {
				//System.out.println("err ::: " + changeErrorSub);
				preNeuron.previousChangeValues.put(neuron.getNeuronIndex(), - change.doubleValue());
				preNeuron.previousError.put(neuron.getNeuronIndex(), neuronError);
				preNeuron.updateWeight(neuron.getNeuronIndex(), -change.doubleValue());
			}
			
			// else ignore
		}
	}

	/**
	 * This is the traditional train-network method
	 * 
	 * @param expectedOutput
	 *            - this it the set of all outputs that correlate with the given
	 *            input. This is the output that we expect to obtain.
	 * @throws Exception
	 */

	public void trainNetwork(ArrayList<Double> expectedOutput) throws Exception {
		Neuron tempNeuron;
		for (int layerIndex = 0; layerIndex < NNetwork.networkData.hiddenLayers.size(); layerIndex++) {
			for (int nIndex = 0;
					nIndex < NNetwork.networkData.hiddenLayers.get(layerIndex).size(); nIndex++) {
				tempNeuron = NNetwork.networkData.hiddenLayers.get(layerIndex).get(nIndex);
				trainNeuron(tempNeuron, expectedOutput);
			}
		}

		for (int nIndex = 0;
				nIndex < NNetwork.networkData.outputNeurons.size(); nIndex++) {
			tempNeuron = NNetwork.networkData.outputNeurons.get(nIndex);
			trainNeuron(tempNeuron, expectedOutput);
		}
	}

}
// To do:
// Need to add initialization methods for initializing the momentum and the
// learning rate of
// each neurons.