/*
 * Copyright (c) 2016 K Sreram, All rights reserved
 */
package neuralNetwork.neuralInterface;
import neuralNetwork.*;

import java.util.ArrayList;

import neuralNetwork.activationFunctions.Activation;

public class TriggerInterface {
	protected boolean isSystemReady = false;
	
	protected Double commonLearningRate = null;
	protected Double commonMomentum = null;
	
	
	protected Integer abstLayerInputs = null;
	protected Integer abstLayerOutputs = null;
	
	
	public TrainingDataSet dataSet = new TrainingDataSet(this);
	protected boolean isDataSetSet = false;
	
	protected ArrayList<Integer> sizeList = new ArrayList<Integer>();
	protected boolean isSizeListSet = false;
	
	protected int noOfWeightValues;
	protected int noOfNeurons;
	protected boolean isNumberOfWeightsNeuronsSet = false;
	
	
	protected ArrayList<Activation> activationFunctions = new ArrayList<Activation>();
	protected boolean isActivationFunctionSet = false;
		// this is for initializing 
		// the output neurons with the same kind of activation functions and for 
		// initializing the hidden layers with another kind of activation function.
		protected Activation outputActivationFunction = null; 
		protected Activation hiddenActivationFucntion = null;
	
	
	protected ConstructNetwork constructNet;
	protected boolean areWeightValuesInitializedIntoNetwork = false;
	protected boolean isNetworkConstructed = false;
	protected boolean areLearningRateMomentumSet = false;
	protected boolean areBiasValuesInitializedIntoNetwork = false;
	
	protected NeuralTrainer neuralTrainer = new ConstructiveTrainer();
	protected boolean isNetworkAssignedToNeuralTrainer = false;
	
	protected ArrayList<Double> weightValues = new ArrayList<Double>();
	protected boolean areWeightValuesSet = false;
	
	
	
	protected ArrayList<ArrayList<Double>> biasWeights = new ArrayList<ArrayList<Double>>();
	protected ArrayList<ArrayList<Double>> biasValues = new ArrayList<ArrayList<Double>>();
	protected boolean areBiasWeightValuesSet = false;
	
	
	protected boolean isTypeOfOutputEvaluationSet = false;
	protected ConstructNetwork.NetworkResultComputationType typeOfEvaluationFunction; 	
	
	protected ArrayList<ArrayList<Neuron> > biasNeurons = new ArrayList<ArrayList <Neuron>>();
	protected ArrayList< Double > weightLog = new ArrayList<Double>();
	

	public boolean setNetworkOutputComputationToTreeTraversal() throws Exception {
		typeOfEvaluationFunction = ConstructNetwork.NetworkResultComputationType.treeTraversal;
		isTypeOfOutputEvaluationSet = true;
		return initializeSystem();
	}
	
	public boolean setNetworkOutputComputationToQuick() throws Exception {
		typeOfEvaluationFunction = ConstructNetwork.NetworkResultComputationType.quick;
		isTypeOfOutputEvaluationSet = true;
		
		return initializeSystem();
	}
	
	
	public NeuralTrainer getNeuralTrainer() {
		return neuralTrainer;
	}
	
	/**
	 * This method directly initializes the learning rate and momentum values. And 
	 * embeds them into the system. 
	 *  
	 * @param lRate
	 * @param momentum
	 * @return
	 * @throws Exception
	 */
	public boolean  initializeLearningRateMomentum(Double lRate, Double momentum) throws Exception{
		commonLearningRate = lRate;
		commonMomentum = momentum;

		return initializeSystem();
	}
	
	
	public void  setLearningRateMomentum(Double lRate, Double momentum) throws Exception{
		neuralTrainer.NNetwork.networkData.setLearningRateMomentum(lRate, momentum);
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
	 * This is for initial initialization. This cannot be used for changing the weight values dynamically. 
	 * For initializing dynamically, call: setWeightValuesDynimic(ArrayList<Double>)
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
				Math.random() * (endRange.doubleValue() - startRange.doubleValue())) + startRange.doubleValue(); 
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
				biasWeights.add(new ArrayList<Double>());
				biasValues.add(new ArrayList<Double>());
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
			biasNeurons.add(new ArrayList<Neuron>());
			for(int j = 0; j < constructNet.NNetwork.networkData.getNoOfNeuronsInLayer(i); j++){
				
				// <!> This code can be replaced with the method connectWith defined in the 
			    // class Neuron. 
				
				temp = constructNet.NNetwork.networkData.getNeuron(i, j);
				Neuron newNeuron = new Neuron();
				newNeuron.input = newNeuron.outputResult = biasValues.get(i).get(j);
				newNeuron.childNeurons.add(temp);
				newNeuron.weightValues.put(temp.getNeuronIndex(), biasWeights.get(i).get(j));
				biasNeurons.get(i).add(newNeuron);
				temp = constructNet.NNetwork.networkData.getNeuron(i, j);
				temp.parentNeurons.add(newNeuron);
			}
		}
		constructNet.NNetwork.networkData.biasNeurons = biasNeurons;
	}
	
	public ArrayList<ArrayList<Neuron>> getBiasNeurons(){
		return biasNeurons;
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
	protected boolean initializeSystem() throws Exception{
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
				isSizeListSet == true && isTypeOfOutputEvaluationSet == true){
			constructNet = 
						new ConstructNetwork(activationFunctions,
								new ErrorFunction(), sizeList,
								typeOfEvaluationFunction);
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
				networkData.setLearningRateMomentum(commonLearningRate,
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
		if(areLearningRateMomentumSet == false)		
			result.add("areLearningRateMomentumSet");
		if(isNetworkAssignedToNeuralTrainer == false)	
			result.add("isNetworkAssignedToNeuralTrainer");
		if(areWeightValuesInitializedIntoNetwork == false)	
			result.add("areWeightValuesInitializedIntoNetwork");
		if(isNetworkConstructed == false)	
			result.add("isNetworkConstructed");
		if(isNumberOfWeightsNeuronsSet == false)	
			result.add("isNumberOfWeightsNeuronsSet");
		if(isDataSetSet == false)	
			result.add("isDataSetSet");
		if(areBiasValuesInitializedIntoNetwork == false) 
			result.add("areBiasValuesInitializedIntoNetwork");
		if(result.size() == 0)
			return null;
		else return result;
	}
	
	/**
	 * This method simply evaluates the final result of the ANN system. 
	 * 
	 * @param systemInput this is the input that must be given to the system. 
	 * @throws Exception 
	 */
	public ArrayList<Double> networkResult (ArrayList <Double> systemInput) {
		neuralTrainer.NNetwork.networkData.setInput(systemInput);
		neuralTrainer.NNetwork.computeNetworkResult(0);
		return NLayerToArray.obtainLayerOutputInArray(neuralTrainer.NNetwork.networkData.outputNeurons);
	}
	public ArrayList<Neuron> getOutputLayer() {
		return this.neuralTrainer.NNetwork.networkData.outputNeurons;
	}
	
}
