/*
* copyright (c) 2016 K Sreram, All rights reserved
*/
package mainFnc;

import neuralNetwork.*;
import java.util.*;


class ActFunction implements Activation {

	@Override
	public Double activation(Double inp) {
		return Math.tanh(inp.doubleValue());
		/*
		 * if(inp.doubleValue() > -100 && inp.doubleValue() < 100) return inp;
		 * else if(inp.doubleValue() < -100) return (double) -100; else return
		 * (double) 100;
		 */

	}

}

class ActFunction2 implements Activation {

	@Override
	public Double activation(Double inp) {
		/*
		 * if(inp.doubleValue() > -100 && inp.doubleValue() < 100) return inp;
		 * else if(inp.doubleValue() < -100) return (double) -100; else return
		 * (double) 100;
		 */
		return inp.doubleValue();
	}
}


public class MainFnc {
	
	public static void main(String [] arg) throws Exception{
		NeuralNetworkInterface neuralNetwork = new NeuralNetworkInterface();
		neuralNetwork.addSizeList(1);
		neuralNetwork.addSizeList(3);
		neuralNetwork.addSizeList(1, true);
		
		neuralNetwork.setActivationFunction(new ActFunction(), new ActFunction2());
		neuralNetwork.setWeightValues(0.0, 1.0);
		neuralNetwork.setBiasWeightValues(-1.0, 1.0, 1.0);
		neuralNetwork.setNetworkOutputComputationToQuick();
		neuralNetwork.setWeightCap(-1.0, 1.0);
		
		neuralNetwork.setLearningRateMomentum(0.1, 0.9);
		neuralNetwork.setMaxMinError(10.0, 0.0);
		
		for(int i = 0; i < 1000; i++)
		neuralNetwork.setTrainingDataSet(new ArrayList<Double> (Arrays.asList((double)i/1000)),
				new ArrayList<Double> (Arrays.asList((double)i*i/1000000)));
		if(neuralNetwork.setTrainingDataSet(new ArrayList<Double> (Arrays.asList((double)1)),
				new ArrayList<Double> (Arrays.asList(((double)1))), true)) {
			System.out.println("initialization successful");
		}
		else System.out.println("initialization failed");
		Double errVal;
		for(int i = 0; i < 1000 ; i++)	{
			errVal = neuralNetwork.runTrainingSystem(new Double(2), false);
			System.out.println(i + " Error = " + errVal );
			//if(errVal.doubleValue() < .000001) break;
		}
		ArrayList<Double> ress;
		for(int i = 1000; i < 1200; i++) {
			ress = neuralNetwork.networkResult(new ArrayList<Double> (Arrays.asList((double)i/1000)));
			System.out.println("Output = " + ress.get(0) + " Expected output = "+ ((double)i*i/1000000) );
		}
		
		System.out.println("Least recorded error = " + neuralNetwork.getLeastRecordedError());
	}
}
