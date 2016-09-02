package neuralNetwork;
/*
 * Copyright (c) 2016 K Sreram, All rights reserved. 
 */

import java.util.ArrayList;

/**
 * 
 * @author sreram
 *
 */
public class ConstructiveTrainer extends NeuralTrainer {

	private Double leastCount = new Double(0.01); // the default least
													// number

	private Double computeGradient(Neuron curNeuron) {
		Double numerator = new Double(0.0);
		Double denominator = new Double(0.0);
		for (int i = 0; i < curNeuron.childNeurons.size(); i++) {
			numerator = numerator.doubleValue() + curNeuron.childNeurons.get(i).gradient.doubleValue()
					* curNeuron.getWeight(curNeuron.childNeurons.get(i).getNeuronIndex());
			denominator = denominator.doubleValue()
					+ curNeuron.getWeight(curNeuron.childNeurons.get(i).getNeuronIndex());
		}
		// taking absolute value should be avoided here. Because the right to
		// determine the direction
		// of weight-value-change lies with the output neurons.
		// System.out.println("(numerator.doubleValue() /
		// denominator.doubleValue()) = " +
		// numerator.doubleValue() + " " + denominator.doubleValue());
		return (numerator.doubleValue() / denominator.doubleValue());
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

	public void setWeight(Neuron neuron){
		Neuron preNeuron;
		for (int i = 0; i < neuron.parentNeurons.size(); i++) {
			preNeuron = neuron.parentNeurons.get(i);
				preNeuron.weightValues.put(neuron.getNeuronIndex(),
						newWeightValues.get(iterativeIndexForNewWeightValues));
				iterativeIndexForNewWeightValues++;
		}
	}
	
	public Double trainNeuron(Neuron neuron, Double expectedOutput) {
		Neuron preNeuron;
		computeOutputOfNeuron(neuron);
		Double neuronOutputInitial = neuron.outputResult;
		Double gradient;
		Double curWeight;
		Double change = new Double(0.0);

		if (expectedOutput != null) {
			gradient = Math.abs((expectedOutput.doubleValue() - neuronOutputInitial.doubleValue()))
					* leastCount.doubleValue();
		} else {
			gradient = computeGradient(neuron);
		}

		for (int i = 0; i < neuron.parentNeurons.size(); i++) {
			preNeuron = neuron.parentNeurons.get(i);
			curWeight = preNeuron.getWeight(neuron.getNeuronIndex());
			// System.out.println(curWeight);
			// if(preNeuron.activation == null) {
			// System.out.println("How ??");
			// }
			if (expectedOutput != null) {

				// Double neuronDeltaOutput = computeOutputOfNeuron(neuron,
				// leastCount);
				// Double fxPlusDelxMinusFx = neuronDeltaOutput.doubleValue() -
				// neuronOutputInitial.doubleValue();
				// System.out.println("neuronDeltaOutput.doubleValue() = " +
				// neuronDeltaOutput.doubleValue() +
				// " neuronOutputInitial.doubleValue() = "
				// +neuronOutputInitial.doubleValue());

				// System.out.println(gradient);
				if (expectedOutput.doubleValue() > neuronOutputInitial.doubleValue()) {
					change = new Double(Math.abs(neuron.learningRate.doubleValue()
							* (gradient.doubleValue() + preNeuron.outputResult.doubleValue() * curWeight.doubleValue()
									* neuron.momentum.doubleValue())));
					if ((change.doubleValue() + preNeuron.getWeight(neuron.getNeuronIndex())) <= cap.upperLimit
							.doubleValue()) {
						preNeuron.updateWeight(neuron.getNeuronIndex(), change.doubleValue());
					} else {
						preNeuron.updateWeight(neuron.getNeuronIndex(), Math.random()
								* (cap.upperLimit.doubleValue() - preNeuron.getWeight(neuron.getNeuronIndex())));
					}
					neuron.gradient = gradient;

				} else if (expectedOutput.doubleValue() < neuronOutputInitial.doubleValue()) {
					change = new Double(Math.abs(neuron.learningRate.doubleValue()
							* (gradient.doubleValue() + preNeuron.outputResult.doubleValue() * curWeight.doubleValue()
									* neuron.momentum.doubleValue())));
					change = -change.doubleValue();
					if ((change.doubleValue() + preNeuron.getWeight(neuron.getNeuronIndex())) >= cap.lowerLimit
							.doubleValue()) {
						preNeuron.updateWeight(neuron.getNeuronIndex(), change.doubleValue());
					} else {
						preNeuron.updateWeight(neuron.getNeuronIndex(),
								Math.random() * (preNeuron.getWeight(neuron.getNeuronIndex()) - cap.lowerLimit));
					}
					neuron.gradient = -gradient.doubleValue();
				} else {
					neuron.gradient = new Double(0.0);
				}
				lastVisitedWeightSet.add(preNeuron.getWeight(neuron.getNeuronIndex()));
			} else {
				// gradient = computeGradient(neuron);
				change = new Double(Math.abs(neuron.learningRate.doubleValue()
						* (gradient.doubleValue() + preNeuron.outputResult.doubleValue() * curWeight.doubleValue()
								* neuron.momentum.doubleValue())));
				if (gradient.doubleValue() < 0) {
					change = new Double(-change.doubleValue());
					if ((change.doubleValue() + preNeuron.getWeight(neuron.getNeuronIndex())) >= cap.lowerLimit
							.doubleValue()) {
						preNeuron.updateWeight(neuron.getNeuronIndex(), change.doubleValue());
					} else {
						preNeuron.updateWeight(neuron.getNeuronIndex(),
								Math.random() * (preNeuron.getWeight(neuron.getNeuronIndex()) - cap.lowerLimit));
					}

				} else {
					if (change.doubleValue() + preNeuron.getWeight(neuron.getNeuronIndex()) <= cap.upperLimit
							.doubleValue()) {
						preNeuron.updateWeight(neuron.getNeuronIndex(), change.doubleValue());
					} else {
						preNeuron.updateWeight(neuron.getNeuronIndex(), Math.random()
								* (cap.upperLimit.doubleValue() - preNeuron.getWeight(neuron.getNeuronIndex())));
					}
				}
				lastVisitedWeightSet.add(preNeuron.getWeight(neuron.getNeuronIndex()));
				neuron.gradient = gradient.doubleValue();
			}

			// System.out.println(gradient);
		}

		return null;
	}

	@Override
	public Double trainNetwork(ArrayList<Double> expectedOutput, Double learningRateChangeFactor) throws Exception {
		Neuron tempNeuron;
		NNetwork.computeNetworkResult(0);
		lastVisitedWeightSet = new ArrayList<Double>();
		for (int nIndex = 0; nIndex < NNetwork.networkData.outputNeurons.size(); nIndex++) {
			tempNeuron = NNetwork.networkData.outputNeurons.get(nIndex);
			trainNeuron(tempNeuron, expectedOutput.get(nIndex));
			// the final error recorded
			// is the error of the new system.
		}
		for (int layerIndex = NNetwork.networkData.hiddenLayers.size() - 1; layerIndex >= 0; layerIndex--) {
			for (int nIndex = 0; nIndex < NNetwork.networkData.hiddenLayers.get(layerIndex).size(); nIndex++) {
				tempNeuron = NNetwork.networkData.hiddenLayers.get(layerIndex).get(nIndex);
				trainNeuron(tempNeuron, null);
			}
		}
		return null;
	}
	
	public Double computeError(ArrayList<Double> expectedOutput) throws Exception {
		NNetwork.computeNetworkResult(0);
		ArrayList<Double> annOutput = NLayerToArray.obtainLayerOutputInArray(NNetwork.networkData.outputNeurons);
		return NNetwork.errorFunction.computeError(expectedOutput, annOutput);
	}

}
