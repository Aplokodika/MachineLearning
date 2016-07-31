/*
 * Author: K Sreram. 
 * copyright (c) 2016 K Sreram, all rights reserved.
 */
package neuralNetwork;

public interface Factory<T> {
	T newElement();
	T newElement(T value);
	
}
