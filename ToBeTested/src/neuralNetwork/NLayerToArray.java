/*
 * Author: K Sreram. 
 * copyright (c) 2016 K Sreram, all rights reserved.
 */

package neuralNetwork;

import java.util.ArrayList;

public class NLayerToArray {

	public static ArrayList <Double> obtainLayerInputInArray(ArrayList<Neuron> layer){
		ArrayList <Double> result = new ArrayList <Double>();
		for(int i = 0; i < layer.size(); i++){
			result.add(layer.get(i).input); 
		}
		return result;
	}
	
	public static ArrayList<Double> obtainLayerOutputInArray(ArrayList<Neuron> layer){
		ArrayList <Double> result = new ArrayList <Double>();
		for(int i = 0; i < layer.size(); i++){
			result.add(layer.get(i).outputResult); 
		}
		return result;
	}
}
