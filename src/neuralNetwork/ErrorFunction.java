package neuralNetwork;

import java.util.ArrayList;

public class ErrorFunction implements ComputeError {

	@Override
	public Double computeError(ArrayList<Double> expectedOut, ArrayList<Double> obtainedOut) {
		Double result = new Double((double) 0);

		for (int i = 0; i < expectedOut.size(); i++) {
			result = result.doubleValue() + (expectedOut.get(i).doubleValue() - obtainedOut.get(i).doubleValue())*
			(expectedOut.get(i).doubleValue() - obtainedOut.get(i).doubleValue());
		}

		return result;
	}

}
