/*
 * copyright (c) 2016 K Sreram, All rights reserved
 */
package mainFnc;

import neuralNetwork.*;
import neuralNetwork.activationFunctions.SinFunction;
import neuralNetwork.neuralInterface.NeuralNetworkInterface;
import neuralNetwork.activationFunctions.LinearUnmodified;
import java.util.*;

public class MainFnc {

	public static void main(String[] arg) throws Exception {
		NeuralNetworkInterface neuralNetwork = new NeuralNetworkInterface();

		/// Depicts the neural network structure.
		neuralNetwork.addSizeList(3); // the input neuron-count
		neuralNetwork.addSizeList(4); // the hidden layer 1 with 3 neurons
		// neuralNetwork.addSizeList(10);
		// neuralNetwork.addSizeList(3);

		neuralNetwork.addSizeList(3, true); // output neuron

		neuralNetwork.setActivationFunction(new SinFunction(), new LinearUnmodified());
		neuralNetwork.setWeightValues(-1., 1.);
		neuralNetwork.setBiasWeightValues(-.1, .1, 1.0);
		neuralNetwork.setNetworkOutputComputationToQuick();
		neuralNetwork.setWeightCap(-1.0, 1.0);

		neuralNetwork.setLearningRateMomentum(0.5, 0.999);
		Double errVal;
		
		neuralNetwork.setTrainingDataSet(new ArrayList<Double>(Arrays.asList(0.5, 0.4, 0.3)),
				new ArrayList<Double>(Arrays.asList(0.9, 0.001, 0.002)));
		neuralNetwork.setTrainingDataSet(new ArrayList<Double>(Arrays.asList(0.2, 0.2, 0.2)),
				new ArrayList<Double>(Arrays.asList(0.9, 0.9, 0.9)));
		neuralNetwork.setTrainingDataSet(new ArrayList<Double>(Arrays.asList(0.1, 0.1, 0.1)),
				new ArrayList<Double>(Arrays.asList(0.9, 1.0, 0.8)), true);

		neuralNetwork.runTrainingSystem(false);
		neuralNetwork.connect(new NeuronAddress(1, 1), new NeuronAddress(1,1), 1.0);
		for (int ll = 0; ll < 1000; ll++) {
			for (int i = 0; i < 1000; i++) {
				errVal = neuralNetwork.runTrainingSystem( false);
				System.out.println(ll + " Error = " + errVal);
			}
			neuralNetwork.setLoggedWeightValues();
		}
		//neuralNetwork.setLoggedWeightValues();

		ArrayList<Double> ress;
		double percentCal;
		int count = 0;

		ress = neuralNetwork.networkResult(new ArrayList<Double>(Arrays.asList(0.5, 0.4, 0.3)));
		for (int i = 0; i < ress.size(); i++) {
			System.out.println("result = " + ress.get(i));
		}

		ress = neuralNetwork.networkResult(new ArrayList<Double>(Arrays.asList(0.5, 0.4, 0.3)));
		for (int i = 0; i < ress.size(); i++) {
			System.out.println("2. result = " + ress.get(i));
		}
		System.out.println(" Cur Error = " + neuralNetwork.computeError(false));
		percentCal = (double) count / 1000 * 100;
		System.out.println("correct percentage :" + percentCal + "%");
		System.out.println("Least recorded error = " + neuralNetwork.getLeastRecordedError());
	}
}