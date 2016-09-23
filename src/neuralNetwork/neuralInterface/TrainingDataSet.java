/*
 * Copyright (c) 2016 K Sreram, All rights reserved. 
 */
package neuralNetwork.neuralInterface;

import java.util.ArrayList;
import java.util.Iterator;

import neuralNetwork.NeuralTrainer;


public class TrainingDataSet implements Iterable<TrainingDataSet.InputExpectedOutput>{
	private ArrayList<ArrayList<Double>> dataSetInputs = new ArrayList<ArrayList<Double>>();
	private ArrayList<ArrayList<Double>> dataSetOutputs = new ArrayList<ArrayList<Double>>();
		
	private TriggerInterface nInterface;
	
	public NeuralTrainer getNeuralTrainer(){
		return nInterface.getNeuralTrainer();
	}
	
	
	public static class InputExpectedOutput{
		public ArrayList<Double> input;
		public ArrayList<Double> output;
	}
	
	
	public class TrainingDataSetIterator implements Iterator<InputExpectedOutput> {
		int curIndex = 0;
		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return (curIndex < size() );
		}

		@Override
		public InputExpectedOutput next() {
			curIndex++;
			InputExpectedOutput result= new InputExpectedOutput();
			result.input = dataSetInputs.get(curIndex - 1);
			result.output = dataSetOutputs.get(curIndex - 1);
			return result;
		}
		
	}
	
	@Override
	public Iterator<InputExpectedOutput> iterator() {
		TrainingDataSetIterator result = new TrainingDataSetIterator();
		return result;
	}
	
	public static class trainingDataUnit {
		public ArrayList<Double> outputs;
		public ArrayList<Double> expectedOutput;
		public ArrayList<Double> inputs;
		public ArrayList<Double> errors; // size is same as outputs
		
	}
	
	public TrainingDataSet(TriggerInterface triggerInterface){
		nInterface = triggerInterface;
	}
	
	
	public static ArrayList<Double> sum(ArrayList<Double> m1, ArrayList<Double> m2) {
		ArrayList<Double> result = new ArrayList<Double>();
		for(int i = 0; i < m1.size(); i++) {
			result.add(m1.get(i) + m2.get(i));
		}
		return result;
	}
	
	public static ArrayList<Double> difference(ArrayList<Double> m1, ArrayList<Double> m2) {
		ArrayList<Double> result = new ArrayList<Double>();
		for(int i = 0; i < m1.size(); i++) {
			result.add(m1.get(i) - m2.get(i));
		}
		return result;
	}
	
	public static ArrayList<Double> differenceSq(ArrayList<Double> m1, ArrayList<Double> m2) {
		ArrayList<Double> result = new ArrayList<Double>();
		for(int i = 0; i < m1.size(); i++) {
			result.add((m1.get(i) - m2.get(i))*
					(m1.get(i) - m2.get(i)));
		}
		return result;
	}
	
	public ArrayList <Double> average(ArrayList<Double> m) {
		ArrayList<Double> result = new ArrayList<Double> ();
		for(int i =0; i< m.size(); i++) {
			result.add(m.get(i)/size());
		}
		return result;
	}
	
	

	public trainingDataUnit getTrainerHandle (Integer trainingDataIndex) throws Exception {
		ArrayList<Double> inputs;
		ArrayList<Double> outputs;
		ArrayList<Double> errors;
		trainingDataUnit result = new trainingDataUnit();
		inputs = dataSetInputs.get(trainingDataIndex);
		outputs = nInterface.networkResult(inputs);
		errors = difference(dataSetOutputs.get(trainingDataIndex), outputs); // difference between expected output and 
														     // observed output
		result.errors = errors;
		result.inputs = inputs;
		result.outputs = outputs;
		result.expectedOutput = dataSetOutputs.get(trainingDataIndex);
		return result;
	}
	
	public void addDataSet(ArrayList<Double> input, ArrayList<Double> output){
		getDataSetInputs().add(input);
		getDataSetOutputs().add(output);
	}

	public ArrayList<ArrayList<Double>> getDataSetInputs() {
		return dataSetInputs;
	}

	
	public ArrayList<ArrayList<Double>> getDataSetOutputs() {
		return dataSetOutputs;
	}
	
	public int size(){
		return dataSetInputs.size(); // both inputs and outputs have the same size
	}

	
	public Double computeError(NeuralTrainer neuralTrainer) {
		double error = 0.0;
		for(int i = 0; i < size(); i++){
				neuralTrainer.computeNetworkResult(getDataSetInputs().get(i));
				error += neuralTrainer.computeError(getDataSetOutputs().get(i));
			}
		error = error/size();
		return error;
	}
	
	
}