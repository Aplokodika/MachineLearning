/*
 * Author: K Sreram. 
 * copyright (c) 2016 K Sreram, all rights reserved.
 */
package neuralNetwork;

import java.util.ArrayList;

public interface ComputeError{
	Double computeError(ArrayList <Double> expectedOut, ArrayList<Double> obtainedOut);
}

