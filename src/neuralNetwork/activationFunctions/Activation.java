/*
 * Author: K Sreram. 
 * copyright (c) 2016 K Sreram, all rights reserved.
 */
package neuralNetwork.activationFunctions;

public interface Activation {
	Double activation(Double inp);

	default double activationDifferential(Double input, Double outputResult){
		return (activation(input.doubleValue() + 1.0e-10) - activation(input.doubleValue()))/1.0e-10;
	}
}
