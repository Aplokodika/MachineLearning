/*
 * copyright (c) 2016 K Sreram, All rights reserved
 */
package mainFnc;
import neuralNetwork.activationFunctions.*;
import neuralNetwork.graphicalAnalysis.SolutionSpace2DGrey;
import neuralNetwork.ioHandler.WeightIOHandler;
import neuralNetwork.neuralInterface.NeuralNetworkInterface;
import neuralNetwork.NeuralNetworkArchitecture.ChangableWeightParameters;
import neuralNetwork.graphicalAnalysis.SolutionSpace2DGrey.ChangableWeightParametersPair; 
import java.util.*;

public class MainFnc {

	public static void main(String[] arg) throws Exception {

		NeuralNetworkInterface neuralNetwork = new NeuralNetworkInterface();
		WeightIOHandler weightIoHandle = new WeightIOHandler(neuralNetwork);

		/// Depicts the neural network structure.
		neuralNetwork.addSizeList(1); // the input neuron-count
		neuralNetwork.addSizeList(3);
		neuralNetwork.addSizeList(2);

		neuralNetwork.addSizeList(1, true); // output neuron

		neuralNetwork.setActivationFunction(new TanhFunction(), new LinearUnmodified());
		neuralNetwork.setWeightValues(-1.0, 1.0);
		neuralNetwork.setBiasWeightValues(-1., 1., 1.0);
		neuralNetwork.setNetworkOutputComputationToQuick();
		neuralNetwork.setWeightCap(-1.0, 1.0);

		neuralNetwork.setLearningRateMomentum(0.01, 0.1);

		for(int i = 1 ; i < 100; i++){
			double k = (1 - 2*Math.random());
			neuralNetwork.setTrainingDataSet(new ArrayList<Double>(Arrays.asList(k)),
					new ArrayList<Double>(Arrays.asList(Math.sin(k*2*Math.PI))), true);
		}
		 //neuralNetwork.runTrainingSystem(false);
		 //neuralNetwork.connect(new NeuronAddress(2, 1), new
		 //NeuronAddress(1,1), 0.5);
		 //neuralNetwork.connect(new NeuronAddress(2, 0), new
		 //NeuronAddress(1,0), 0.5);
		//neuralNetwork.connect(new NeuronAddress(2, 2), new
		//NeuronAddress(1,2), 0.5);

		ArrayList<Double> ress;
		/*for (double l = 0.0; l < 1; l += 0.1)
			for (double m = 0.0; m < 1; m += 0.1) {
				ress = neuralNetwork.networkResult(new ArrayList<Double>(Arrays.asList(l, m)));
				for (int i = 0; i < ress.size(); i++) {
					System.out.println("result = " + ress.get(i));
				}

				ress = neuralNetwork.networkResult(new ArrayList<Double>(Arrays.asList(l, m)));
				for (int i = 0; i < ress.size(); i++) {
					System.out.println("2. result = " + ress.get(i));
				}
				System.out.println("\n");
				ress = neuralNetwork.networkResult(new ArrayList<Double>(Arrays.asList(l, m)));
				for (int i = 0; i < ress.size(); i++) {
					System.out.println("3. result = " + ress.get(i));
				}

				System.out.println("\n");
				ress = neuralNetwork.networkResult(new ArrayList<Double>(Arrays.asList(l, m)));
				for (int i = 0; i < ress.size(); i++) {
					System.out.println("4. result = " + ress.get(i));
				}

			}*/
		SolutionSpace2DGrey graph = new SolutionSpace2DGrey();
		
		
		weightIoHandle.setFileName("outdata.dat");
		//weightIoHandle.read();

		graph
			.setFileName("graphs/graph.png")
			.setImageFormat("png")
			.setTrainingDataSet(neuralNetwork.dataSet)
			.setNeuralNetwork(neuralNetwork.getNeuralTrainer().NNetwork);

		ChangableWeightParameters temp1 = new 
				ChangableWeightParameters(graph.getNeuron(1, 0),graph.getNeuron(2, 0)); 
		
		ChangableWeightParameters temp2 = new 
				ChangableWeightParameters(graph.getNeuron(1, 1), graph.getNeuron(2, 0));
		
		ChangableWeightParametersPair tempPair = 
				new ChangableWeightParametersPair(temp1, temp2);
		


		for (Integer ll = new Integer(0); ll.intValue() < 1000; ll = new Integer(ll.intValue() + 1)) {
				neuralNetwork.runTrainingSystem(false);
			
				// System.out.println(" Cur Error = " +
				// neuralNetwork.computeError(false));
		// neuralNetwork.setLoggedWeightValues();
		// System.out.println(" Cur Error = " +
		// neuralNetwork.computeError(false));

		}		
		// neuralNetwork.setLoggedWeightValues();
		for(int i = 0; i < 100; i++ ){
			double k = (1 - 2*Math.random());
			ress = neuralNetwork.networkResult(new ArrayList<Double>(Arrays.asList(k)));
				System.out.println("->result = " + ress.get(0) + " Expected output = " + Math.sin(k*2*Math.PI));
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
		.setIncrementalRate(1.0e-3)
		.setGraphSize(200, 200)
		.setChangableParameters(tempPair)
		.setDefaultMidPosition();
		
		graph
			.setError2D()
			.convertError2DtoImage(3)
			.writeGraphToFile()
			.setFileName("graphs/graph.png");
		System.out.println("Done");
	}
}

