package neuralNetwork;
/*
 * Copyright (c) 2016 K Sreram, All rights reserved.
 */

import java.util.ArrayList;

import neuralNetwork.neuralInterface.TrainingDataSet;

/**
 * 
 * @author sreram
 *
 */
public class ConstructiveTrainer extends NeuralTrainer {

	
	private Double computeError(Neuron curNeuron) {
		Double result = new Double(0.0);
		for (int i = 0; i < curNeuron.childNeurons.size(); i++) {
			result = result.doubleValue() + curNeuron.childNeurons.get(i).error.doubleValue()
					* curNeuron.getWeight(curNeuron.childNeurons.get(i).getNeuronIndex())*
					curNeuron.childNeurons.get(i).differentialValue;
		}
		// taking absolute value should be avoided here. Because the right to
		// determine the direction
		// of weight-value-change lies with the output neurons.
		return result;
	}

	public static Double computeOutputOfNeuron(Neuron neuron) {
		neuron.pullInput();
		neuron.computeOutput();
		return neuron.outputResult;
	}

	private ArrayList<Double> lastVisitedWeightSet;

	public ArrayList<Double> retriveWeightValues() {
		return lastVisitedWeightSet;
	}

	private ArrayList<Double> newWeightValues;
	private int iterativeIndexForNewWeightValues = 0;

	public void setWeightValues(ArrayList<Double> newWeights) {
		newWeightValues = newWeights;
		setWeightValues();
	}

	public void setWeightValues() {
		Neuron tempNeuron;
		iterativeIndexForNewWeightValues = 0;
		for (int nIndex = 0; nIndex < NNetwork.networkData.outputNeurons.size(); nIndex++) {
			tempNeuron = NNetwork.networkData.outputNeurons.get(nIndex);
			setWeight(tempNeuron);
		}
		for (int layerIndex = NNetwork.networkData.hiddenLayers.size() - 1; layerIndex >= 0; layerIndex--) {
			for (int nIndex = 0; nIndex < NNetwork.networkData.hiddenLayers.get(layerIndex).size(); nIndex++) {
				tempNeuron = NNetwork.networkData.hiddenLayers.get(layerIndex).get(nIndex);
				setWeight(tempNeuron);
			}
		}
	}

	public void setWeight(Neuron neuron) {
		Neuron preNeuron;
		for (int i = 0; i < neuron.parentNeurons.size(); i++) {
			preNeuron = neuron.parentNeurons.get(i);
			preNeuron.weightValues.put(neuron.getNeuronIndex(), newWeightValues.get(iterativeIndexForNewWeightValues));
			iterativeIndexForNewWeightValues++;
		}
	}

	
	private void trainNeuron(Neuron neuron) {
		Neuron preNeuron;
		Double change;
		Double deltaW;
		Double curWeight;
		for (int i = 0; i < neuron.parentNeurons.size(); i++) {			
			preNeuron = neuron.parentNeurons.get(i);
			curWeight = preNeuron.getWeight(neuron.getNeuronIndex());
			lastVisitedWeightSet.add(curWeight);
			neuron.differentialValue = neuron.activation.activationDifferential(neuron.input, neuron.outputResult);
			deltaW = neuron.error*
					 neuron.differentialValue*
					 preNeuron.outputResult * NNetwork.networkData.learningRate;
			change = deltaW + 
					 NNetwork.networkData.momentum * 
					 ((preNeuron.preChangeInWeight.get(neuron.getNeuronIndex()) != null)?
						 preNeuron.preChangeInWeight.get(neuron.getNeuronIndex()) : 0.0);

			preNeuron.weightValues.put(neuron.getNeuronIndex(), 
					preNeuron.weightValues.get(neuron.getNeuronIndex()) + change.doubleValue());
			preNeuron.preChangeInWeight.put(neuron.getNeuronIndex(), deltaW);
		}
	}

	public void trainNetwork(TrainingDataSet.trainingDataUnit trainingDataUnit) {
		
		Neuron tempNeuron;
		NNetwork.networkData.setInput(trainingDataUnit.inputs);
		NNetwork.computeNetworkResult(0);
		lastVisitedWeightSet = new ArrayList<Double>();
		for (int nIndex = 0; nIndex < NNetwork.networkData.outputNeurons.size(); nIndex++) {
			tempNeuron = NNetwork.networkData.outputNeurons.get(nIndex);
			tempNeuron.error = trainingDataUnit.errors.get(nIndex);
			trainNeuron(tempNeuron);
		}
		for (int layerIndex = NNetwork.networkData.hiddenLayers.size() - 1; layerIndex >= 0; layerIndex--) {
			for (int nIndex = 0; nIndex < NNetwork.networkData.hiddenLayers.get(layerIndex).size(); nIndex++) {
				tempNeuron = NNetwork.networkData.hiddenLayers.get(layerIndex).get(nIndex);
				tempNeuron.error = computeError(tempNeuron); // grabs the error from the succeeding neurons
				trainNeuron(tempNeuron);
			}
		}
	}
	
	

}