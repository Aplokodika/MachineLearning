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
package mainFnc;
import neuralNetwork.*;
import java.util.*;


class NetworkWithFnc extends Neuron{

	@Override
	public Neuron newElement() {
		NetworkWithFnc result = new NetworkWithFnc();
		result.setFactory(result);
		return result;
	}

	@Override
	public Neuron newElement(Neuron value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Float activation(Float inValue) {
		return inValue;
	}
	
}

class NetworkWithFncNew implements Factory<NetworkWithFnc>{

	@Override
	public NetworkWithFnc newElement() {
		return new NetworkWithFnc();
	}

	@Override
	public NetworkWithFnc newElement(NetworkWithFnc value) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

class ErrorFunction implements ComputeError <NetworkWithFnc>{

	@Override
	public Float computeError(ArrayList<Float> expectedOut, ArrayList<Float> obtainedOut) {
		float result = 0;
		for(int i = 0;  i < expectedOut.size(); i++){
			result += (expectedOut.get(i).floatValue()  - obtainedOut.get(i).floatValue())*
					(expectedOut.get(i).floatValue()  - obtainedOut.get(i).floatValue());
		}
		return result;
	}
	
}

public class MainFnc{
	public static void main(String [] arg) throws Exception{
		ArrayList<Integer> sizeList = new ArrayList<Integer>();
		sizeList.add(2);
		sizeList.add(3);
		sizeList.add(4);
		sizeList.add(2);
		sizeList.add(1);
		int noOfWeightValues = ConstructNetwork.weightPairSize(sizeList);
		ConstructNetwork<NetworkWithFnc> neuralNet;
		neuralNet = new ConstructNetwork<NetworkWithFnc>(new NetworkWithFncNew(), 
														 new ErrorFunction(), sizeList);
		ArrayList<Float> weightValues = new ArrayList<Float>();
		WeightPair pair = new WeightPair();
		
		for(int i = 0; i < noOfWeightValues; i++){
			weightValues.add((float)1);
		}
		pair.weight = weightValues;
		
		
			neuralNet.constructNetworkLayered(sizeList, pair);
			ArrayList<Float> inputs = new ArrayList<Float>();
			for(int i = 0; i < 2; i++){
				inputs.add((float)2);
			}
			neuralNet.NNetwork.networkData.setInput(inputs);
			neuralNet.NNetwork.computeNetworkResult(0, NeuralNetwork.MoveOrder.moveForward);
			for(int i = 0; i < 1; i++){
				System.out.println(neuralNet.NNetwork.networkData.outputNeurons.get(i).input);
			}
		
		
		System.out.println("Done");
		Map<Integer, Float> m1 = new HashMap<Integer, Float>();
		m1.put(new Integer(1), (float)1);
		m1.put(new Integer(2), (float)2);
		m1.put(new Integer(1), (float)10);
		System.out.println(m1.get(2).toString());
	}
	
}

