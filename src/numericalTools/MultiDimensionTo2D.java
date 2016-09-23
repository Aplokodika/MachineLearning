package numericalTools;

import java.util.ArrayList;
/**
 * This class stores the integer values in a numerical coding pattern following the denoted method. 
 * Like binary coding, decimal coding and hexadecimal coding which has equal number of unique values for
 * each of digit, this class allows the user to have a combination of values. 
 * 
 * This allows the user to define the capacity of each of the digit separately. This class is 
 * specifically meant to be a counter (or more specifically a 2D coordinate counter, obtained from
 * n dimensions) and is not for arithmetic or mathematical purposes. 
 * @author sreram
 *
 */
public class MultiDimensionTo2D {
	
	
	private boolean typeIsSet = false;
	
	public boolean isTypeSet() {
		return typeIsSet;
	}
	
	private int [] typeX;
	private int [] typeY;
	
	private Integer xValue = new Integer(0);
	private Integer yValue = new Integer(0);
	private boolean haveValuesChanged = true;
	
	
	private int maxValueX, maxValueY;	
	
	private ValueList valueList;

	
	/**
	 * This denotes the pattern to be followed while assigning the maximum value for 
	 * each digit in the number system. 
	 * @author sreram
	 *
	 */
	public static enum WeightChangePriorityType{
		LINEAR, LINEAR_INCREMENTAL, 
		LINEAR_DECREMENTAL, FACTORIAL, 
		LINEAR_CHANGABLE
	}
	
	/**
	 * 
	 * @author sreram
	 *
	 */
	public static class ValueList{
		public ArrayList<Integer>  xValueList;
		public ArrayList<Integer> 	yValueList;
		
		public ValueList(ValueList vList){
			this.xValueList = vList.xValueList;
			this.yValueList = vList.yValueList;
		}
		
		public ValueList(ArrayList<Integer> pXValueList, ArrayList<Integer> pYValueList){
			xValueList = pXValueList;
			yValueList = pYValueList;
		}
		
		public ValueList(int pSizeXValueList, int pSizeYValueList){
			this();
			
			for(int i = 0; i < pSizeXValueList; i++)
				xValueList.add(0);
			for(int i = 0; i < pSizeYValueList; i++)
				yValueList.add(0);
		}
		
		public ValueList(){
			xValueList = new ArrayList<Integer>();
			yValueList = new ArrayList<Integer>();
		}
	}
	/**
	 * This initializes the split up of capacity. 
	 * @param pDivisionX - the split-up for X axis counter
	 * @param pDivisionY - the split-up for Y axis counter
	 */
	public MultiDimensionTo2D(int pDivisionX, int pDivisionY){
		this.typeX = new int [pDivisionX];
		this.typeY = new int [pDivisionY];
		setValueList(new ValueList(pDivisionX, pDivisionY));
	}
	
	public MultiDimensionTo2D (int pNetDimensions){
		int xMax, yMax;
		if((pNetDimensions % 2) == 0){
			yMax = xMax = pNetDimensions/2;
			
		} else {
			xMax = (pNetDimensions-1)/2;
			yMax = ((pNetDimensions-1)/2) + 1;
		}
		
		this.typeX = new int [xMax];
		this.typeY = new int [yMax];
		setValueList(new ValueList(this.typeX.length, this.typeY.length));
	}
	
	public MultiDimensionTo2D (int [] pTypeX, int [] pTypeY){
		this.typeX = pTypeX;
		this.typeY = pTypeY;
		setValueList(new ValueList(this.typeX.length, this.typeY.length));

	}
	
	
	
	public MultiDimensionTo2D setTypeFactorial(int factorialLim){
		int resultX = 1, resultY = 1, fact;
		fact= factorialLim;
		
		for(int i = 0; i < typeX.length; i++, fact--){
			resultX *= fact;
			this.typeX[i] = fact;
		}
		
		this.setMaxValueX(resultX);
		fact = factorialLim;
		
		for(int i = 0; i < typeY.length; i++, fact-- ){
			resultY *= fact;
			this.typeY[i] = fact;
		}
		
		this.setMaxValueY(resultY);
		
		this.typeIsSet = true; 
		
		return this;
	}
	
	/**
	 *  This method sets each of the Type variables to the same 
	 *  value which equals  linearLim. 
	 * @param linearLim - this is the value that is evenly initialized with 
	 * 						all the fields of linearLim;
	 * @return returns the object of the current class (it reflects itself)
	 */
	public MultiDimensionTo2D setTypeLinear(int linearLim){
		
		int pMaxValueX = 1, pMaxValueY = 1;
		for(int i = 0; i < this.typeX.length; i++){
			this.typeX[i] = linearLim;
			pMaxValueX *= linearLim;
		}
		
		this.setMaxValueX(pMaxValueX);
		
		for(int i = 0; i < this.typeY.length; i++){
			this.typeY[i] = linearLim;
			pMaxValueY *= linearLim;
		}
		this.setMaxValueY(pMaxValueY);
		
		this.typeIsSet = true;
		return this;
	}
	
	public MultiDimensionTo2D setTypeLinearIncremental(int pLowerLim){
		int lowerLim = pLowerLim;
		int pMaxValueX = 1, pMaxValueY = 1;
		for(int i = 0; i < this.typeX.length; i++, lowerLim++){
			this.typeX[i] = lowerLim;
			pMaxValueX *= lowerLim;
		}
		
		this.setMaxValueX(pMaxValueX);
		lowerLim = pLowerLim;
		
		for(int i = 0; i < this.typeY.length; i++, lowerLim++){
			this.typeY[i] = lowerLim;
			pMaxValueY *= lowerLim;
		}
		this.setMaxValueY(pMaxValueY);
		this.typeIsSet = true;
		return this;
	}
	
	public MultiDimensionTo2D setTypeLinearChangable(int pLowerLim, int change){
		int lowerLim = pLowerLim;
		int pMaxValueX = 1, pMaxValueY = 1;
		for(int i = 0; i < this.typeX.length; i++, lowerLim += change){
			if(lowerLim <= 0) lowerLim = 1;
			this.typeX[i] = lowerLim;
			pMaxValueX *= lowerLim;
		}
		
		this.setMaxValueX(pMaxValueX);
		lowerLim = pLowerLim;
		
		for(int i = 0; i < this.typeY.length; i++, lowerLim += change){
			if(lowerLim <= 0) lowerLim = 1;	
			this.typeY[i] = lowerLim;
			pMaxValueY *= lowerLim;
		}
		this.setMaxValueY(pMaxValueY);
		this.typeIsSet = true;
		return this;
	}
	
	public MultiDimensionTo2D setTypeLinearDecremental(int pUpperLim){
		int upperLim = pUpperLim;
		int pMaxValueX = 1, pMaxValueY = 1;
		for(int i = 0; i < this.typeX.length; i++, upperLim--){
			this.typeX[i] = upperLim;
			pMaxValueX *= upperLim;
		}
		
		this.setMaxValueX(pMaxValueX);
		upperLim = pUpperLim;
		
		for(int i = 0; i < this.typeY.length; i++, upperLim--){
			this.typeY[i] = upperLim;
			pMaxValueY *= upperLim;
		}
		this.setMaxValueY(pMaxValueY);
		this.typeIsSet = true;
		return this;
	}
	
	
	private void setMaxValueX(int maxValueX) {
		this.maxValueX = maxValueX;
	}

	private void setMaxValueY(int maxValueY) {
		this.maxValueY = maxValueY;
	}

	private void setValueList(ValueList valueList) {
		this.valueList = valueList;
	}
	
	
	public int getMaxValueX() {
		return maxValueX;
	}

	

	public int getMaxValueY() {
		return maxValueY;
	}

	
	
	/**
	 * This method returns the value in the form of ValueList (in the form of 
	 * a pair of lists that contain the set of values for both the x axis and y 
	 * axis). This method computes the ValueList only if it is not computed before. 
	 * If no changes are made to the values: xValue and yValue then this method 
	 * returns the previously computed value.  
	 * @return returns the list of values (converted to the represented number system)
	 * 			for both X axis and Y axis. 
	 * @throws Exception  if the structure of the number system is not initialized 
	 * 						it throws it as an error. 
	 */
	public ValueList getValueList() throws Exception {
		
		if(this.isTypeSet() == false){
			Exception e = new Exception("Error: weight value's change periority type is not set");
			throw e;
		}
		
		if(haveValuesChanged == false)
			return valueList;
		
		int pXValue = this.xValue.intValue();
		int pYValue = this.yValue.intValue();
		int difference;
		
		for(int i = 0; i < this.typeX.length; i++){
			difference  = pXValue % this.typeX[i];
			pXValue -= difference;
			pXValue /= this.typeX[i];
			valueList.xValueList.set(i, difference);
		}
		
		for(int i = 0; i < this.typeY.length; i++){
			difference  = pYValue % this.typeY[i];
			pYValue -= difference;
			pYValue /= this.typeY[i];
			valueList.yValueList.set(i, difference);
		}
		
		haveValuesChanged = false;
		
		return valueList;
	}

	public Integer getxValue() throws Exception {
		if(this.isTypeSet() == false){
			Exception e = new Exception("Error: weight value's change periority type is not set");
			throw e;
		}
		return xValue;
	}

	public void setxValue(Integer xValue) throws Exception {
		if(this.isTypeSet() == false){
			Exception e = new Exception("Error: weight value's change periority type is not set");
			throw e;
		}
		haveValuesChanged = true;
		this.xValue = xValue;
	}

	
	public Integer getyValue() throws Exception {
		if(this.isTypeSet() == false){
			Exception e = new Exception("Error: weight value's change periority type is not set");
			throw e;
		}
		return yValue;
	}

	public void setyValue(Integer yValue) throws Exception {
		if(this.isTypeSet() == false){
			Exception e = new Exception("Error: weight value's change periority type is not set");
			throw e;
		}
		haveValuesChanged = true;
		this.yValue = yValue;
	}

	
	
}
