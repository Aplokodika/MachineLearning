/*
 * copyright (c) 2016 K Sreram, All rights reserved
 */
package neuralNetwork.ioHandler;
import java.util.ArrayList;

import neuralNetwork.neuralInterface.*;
/**
 * 
 * @author sreram
 *
 */
public class WeightIOHandler {
		String fileName = null;
		NeuralNetworkInterface interfaceHandle;
		
		DataFormatWeightValues ioHandler = new DataFormatWeightValues();
		
		public WeightIOHandler(NeuralNetworkInterface inf){
			interfaceHandle = inf;
		}
		
		public void setFileName(String fName) {
			fileName = fName;
		}
		
		public void write() throws NullPointerException {
			if(fileName == null){
			NullPointerException e = new NullPointerException("File name is not initialized. Try calling "
																+ "`setFileName` method " +
											"Before attempting to perform write operation");
				throw e;
			}
			ArrayList<Double> weightValues = interfaceHandle.getWeightValues();
			ioHandler.weightValues = new String();
			for(int i = 0; i < weightValues.size(); i++) {
				ioHandler.weightValues += weightValues.get(i).toString() + " ";
			}
			ioHandler.writeToFile(fileName);
		}
		
		public void read() throws Exception {
			if(fileName == null){
				NullPointerException e = new NullPointerException("File name is not initialized. Try calling "
						+ "`setFileName` method " +
						"Before attempting to perform write operation");
				throw e;
				}
			ioHandler.getFromFile(fileName);
			String[] splitUp = ioHandler.weightValues.split(" ");
			ArrayList<Double> weightSet = new ArrayList<Double>();
			for(String str : splitUp){
				weightSet.add(Double.valueOf(str));
			}
			interfaceHandle.setWeightValuesDynimic(weightSet);
		}
		
}
