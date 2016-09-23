package neuralNetwork.graphicalAnalysis;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import neuralNetwork.ConstructiveTrainer;
import neuralNetwork.NeuralNetwork;
import neuralNetwork.NeuralNetworkArchitecture;
import neuralNetwork.NeuralTrainer;
import neuralNetwork.Neuron;
import neuralNetwork.neuralInterface.TrainingDataSet;

public class AnalysisGraph {
	protected BufferedImage finalGraph = null;
	protected TrainingDataSet dataSet = null;
	protected NeuralNetwork neuralNetwork = null;
	protected NeuralTrainer neuralTrainer = new ConstructiveTrainer();
	protected NeuralNetworkArchitecture nnArchitecture = new NeuralNetworkArchitecture();
	protected Double maxError = null, minError = null;

	private String imgFormat;
	private String fileName;
	
	public AnalysisGraph setFileName(String fName) {
		fileName = fName;
		return this;
	}
	
	public AnalysisGraph setImageFormat(String imgF){
		imgFormat = imgF;
		return this;
	}
	
	public AnalysisGraph setTrainingDataSet(TrainingDataSet pDataSet) {
		dataSet = pDataSet;
		return this;
	}
	
	public AnalysisGraph writeGraphToFile(){
		File outputfile = new File(fileName);
	    
		try {
			ImageIO.write(finalGraph, imgFormat, outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public AnalysisGraph setCopyOfNeuralNetwork(NeuralNetwork pNeuralNetwork) throws Exception{
		neuralNetwork = nnArchitecture.copyNeuralNetwork(pNeuralNetwork);
		neuralTrainer.NNetwork = neuralNetwork;
		return this;
	}
	
	
	public AnalysisGraph setNeuralNetwork(NeuralNetwork pNeuralNetwork){
		neuralNetwork = pNeuralNetwork;
		neuralTrainer.NNetwork = neuralNetwork;
		return this;
	}
	
	public Neuron getNeuron(int layer, int neuron){
		return neuralTrainer.NNetwork.networkData.getNetLayers().get(layer).get(neuron);
	}
	
	protected Double findErrorAndUpdateMinMaxError(){
		Double error =  dataSet.computeError(neuralTrainer);
		
		if(maxError == null ){
			maxError = error;
			if(minError == null) {
				minError = error;
			}
		}
		
		if(error.doubleValue() > maxError.doubleValue() )
			maxError = error;
		if(error.doubleValue() < minError.doubleValue())
			minError = error;
		return error;
	}
	
}
