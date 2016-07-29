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

import java.util.*;

/**
 * This is the class containing the neuron structure. 
 * It has the `activation function` as abstract, to provide the possibility
 * of defining an unique activation function for each neuron if needed.
 * The activation function in this class returns a particular value, which 
 * is considered as the output of this neuron and becomes a part of the input
 * to the successive neuron.  
 * 
 * For input neurons, there won't be any parent neurons, and for output neurons
 * there won't be any child neurons. 
 * 
 * The link back to the parent neuron is required to make back-tracking feasible. 
*/
abstract public class Neuron implements Factory<Neuron>{
	
	Factory<Neuron> factory;
	
	public Neuron(Factory<Neuron> fact){
		setFactory(fact);
	}
	
	public void setFactory(Factory<Neuron> fact){
		neuronIndex = newNeuronIndex();
		factory = fact;
	}
	
	public Neuron(){
		this.neuronIndex = newNeuronIndex();
	}
	public static void reset(Integer setVal){
		curUnusedIndex = new Integer(setVal.intValue());
	}

	/**	newNeuronIndex
	 *  ==============
	 *  This returns a new index each time this method is invoked. 
	 */
	public static Integer newNeuronIndex(){
		if(curUnusedIndex == null){
			curUnusedIndex = new Integer(0);
		}
		Integer result = new Integer(curUnusedIndex.intValue());
		curUnusedIndex = new Integer(curUnusedIndex.intValue() + 1);
		return result;
	}
	
	
	// holds the next unused Index for the neurons. Each time it is used, 
	// the index increments. This ensures that each neurons will get a unique 
	// index value. 
	public static Integer curUnusedIndex;
		
	// the index of the current layer. 
	public Integer neuronIndex;
	
	
	// The activation function of the neuron.
	abstract public Float activation(Float inValue);

	// Neuron's input: This is the summation of output from each of the
	// previous layer neuron's output times the corresponding connecting
	// weight values.
	public Float input;

	// stores the output of the activation function.
	public Float outputResult;

	// This collectively stores the parent neurons, to enable back-tracking. 
	public ArrayList<Neuron> parentNeurons = new ArrayList<Neuron>();
	
	// The weight values of the forward neurons
	public Map<Integer, Float> weightValues = new HashMap<Integer, Float>();

	// Link to the next neurons (forward).
	public ArrayList<Neuron> childNeurons = new ArrayList<Neuron>();

	
	// null in case of root node

	/**
	 * The following stores the previous values.
	 * calling function updateWeight..
	 * causes the weight modification
	 * to be reversed
	*/
	private Float previousWeight;
	private Integer previousIndex;
	
	public Neuron generateNewNeuron(){
		return factory.newElement();
	}
	

	/** updateWeight
	 * =========== 
	 * The variable 'weightValues' stores the weight values of each of the 
	 * links to the next consecutive nodes. The following method updates 
	 * the weight values. 
	 * 
	 * @index - for identifying the node. 
	 * @change - the change to be incorporated (can either be negative or 
	 * 			 positive).  
	 * */
	public void updateWeight(Integer index, Float change) {
		previousWeight = new Float(weightValues.get(index.intValue()));
		
		weightValues.put(index, 
							previousWeight.floatValue() + change.floatValue());

	}

	/** reverseUpdate
	 * =============
	 * This method reverses the modification previously done on the weight
	 * values.
	*/
	public void reverseUpdate() {

		if (previousWeight == null)
			return;
		weightValues.put(previousIndex, previousWeight.floatValue());
		previousWeight = null;
		previousIndex = null;

	}

	/** addNeuron
	 * =========
	 * This connects another neuron to the current neuron. the current 
	 * neuron is preceded by the newer neuron. This neural network 
	 * architecture has each of the succeeding neurons connected to each
	 * of the neurons in the preceding layer.
	 * 
	 * @neuron -  reference to the neuron to be added, which is defined 
	 * 			  elsewhere.
	 * @weight -  weight of the newly added neuron.
	 * @index  -  variables `weightValues`, `childNeurons` and `patentNeurons`
	 * 			  are of type ArrayList. So, they need a common index as an 
	 * 			  address in the layer they are present. This purpose is served
	 * 		      by index.     
	 */
	public void addNeuron(Neuron neuron, Float weight){
		this.weightValues.put(neuron.neuronIndex, weight);
		this.childNeurons.add(neuron); 
		neuron.parentNeurons.add(this);
	}
	
	
	/** This function pulls in the input from the previous layer's neurons. This enables the 
	 *  current neuron to compute the result obtained from the previous neurons. 
	 *  
	 *  The summation of weight*previous_neurons_output is computed over each neuron in the 
	 *  previous layer connecting to the current neuron and stored in the variable `input`.  
	 */
	public void pullInput(){
		input = new Float(0);
		if (parentNeurons == null) return; // the case when these are input neurons
		
		for(int i = 0; i < parentNeurons.size(); i++){
			
			input = new Float(
					input.floatValue() + 
					(parentNeurons.get(i).weightValues.get(this.neuronIndex.intValue()).floatValue()*
					 parentNeurons.get(i).outputResult.floatValue())
					);
		}
	}
	/**
	 * This calls the activation function and stores the result in the variable, 
	 * `outputResult`.
	 */
	public void computeOutput(){
		outputResult = activation(new Float(input));
	}
		
		
}

