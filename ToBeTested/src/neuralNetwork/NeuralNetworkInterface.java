/*
 * Author: K Sreram. 
 * copyright (c) 2016 K Sreram, all rights reserved.
 */
package neuralNetwork;

import java.util.ArrayList;



class TrainingDataSet{
	private ArrayList<ArrayList<Double>> dataSetInputs = new ArrayList<ArrayList<Double>>();
	private ArrayList<ArrayList<Double>> dataSetOutputs = new ArrayList<ArrayList<Double>>();
	
	public void addDataSet(ArrayList<Double> input, ArrayList<Double> output){
		getDataSetInputs().add(input);
		getDataSetOutputs().add(output);
	}

	public ArrayList<ArrayList<Double>> getDataSetInputs() {
		return dataSetInputs;
	}

	
	public ArrayList<ArrayList<Double>> getDataSetOutputs() {
		return dataSetOutputs;
	}
	
	public int size(){
		return dataSetInputs.size(); // both inputs and outputs have the same size
	}

	
}



/**
 * This class encapsulates the neural network system written in this package. 
 * */
public class NeuralNetworkInterface {
	public static class ErrorFunction implements ComputeError {

		@Override
		public Double computeError(ArrayList<Double> expectedOut, ArrayList<Double> obtainedOut) {
			Double result = new Double((double) 0);

			Double val;
			for (int i = 0; i < expectedOut.size(); i++) {
				val = expectedOut.get(i).doubleValue() - obtainedOut.get(i).doubleValue();
				result = result.doubleValue() + val.doubleValue() * val.doubleValue();

			}

			return result;
		}

	}
	
	private boolean isSystemReady = false;
	
	private Double commonLearningRate = null;
	private Double commonMomentum = null;
	
	
	private Integer abstLayerInputs = null;
	private Integer abstLayerOutputs = null;
	
	
	private TrainingDataSet dataSet = new TrainingDataSet();
	private boolean isDataSetSet = false;
	
	private ArrayList<Integer> sizeList = new ArrayList<Integer>();
	private boolean isSizeListSet = false;
	
	private int noOfWeightValues;
	private int noOfNeurons;
	private boolean isNumberOfWeightsNeuronsSet = false;
	
	
	private ArrayList<Activation> activationFunctions = new ArrayList<Activation>();
	private boolean isActivationFunctionSet = false;
		// this is for initializing 
		// the output neurons with the same kind of activation functions and for 
		// initializing the hidden layers with another kind of activation function.
		private Activation outputActivationFunction = null; 
		private Activation hiddenActivationFucntion = null;
	
	
	private ConstructNetwork constructNet;
	private boolean areWeightValuesInitializedIntoNetwork = false;
	private boolean isNetworkConstructed = false;
	private boolean areLearningRateMomentumSet = false;
	private boolean areBiasValuesInitializedIntoNetwork = false;
	
	private NeuralTrainer neuralTrainer = new NeuralTrainer();
	private boolean isNetworkAssignedToNeuralTrainer = false;
	
	private ArrayList<Double> weightValues = new ArrayList<Double>();
	private boolean areWeightValuesSet = false;
	
	
	
	private ArrayList<ArrayList<Double>> biasWeights = new ArrayList<ArrayList<Double>>();
	private ArrayList<ArrayList<Double>> biasValues = new ArrayList<ArrayList<Double>>();
	private boolean areBiasWeightValuesSet = false;
	
	
	/**
	 * This method directly initializes the learning rate and momentum values. And 
	 * embeds them into the system. 
	 *  
	 * @param lRate
	 * @param momentum
	 * @return
	 * @throws Exception
	 */
	public boolean  setLearningRateMomentum(Double lRate, Double momentum) throws Exception{
		commonLearningRate = lRate;
		commonMomentum = momentum;
		return initializeSystem();
	}
	
	private void initializeActivationFunctions() throws Exception {
		if(isSizeListSet == false)	{
			Exception e = new Exception("Error: must initialize sizeList first ");
			throw e;
		}
		for(int i = 0; i < noOfNeurons - sizeList.get(sizeList.size()-1); i++)
			activationFunctions.add(hiddenActivationFucntion);
		for(int i = 0; i < sizeList.get(sizeList.size()-1); i++)
			activationFunctions.add(outputActivationFunction);
		isActivationFunctionSet = true;
		
	}
	
	public boolean  setActivationFunction(Activation hiddenFnc, Activation outputFnc) throws Exception{
		hiddenActivationFucntion = hiddenFnc;
		outputActivationFunction = outputFnc;
		return initializeSystem();
	}
	
	/**
	 * From here, the initialization region begins. 
	 * 
	 * 
	 */
	
	/**
	 * 
	 * @param inputLayerAbst
	 * @param outputLayerAbst
	 */
	public void setAbstraction(Integer inputLayerAbst, Integer outputLayerAbst) {
		abstLayerInputs = inputLayerAbst;
		abstLayerOutputs = outputLayerAbst;
	}
	/**
	 * 
	 * @param sizeLst
	 * @return
	 * @throws Exception
	 */
	public boolean setSizeList( ArrayList<Integer> sizeLst) throws Exception{
		if(isSizeListSet == true){
			Exception e = new Exception("Error in setSizeList: isSizeListSet"
					+ " is false. SizeList has already been initialized");
			throw e;
		}
		sizeList = sizeLst;
		isSizeListSet = true;
		return initializeSystem();
	}
	/**
	 * 
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public boolean addSizeList(Integer size) throws Exception{
		return addSizeList(size, false); // this return is default
	}
	
	/**
	 * 
	 * @param size
	 * @param isLast
	 * @return
	 * @throws Exception
	 */
	public boolean addSizeList(Integer size, boolean isLast) throws Exception{
		sizeList.add(size);
		
		if(isLast) {
			isSizeListSet = true;
			abstLayerInputs = new Integer(0);
			abstLayerOutputs = new Integer(sizeList.size() - 1);
			return initializeSystem();
		}
		else return false;
	}
	
	
	
	
	
	/**
	 * 
	 * @param weights
	 * @return
	 * @throws Exception
	 */
	public boolean setWeightValues(ArrayList<Double> weights) throws Exception{
		
		initializeSystem();
		
		if(noOfWeightValues == weights.size())
			weightValues = weights;
		areWeightValuesSet = true;
		return initializeSystem();
	}
	/**
	 * 
	 * @param biasWeight
	 * @param biasValue
	 * @return
	 * @throws Exception
	 */
	public boolean setBiasWeightValues(ArrayList<ArrayList<Double>> biasWeight, 
									ArrayList<ArrayList<Double>> biasValue ) throws Exception{
		
		initializeSystem();
		
		if(noOfNeurons == biasWeight.size()) {
			biasWeights = biasWeight;
			biasValues = biasValue;
		}
		areBiasWeightValuesSet = true;
		return initializeSystem();
	}
	
	/**
	 * 
	 * @param startRange
	 * @param endRange
	 * @return
	 */
	private Double randomValue(Double startRange, Double endRange){
		return (startRange.doubleValue() + 
				Math.random() * (endRange.doubleValue() - startRange.doubleValue())); 
	}
	
	
	/**
	 * This method sets the weight values randomly. The inputed rage determines 
	 * the range within which the random values must vary. 
	 * 
	 * @param startRange the lower limit of the weight values that are to be assigned randomly.
	 * @param endRange  the upper limit of the weight values that are to be assigned randomly. 
	 * @return returns true if the system is initialized. 
	 * @throws Exception
	 */
	public boolean setWeightValues(Double startRange, Double endRange) throws Exception{
		
		initializeSystem();
		if(isNumberOfWeightsNeuronsSet == true){
			for(int i = 0; i < noOfWeightValues; i++)
				weightValues.add(randomValue(startRange, endRange));
		}	else {
			Exception e = new Exception("Error in setWeightValues: isNumberOfWeightsNeuronsSet"
					+ " is false. Possible fix: call setSizeList() before this method");
			throw e;
		}
		areWeightValuesSet = true;
		return initializeSystem();
	}
	
	/**
	 * 
	 * @param startRange
	 * @param endRange
	 * @param biasValue
	 * @return
	 * @throws Exception
	 */
	public boolean setBiasWeightValues(Double startRange, Double endRange,
													Double biasValue) throws Exception{
		initializeSystem();
		if(isSizeListSet == true){
			int layerCount = sizeList.size();
			for(int i = 0; i < layerCount; i++){
				biasWeights.add(i, new ArrayList<Double>());
				biasValues.add(i, new ArrayList<Double>());
				for(int j = 0; j < sizeList.get(i); j++){

				biasWeights.get(i).add(randomValue(startRange, endRange));
				biasValues.get(i).add(biasValue);
				}
			}
		}	else {
			Exception e = new Exception("Error in setBiasWeightValues: "
					+ " isSizeListSet is false. "
					+ "sizeList is not initialized. "
					+ "possible fix call setSizeList() before this method");
			throw e;
		}
		areBiasWeightValuesSet = true;
		return initializeSystem();
	}
	
	
	
	/**
	 * This method initializes the bias weight values and the bias input values. 
	 * this method gets called by the method `initialize`, to construct the bias 
	 * system. The bias system contains a particular bias neuron for each neuron
	 * in the system. This is done to maximize flexibility. 
	 */
	private void setBiasValuesInNetwork(){
		Neuron temp;
		int layerCount = constructNet.NNetwork.networkData.getNoOfLayers();
		for(int i = 0; i < layerCount; i++){
			for(int j = 0; j < constructNet.NNetwork.networkData.getNoOfNeuronsInLayer(i); j++){
				
				// <!> This code can be replaced with the method connectWith defined in the 
			    // class Neuron. 
				
				temp = constructNet.NNetwork.networkData.getNeuron(i, j);
				Neuron newNeuron = new Neuron();
				newNeuron.input = newNeuron.outputResult = biasValues.get(i).get(j);
				newNeuron.childNeurons.add(temp);
				newNeuron.weightValues.put(temp.getNeuronIndex(), biasWeights.get(i).get(j));
				temp = constructNet.NNetwork.networkData.getNeuron(i, j);
				temp.parentNeurons.add(newNeuron);
			}
		}
	}
	/**
	 * This is the initialization method. This initialization method helps in 
	 * initializing the system based on the past information it already has. 
	 * Depending on what variables are already set and what variables need to be
	 * set, this method decided which initialization method needs to be called
	 * after setting each variable. 
	 * 
	 * @return true when the network system is ready for training
	 * @throws Exception
	 */
	private boolean initializeSystem() throws Exception{
		if(isNumberOfWeightsNeuronsSet == false && isSizeListSet == true){
			noOfWeightValues =  ConstructNetwork.weightPairSize(sizeList);
			noOfNeurons = ConstructNetwork.noOfNeurons(sizeList);
			isNumberOfWeightsNeuronsSet = true;
		}
		
		if(isActivationFunctionSet == false && outputActivationFunction != null &&
				hiddenActivationFucntion != null) {
			initializeActivationFunctions();
			isActivationFunctionSet = true;
		}
		
		if(isNetworkConstructed == false && isActivationFunctionSet == true &&
				isSizeListSet == true ){
			constructNet = 
						new ConstructNetwork(activationFunctions,
								new NeuralNetworkInterface.ErrorFunction(), sizeList);
			isNetworkConstructed = true;	
		}
		
		if(areWeightValuesInitializedIntoNetwork == false && isNetworkConstructed == true && 
				areWeightValuesSet == true ){
			WeightPair pair = new WeightPair();
			pair.weight = weightValues;
			constructNet.constructNetworkLayered(sizeList, pair);
			areWeightValuesInitializedIntoNetwork = true;
		}
		
		if(areBiasValuesInitializedIntoNetwork == false && areBiasWeightValuesSet == true &&
				isNetworkConstructed == true){
			setBiasValuesInNetwork();
			areBiasValuesInitializedIntoNetwork = true;
		}
		
		if(isNetworkAssignedToNeuralTrainer == false && isNetworkConstructed == true){
			neuralTrainer.setNNetwork(constructNet.NNetwork);
			isNetworkAssignedToNeuralTrainer = true;
		}
		
		if(areLearningRateMomentumSet == false && isNetworkConstructed == true &&
				commonLearningRate != null && commonMomentum != null ){
			constructNet.NNetwork.
				networkData.setCommonLearningRateMomentum(sizeList, commonLearningRate,
						commonMomentum);
			areLearningRateMomentumSet = true;
		}
		
		isSystemReady = (areLearningRateMomentumSet &&
				isNetworkAssignedToNeuralTrainer &&
				areWeightValuesInitializedIntoNetwork &&
				isNetworkConstructed &&
				isNumberOfWeightsNeuronsSet &&
				isDataSetSet &&
				areBiasValuesInitializedIntoNetwork);
		return isSystemReady;
	}
	
	public ArrayList<String> getUnInitializedValues(){
		ArrayList<String> result = new ArrayList<String>();
		if(areLearningRateMomentumSet == false)		result.add("areLearningRateMomentumSet");
		if(isNetworkAssignedToNeuralTrainer == false)	result.add("isNetworkAssignedToNeuralTrainer");
		if(areWeightValuesInitializedIntoNetwork == false)	result.add("areWeightValuesInitializedIntoNetwork");
		if(isNetworkConstructed == false)	result.add("isNetworkConstructed");
		if(isNumberOfWeightsNeuronsSet == false)	result.add("isNumberOfWeightsNeuronsSet");
		if(isDataSetSet == false)	result.add("isDataSetSet");
		if(areBiasValuesInitializedIntoNetwork == false) result.add("areBiasValuesInitializedIntoNetwork");
		
		if(result.size() == 0)
			return null;
		else return result;
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
	 */
	public double runTrainingSystem(boolean setAbst) throws Exception{
		double error = 0.0; 
	
		for(int i = 0; i < dataSet.size(); i++){
			
			if(setAbst && (abstLayerInputs.intValue() == 0))
				neuralTrainer.NNetwork.setAbstraction(abstLayerInputs, abstLayerOutputs);
			else if(setAbst){
				neuralTrainer.NNetwork.setAbstraction(0, abstLayerOutputs);
				neuralTrainer.NNetwork.networkData.setInput(dataSet.getDataSetInputs().get(i));
				neuralTrainer.NNetwork.computeNetworkResult(0);
				neuralTrainer.NNetwork.setAbstraction(abstLayerInputs, abstLayerOutputs);
				error += neuralTrainer.trainNetwork(dataSet.getDataSetOutputs().get(i));
				continue;
			}
			
			neuralTrainer.NNetwork.networkData.setInput(dataSet.getDataSetInputs().get(i));
			error += neuralTrainer.trainNetwork(dataSet.getDataSetOutputs().get(i));
		}
		
		error = error/dataSet.size();
		return error;
	}
	
	/**
	 * This method simply evaluates the final result of the ANN system. 
	 * 
	 * @param systemInput this is the input that must be given to the system. 
	 * @throws Exception 
	 */
	public ArrayList<Double> networkResult (ArrayList <Double> systemInput) throws Exception {
		neuralTrainer.NNetwork.networkData.setInput(systemInput);
		neuralTrainer.NNetwork.computeNetworkResult(0);
		return NLayerToArray.obtainLayerOutputInArray(neuralTrainer.NNetwork.networkData.outputNeurons);
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
	
}
