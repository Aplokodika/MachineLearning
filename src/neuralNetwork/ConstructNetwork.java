/*
 * Author: K Sreram. 
 * copyright (c) 2016 K Sreram, all rights reserved.
 */
package neuralNetwork;

import java.util.ArrayList;

import neuralNetwork.activationFunctions.Activation;



public class ConstructNetwork  {

	public enum NetworkResultComputationType {
		quick, treeTraversal
	}
	/**
	 * 
	 * This constructs the network, calling the method `NNetwork.networkData.initializeNeurons(sizeList, act);`
	 * 
	 * @param act - list of all activation functions
	 * @param errFnc - the network's error function. This is mostly standardized, unless it is a special case
	 * @param sizeList - the list of the number of neurons in each layer. This also holds the information to the 
	 * 						number of layers in the network. 
	 * @param type - There are two implementations. The treeTraversal implementation must be used for only special
	 * 				 cases, for example when any two successive layers aren't bijective. 
	 */
	public ConstructNetwork( ArrayList<Activation> act, 
			ComputeError errFnc, ArrayList<Integer> sizeList, NetworkResultComputationType type){
		if(type.equals(NetworkResultComputationType.treeTraversal))
			NNetwork = new NeuralNetwork(errFnc);
		else if(type.equals(NetworkResultComputationType.quick) )
			NNetwork = new NeuralNetworkQuick(errFnc);
		NNetwork.networkData.initializeNeurons(sizeList, act);
	}
	
	public NeuralNetwork NNetwork; 
	/**
	 * This method forms links between the layers, layer1 and layer2, connecting 
	 * each of the neurons in layer1 with each neurons in layer2 assigning the 
	 * weight values from the variables wPair. the variable wPair.index is a temporary
	 * variable (initialized as zero), which keeps record of the the number of weight 
	 * values assigned used in forming the weighed connection.  
	 * 
	 * @param layer1 - the backward layer
	 * @param layer2 - the forward layer
	 * @param wPair - contains the weight values
	 * @throws Exception - this exception gets thrown if there aren't enough weight 
	 * 					   values in wPair
	 */
	public static void formWeightLinks(ArrayList<Neuron> layer1, ArrayList<Neuron> layer2,
			WeightPair wPair)throws Exception{
		
		for(int i = 0; i < layer1.size(); i++){
			for(int j = 0; j < layer2.size(); j++){
				if(wPair.index == wPair.weight.size()  ){
					Exception e = new Exception("Error in formWeightLinks: not enough"
							+ " weight values in array");
					throw e;
				}
				layer1.get(i).connectWith( layer2.get(j), wPair.weight.get(wPair.index));
				 wPair.index++;
			}
		}
	}
	/**
	 * Used for initializing WeightPair variables
	 * @param sizeList this shows the number of neurons in each layer 
	 * @return The number of weight values required for initializing the system.  
	 */
	public static int weightPairSize(ArrayList<Integer> sizeList){
		int result = 0;
		for(int i = 0; i < sizeList.size() - 1; i++){
			result = sizeList.get(i).intValue()*sizeList.get(i + 1) + result;
		}
		return result;
	}
	/**
	 * This method constructs the neural network by forming the required links. 
	 * 
	 * @param size - (Or sizeList) is the list of number of neurons in each layer. 
	 * @param wPair - the list of all the weight values to be initialized while 
	 * 					initialization
	 * @throws Exception throws exception from formWeightLinks
	 */
	public void constructNetworkLayered(ArrayList<Integer> size, WeightPair wPair) throws Exception{
		wPair.index = 0; 
		int maxHidden = NNetwork.networkData.hiddenLayers.size();
		// for adding input and starting hidden neuron weights
		formWeightLinks(NNetwork.networkData.inputNeurons, 
					NNetwork.networkData.hiddenLayers.get(0),wPair);
		
		// for forming connections within the hidden neurons. 
		for(int i = 0; i < NNetwork.networkData.hiddenLayers.size()-1; i++){
			formWeightLinks(NNetwork.networkData.hiddenLayers.get(i), 
					NNetwork.networkData.hiddenLayers.get(i+1), wPair);
		}
		
		//for connecting the final layer in the hidden neuron's layer-set with the output 
		// neurons. 
		
		formWeightLinks(NNetwork.networkData.hiddenLayers.get(maxHidden-1), 
				NNetwork.networkData.outputNeurons, wPair);
		
		NNetwork.constructStatus = true;
	}
	/**
	 * computes the number of neurons in the system. 
	 * @param layerSize list of number of neurons in each layer
	 * @return returns the summation of the list layerSize
	 */
	public static int noOfNeurons(ArrayList<Integer> layerSize){
		int result = 0;
		for(int i = 0; i < layerSize.size(); i++)
			result = result + layerSize.get(i);
		return result;
	}
	
}
