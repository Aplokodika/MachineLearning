/*
 * Author: K Sreram. 
 * copyright (c) 2016 K Sreram, all rights reserved.
 */
package neuralNetwork;

import java.util.ArrayList;

public interface ComputeError{
	Float computeError(ArrayList <Float> expectedOut, ArrayList<Float> obtainedOut);
}

