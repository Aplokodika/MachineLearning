/*
 * Author: K Sreram. 
 * copyright (c) 2016 K Sreram, all rights reserved.
 */

package neuralNetwork;

import java.util.ArrayList;

public class NetworkData{

	public boolean initializedInputs = false;
	
	public ArrayList <Neuron> inputNeurons = new ArrayList <Neuron>();
	public ArrayList <Neuron> outputNeurons = new ArrayList <Neuron>();
	public ArrayList <Double> expectedOutputSet = new ArrayList <Double>();
	
	private int noOfNeurons = 0;
		
	public ArrayList <ArrayList<Neuron>> hiddenLayers = new ArrayList <ArrayList<Neuron>>();
	
	public NetworkData(){
		
	}
	
	
	public void initializeNeurons(ArrayList<Integer> sizeList, ArrayList<Activation> act){
		int endSize = sizeList.size() - 1;
		Neuron temp;
		int k = 0;
		// for input neurons
		for(int i = 0; i < sizeList.get(0); i++, k++){
			temp = new Neuron();
			temp.setActivation(act.get(k));
			inputNeurons.add(temp);
		}
		
		// for the hidden layers
		for (int i = 0; i < endSize - 1; i++){
			hiddenLayers.add(i, new ArrayList<Neuron>());
			for(int j = 0; j < sizeList.get(i + 1); j++, k++){ // i + 1 because, sizeList stores the size for the input layer,
				temp = new Neuron();						   // hidden layer and the output layer. Hence it must succeed by 1
				temp.setActivation(act.get(k));
				hiddenLayers.get(i).add(temp);
			}
		}
		
		//for output neurons
		for(int i = 0; i < sizeList.get(endSize); i++, k++){
			temp = new Neuron();
			temp.setActivation(act.get(k));
			outputNeurons.add(temp);
		}
		noOfNeurons = k;
	}
	
	/**
	 * In this Neural network architecture, each neuron has an unique learning rate and momentum value. 
	 * This value is applicable to any weight value-set that connects the current neuron. 
	 * 
	 * This method initializes each of these neurons to the same learning rate and momentum. 
	 * 
	 * To do: write a customized initialization method for initializing groups of neurons to similar
	 * learning rate and momentum values.  
	 * @param sizeList - This lists the number of neurons in each layer. The array index indicates 
	 * 					 the layer number. (sizeList.get(0) -> for input layer. 
	 * 					 sizeList.get(sizeList.size()) -> for output layer. 
	 * @param lRate	-  the common learning rate to assign. 
	 * @param momentum -  the common momentum value to assign. 
	 */
	public void setCommonLearningRateMomentum(ArrayList<Integer> sizeList, Double lRate, Double momentum){
		int endSize = sizeList.size() - 1;
		for(int i = 0; i < sizeList.get(0); i++){
			inputNeurons.get(i).learningRate = lRate;
			inputNeurons.get(i).momentum = momentum;
		}
		
		// for the hidden layers
		for (int i = 0; i < endSize - 1; i++){
			for(int j = 0; j < sizeList.get(i + 1); j++){ 
				// i + 1 because, sizeList stores the size for the input layer,
			// hidden layer and the output layer. Hence it must succeed by 1		
				hiddenLayers.get(i).get(j).learningRate = lRate;
				hiddenLayers.get(i).get(j).momentum = momentum;
			}
		}
		
		//for output neurons
		for(int i = 0; i < sizeList.get(endSize); i++){
			outputNeurons.get(i).learningRate = lRate;
			outputNeurons.get(i).momentum = momentum;
		}
	}
	
	int  getNoOfNeurons(){
		return noOfNeurons;
	}
	
	
	/** 
	 * This assigned the input to the input neurons.
	 * Before calling this method, the neural network system must be constructed. 
	 * @param 
	 * 	  inp - This is the input given to the network model in terms of ArrayList<Double>
	 * 
	 */
	public void setInput(ArrayList<Double> inp){
		initializedInputs = true;
		for(int index = 0; index < inp.size(); index++){
			inputNeurons.get(index).input = inputNeurons.get(index).outputResult 
						= new Double(inp.get(index));
			//inputNeurons.get(index).parentNeurons = null;
		}
	}
	

}
