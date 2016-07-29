/*
 * Author: K Sreram. 
 * copyright (c) 2016 K Sreram, all rights reserved.
 * 
 * Developer License Agreement.
 * ..............................
 * 
 * Members of Aplokodika may update the following source and may claim the ownership of
 * copyright to the source they contribute. The members of Aplokodika may modify, 
 * create or alter a whole or a part of the following code with consent from the 
 * majority of the copyright holders of the source code. Each modification must be 
 * documented and notified, with appropriate identity information of the author who helped 
 * modify the source; this includes the author's name, personal email address.
 * 
 * By contributing to this code, you agree to grant Aplokodika free license to store, modify, share, 
 * sell, republish and grant such license to third parties without any cost or conditions. 
 * 
 * Authors contributing to this project own the code they write. That is, Aplokodika does
 * not claim to own the copyright to the content contributed by an author unless the rights are 
 * explicitly transfered. By modifying/creating/contributing to this project, the authors/copyright
 * holders agree to grant Aplokodika organization free license to store, modify, share, sell, republish
 * this software, as source or as a binary release and the authors also agree that Aplokodika may grant
 * such license to third parties.
 * 
 * The authors contributing to this software also agree that, Aplokodika reserves the rights to 
 * modify this license at will, and modifications may not be notified instantly. All notification 
 * mechanisms used to notify such changes are only for the ease of reference.  
 */
package neuralNetwork;

public interface Factory<T> {
	T newElement();
	T newElement(T value);
	
}
