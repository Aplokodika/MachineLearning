/*
 * Author: K Sreram. 
 * copyright (c) 2016 K Sreram, all rights reserved.
 */
package neuralNetwork;

import java.util.ArrayList;

public class WeightPair{
	public ArrayList<Float> weight;
	public int index;
	public WeightPair(){
		index = 0;
	}
	WeightPair(ArrayList<Float> w){
		weight  = w;
	}
}