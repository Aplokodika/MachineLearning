/*
 * Author: K Sreram. 
 * copyright (c) 2016 K Sreram, all rights reserved.
 */
package neuralNetwork;

import java.util.ArrayList;

class WeightCap {
	public Double upperLimit;
	public Double lowerLimit;
	
}


/**
 * 
 * @author Sreram
 *
 */
public class NeuralTrainer {

	

	public NeuralNetwork NNetwork;

	private Double maxError;
	private Double minError;
	private Double deltaError;
	private Double deltaErrorNet;
	
	public void setMaxMinError(Double max, Double min )	{
		maxError = max;
		minError = min;
		deltaErrorNet = new Double(max.doubleValue() - min.doubleValue());
	}
	
	public NeuralTrainer() {
		cap.upperLimit = new Double	(1000 );
		cap.lowerLimit = new Double (-1000);
	}
	
	
	public void setCap(Double lowerLimit, Double upperLimit) {
		cap.lowerLimit = lowerLimit;
		cap.upperLimit = upperLimit;
	}
	
	public WeightCap cap = new WeightCap();
	
	public void setNNetwork(NeuralNetwork Nnet) {
		NNetwork = Nnet;
	}
	
	/**
	 * This checks to see if the error of the system reduced on incremental change 
	 * in the weight values or whether the error reduced on decrementing change in 
	 * the weight values of the system. 
	 * @param changeErrorAdd show the change in error, because of incrementing the 
	 * 						 weight values
	 * @param changeErrorSub shows the change in error, because of decrementing the 
	 * 						 weight values
	 * @param neuronError The error recorded before changing the system. 
	 * @return this returns the direction, or the kind of change to be made.  
	 * 		NeuralTrainer.ChangeWeight.increaseWeight, NeuralTrainer.ChangeWeight.decreaseWeight,
	 * 		NeuralTrainer.ChangeWeight.noChange
	 */
	private Double checkChange(Double changeErrorAdd, Double changeErrorSub,
									Double neuronError, Double change, 
									Double weight) {
		//System.out.println("weight" + weight);
		if (changeErrorAdd.doubleValue() <= neuronError.doubleValue() ) {
			if (changeErrorSub.doubleValue() <= neuronError.doubleValue()) {
				if (changeErrorAdd.doubleValue() < changeErrorSub.doubleValue()) {
			
					return change;
				} else if(changeErrorAdd.doubleValue() == changeErrorSub.doubleValue()){
					// though the change causes equal change in error, the system is driven to a 
					// different direction. We have landed up in a position where we decide which
					// direction we take, and as per the information we currently, both leads us to
					// the goal of reducing the error. So the path to be taken is chosen randomly 
					// with equal probability distribution. 
					return (Math.random() >= 0.5)? change: new Double( -change.doubleValue());
				} else {
					return new Double( -change.doubleValue());
				}
			} else {
				return change;
			}
		} else if (changeErrorSub.doubleValue() <= neuronError.doubleValue() ) {
			return new Double( -change.doubleValue());
		}
		//System.out.println("Boo");
		return null;
	}
	
	private Double leastError = null;
	
	public Double getLeastRecorded () {
		return leastError;
	}
	
	public boolean setLeastError(Double newError){
		boolean result = false;
		if(leastError == null) {
			leastError = newError;
			result = true;
		}
		else if(newError.doubleValue() < leastError.doubleValue())	{
			leastError = newError;
			result = true;
		}
		// else, do nothing
		return result; // becomes true when a change is made.
	}
	
	private Double positiveValue(Double val) {
		if (val.doubleValue() < 0)
			return new Double(-val.doubleValue());
		return val;
	}
	
	public void resetLeastError(){
		leastError = null;
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
	 * @author Sreram
	 */
	public Double trainNeuron(Neuron neuron, ArrayList<Double> expectedOutput,
											Double learningRateChangeFactor) throws Exception {
		Neuron preNeuron;
		Double neuronError;
		
		NNetwork.computeNetworkResult(0);

		ArrayList<Double> annOutput =
				NLayerToArray.obtainLayerOutputInArray(NNetwork.networkData.outputNeurons);
		
			neuronError = NNetwork.errorFunction.computeError(expectedOutput, annOutput);
			if(neuronError.doubleValue() < maxError.doubleValue() &&
					neuronError.doubleValue() > minError.doubleValue() ) {
				deltaError = new Double (neuronError.doubleValue() - minError.doubleValue());
			}	else if (neuronError.doubleValue() > maxError.doubleValue()) {
				deltaError = deltaErrorNet;
			}	else {
				deltaError = new Double(0.0);
			}

		Double changeErrorAdd = new Double(Double.MAX_VALUE);
		Double changeErrorSub = changeErrorAdd;
		Double change;
		Double result = null;
		Double curWeight;
		Double qCur;
		for (int i = 0; i < neuron.parentNeurons.size(); i++) {
			preNeuron = neuron.parentNeurons.get(i);
			curWeight = preNeuron.weightValues.get(neuron.getNeuronIndex());
			/*if ((preNeuron.previousChangeValues.get(neuron.getNeuronIndex()) != null)
					&& (preNeuron.previousError.get(neuron.getNeuronIndex()) != null)) {
				change = positiveValue((neuron.learningRate.doubleValue() * positiveValue(
						neuronError.doubleValue() - preNeuron.previousError.get(neuron.getNeuronIndex())))
						+ (neuron.momentum.doubleValue()
								* (preNeuron.previousChangeValues.get(neuron.getNeuronIndex()))));
			} else if (preNeuron.previousError.get(neuron.getNeuronIndex()) != null) {
				preNeuron = neuron.parentNeurons.get(i);
				change = positiveValue(neuron.learningRate.doubleValue() * positiveValue(
						neuronError.doubleValue() - preNeuron.previousError.get(neuron.getNeuronIndex())));
			} else {*/
			//neuronError.doubleValue() - neuron.previousError.get(neuron.getNeuronIndex())
			change = positiveValue(neuron.learningRate.doubleValue() +
					(neuron.momentum.doubleValue()
							* (curWeight.doubleValue())));
				
			//}
			//System.out.println("change 1 = " + change
				//	+ "  learning rate :" + neuron.learningRate.doubleValue()
					//+ "momentum :" + neuron.momentum.doubleValue() 
					//+ "Cur weight :" + curWeight.doubleValue());
			change =  (deltaError.doubleValue()/ deltaErrorNet.doubleValue())*
					learningRateChangeFactor.doubleValue()*Math.random();
			//System.out.println("Change = " + change);
			/*if((change.doubleValue() + curWeight.doubleValue())
						> cap.upperLimit.doubleValue()) {
				change = 
						new Double((cap.upperLimit.doubleValue() - curWeight.doubleValue())*Math.random() );
			}
			else if((curWeight.doubleValue()-change.doubleValue() )
					< cap.lowerLimit.doubleValue()) {
				change = new Double((curWeight.doubleValue() - cap.lowerLimit.doubleValue())*Math.random());
			}*/
			
			qCur = new Double (cap.upperLimit.doubleValue() - curWeight.doubleValue());
			if(change.doubleValue() > 1)
			change  = positiveValue(((change.doubleValue() - 1)/(change.doubleValue() + 1)) * 
						qCur.doubleValue());
			if (preNeuron.checkFeasibility(neuron.getNeuronIndex(), change)) {
				preNeuron.updateWeight(neuron.getNeuronIndex(), change);
				NNetwork.computeNetworkResult(0);// finds the result
				annOutput = NLayerToArray.obtainLayerOutputInArray(NNetwork.networkData.outputNeurons);
				changeErrorAdd = NNetwork.errorFunction.computeError(expectedOutput, annOutput);
				preNeuron.updateWeight(neuron.getNeuronIndex(), -change.doubleValue());
			}

			if (preNeuron.checkFeasibility(neuron.getNeuronIndex(), -change.doubleValue())) {
				preNeuron.updateWeight(neuron.getNeuronIndex(), -change.doubleValue());
				NNetwork.computeNetworkResult(0);// finds the result
				annOutput = NLayerToArray.obtainLayerOutputInArray(NNetwork.networkData.outputNeurons);
				changeErrorSub = NNetwork.errorFunction.computeError(expectedOutput, annOutput);

				preNeuron.updateWeight(neuron.getNeuronIndex(), change);
			}

			Double nowChange = checkChange(changeErrorAdd, changeErrorSub, neuronError, change, 
											preNeuron.weightValues.get(neuron.getNeuronIndex()));
			///*****************TEST**********************************
			
			///*********************************************************
			
			if (nowChange != null) {
				preNeuron.previousChangeValues.put(neuron.getNeuronIndex(), change);
				preNeuron.previousError.put(neuron.getNeuronIndex(), neuronError);
				preNeuron.updateWeight(neuron.getNeuronIndex(), nowChange);
				if(nowChange.doubleValue() < 0)
					result = changeErrorAdd;
				else if(nowChange.doubleValue() > 0)
					result = changeErrorSub;
				else result = changeErrorSub;

			} 
				else  {
					result = neuronError;
				}
		}
		
		if(result.doubleValue() == 0.0){
			System.out.println("Booo");
		}
		return result; // the current error is returned in case of now change
	}
	
	
	

	/**
	 * This is the traditional train-network method
	 * 
	 * @param expectedOutput
	 *            - this it the set of all outputs that correlate with the given
	 *            input. This is the output that we expect to obtain.
	 * @throws Exception
	 */

	public Double trainNetwork(ArrayList<Double> expectedOutput,
										Double learningRateChangeFactor) throws Exception {
		Neuron tempNeuron;
		Double result = null;
		for (int nIndex = 0; nIndex < NNetwork.networkData.outputNeurons.size(); nIndex++) {
			tempNeuron = NNetwork.networkData.outputNeurons.get(nIndex);
			result = trainNeuron(tempNeuron, expectedOutput, learningRateChangeFactor); 
															  // the final error recorded 
															  // is the error of the new system.
		}
		for (int layerIndex =  NNetwork.networkData.hiddenLayers.size()-1; layerIndex >= 0; layerIndex--) {
			for (int nIndex = 0; nIndex < NNetwork.networkData.hiddenLayers.get(layerIndex).size(); nIndex++) {
				tempNeuron = NNetwork.networkData.hiddenLayers.get(layerIndex).get(nIndex);
				trainNeuron(tempNeuron, expectedOutput, learningRateChangeFactor);
			}
		}
		return result;
	}

	private Double trainRecursive(ArrayList<Double> expectedOutput, Neuron curNeuron,
			Integer curLayer, Integer maxLayer, Double learningRateChangeFactor) throws Exception {
		Double result = null;
		trainNeuron(curNeuron, expectedOutput, learningRateChangeFactor);
		if (curLayer != (maxLayer - 1)) {
			for (int i = 0; i < curNeuron.childNeurons.size(); i++) {
				result = trainRecursive(expectedOutput, curNeuron.childNeurons.get(i),
										curLayer.intValue() + 1, maxLayer, learningRateChangeFactor);

			}

		} else {
			Neuron tempNeuron;
			for (int nIndex = 0; nIndex < NNetwork.networkData.outputNeurons.size(); nIndex++) {
				tempNeuron = NNetwork.networkData.outputNeurons.get(nIndex);
				result = trainNeuron(tempNeuron, expectedOutput, learningRateChangeFactor);
			}
		}
		return result;

	}

	/**
	 *    CAUTION:- 
	 *    This method is not completed
	 * @param expectedOutput
	 * @param probabilityOfUsingOldError
	 * @return
	 * @throws Exception
	 */
	public Double trainNetworkDepth(ArrayList<Double> expectedOutput,
			Double probabilityOfUsingOldError) throws Exception {
		Double result = null;
		for (int index = 0; index < NNetwork.networkData.hiddenLayers.get(0).size(); index++) {
			result = trainRecursive(expectedOutput, NNetwork.networkData.hiddenLayers.get(0).get(index), 
							0, NNetwork.networkData.hiddenLayers.size(), probabilityOfUsingOldError);
		}
		return result;
	}

}
