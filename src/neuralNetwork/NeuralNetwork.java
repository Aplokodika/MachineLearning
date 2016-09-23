/*
 * Author: K Sreram. 
 * copyright (c) 2016 K Sreram, all rights reserved.
 */
package neuralNetwork;

import java.util.*;

public class NeuralNetwork {


	public boolean constructStatus = false;

	public NetworkData networkData;

	
	public int networkLayerSize(){
		return (networkData.hiddenLayers.size() + 2);
	}
	
	/**
	 * This method creates a new abstraction for selecting the input, output and hidden 
	 * layers specifically. This is to allow deep-learning possible.  
	 * 
	 * Note: the layers in between the `inputLayer` and the `outputLayer`
	 * @param inputLayer
	 * 		 The index of the layer in netLayers variable that has to be considered as the input 
	 * 		 layer. 
	 * @param outputLayer
	 * 		 The index of the layer in the netLayers variable that has to be considered as the 
	 * 		 output layer. 
	 * 
	 */
	
	public void setAbstraction(int inputLayer, int outputLayer){
		networkData.hiddenLayers = new ArrayList<ArrayList<Neuron>>();
		// for resetting hidden layers
		for(int i = inputLayer + 1; i < outputLayer; i++){
			networkData.hiddenLayers.add(networkData.getLayer(i));
		}
		
		networkData.inputNeurons = networkData.getLayer(inputLayer);
		networkData.outputNeurons = networkData.getLayer(outputLayer);
	}
	
	ComputeError errorFunction;

	NeuralNetwork(ComputeError errFnc) {
		networkData = new NetworkData();
		errorFunction = errFnc;
	}

	
	//private Neuron.NeuronCallSession currentSession = Neuron.NeuronCallSession.SESSION_1;
	
	/*public void resetSission(){
		currentSession = Neuron.NeuronCallSession.SESSION_1;
		networkData.resetFlagsNetLayers();
	}*/
	
	
	public void computeNetworkResultQuick(int startIndex){
		for(int i = startIndex; i < networkData.hiddenLayers.size(); i++ ){
			for(int j = 0; j < networkData.hiddenLayers.get(i).size(); j++){
				networkData.hiddenLayers.get(i).get(j).pullInput();
				networkData.hiddenLayers.get(i).get(j).computeOutput();
			}
		}
		
		for(int i = 0; i < networkData.outputNeurons.size(); i++)	{
			networkData.outputNeurons.get(i).pullInput();
			networkData.outputNeurons.get(i).computeOutput();
		}
	}
	
	/**
	 * This computes the final output value based on the inputs from the
	 * previous neurons, iterating through each layers. This method is called
	 * after the `setInput` method gets called.
	 * 
	 * This uses tree traversal
	 * 
	 * @throws Exception
	 */
	public void computeNetworkResult(int startIndex){
		
		Map<Integer, Boolean> sessionHash = new HashMap<Integer, Boolean>();
		
		Queue<Neuron> neuronQueue = new LinkedList<Neuron>();

		// The following adds the first hidden layer's neurons into the queue
		// after pulling
		// the input from the input layers. ( ! ) 
		
	
		
		for (int indexNeuron = 0; 
				indexNeuron < networkData.hiddenLayers.get(startIndex).size(); 
																		indexNeuron++) {
			networkData.hiddenLayers.get(startIndex).get(indexNeuron).pullInput();
			networkData.hiddenLayers.get(startIndex).get(indexNeuron).computeOutput();
			neuronQueue.add(networkData.hiddenLayers.get(startIndex).get(indexNeuron));
		}

		// the following loop continues until the queue becomes empty. If the
		// queue becomes empty,
		// it signifies that the output has reached the output neurons.
		while (neuronQueue.isEmpty() == false) {
			Neuron neuronTemp;
			Neuron neuron = neuronQueue.poll();
			for (int i = 0; i < neuron.childNeurons.size(); i++) {
				
				neuronTemp = neuron.childNeurons.get(i);
				
				if (neuronTemp != null && sessionHash.get(neuronTemp.getNeuronIndex()) == null ) {
					neuronTemp.pullInput();
					neuronTemp.computeOutput();
					neuronQueue.add(neuronTemp);
					//neuronTemp.callSession = session;
					sessionHash.put(neuronTemp.getNeuronIndex(), true);
				}
			}
		}

	}

}
