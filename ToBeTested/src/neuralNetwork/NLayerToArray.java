/*
 * Author: K Sreram. 
 * copyright (c) 2016 K Sreram, all rights reserved.
 */

package neuralNetwork;

import java.util.ArrayList;

public class NLayerToArray {

	public static ArrayList <Float> obtainLayerInputInArray(ArrayList<Neuron> layer){
		ArrayList <Float> result = new ArrayList <Float>();
		for(int i = 0; i < layer.size(); i++){
			result.add(layer.get(i).input); 
		}
		return result;
	}
	
	public static ArrayList<Float> obtainLayerOutputInArray(ArrayList<Neuron> layer){
		ArrayList <Float> result = new ArrayList <Float>();
		for(int i = 0; i < layer.size(); i++){
			result.add(layer.get(i).outputResult); 
		}
		return result;
	}
}
