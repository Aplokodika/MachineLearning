/*
 * copyright (c) 2016 K Sreram (sreramk1@gmail.com) all rights reserved 
 */
package neuralNetwork.graphicalAnalysis;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import neuralNetwork.NeuralNetworkArchitecture;
import neuralNetwork.NeuralNetworkArchitecture.ChangableWeightParameterListPair;
import neuralNetwork.NeuralNetworkArchitecture.ChangableWeightParameterListPair.Priority;
import neuralNetwork.NeuralNetworkArchitecture.ChangableWeightParameters;
import numericalTools.MultiDimensionTo2D;
import numericalTools.MultiDimensionTo2D.ValueList;
import numericalTools.MultiDimensionTo2D.WeightChangePriorityType;
/**
 * This method explores the error space of the system and plots a 
 * grey scale graph. More darker the graph is, more greater will be 
 * the depth of the minima. 
 * @author K Sreram (sreramk1@gmail.com)
 *
 */
public class SolutionSpace2DGrey extends AnalysisGraph {
	
	private ArrayList<ArrayList<Double> > error2D = new ArrayList<ArrayList<Double> > ();

	private Position midPosition = new Position();
	
	private Double incrementalRate;
	
	private ChangableWeightParametersPair paraValues;
	
	private NeuralNetworkArchitecture weightPairHandle = new NeuralNetworkArchitecture(); 
	
	public static class ChangableWeightParametersPair{
		public final ChangableWeightParameters para1;
		public final ChangableWeightParameters para2;
		
		public ChangableWeightParametersPair(ChangableWeightParametersPair para){
			this.para1 = para.para1;
			this.para2 = para.para2;
		}
		
		public ChangableWeightParametersPair(ChangableWeightParameters pPara1, 
				ChangableWeightParameters pPara2){
			this.para1 = pPara1;
			this.para2 = pPara2;
		}
	}
	
	public static class Position{
		public Integer posX;
		public Integer posY;
	}
	
		
	
	
	public SolutionSpace2DGrey setGraphSize(int width, int hight) {
		super.finalGraph = new BufferedImage(width, hight, BufferedImage.TYPE_3BYTE_BGR);
		
		// zero initialize the 2d array list
		for(int i = 0; i < hight; i++){
			error2D.add(new ArrayList<Double>());
			for(int j = 0; j < width; j++){
				error2D.get(i).add(new Double(0));
			}
		}
		return this;
	}
	
	public SolutionSpace2DGrey setMidPoint(Position pMidPosition){
		midPosition = pMidPosition;
		return this;
	}
	
	public SolutionSpace2DGrey setDefaultMidPosition () {
		midPosition.posX = super.finalGraph.getWidth()/2;
		midPosition.posY = super.finalGraph.getHeight()/2;
		return this;
	}
	
	public SolutionSpace2DGrey setIncrementalRate(Double pIncrementalRate){
		incrementalRate = pIncrementalRate;
		return this;
	}
	
	
	/**
	 * This method sets the weight value that have to be modified in the analysis process
	 * @param para - this contains the pair of pairs of neurons each addressing a single 
	 * 					weight value. 
	 */
	public SolutionSpace2DGrey setChangableParameters(ChangableWeightParametersPair para){
		
		paraValues = para;
		return this;
	}
		
	
	private Double updateParameter(ChangableWeightParameters para, double change){
		
		neuralTrainer.imposeChange(change, para.neuronFrom, 
				para.neuronTo);
		
		return findErrorAndUpdateMinMaxError();
	}
	
	
	public SolutionSpace2DGrey resetError(){
		maxError = null; minError = null;
		return this;
	}
	
	public SolutionSpace2DGrey setError2D(){
		// the para1 weight changes for along the height
		Double changeMidH = new Double (incrementalRate.doubleValue() * midPosition.posY.doubleValue());
		// the para2 weight changed along the breadth. 
		Double changeMidW = new Double (incrementalRate.doubleValue() * midPosition.posX.doubleValue());
		
		Double error;
		
		updateParameter(paraValues.para1, -changeMidH); // for height 
		error = updateParameter(paraValues.para2, -changeMidW); // for width
		
		
		for(int i = 0; i < finalGraph.getHeight(); i++ ){ // i iterates through height
			for(int j = 0; j < finalGraph.getWidth(); j++){ // j iterates through width
				error2D.get(i).set(j, error); 					// Width
				error = updateParameter(paraValues.para2, incrementalRate.doubleValue());
			}
			updateParameter(paraValues.para1, incrementalRate.doubleValue()); // height 
			updateParameter(paraValues.para2, -(finalGraph.getWidth() * 	
					incrementalRate.doubleValue())); // as the previous iteration would have already
															// lead this to its max
		}
		updateParameter(paraValues.para1, -((finalGraph.getHeight() -midPosition.posY.doubleValue())*
					incrementalRate.doubleValue()));
		updateParameter(paraValues.para2, (incrementalRate.doubleValue() * midPosition.posX.doubleValue()));
		return this;
	}
	
	private final int scaleSize = 255 * 255 * 255;
	private final int minScaleSize = 255;
	
	public SolutionSpace2DGrey convertError2DtoImage(int power){
		Color color;
		int magnitude;
		int redValue;
		int greenValue;
		int blueValue;
		Double factor = new Double( maxError.doubleValue());
		
		for(int i = 0; i < finalGraph.getHeight(); i++){
			for(int j = 0; j < finalGraph.getWidth(); j++){
				magnitude = (int)(scaleSize*
						Math.pow((error2D.get(i).get(j)/factor.doubleValue()), power));
				
				blueValue = magnitude % minScaleSize;
				magnitude = magnitude / minScaleSize;
				
				greenValue = magnitude % minScaleSize;
				magnitude = magnitude / minScaleSize;
				
				redValue = magnitude % minScaleSize;
				magnitude = magnitude / minScaleSize;
				
				color = new Color(redValue, greenValue, blueValue);
				finalGraph.setRGB(i, j, color.getRGB());
			}
		}
		finalGraph.setRGB(midPosition.posY, midPosition.posX, Color.RED.getRGB());
		
		return this;
	}
	
	// For multidimensional analysis. 
	
	private ChangableWeightParameterListPair weightParameterListPair = new ChangableWeightParameterListPair();
	
	private MultiDimensionTo2D commonCount = null;
	
	
	public SolutionSpace2DGrey setWeightPairHandleMultiDimensional(Priority priority, 
										WeightChangePriorityType changePriority, int limit, int change) throws Exception{
		weightPairHandle.setNeuralNetwork(neuralNetwork);
		weightPairHandle.findArchitecture();
		weightPairHandle.getWeightParametersAsAListOfNeuronPair();
		weightParameterListPair = weightPairHandle.splitChangabeWeightParameterListPairEqually(priority); 
		commonCount = new MultiDimensionTo2D(weightParameterListPair.changableWeightParameterListX.size(),
								weightParameterListPair.changableWeightParameterListY.size());
		
		switch(changePriority){
		case FACTORIAL:
			commonCount.setTypeFactorial(limit);
			break;
		case LINEAR:
			commonCount.setTypeLinear(limit);
			break;
		case LINEAR_DECREMENTAL:
			commonCount.setTypeLinearDecremental(limit);
			break;
		case LINEAR_INCREMENTAL:
			commonCount.setTypeLinearIncremental(limit);
			break;
		case LINEAR_CHANGABLE :
			commonCount.setTypeLinearChangable(limit, change);
			break;
		default:
			Exception e = new Exception("Error: invalid WeightChangePriorityType");
			throw e;
		}
		return this;
	}
	
	public SolutionSpace2DGrey setGraphSizeAutomaticallyForMultiDimensions() throws Exception{
		if(commonCount == null){
			Exception e = new Exception("Error: commonCount not initialized. Try calling method :"
					+ "setWeightPairHandleMultiDimensional before ivoking "
					+ " the method :setGraphSizeAutomaticallyForMultiDimensions");
			throw e;
		}
		this.setGraphSize(commonCount.getMaxValueX(), commonCount.getMaxValueY());
		System.out.println(commonCount.getMaxValueX() + "--- " + commonCount.getMaxValueY());
		return this;
	}
	
	private int getSign(int value){
		if(value >= 0){
			return 1;
		} else {
			return -1;
		}
	}
	
	private void changeWeightMultiDimensional(int changeX, int changeY) throws Exception{
		ValueList tempValueList;
        commonCount.setxValue(Math.abs(changeX));
        commonCount.setyValue(Math.abs(changeY));
		tempValueList = commonCount.getValueList();
		ChangableWeightParameters tempChangableWP;
		
		int i = 0;
		if(changeX != 0)
			for(Integer xValue : tempValueList.xValueList){
				tempChangableWP = weightParameterListPair.changableWeightParameterListX.get(i);
				neuralTrainer.imposeChange(xValue.intValue() *incrementalRate.doubleValue()*getSign(changeX), 
						tempChangableWP.neuronFrom, tempChangableWP.neuronTo);
				i++;
			}
		
		i = 0;
		if(changeY != 0)
			for(Integer yValue : tempValueList.yValueList){
				tempChangableWP = weightParameterListPair.changableWeightParameterListY.get(i);
				neuralTrainer.imposeChange(yValue.intValue() *incrementalRate.doubleValue()*getSign(changeY), 
						tempChangableWP.neuronFrom, tempChangableWP.neuronTo);
				i++;
			}
	}

	
	private Double changeWeightMultiDimensionalComputeError(int changeX, int changeY) throws Exception{
		changeWeightMultiDimensional(changeX, changeY);
		return findErrorAndUpdateMinMaxError();
	}
	
	public SolutionSpace2DGrey setErrorMultiDimensionsTo2D() throws Exception{
		Double error;
		// change the coordinates in accordance with the mid point. 
		error = changeWeightMultiDimensionalComputeError( -midPosition.posX.intValue(), 
																			-midPosition.posY.intValue());
		for(int i =0; i < finalGraph.getHeight(); i++){
			for(int j = 0; j < finalGraph.getWidth(); j++){
				error2D.get(i).set(j, error); 
				error = changeWeightMultiDimensionalComputeError(j, i); 
				changeWeightMultiDimensional(-j, -i); // reverse change; error computation is not required here
			}
		}
		changeWeightMultiDimensional(  midPosition.posX.intValue(), midPosition.posY.intValue());
		
		return this;
	}
	
	
}
