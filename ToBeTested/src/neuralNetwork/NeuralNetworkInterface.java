/*
 * Author: K Sreram. 
 * copyright (c) 2016 K Sreram, all rights reserved.
 */
package neuralNetwork;

import java.util.ArrayList;



class TrainingDataSet{
	public ArrayList<ArrayList<Double>> dataSetInputs = new ArrayList<ArrayList<Double>>();
	public ArrayList<ArrayList<Double>> dataSetOutputs = new ArrayList<ArrayList<Double>>();
	
	public void addDataSet(ArrayList<Double> input, ArrayList<Double> output){
		dataSetInputs.add(input);
		dataSetOutputs.add(output);
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
	
	private Double commonLearningRate = null;
	private Double commonMomentum = null;
	
	
	private Integer abstLayerInputs = null;
	private Integer abstLayerOutputs = null;
	
	
	private TrainingDataSet dataSet;
	private boolean isDataSetSet = false;
	
	private ArrayList<Integer> sizeList = new ArrayList<Integer>();
	private boolean isSizeListSet = false;
	
	private int noOfWeightValues;
	private int noOfNeurons;
	private boolean isNumberOfWeightsNeuronsSet = false;
	
	
	private ArrayList<Activation> activationFunctions = new ArrayList<Activation>();
	private boolean isActivationFunctionSet = false;
	
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
	 * From here, the initialization region begins. 
	 * 
	 * 
	 */

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
	 * 
	 * @param startRange
	 * @param endRange
	 * @return
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
	 * 
	 * @param biasValues
	 * @param biasWeights
	 */
	private void setBiasValuesInNetwork(){
		Neuron temp;
		int layerCount = constructNet.NNetwork.networkData.getNoOfLayers();
		for(int i = 0; i < layerCount; i++){
			for(int j = 0; j < constructNet.NNetwork.networkData.getNoOfNeuronsInLayer(i); j++){
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
		
		return (areLearningRateMomentumSet &&
				isNetworkAssignedToNeuralTrainer &&
				areWeightValuesInitializedIntoNetwork &&
				isNetworkConstructed &&
				isNumberOfWeightsNeuronsSet &&
				isDataSetSet &&
				areBiasValuesInitializedIntoNetwork);
	}
	
	
	/*************************************************************************************
	 * Initialization region ends here. 
	 * */
}
