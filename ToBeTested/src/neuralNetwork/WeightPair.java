package neuralNetwork;

import java.util.ArrayList;

public class WeightPair{
	public ArrayList<Float> weight;
	public int index;
	WeightPair(){
		index = 0;
	}
	WeightPair(ArrayList<Float> w){
		weight  = w;
	}
}