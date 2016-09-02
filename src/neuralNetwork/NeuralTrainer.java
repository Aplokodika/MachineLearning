/*
 * Author: K Sreram. 
 * copyright (c) 2016 K Sreram, all rights reserved.
 */
package neuralNetwork;



/**
 * 
 * @author Sreram
 *
 */
public class NeuralTrainer {

	

	public NeuralNetwork NNetwork;
	
	public NeuralTrainer() {
		cap.upperLimit = new Double	(1000 );
		cap.lowerLimit = new Double (-1000);
	}
	
	
	public void setCap(Double lowerLimit, Double upperLimit) {
		cap.lowerLimit = lowerLimit;
		cap.upperLimit = upperLimit;
	}
	
	public WeightCap cap = new WeightCap();
	
	public void setNNetwork(NeuralNetwork Nnet) {
		NNetwork = Nnet;
	}
	
	private Double leastError = null;
	
	public Double getLeastRecorded () {
		return leastError;
	}
	
	public boolean setLeastError(Double newError){
		boolean result = false;
		if(leastError == null) {
			leastError = newError;
			result = true;
		}
		else if(newError.doubleValue() < leastError.doubleValue())	{
			leastError = newError;
			result = true;
		}
		// else, do nothing
		return result; // becomes true when a change is made.
	}
	
	public void resetLeastError(){
		leastError = null;
	}
}
