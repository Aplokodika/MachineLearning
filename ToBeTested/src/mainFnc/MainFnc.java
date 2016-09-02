/*
 * copyright (c) 2016 K Sreram, All rights reserved
 */
package mainFnc;

import neuralNetwork.*;
import java.util.*;

/**
 * Activation function for the hidden layers
 * 
 * @author Sreram
 *
 */
class ActFunction implements Activation {

	@Override
	public Double activation(Double inp) {
		return Math.tanh(inp.doubleValue());

	}

}

/**
 * Activation function for the output neurons
 * 
 * @author Sreram
 *
 */

class ActFunction2 implements Activation {

	@Override
	public Double activation(Double inp) {
		return inp.doubleValue();
	}
}

public class MainFnc {

	public static void main(String[] arg) throws Exception {
		NeuralNetworkInterface neuralNetwork = new NeuralNetworkInterface();

		/// Depicts the neural network structure.
		neuralNetwork.addSizeList(3); // the input neuron-count
		neuralNetwork.addSizeList(4); // the hidden layer 1 with 3 neurons
		// neuralNetwork.addSizeList(10);
		// neuralNetwork.addSizeList(3);

		neuralNetwork.addSizeList(3, true); // output neuron

		neuralNetwork.setActivationFunction(new ActFunction(), new ActFunction2());
		neuralNetwork.setWeightValues(-.5, .5);
		neuralNetwork.setBiasWeightValues(-.1, .1, 1.0);
		neuralNetwork.setNetworkOutputComputationToQuick();
		neuralNetwork.setWeightCap(-1.0, 1.0);

		neuralNetwork.setLearningRateMomentum(0.1, 0.9999);
		neuralNetwork.setMaxMinError(10.0, 0.0);

		double k;
		Double errVal;
		/*
		 * if(neuralNetwork.setTrainingDataSet(new ArrayList<Double>
		 * (Arrays.asList((double)100)), new ArrayList<Double>
		 * (Arrays.asList(Math.cos(1))), true)) {
		 * System.out.println("initialization successful"); } else
		 * System.out.println("initialization failed");
		 */
		/*
		 * for(int i = 0; i < 1000; i++) { k = Math.random()*Math.PI*1000;
		 * neuralNetwork.setTrainingDataSet(new ArrayList<Double>
		 * (Arrays.asList((double)k/10000)), new ArrayList<Double>
		 * (Arrays.asList( ((Math.cos(k/100) > 0)? 1.0: -1.0) ))); }
		 */
		neuralNetwork.setTrainingDataSet(new ArrayList<Double>(Arrays.asList(0.5, 0.4, 0.3)),
				new ArrayList<Double>(Arrays.asList(0.9, 0.001, 0.002)));
		neuralNetwork.setTrainingDataSet(new ArrayList<Double>(Arrays.asList(0.2, 0.2, 0.2)),
				new ArrayList<Double>(Arrays.asList(0.9, 0.9, 0.9)));
		neuralNetwork.setTrainingDataSet(new ArrayList<Double>(Arrays.asList(0.1, 0.1, 0.1)),
				new ArrayList<Double>(Arrays.asList(0.9, 1.0, 0.8)), true);

		neuralNetwork.runTrainingSystem(new Double(20), false);
		 neuralNetwork.connect(new NeuronAddress(2, 1), new NeuronAddress(1,1), 1.0);
		for (int ll = 0; ll < 1000; ll++) {
			for (int i = 0; i < 1000; i++) {
				errVal = neuralNetwork.runTrainingSystem(new Double(0.9), false);
				System.out.println(i + " Error = " + errVal);
				// if(errVal.doubleValue() < .000001) break;
			}
			neuralNetwork.setLoggedWeightValues();
		}
		neuralNetwork.setLoggedWeightValues();

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