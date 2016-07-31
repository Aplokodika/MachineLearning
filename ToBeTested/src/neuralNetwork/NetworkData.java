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
	public ArrayList <Float> expectedOutputSet = new ArrayList <Float>();
	
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
			temp.activation = act.get(k);
			inputNeurons.add(temp);
		}
		
		// for the hidden layers
		for (int i = 0; i < endSize - 1; i++){
			hiddenLayers.add(i, new ArrayList<Neuron>());
			for(int j = 0; j < sizeList.get(i + 1); j++, k++){ // i + 1 because, sizeList stores the size for the input layer,
				temp = new Neuron();						   // hidden layer and the output layer. Hence it must succeed by 1
				temp.activation = act.get(k);
				hiddenLayers.get(i).add(temp);
			}
		}
		
		//for output neurons
		for(int i = 0; i < sizeList.get(endSize); i++, k++){
			temp = new Neuron();
			temp.activation = act.get(k);
			outputNeurons.add(temp);
		}
		noOfNeurons = k;
	}
	
	
	int  getNoOfNeurons(){
		return noOfNeurons;
	}
	
	
	/** 
	 * This assigned the input to the input neurons.
	 * Before calling this method, the neural network system must be constructed. 
	 * @param 
	 * 	  inp - This is the input given to the network model in terms of ArrayList<Float>
	 * 
	 */
	public void setInput(ArrayList<Float> inp){
		initializedInputs = true;
		for(int index = 0; index < inp.size(); index++){
			inputNeurons.get(index).input = inputNeurons.get(index).outputResult 
						= new Float(inp.get(index));
			//inputNeurons.get(index).parentNeurons = null;
		}
	}
	

}
