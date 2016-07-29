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



public class ConstructNetwork <NeuronWithFnc extends Neuron>  {

	public Factory<NeuronWithFnc> factory;
	
	public ConstructNetwork(Factory<NeuronWithFnc> fact, 
			ComputeError<NeuronWithFnc> errFnc, ArrayList<Integer> sizeList){
		factory = fact;
		NNetwork = new NeuralNetwork<NeuronWithFnc>(factory, errFnc);
		NNetwork.networkData.initializeNeurons(sizeList);
	}
	
	public NeuralNetwork<NeuronWithFnc> NNetwork; 
	
	public static void formWeightLinks(ArrayList<Neuron> layer1, ArrayList<Neuron> layer2,
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
	
	public static int weightPairSize(ArrayList<Integer> sizeList){
		int result = 0;
		for(int i = 0; i < sizeList.size() - 1; i++){
			result = sizeList.get(i).intValue()*sizeList.get(i + 1) + result;
		}
		return result;
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
		
		NNetwork.constructStatus = true;
	}
	
}
