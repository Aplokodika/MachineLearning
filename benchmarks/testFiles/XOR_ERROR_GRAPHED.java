/*
 * copyright (c) 2016 K Sreram, All rights reserved
 */
package mainFnc;

import neuralNetwork.activationFunctions.*;
import neuralNetwork.graphicalAnalysis.ErrorGraph;
import neuralNetwork.ioHandler.WeightIOHandler;
import neuralNetwork.neuralInterface.NeuralNetworkInterface;

import java.awt.Color;
import java.util.*;

public class MainFnc {

	public static void main(String[] arg) throws Exception {

		NeuralNetworkInterface neuralNetwork = new NeuralNetworkInterface();
		WeightIOHandler weightIoHandle = new WeightIOHandler(neuralNetwork);

		/// Depicts the neural network structure.
		neuralNetwork.addSizeList(2); // the input neuron-count
		neuralNetwork.addSizeList(2);

		neuralNetwork.addSizeList(1, true); // output neuron

		neuralNetwork.setActivationFunction(new TanhFunction(), new LinearUnmodified());
		neuralNetwork.setWeightValues(-1.0, 1.0);
		neuralNetwork.setBiasWeightValues(-1., 1., 1.0);
		neuralNetwork.setNetworkOutputComputationToQuick();
		neuralNetwork.setWeightCap(-1.0, 1.0);

		neuralNetwork.setLearningRateMomentum(0.3, 0.5);

		// for(int i = 0 ; i < 1000; i++)
		neuralNetwork.setTrainingDataSet(new ArrayList<Double>(Arrays.asList(1., 1.)),
				new ArrayList<Double>(Arrays.asList(0.)));

		// for(int i = 0 ; i < 1000; i++)
		neuralNetwork.setTrainingDataSet(new ArrayList<Double>(Arrays.asList(1., 0.0)),
				new ArrayList<Double>(Arrays.asList(1.0)));

		// for(int i = 0 ; i < 1000; i++)
		neuralNetwork.setTrainingDataSet(new ArrayList<Double>(Arrays.asList(0.0, 1.)),
				new ArrayList<Double>(Arrays.asList(1.0)));
		// for(int i = 0 ; i < 5; i++)
		// neuralNetwork.setTrainingDataSet(new
		// ArrayList<Double>(Arrays.asList(0.0, 0.0)),
		// new ArrayList<Double>(Arrays.asList(0.)));

		neuralNetwork.setTrainingDataSet(new ArrayList<Double>(Arrays.asList(0.0, 0.0)),
				new ArrayList<Double>(Arrays.asList(0.)), true);

		// neuralNetwork.runTrainingSystem(false);
		// neuralNetwork.connect(new NeuronAddress(2, 1), new
		// NeuronAddress(1,1), 0.5);
		// neuralNetwork.connect(new NeuronAddress(2, 0), new
		// NeuronAddress(1,0), 0.5);
		// neuralNetwork.connect(new NeuronAddress(2, 2), new
		// NeuronAddress(1,2), 0.5);

		ArrayList<Double> ress;
		/*
		 * for (double l = 0.0; l < 1; l += 0.1) for (double m = 0.0; m < 1; m
		 * += 0.1) { ress = neuralNetwork.networkResult(new
		 * ArrayList<Double>(Arrays.asList(l, m))); for (int i = 0; i <
		 * ress.size(); i++) { System.out.println("result = " + ress.get(i)); }
		 * 
		 * ress = neuralNetwork.networkResult(new
		 * ArrayList<Double>(Arrays.asList(l, m))); for (int i = 0; i <
		 * ress.size(); i++) { System.out.println("2. result = " + ress.get(i));
		 * } System.out.println("\n"); ress = neuralNetwork.networkResult(new
		 * ArrayList<Double>(Arrays.asList(l, m))); for (int i = 0; i <
		 * ress.size(); i++) { System.out.println("3. result = " + ress.get(i));
		 * }
		 * 
		 * System.out.println("\n"); ress = neuralNetwork.networkResult(new
		 * ArrayList<Double>(Arrays.asList(l, m))); for (int i = 0; i <
		 * ress.size(); i++) { System.out.println("4. result = " + ress.get(i));
		 * }
		 * 
		 * }
		 */

		ErrorGraph graph = new ErrorGraph();

		weightIoHandle.setFileName("outdata.dat");
		// weightIoHandle.read();
		graph
			.setFileName("graphs/graph.png")
			.setImageFormat("png")
			.setTrainingDataSet(neuralNetwork.dataSet)
			.setNeuralNetwork(neuralNetwork.getNeuralTrainer().NNetwork);
		
		
		

		for (Integer ll = new Integer(0); ll.intValue() < 1000; ll = new Integer(ll.intValue() + 1)) {
				
			graph.captureError();
			neuralNetwork.runTrainingSystem(false);
			if(ll.doubleValue() % 100 == 0) {
				System.out.println(ll);
			}
			// neuralNetwork.setLoggedWeightValues();
			// System.out.println(" Cur Error = " +
			// neuralNetwork.computeError(false));

		}
		// neuralNetwork.setLoggedWeightValues();

		ress = neuralNetwork.networkResult(new ArrayList<Double>(Arrays.asList(1., 1.)));
		for (int i = 0; i < ress.size(); i++) {
			System.out.println("->result = " + ress.get(i));
		}

		ress = neuralNetwork.networkResult(new ArrayList<Double>(Arrays.asList(1., 0.0)));
		for (int i = 0; i < ress.size(); i++) {
			System.out.println("->2. result = " + ress.get(i));
		}
		System.out.println("\n");
		ress = neuralNetwork.networkResult(new ArrayList<Double>(Arrays.asList(0.0, 1.)));
		for (int i = 0; i < ress.size(); i++) {
			System.out.println("->3. result = " + ress.get(i));
		}

		System.out.println("\n");
		ress = neuralNetwork.networkResult(new ArrayList<Double>(Arrays.asList(0.0, 0.0)));
		for (int i = 0; i < ress.size(); i++) {
			System.out.println("->4. result = " + ress.get(i));
		}

		System.out.println(" Cur Error = " + neuralNetwork.computeError(false));

		// Scanner scan = new Scanner(System.in);
		// char by;
		// System.out.println("Do you want to write to file? (y/n)");
		// by = scan.next().charAt(0);
		// if(by == 'y'){
		// System.out.println("Writing to file");
		weightIoHandle.write();
		// }
		graph
			 .setDuelColourForErrorIncrementAndDecrement(Color.GREEN, Color.YELLOW)
			 .createGraph(1000, 50, Color.GREEN,Color.RED, 10, 10)
			 .writeGraphToFile();
		
		System.out.println("Done");
	}
}
