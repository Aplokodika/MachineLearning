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
 * Authors contributing to this project owns the piece of code they write. That is, Aplokodika does
 * not claim to own the copyright to the content contributed by an author unless the rights are 
 * explicitly transfered. By modifying/creating/contributing to this project, the authors agree 
 * to grant Aplokodika organization free license to store, modify, share, 
 * sell, republish and grant such license to third parties without any cost or conditions.      
 */
package neuralNetwork;

import java.util.ArrayList;



public class constructNetwork <NeuronWithFnc extends Neuron>  {

	public Factory<NeuronWithFnc> factory;
	
	constructNetwork(Factory<NeuronWithFnc> fact, ComputeError<NeuronWithFnc> errFnc){
		factory = fact;
		NNetwork = new NeuralNetwork<NeuronWithFnc>(factory, errFnc);
	}
	
	public NeuralNetwork<NeuronWithFnc> NNetwork; 
	
	public void formWeightLinks(ArrayList<Neuron> layer1, ArrayList<Neuron> layer2,
			WeightPair wPair)throws Exception{
		
		for(int i = 0; i < layer1.size(); i++){
			for(int j = 0; j < layer2.size(); j++){
				if(wPair.weight.get(wPair.index) == null){
					Exception e = new Exception("Error in formWeightLinks: not enough"
							+ " weight values in array");
					throw e;
				}
				 layer1.get(i).addNeuron( layer2.get(j), wPair.weight.get(wPair.index));
				 wPair.index++;
			}
		}
	}
	
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
		
	}
}
