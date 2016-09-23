/*
 * Author: K Sreram. 
 * copyright (c) 2016 K Sreram, all rights reserved.
 */
package neuralNetwork;

import java.util.ArrayList;

import neuralNetwork.neuralInterface.TrainingDataSet.trainingDataUnit;

/**
 * 
 * @author Sreram
 *
 */
public abstract class NeuralTrainer {

	

	public NeuralNetwork NNetwork;
	
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
	
	public void resetLeastError(){
		leastError = null;
	}


	public abstract void setWeightValues(ArrayList<Double> dyn);

	public abstract ArrayList<Double> retriveWeightValues();

	public abstract void trainNetwork(trainingDataUnit trainerHandle);


	/**
	 * This method forcibly imposes change to one of the weight values connecting the output 
	 * neurons
	 * @param change -  the factor by which the system must change
	 * @param neuronFrom - the last hidden layer neuron connected to the output layer neuron
	 * @param neuronTo  - the neuron in the output layer that is being connected
	 * @param expectedOutput - the expected output of the neuron neuronTo. 
	 */
	
	public void imposeChange(Double change, Neuron neuronFrom, Neuron neuronTo) {
		neuronFrom.weightValues.put(neuronTo.getNeuronIndex(),
				neuronFrom.weightValues.get(neuronTo.getNeuronIndex()) + change.doubleValue());
	}	
	/**
	 * 
	 */
	public Double computeError(ArrayList<Double> input, ArrayList<Double> expectedOutput) {
		NNetwork.networkData.setInput(input);
		return computeError(expectedOutput);
	}
	
	/**
	 * 
	 */
	public Double computeError(ArrayList<Double> expectedOutput){
		NNetwork.computeNetworkResult(0);
		ArrayList<Double> annOutput = NLayerToArray.obtainLayerOutputInArray(NNetwork.networkData.outputNeurons);
		return NNetwork.errorFunction.computeError(expectedOutput, annOutput);
	}
	/**
	 * 
	 * @param input
	 * @return
	 */
	public ArrayList<Double> computeNetworkResult(ArrayList<Double> input){
		NNetwork.networkData.setInput(input);
		NNetwork.computeNetworkResult(0);
		return NLayerToArray.obtainLayerOutputInArray(NNetwork.networkData.outputNeurons);
	}

}
