/*
 * Author: K Sreram. 
 * copyright (c) 2016 K Sreram, all rights reserved.
 */
package mainFnc;

import neuralNetwork.*;
import java.util.*;

class ActFunction implements Activation {

	@Override
	public Float activation(Float inp) {
		return inp.floatValue();
	}

}

class ActFunction2 implements Activation {

	@Override
	public Float activation(Float inp) {
		return inp.floatValue();
	}

}

class ErrorFunction implements ComputeError {
	
	

	@Override
	public Float computeError(ArrayList<Float> expectedOut, ArrayList<Float> obtainedOut) {
		float result = new Float(0);
		for (int i = 0; i < expectedOut.size(); i++) {
			result += (expectedOut.get(i).floatValue() - obtainedOut.get(i).floatValue())*
					(expectedOut.get(i).floatValue() - obtainedOut.get(i).floatValue());
		}
		return result;
	}

}

public class MainFnc {
	public static void main(String[] arg) throws Exception {
		ArrayList<Activation> inp = new ArrayList<Activation>();
		ArrayList<Integer> sizeList = new ArrayList<Integer>();

		sizeList.add(2);
		sizeList.add(3);
		sizeList.add(4);
		sizeList.add(2);
		sizeList.add(1);
		sizeList.add(2);
		int noOfWeightValues = ConstructNetwork.weightPairSize(sizeList);
		int noOfNeurons = ConstructNetwork.noOfNeurons(sizeList);

		for (int i = 0; i < noOfNeurons; i++) {
			if (i % 2 == 0)
				inp.add(new ActFunction());
			else
				inp.add(new ActFunction2());
		}

		ConstructNetwork neuralNet;

		neuralNet = new ConstructNetwork(inp, new ErrorFunction(), sizeList);
		ArrayList<Float> weightValues = new ArrayList<Float>();
		WeightPair pair = new WeightPair();

		for (int i = 0; i < noOfWeightValues; i++) {
			weightValues.add((float) .001);
		}
		pair.weight = weightValues;

		neuralNet.constructNetworkLayered(sizeList, pair);
		ArrayList<Float> inputs = new ArrayList<Float>();
		for (int i = 0; i < neuralNet.NNetwork.networkData.inputNeurons.size(); i++) {
			inputs.add((float) 0.0001);
		}
		neuralNet.NNetwork.networkData.setInput(inputs);
		neuralNet.NNetwork.computeNetworkResult(0, NeuralNetwork.MoveOrder.moveForward);
		
		
		NeuralTrainer neuralTrainer = new NeuralTrainer();
		
		ArrayList<Float> expectedOutput = new ArrayList<Float>();
		expectedOutput.add((float).009);
		expectedOutput.add((float).008);
		neuralTrainer.setNNetwork(neuralNet.NNetwork);
		neuralTrainer.NNetwork.networkData.setCommonLearningRateMomentum(sizeList, (float).0002, (float).9);
		for(int i = 0; i < 100; i++)
		neuralTrainer.trainNetwork(expectedOutput);
		
		for (int i = 0; i < neuralNet.NNetwork.networkData.outputNeurons.size(); i++) {
			System.out.println(neuralNet.NNetwork.networkData.outputNeurons.get(i).outputResult);
		}

		System.out.println("Done");
		
	}

}
