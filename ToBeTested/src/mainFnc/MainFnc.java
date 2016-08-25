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
		neuralNetwork.addSizeList(3);
		neuralNetwork.addSizeList(5);
		neuralNetwork.addSizeList(3);
		neuralNetwork.addSizeList(5);
		neuralNetwork.addSizeList(3, true);
		
		
		/// <!> Activation function initialization does not work when this
		/// is called before addSizeList. Need to fix it 
		neuralNetwork.setActivationFunction(new ActFunction (), new ActFunction2 ());
		
		neuralNetwork.setWeightValues((double)0, (double)2);
		neuralNetwork.setBiasWeightValues((double)0, (double) 2, (double) 1);
		
		
		
		neuralNetwork.setTrainingDataSet(new ArrayList<Double>(Arrays.asList(1.0,2.0,3.0)) ,
				new ArrayList<Double>(Arrays.asList(2.1,3.1,5.0)));
		
		neuralNetwork.setTrainingDataSet(new ArrayList<Double>(Arrays.asList(2.0,2.0,5.0)) ,
				new ArrayList<Double>(Arrays.asList(0.01,1.0,0.0)));
		
		neuralNetwork.setTrainingDataSet(new ArrayList<Double>(Arrays.asList(1.0,5.0,5.0)) ,
				new ArrayList<Double>(Arrays.asList(2.01,10.0,1.0)));
		
		neuralNetwork.setTrainingDataSet(new ArrayList<Double>(Arrays.asList(3.0,3.0,3.0)) ,
				new ArrayList<Double>(Arrays.asList(1.01,1.0,1.0)));
		
		neuralNetwork.setTrainingDataSet(new ArrayList<Double>(Arrays.asList(2.0,2.0,2.0)) ,
				new ArrayList<Double>(Arrays.asList(5.01,5.0,5.0)), true);
		
		if(neuralNetwork.setLearningRateMomentum(0.01, 0.9)) System.out.println("Awsome!!!");
		else System.out.println("Npoooo");
		ArrayList<String> errors =  neuralNetwork.getUnInitializedValues();
		if(errors != null){
			for(String str: errors){
				System.out.println(str);
			}
		}
		neuralNetwork.setAbstraction(0, 4);
		for(int i = 0; i < 1000; i++)
			neuralNetwork.runTrainingSystem(true);
		
		ArrayList <Double> result =
				neuralNetwork.networkResult(new ArrayList<Double>(Arrays.asList(3.0,3.0,3.0)));
		
		for(int i = 0; i < 3; i++) {
			System.out.println(result.get(i));
		}
	}
}