/*
 * Author: K Sreram. 
 * copyright (c) 2016 K Sreram, all rights reserved.
 */
package neuralNetwork.neuralInterface;

import java.util.ArrayList;

import neuralNetwork.Neuron;
import neuralNetwork.NeuronAddress;




/**
 * This class acts as an interface for the neural network system. 
 * @author sreram
 *
 */
public class NeuralNetworkInterface extends TriggerInterface{
	
	/**
	 * 
	 * @author sreram
	 *
	 */

	
	
	
	/**
	 * This sets the weight-cap. It says how far weight values must vary, and 
	 * thus keeps the weight values within the range.  
	 * @param lowerLimit - the least possible weight value. 
	 * @param upperLimit - the greatest possible weight value. 
	 * @return
	 */
	
	public boolean setWeightCap(Double lowerLimit, Double upperLimit) {
		if(isNetworkAssignedToNeuralTrainer == true) {
			neuralTrainer.cap.lowerLimit = lowerLimit;
			neuralTrainer.cap.upperLimit = upperLimit;
			return true;
		}
		return false;
	}
	
	/**
	 * This method is used for setting the training data-set for the 
	 * training procedure. 
	 * @param tDataSet contains the training data-set values
	 * @return initializes and returns status
	 * @throws Exception 
	 */
	public boolean setTrainingDataSet(TrainingDataSet tDataSet) throws Exception{
		dataSet = tDataSet;
		isDataSetSet = true;
		return initializeSystem();
	}
	/**
	 * This method sets the training data-set based on the set of inputs and its 
	 * corresponding output set. This method sets this data-set one input-output
	 * pair at a time. This process isn't expensive as the time-complexity for each 
	 * addition is one. 
	 * 
	 * @param inputForSystem - The input that is to be given to the neural network 
	 * 							system.
	 * @param expectedOutput - the output that is expected from the system, given the 
	 * 						   input `inputForSystem`. 
	 * @return returns false always; this is because, the initialization method 
	 * 					must not be called at this point. 
	 * @throws Exception	
	 */
	public boolean setTrainingDataSet(ArrayList<Double> inputForSystem,
			ArrayList<Double> expectedOutput) throws Exception{
		dataSet.getDataSetInputs().add(inputForSystem);
		dataSet.getDataSetOutputs().add(expectedOutput);
		return false;
	}
	/**
	 * This method sets the training data-set based on the set of inputs and its 
	 * corresponding output set. This method sets this data-set one input-output
	 * pair at a time. This process isn't expensive as the time-complexity for each 
	 * addition is one.
	 * 
	 * But this method also comes with a method to denote if the added input-output
	 * pair was the last one or not. 
	 * @param inputForSystem The input that is to be given to the neural network 
	 * 							system.
	 * @param expectedOutput the output that is expected from the system, given the 
	 * 						   input `inputForSystem`.
	 * @param isLast
	 * @return This returns true if two conditions satisfy. The first condition 
	 * 			states if the data-set entered was the last one, then `isDataSetSet`
	 * 			flag gets set and the system attempts to initialize the system. 
	 * 			if the initialization is successful and if the system is ready to be used, 
	 * 			then, true is returned. For all the other outcomes, false is returned. 
	 * @throws Exception
	 */
	public boolean setTrainingDataSet(ArrayList<Double> inputForSystem,
			ArrayList<Double> expectedOutput, boolean isLast ) throws Exception{
		dataSet.getDataSetInputs().add(inputForSystem);
		dataSet.getDataSetOutputs().add(expectedOutput);
		if(isLast){
			isDataSetSet = true;
			return initializeSystem();
		} else {
			return false;
		}
	}
	
	
	/*************************************************************************************
	 * Initialization region ends here. 
	 * */
	/**
	 * This trains the neural network system to adapt to the training data-set.
	 * 
	 * ---Future update---
	 * The system must have a mechanism to record the smallest error encountered
	 * to be able to reverse the system to that state it was when the minimum error 
	 * occurs. Over using this method will cause the system to quickly over-train
	 * causing the white noise to have a greater impact over the neural network update 
	 * system. 
	 * 
	 * This can be solved by adapting a mechanism to alternate the primitive mechanism 
	 * with the one described above using probabilistic constrains. 
	 * 
	 * This update is required to make the software fit for any ANN use. 
	 * 
	 * Hint: write a method and a container to obtain all the weight values 
	 * in the system. This can only be done by implementing the tree traversal 
	 * algorithm in this current design. But the inefficiency caused by this need 
	 * not be significant in altering the time complexity, unless the probability 
	 * of using this new method is same as the probability of the system in 
	 * using the primitive method is same. 
	 *  
	 * @param setAbst if this value is set to true, it takes into account the 
	 * 					initialized abstraction layers and sets the appropriate  
	 * 					abstraction. 
	 * @return returns the average error in the system. 
	 * @throws Exception
	 * 
	 * 
		neuralTrainer.trainNetwork(errorList);	
	 */
	public void runTrainingSystem( boolean setAbst) throws Exception{
		TrainingDataSet.trainingDataUnit trainerHandle;
		
		if(setAbst)
			neuralTrainer.NNetwork.setAbstraction(abstLayerInputs, abstLayerOutputs);
		
		for(int i = 0; i < dataSet.size(); i++)
			{
				trainerHandle = dataSet.getTrainerHandle(i);
				neuralTrainer.trainNetwork(trainerHandle);

			}
	}
	
	public ArrayList<Double> getWeightValues() {
		return neuralTrainer.retriveWeightValues();
	}
	
	public Double computeError(boolean setAbst) {
		double error = 0.0;
		if(setAbst )
			neuralTrainer.NNetwork.setAbstraction(abstLayerInputs, abstLayerOutputs);
		for(int i = 0; i < dataSet.size(); i++){
						neuralTrainer.NNetwork.networkData.setInput(dataSet.getDataSetInputs().get(i));
			error += neuralTrainer.computeError(dataSet.getDataSetInputs().get(i), 
					dataSet.getDataSetOutputs().get(i));
		}
		error = error/dataSet.size();
		return error;
	}
	
	
	public void setWeightValuesDynimic(ArrayList<Double> dyn){
		neuralTrainer.setWeightValues(dyn);
	}
	
	public Double getLeastRecordedError() {
		return neuralTrainer.getLeastRecorded();
	}
	
	/**
	 * This forms a connection between any two neurons in the network. This facility is 
	 * specifically meant for allowing recursive neural networks. 
	 * @param from
	 * @param to
	 * @param weight
	 */
	public void connect(NeuronAddress from, NeuronAddress to, Double weight)	{
		Neuron fromNeuron = neuralTrainer.NNetwork.networkData.getNeuron(from.layerNo, from.neuronNo);
		Neuron toNeuron = neuralTrainer.NNetwork.networkData.getNeuron(to.layerNo, to.neuronNo);
		fromNeuron.connectWith(toNeuron, weight);
	}
	/**
	 * This disconnects the connection from the `from` neuron and the `to` neuron
	 * @param from
	 * @param to
	 * @throws Exception
	 */
	public void disconnect(NeuronAddress from, NeuronAddress to) throws Exception	{
		Neuron fromNeuron = neuralTrainer.NNetwork.networkData.getNeuron(from.layerNo, from.neuronNo);
		Neuron toNeuron = neuralTrainer.NNetwork.networkData.getNeuron(to.layerNo, to.neuronNo);
		fromNeuron.disconnectWith(toNeuron);
	}
	
	
	public TrainingDataSet getTrainingDataSet(){
		return dataSet;	
	}

	
}
