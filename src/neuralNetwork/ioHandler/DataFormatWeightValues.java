/*
 * copyright (c) 2016 K Sreram, All rights reserved
 */
package neuralNetwork.ioHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * This structure does not contain any information regarding the structure of the
 * neural network system. It only contains the structure of the weight data-set. 
 * 
 * There must be another similar structure for training data-set. 
 * @author sreram
 *
 */

class DataFormatWeightValues {
	public String weightValues;
	
	public void getFromFile(String fName) {
		try {
			FileInputStream file = new FileInputStream(fName);
			BufferedReader buffer = new BufferedReader(new InputStreamReader(file));
			weightValues = buffer.readLine();
			buffer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeToFile(String fName) {
		
		FileOutputStream file;
		try {
			file = new FileOutputStream(fName);
			BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(file));
			buffer.write(weightValues);
			buffer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}
}
