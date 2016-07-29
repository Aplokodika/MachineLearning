/*
 * Author: K Sreram. 
 * copyright (c) 2016 K Sreram, all rights reserved.
 * 
 * Developer License Agreement.
 * ..............................
 * 
 * Members of Aplokodika may update the following source and may claim the ownership of
 * copyright to the source they contribute. The members of Aplokodika may modify, 
 * create or alter a whole or a part of the following code with consent from the 
 * majority of the copyright holders of the source code. Each modification must be 
 * documented and notified, with appropriate identity information of the author who helped 
 * modify the source; this includes the author's name, personal email address.
 * 
 * By contributing to this code, you agree to grant Aplokodika free license to store, modify, share, 
 * sell, republish and grant such license to third parties without any cost or conditions. 
 * 
 * Authors contributing to this project own the code they write. That is, Aplokodika does
 * not claim to own the copyright to the content contributed by an author unless the rights are 
 * explicitly transfered. By modifying/creating/contributing to this project, the authors/copyright
 * holders agree to grant Aplokodika organization free license to store, modify, share, sell, republish
 * this software, as source or as a binary release and the authors also agree that Aplokodika may grant
 * such license to third parties.
 * 
 * The authors contributing to this software also agree that, Aplokodika reserves the rights to 
 * modify this license at will, and modifications may not be notified instantly. All notification 
 * mechanisms used to notify such changes are only for the ease of reference.  
 */
package neuralNetwork;

import java.util.ArrayList;

public class NetworkData <NeuronWithFnc extends Neuron>{

	public boolean initializedInputs = false;
	
	public ArrayList <Neuron> inputNeurons = new ArrayList <Neuron>();
	public ArrayList <Neuron> outputNeurons = new ArrayList <Neuron>();
	public ArrayList <Float> expectedOutputSet = new ArrayList <Float>();
	
	
	public Factory<NeuronWithFnc> factory;
	
	public ArrayList <ArrayList<Neuron>> hiddenLayers = new ArrayList <ArrayList<Neuron>>();
	
	NetworkData(Factory<NeuronWithFnc> fact){
		this.factory = fact;
	}
	
	
	public void initializeNeurons(ArrayList<Integer> sizeList){
		int endSize = sizeList.size() - 1;
		
		// for input neurons
		for(int i = 0; i < sizeList.get(0); i++){
			inputNeurons.add(factory.newElement());
		}
		
		// for the hidden layers
		for (int i = 0; i < endSize - 1; i++){
			hiddenLayers.add(i, new ArrayList<Neuron>());
			for(int j = 0; j < sizeList.get(i); j++){
				hiddenLayers.get(i).add(factory.newElement());
			}
		}
		
		//for output neurons
		for(int i = 0; i < sizeList.get(endSize); i++){
			outputNeurons.add(factory.newElement());
		}
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
			inputNeurons.get(index).parentNeurons = null;
		}
	}
	

}
