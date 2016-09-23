/*
 * copyright (c) 2016 K Sreram, All rights reserved
 */
package mainFnc;

import neuralNetwork.activationFunctions.*;
import neuralNetwork.graphicalAnalysis.ErrorGraph;
import neuralNetwork.ioHandler.WeightIOHandler;
import neuralNetwork.neuralInterface.NeuralNetworkInterface;

import java.awt.Color;
import java.awt.Font;
import java.util.*;

import javax.swing.text.AttributeSet.ColorAttribute;

public class MainFnc {

	public static void main(String[] arg) throws Exception {

		NeuralNetworkInterface neuralNetwork = new NeuralNetworkInterface();
		WeightIOHandler weightIoHandle = new WeightIOHandler(neuralNetwork);

		/// Depicts the neural network structure.
		neuralNetwork.addSizeList(1); // the input neuron-count
		neuralNetwork.addSizeList(3);
		neuralNetwork.addSizeList(3);

		neuralNetwork.addSizeList(1, true); // output neuron

		neuralNetwork.setActivationFunction(new TanhFunction(), new LinearUnmodified());
		neuralNetwork.setWeightValues(-1.0, 1.0);
		neuralNetwork.setBiasWeightValues(-1., 1., 1.0);
		neuralNetwork.setNetworkOutputComputationToQuick();
		neuralNetwork.setWeightCap(-1.0, 1.0);

		neuralNetwork.setLearningRateMomentum(0.01, 0.1);

		for(int i = 1 ; i < 1000; i++){
			double k = (1 - 2*Math.random());
			neuralNetwork.setTrainingDataSet(new ArrayList<Double>(Arrays.asList(k)),
					new ArrayList<Double>(Arrays.asList(Math.sin(k*2*Math.PI))), true);
		}

		ArrayList<Double> ress;
		weightIoHandle.setFileName("outdata.dat");

		ErrorGraph graph = new ErrorGraph();
		graph
			.setFileName("graph.bmp")
			.setImageFormat("bmp")
			.setTrainingDataSet(neuralNetwork.dataSet)
			.setNeuralNetwork(neuralNetwork.getNeuralTrainer().NNetwork);
			
		graph.setMinError(1.0e-4);
		
		for (int ll = 0; ll < 1; ll++) {
			for (int i = 0; i < 1000; i++) {
				neuralNetwork.runTrainingSystem(false);
				graph.captureError();
				System.out.println(" Cur Error = " + neuralNetwork.computeError(false));
			}

		}
		for(int i = 0; i < 100; i++ ){
			double k = (1 - 2*Math.random());
			ress = neuralNetwork.networkResult(new ArrayList<Double>(Arrays.asList(k)));
				System.out.println("->result = " + ress.get(0) + " Expected output = " + Math.sin(k*2*Math.PI));
		}

		System.out.println(" Cur Error = " + neuralNetwork.computeError(false));

		
		weightIoHandle.write();
		graph
		 	.setDuelColourForErrorIncrementAndDecrement(Color.GREEN, Color.YELLOW)
		 	.createGraph(1000, 200, Color.GREEN,Color.RED, 20, 10, new Font("Times New Roman", Font.BOLD, 100), 
		 					Color.GRAY, 10, 20)
		 	.writeGraphToFile();
		System.out.println("done");
	}
}