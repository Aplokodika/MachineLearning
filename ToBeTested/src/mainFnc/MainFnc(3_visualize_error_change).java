/*
 * Author: K Sreram. 
 * copyright (c) 2016 K Sreram, all rights reserved.
 */
package mainFnc;

import neuralNetwork.*;
import java.util.*;

class ActFunction implements Activation {

	@Override
	public Double activation(Double inp) {
		return Math.tanh(inp.doubleValue());
		/*if(inp.doubleValue() > -100 && inp.doubleValue() < 100)
			return inp;
		else if(inp.doubleValue() < -100)
			return (double) -100;
		else 
			return (double) 100;*/
			
	}

}

class ActFunction2 implements Activation {

	@Override
	public Double activation(Double inp) {
		/*if(inp.doubleValue() > -100 && inp.doubleValue() < 100)
			return inp;
		else if(inp.doubleValue() < -100)
			return (double) -100;
		else 
			return (double) 100;*/
		return inp.doubleValue();
	}
}

class ErrorFunction implements ComputeError {
	
	

	@Override
	public Double computeError(ArrayList<Double> expectedOut, ArrayList<Double> obtainedOut){
 		Double result = new Double((double)0);
 		
 		Double  val;
		for (int i = 0; i < expectedOut.size(); i++) {
			val = expectedOut.get(i).doubleValue() - obtainedOut.get(i).doubleValue();
			result = result.doubleValue() + val.doubleValue()*val.doubleValue();
			
		}
		
		return result;
	}

}

public class MainFnc {
	public static void main(String[] arg) throws Exception {
		ArrayList<Activation> inp = new ArrayList<Activation>();
		ArrayList<Integer> sizeList = new ArrayList<Integer>();

		sizeList.add(2);
		//sizeList.add(10);
		//sizeList.add(5);
		sizeList.add(10);
		sizeList.add(10);

		sizeList.add(2);
		int noOfWeightValues = ConstructNetwork.weightPairSize(sizeList);
		int noOfNeurons = ConstructNetwork.noOfNeurons(sizeList);

		for (int i = 0; i < noOfNeurons-2; i++) {
			//if (i % 2 == 0)
				inp.add(new ActFunction());
			//else
				//inp.add(new ActFunction2());
		}
		
		inp.add(new ActFunction2());
		inp.add(new ActFunction2());

		ConstructNetwork neuralNet;

		neuralNet = new ConstructNetwork(inp, new ErrorFunction(), sizeList);
		ArrayList<Double> weightValues = new ArrayList<Double>();
		WeightPair pair = new WeightPair();

		for (int i = 0; i < noOfWeightValues; i++) {
			weightValues.add((double) 0);
		}
		pair.weight = weightValues;

		neuralNet.constructNetworkLayered(sizeList, pair);
		ArrayList<Double> inputs = new ArrayList<Double>();
		
		
		for (int i = 0; i < neuralNet.NNetwork.networkData.inputNeurons.size(); i++) {
			inputs.add((double).2);
		}
		neuralNet.NNetwork.networkData.setInput(inputs);
		neuralNet.NNetwork.computeNetworkResult(0, NeuralNetwork.MoveOrder.moveForward);
		
		
		NeuralTrainer neuralTrainer = new NeuralTrainer();
		
		ArrayList<Double> expectedOutput = new ArrayList<Double>();
		expectedOutput.add((double).1);
		expectedOutput.add((double).2);
		neuralTrainer.setNNetwork(neuralNet.NNetwork);
		neuralTrainer.NNetwork.
		networkData.setCommonLearningRateMomentum(sizeList, (double).01, (double).9);
		
		Double error;
		
		for(int i = 0; i <1000; i++) {
			for (int j = 0; j < neuralNet.NNetwork.networkData.inputNeurons.size(); j++) {
				inputs.set(j, (double) .2);
			}
			neuralNet.NNetwork.networkData.setInput(inputs);
			expectedOutput.set(0,(double).999);
			expectedOutput.set(1,(double) .999);
			error = neuralTrainer.trainNetwork(expectedOutput);
			System.out.println(i + " :: "+error);
			for(int j = 0; j < neuralNet.NNetwork.networkData.inputNeurons.size(); j++) {
				inputs.set(j, (double) .1);
			}
		
			neuralNet.NNetwork.networkData.setInput(inputs);
			expectedOutput.set(0,(double) .211);
			expectedOutput.set(1,(double) .211);
			error = neuralTrainer.trainNetwork(expectedOutput);
			System.out.println(i + " :: "+error);
			
			for(int j = 0; j < neuralNet.NNetwork.networkData.inputNeurons.size(); j++) {
				inputs.set(j, (double) .3);
			}
		
			neuralNet.NNetwork.networkData.setInput(inputs);
			expectedOutput.set(0,(double) .01);
			expectedOutput.set(1,(double) .01);
			error = neuralTrainer.trainNetwork(expectedOutput);
			System.out.println(i + " :: "+error);
		}
		
		
		
		
		
		
		
		
		
		/*for(int i = 0; i <1000; i++) {
			for (int j = 0; j < neuralNet.NNetwork.networkData.inputNeurons.size(); j++) {
				inputs.set(j, (double) 5);
			}
			neuralNet.NNetwork.networkData.setInput(inputs);
			expectedOutput.set(0,(double) 10);
			expectedOutput.set(1,(double) 20);
			neuralTrainer.trainNetwork(expectedOutput);
			
		}
		
		for(int i = 0; i <1000; i++) {
			for(int j = 0; j < neuralNet.NNetwork.networkData.inputNeurons.size(); j++) {
				inputs.set(j, (double) 1);
			}
			
		
			neuralNet.NNetwork.networkData.setInput(inputs);
			expectedOutput.set(0,(double) 600);
			expectedOutput.set(1,(double) 600);
			neuralTrainer.trainNetwork(expectedOutput);
		}*/
		
		
		for (int j = 0; j < neuralNet.NNetwork.networkData.inputNeurons.size(); j++) {
			inputs.set(j, (double) .2);
		}
		neuralNet.NNetwork.networkData.setInput(inputs);

		neuralNet.NNetwork.computeNetworkResult(0, NeuralNetwork.MoveOrder.moveForward);

		for (int i = 0; i < neuralNet.NNetwork.networkData.outputNeurons.size(); i++) {
			System.out.println(i + "  Out :"
					+neuralNet.NNetwork.networkData.outputNeurons.get(i).outputResult);
		}
		
		//for(int k = 0; k < 1000; k++) {

			for (int j = 0; j < neuralNet.NNetwork.networkData.inputNeurons.size(); j++) {
				inputs.set(j, (double) .1);
			}
			neuralNet.NNetwork.networkData.setInput(inputs);

			neuralNet.NNetwork.computeNetworkResult(0, NeuralNetwork.MoveOrder.moveForward);

			for (int i = 0; i < neuralNet.NNetwork.networkData.outputNeurons.size(); i++) {
				System.out.println( i + "  Out :"
						+ neuralNet.NNetwork.networkData.outputNeurons.get(i).outputResult  );
			}
			
			for (int j = 0; j < neuralNet.NNetwork.networkData.inputNeurons.size(); j++) {
				inputs.set(j, (double) .3);
			}
			neuralNet.NNetwork.networkData.setInput(inputs);

			neuralNet.NNetwork.computeNetworkResult(0, NeuralNetwork.MoveOrder.moveForward);

			for (int i = 0; i < neuralNet.NNetwork.networkData.outputNeurons.size(); i++) {
				System.out.println( i + "  Out :"
						+ neuralNet.NNetwork.networkData.outputNeurons.get(i).outputResult  );
			}
		//}
		System.out.println("******************************* Weight Values ***************************");
		
		for(int i = 0; i < neuralNet.NNetwork.networkData.inputNeurons.size(); i++){
			for(Map.Entry entry :
				neuralNet.NNetwork.networkData.inputNeurons.get(i).weightValues.entrySet()){
				System.out.println(entry.getValue());
			}
		}
		
		for(int i = 0; i < neuralNet.NNetwork.networkData.hiddenLayers.size(); i++){
			for(int j = 0; j < neuralNet.NNetwork.networkData.hiddenLayers.get(i).size(); j++){
				for(Map.Entry entry :
					neuralNet.NNetwork.networkData.hiddenLayers.get(i).get(j).weightValues.entrySet()){
					System.out.println(entry.getValue());
				}
			}
		}
		
		for(int i = 0; i < neuralNet.NNetwork.networkData.outputNeurons.size(); i++){
			for(Map.Entry entry :
					neuralNet.NNetwork.networkData.outputNeurons.get(i).weightValues.entrySet()){
				System.out.println(entry.getValue());
			}
		}
		
		
		System.out.println("Done");
		
	}
}
