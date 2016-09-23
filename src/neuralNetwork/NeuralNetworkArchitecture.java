/*
 * copyright (c) 2016 K Sreram (sreramk1@gmail.com) all rights reserved 
 */
package neuralNetwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import neuralNetwork.activationFunctions.Activation;
import neuralNetwork.NeuralNetworkArchitecture.ChangableWeightParameterListPair.Priority;
/**
 * 
 * @author sreram
 *
 */
public class NeuralNetworkArchitecture {
		
	
	
	protected Double learningRate = null;
	protected Double momentum = null;
	protected Boolean constructStatus = new Boolean(false);
	protected NeuralNetworkArcheitecturalData networkArchitectureData = new NeuralNetworkArcheitecturalData();
	protected NeuralNetwork newNetwork = null;
	protected NeuralNetwork currentNetwork = null;
	
	
	protected ArrayList<ChangableWeightParameters> 
						changableWeightParameterList = new ArrayList<ChangableWeightParameters>();
	
	protected ChangableWeightParameterListPair pairOfWeightParameterList= new ChangableWeightParameterListPair(); 
	
	private ConstructNetwork constructor;

	
	
	public static class ChangableWeightParameterListPair{
		
		// determines which is given more importance, X or Y.  
		public static enum Priority{
			X_PRIORITY, Y_PRIORITY
		}
		
		public ArrayList<ChangableWeightParameters> changableWeightParameterListX;
		public ArrayList<ChangableWeightParameters> changableWeightParameterListY;
		
		public ChangableWeightParameterListPair(){
			changableWeightParameterListX = new ArrayList<ChangableWeightParameters>();
			changableWeightParameterListY = new ArrayList<ChangableWeightParameters>();
		}
	}
	
	public static class NeuronLayerSet{
		ArrayList<ArrayList<Neuron>> netLayers;
		ArrayList<ArrayList<Neuron>> biasNeurons;
	}
	
	
	public NeuralNetworkArchitecture(){
		
	}
	
	public NeuralNetworkArchitecture(NeuralNetwork curNetwork){
		currentNetwork = curNetwork;
	}
	
	public static class ChangableWeightParameters{
		public final Neuron neuronFrom;
		public final Neuron neuronTo;
		
		public ChangableWeightParameters (ChangableWeightParameters para ){
			neuronFrom = para.neuronFrom;
			neuronTo = para.neuronTo;
		}
		

		public ChangableWeightParameters(Neuron pNeuronFrom, Neuron pNeuronTo) {
			neuronFrom = pNeuronFrom;
			neuronTo = pNeuronTo;
		}
		

	}
	

	
	
	/**
	 * This class stores the address to each neuron in the system. 
	 * @author sreram
	 *
	 */
	public static class Position{
		public final int layer;
		public final int neuron;
		public final double biasValue;
		public final double biasWeight;
		public Position(Position pos){
			this.layer = pos.layer;
			this.neuron = pos.neuron;
			this.biasValue = pos.biasValue;
			this.biasWeight = pos.biasWeight;
		}
		public Position(int pLayer, int pNeuron, Double pBiasValue, Double pBiasWeight) {
			layer = pLayer;
			neuron = pNeuron;
			biasValue = pBiasValue;
			biasWeight = pBiasWeight;
		}
	}
	
	/**
	 * 
	 * @author sreram
	 *
	 */
	public static class Connection {
		public Position from;
		public Position to;
		public Double weight;
		
		/**
		 * 
		 * @param pFrom
		 * @param pTo
		 */
		public void setConnection(Position pFrom, Position pTo, Double pWeight){
			from  = pFrom;
			to = pTo;
			weight  = pWeight;
		}
		
		
	}
	
	/**
	 * This holds the data that represents the design of the neural network system.
	 * @author sreram
	 *
	 */
	public static class NeuralNetworkArcheitecturalData{
		
		public static enum NetworkType{
			layered, complex
		}
		public Integer numberOfNeurons;
		
		// The first and the last layers are input and output layers respectively
		public ArrayList<Integer> layerSize;
		public ArrayList<Activation> activationFunctions;
		public ArrayList<Connection> connections;
		public NetworkType networkType;
		
	}
				
	
	public NeuralNetwork getNeuralNetwork() {
		return newNetwork;
	}
	
	public NeuralNetworkArcheitecturalData getNetworkArchitecture(){
		return networkArchitectureData;
	}
	
	/**
	 * This method creates a neural network system and returns it. The information 
	 * required for constructing the neural network system, will be encoded within 
	 * this object. 
	 * @return
	 */
	public NeuronLayerSet createNetwork(){
		NeuronLayerSet neuronGraph = new NeuronLayerSet();
		neuronGraph.biasNeurons = new ArrayList<ArrayList<Neuron>>();
		
		for(int i = 0; i < networkArchitectureData.layerSize.size(); i++){
			neuronGraph.biasNeurons.add(new ArrayList<Neuron>());
			for(int j = 0; j < networkArchitectureData.layerSize.get(i); j++){
				neuronGraph.biasNeurons.get(i).add(null);
			}
		}
		
		constructor = new ConstructNetwork (
				networkArchitectureData.activationFunctions,
				new ErrorFunction(),
				networkArchitectureData.layerSize, 
				((networkArchitectureData.networkType  == NeuralNetworkArcheitecturalData.NetworkType.layered) ?
						ConstructNetwork.NetworkResultComputationType.quick : 
							ConstructNetwork.NetworkResultComputationType.treeTraversal)
				);
		
		neuronGraph.netLayers = constructor.NNetwork.networkData.getNetLayers();
		Map<Neuron, Boolean> checkBias = new HashMap<Neuron, Boolean>();
		
		for(Connection connection : networkArchitectureData.connections){
			// it is possible for this to be null, if the connecting neurons do not belong 
			// to any of these neurons in the current network, but rather when it refers to a 
			// foreign network. 
			if(connection.to != null){
				constructor.NNetwork.networkData.connect(
						neuronGraph.netLayers.get(connection.from.layer).get(connection.from.neuron),
						neuronGraph.netLayers.get(connection.to.layer).get(connection.to.neuron), 
						connection.weight);
				
				if(checkBias.get(neuronGraph.netLayers.get(connection.from.layer).get(connection.from.neuron)) 
						== null){
					
					Neuron temp = new Neuron();
					
					temp.outputResult = connection.from.biasValue;
					
					constructor.NNetwork.networkData.connect(temp,
							neuronGraph.netLayers.get(connection.from.layer).get(connection.from.neuron),
							connection.from.biasWeight);
					neuronGraph.biasNeurons.get(connection.from.layer).set(connection.from.neuron, temp);
					checkBias.put(neuronGraph.netLayers.get(connection.from.layer).get(connection.from.neuron),
							new Boolean(true));
				}
			}
		}
		return neuronGraph;
	}
	
	/**
	 * This observes the network and records its architecture. This method 
	 * obtains the input as ArrayList<ArrayList<Neuron>> and finds the 
	 * connection between each neuron in the list with every other possible neuron
	 * and finds the NeuralNetworkArcheitecturalData for it. 
	 * 
	 * If it visits child neurons of other foreign networks (if the current 
	 * network is connected to a totally different network partially) 
	 * for such a connection the 'to' value is set to null. 
	 * @param netLayers structural definition of the network. 
	 */
	public void findArchitecture(NeuronLayerSet neuronGraph){
		
		// The Integer (key) represents the neuron index and the Position represents 
		// its position in the netLayers arrayList. 
		Map<Integer, Position> addressedIndex = new HashMap<Integer, Position>();
		networkArchitectureData.connections = new ArrayList<Connection>();
		networkArchitectureData.activationFunctions = new ArrayList<Activation>();
		networkArchitectureData.layerSize = new ArrayList<Integer>();
		Connection temp = new Connection();
		
		
		// find the position of each neuron (and record the number of neurons)
		// also set the activation functions
		int k = 0;
		for(int i = 0; i < neuronGraph.netLayers.size(); i++) {
			for(int j = 0; j < neuronGraph.netLayers.get(i).size(); j++, k++){
				addressedIndex.put(neuronGraph.netLayers.get(i).get(j).getNeuronIndex(), 
								new Position(i, j, // the position of the neuron
										neuronGraph.biasNeurons.get(i).get(j).outputResult, // bias value
										neuronGraph.biasNeurons.get(i).get(j).weightValues. 
										get(neuronGraph.netLayers.get(i).get(j).getNeuronIndex())));
				// the bias neuron has only one weight-link, and that will be the neuron in the 
				// arrayAddress i,j. Such a neuron will have its neuron index as,
				// neuronGraph.netLayers.get(i).get(j).getNeuronIndex()
				networkArchitectureData.activationFunctions.add(neuronGraph.netLayers.get(i).get(j).activation);
			}
		}
		networkArchitectureData.numberOfNeurons = k;
		
		// record the connections that are made
		// both the from and to links will contain the represented neuron's 
		// bias weight value and bias value. 
		Neuron parentNeuron, childNeuron;
		for(int i = 0; i < neuronGraph.netLayers.size()-1; i++){
			for(int j = 0; j < neuronGraph.netLayers.get(i).size(); j++){
				
				parentNeuron = neuronGraph.netLayers.get(i).get(j);
				
				for(int l = 0; l < parentNeuron.childNeurons.size(); l++){
					temp = new Connection();
					childNeuron = parentNeuron.childNeurons.get(l);
					
					temp.from = new Position(addressedIndex.get(parentNeuron.getNeuronIndex()));
					temp.to = new Position(addressedIndex.get(childNeuron.getNeuronIndex()));
					temp.weight = parentNeuron.weightValues.get(childNeuron.getNeuronIndex());
					
					networkArchitectureData.connections.add(temp);
				}
			}
		}
		
		// set layer size. 
		for(int i = 0; i < neuronGraph.netLayers.size(); i++){
			networkArchitectureData.layerSize.add(neuronGraph.netLayers.get(i).size());
		}
		
		
	}
	
	public void findArchitecture(){
		NeuronLayerSet neuronGraph = new NeuronLayerSet();
		neuronGraph.netLayers = currentNetwork.networkData.getNetLayers();
		neuronGraph.biasNeurons = currentNetwork.networkData.biasNeurons;
		findArchitecture(neuronGraph);
	}
	
	/**
	 *  This method discovers the kind of architecture the neural system has. 
	 * @param neuralNetwork -  the neural network class that is to be copied
	 */
	public void discoverTheArchitecture(NeuralNetwork neuralNetwork ){
		NeuronLayerSet neuronLayerSet = new NeuronLayerSet();
		neuronLayerSet.netLayers = neuralNetwork.networkData.getNetLayers();
		neuronLayerSet.biasNeurons = neuralNetwork.networkData.getBiasNeurons();
		findArchitecture(neuronLayerSet);
		learningRate = neuralNetwork.networkData.learningRate;
		momentum = neuralNetwork.networkData.momentum;
		constructStatus = true;
	}
	
	
	public NeuralNetwork getCopyOfTheNeuralNetwork(){
		NeuralNetwork result = new NeuralNetwork(new ErrorFunction());
		NeuronLayerSet neuronLayerSetNew = new NeuronLayerSet();

		neuronLayerSetNew = createNetwork(); // create a copy of the previous network
		
		result.networkData.setNetLayers(neuronLayerSetNew.netLayers);
		result.networkData.setBiasNeurons(neuronLayerSetNew.biasNeurons);
		
		// initialize input and output neurons. Note that when the architecture is getting 
		// constructed, it does not take the layer abstraction into consideration
		result.networkData.inputNeurons = result.networkData.getNetLayers().get(0);
		result.networkData.outputNeurons = result.networkData.getNetLayers().get(
												result.networkData.getNetLayers().size() -1);
		result.networkData.hiddenLayers = new ArrayList<ArrayList<Neuron>>();
		
		
		// copies the hidden layers
		for(int i = 1; i < neuronLayerSetNew.netLayers.size() -1; i++){
			result.networkData.hiddenLayers.add(result.networkData.getNetLayers().get(i));
		}
		
		// this sets the other parameters 
		result.networkData.learningRate = learningRate;
		result.networkData.momentum = momentum;
		result.constructStatus = constructStatus;
		newNetwork = result;
		return result;
	}
	
	/**
	 * this method copies an entire neural network data-set and 
	 * creates a totally new one.  
	 * @param neuralNetwork - this is the old neural network system
	 * @return NeuralNetwork the copied neural network system gets returned. 
	 * @throws Exception 
	 */
	public NeuralNetwork copyNeuralNetwork(NeuralNetwork neuralNetwork) throws Exception{
		currentNetwork = neuralNetwork;
		return copyNeuralNetwork();
	}
	
	public void setNeuralNetwork(NeuralNetwork pCurNetwork){
		currentNetwork = pCurNetwork;
	}
	
	public NeuralNetwork copyNeuralNetwork () throws Exception{
		if(currentNetwork == null){
			Exception e = new Exception ("Error: neural network graph is not initialized. " 
									+"Try calling method "
									+ "neuralNetwork.NeuralNetworkArchitecture.setNeuralNetwork"
									+ "Before calling the method "
									+ "neuralNetwork.NeuralNetworkArchitecture.copyNeuralNetwork");
			throw e;
		}
		discoverTheArchitecture(currentNetwork);
		return getCopyOfTheNeuralNetwork();
	}
	
	
	/**
	 * This method is used to obtain the list of all weight values as pairs for `neuronFrom` 
	 * and `neuronTo` and stores them into the ArrayList Variable: changableWeightParameterList. 
	 * This variable is of type ArrayList<\ChangableWeightParameters\>. This value is returned 
	 * after storing all the possible combinations of neuron_pairs. Each neuron_pair contains a 
	 * from neuron's and a to neuron's address.
	 * 
	 * The weight values of the output neurons are added prior to other neurons and the bias neurons 
	 * are added at the end. 
	 * 
	 * @return returns the list of pairs of  neurons depicting each individual possible connection.  
	 * @throws Exception
	 */
	public  ArrayList<ChangableWeightParameters> getWeightParametersAsAListOfNeuronPair() throws Exception{
		if(currentNetwork == null){
			Exception e = new Exception ("Error: neural network graph is not initialized. " 
								+"Try calling method "
								+ "neuralNetwork.NeuralNetworkArchitecture.setNeuralNetwork"
								+ "Before calling the method "
								+ "neuralNetwork.NeuralNetworkArchitecture.getWeightParametersAsAListOfNeuronPair");
			throw e;
		}
		
		changableWeightParameterList = new ArrayList<ChangableWeightParameters>();
		
		Neuron neuronFrom, neuronTo;
		
		// The loop follows in reverse order because, the weight values connecting the 
		// output neurons are stored at the end of the connection list. 
		for(int i = networkArchitectureData.connections.size() -1; i >= 0; i--){
			neuronFrom = currentNetwork.networkData.getNeuron(
							networkArchitectureData.connections.get(i).from.layer,
							networkArchitectureData.connections.get(i).from.neuron);
			neuronTo =  currentNetwork.networkData.getNeuron(
							networkArchitectureData.connections.get(i).to.layer,
							networkArchitectureData.connections.get(i).to.neuron);
			
			changableWeightParameterList.add(new ChangableWeightParameters(neuronFrom, neuronTo));
		}
		
		// record the changeable weight parameter list for bias neurons. 
		for(int i = 0; i < currentNetwork.networkData.getBiasNeurons().size(); i++){
			for(int j = 0; j < currentNetwork.networkData.getBiasNeurons().get(i).size(); j++){
				neuronFrom = currentNetwork.networkData.biasNeurons.get(i).get(j);
				neuronTo =  currentNetwork.networkData.getNeuron(i, j);
				changableWeightParameterList.add(new ChangableWeightParameters(neuronFrom, neuronTo));				
			}
		}
		
		return changableWeightParameterList;
	}
	
	/**
	 * This method splits the list containing the set of all possible neuron connection (which 
	 * includes the bias neurons) into two parts by sizes pair1 and pair2. Now this is then 
	 * separately stored in the variable `pairOfWeightParameterList` which is then returned 
	 * to the parent method. 
	 * 
	 * This method must be called only after the method getWeightParametersAsAListOfNeuronPair
	 * is executed successfully. Else, the list of pair of connecting edges (with the 
	 * neurons as vertices ) will be uninitialized.  
	 * 
	 * pair1 + pair2 must be equal to the size of the list of, the set of connecting nodes(neurons). 
	 * @param pair1 - the size for the first split up. 
	 * @param pair2 - the size for the second split up. 
	 * @return this returns the new list containing the separated pairs. 
	 */
	public ChangableWeightParameterListPair splitChangabeWeightParameterListPair(int pair1, int pair2){
		
		
		// changableWeightParameterList.size() must be equal to pair1 + pair2;
		
		int iterateSize = 2*((pair1 > pair2) ? pair2 : pair1);
		int remainingSize = ((pair1 > pair2) ? (pair1 - pair2) : (pair2 - pair1));
		
		pairOfWeightParameterList = new ChangableWeightParameterListPair(); 
		ChangableWeightParameters temp;
		for(int i = 0; i < iterateSize; i++){
			temp = new ChangableWeightParameters(changableWeightParameterList.get(i));
			if(i%2 == 0)	{
				pairOfWeightParameterList.changableWeightParameterListX.add(temp);
			} else {
				pairOfWeightParameterList.changableWeightParameterListY.add(temp);
			}
				
		}
		
		ArrayList<ChangableWeightParameters> longerWeightParaList = 
				((pair1 > pair2)? pairOfWeightParameterList.changableWeightParameterListX:
					pairOfWeightParameterList.changableWeightParameterListY);
		
		for(int i = iterateSize/2; i < remainingSize; i++){
			longerWeightParaList.add(changableWeightParameterList.get(i));
		}
		
		return pairOfWeightParameterList;
	}
	
	
	/**
	 * This method splits the edges equally among two ArrayLists. 
	 * @param priority - In case the number of edges is an odd number, either one of the two
	 * 			lists must take an extra edge. The priority variable denotes which takes the extra
	 * 			number
	 * @return returns the list set. 
	 */
	public ChangableWeightParameterListPair splitChangabeWeightParameterListPairEqually
														(Priority priority){
		int size = changableWeightParameterList.size();
		int sizeX, sizeY;
		sizeX = sizeY = size/2;
		if(size % 2 != 0){
			if(priority.equals(Priority.X_PRIORITY)){
				sizeX += 1; 
			} else {
				sizeY += 1;
			}
		}
		return splitChangabeWeightParameterListPair(sizeX, sizeY);
	}
}
