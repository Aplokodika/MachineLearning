/*
 * Author: K Sreram. 
 * copyright (c) 2016 K Sreram, all rights reserved.
 */
package neuralNetwork;

import java.util.ArrayList;



public class ConstructNetwork  {

	public ConstructNetwork( ArrayList<Activation> act, 
			ComputeError errFnc, ArrayList<Integer> sizeList){
		NNetwork = new NeuralNetwork(errFnc);
		NNetwork.networkData.initializeNeurons(sizeList, act);
	}
	
	public NeuralNetwork NNetwork; 
	
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
	
	public static int noOfNeurons(ArrayList<Integer> layerSize){
		int result = 0;
		for(int i = 0; i < layerSize.size(); i++)
			result = result + layerSize.get(i);
		return result;
	}
	
}
