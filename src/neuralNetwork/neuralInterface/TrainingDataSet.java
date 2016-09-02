/*
 * Copyright (c) 2016 K Sreram, All rights reserved. 
 */
package neuralNetwork.neuralInterface;

import java.util.ArrayList;

public class TrainingDataSet{
	private ArrayList<ArrayList<Double>> dataSetInputs = new ArrayList<ArrayList<Double>>();
	private ArrayList<ArrayList<Double>> dataSetOutputs = new ArrayList<ArrayList<Double>>();
	
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

	
}