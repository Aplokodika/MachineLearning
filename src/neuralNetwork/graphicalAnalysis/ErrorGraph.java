/*
 * copyright (c) 2016 K Sreram, All rights reserved
 */
package neuralNetwork.graphicalAnalysis;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ErrorGraph extends AnalysisGraph {
	
	private ArrayList<Double> errorRecord = new ArrayList<Double>(); 
	
	
	private void setGraphsSize(int width, int hight){
		super.finalGraph = new BufferedImage(width, hight, BufferedImage.TYPE_3BYTE_BGR);
	}
	
	public ErrorGraph reset(){
		errorRecord.clear();
		super.finalGraph = null;
		incrementDecrementIsSet = false;
		return this;
	}
	
	public ErrorGraph captureError(){
		errorRecord.add(super.findErrorAndUpdateMinMaxError());
		return this;
	}
	
	
	private int calculateHight(int delta, Double error){
		int result;
		result = super.finalGraph.getHeight() - (int)((super.finalGraph.getHeight() - 2*delta)*
						((error - super.minError)/(super.maxError - super.minError))) - delta;
		return result;
	}
	
	
	private Color increment = null, decrement  = null;
	private boolean incrementDecrementIsSet = false;
	
	public ErrorGraph setDuelColourForErrorIncrementAndDecrement(Color pIncrement, Color pDecrement){
		increment = pIncrement;
		decrement = pDecrement;
		incrementDecrementIsSet = true;
		return this;
	}
	
	public ErrorGraph setMinError(Double minError){
		super.minError = minError;
		return this;
	}
	
	private void graphBG(Graphics2D graphics, int delta, int noOfDivisions, Color divisionColor, 
							int divisionStrokeSize, int xStretch, int strokeSize, Color range	){
		int div = super.finalGraph.getHeight() - 2*delta;
		div = div/noOfDivisions;
		graphics.setPaint(divisionColor);
		graphics.setStroke(new BasicStroke(divisionStrokeSize));

		for(int i =0; i < noOfDivisions; i++){
			graphics.drawLine(0, delta + div*i, errorRecord.size() * xStretch, delta + div*i);
		}
		
		
		graphics.setStroke(new BasicStroke(strokeSize));

		
		graphics.setPaint(range);
		graphics.drawLine(0, delta, errorRecord.size() * xStretch, delta);
		graphics.drawLine(0, super.finalGraph.getHeight() - delta, 
								errorRecord.size() * xStretch, 
									super.finalGraph.getHeight() - delta);

	}
	
	public ErrorGraph createGraph(int hight, int delta, Color colour,Color range, 
								int strokeSize, int xStretch, Font font, 
								Color divisionColor, int divisionStrokeSize, 
								int noOfDivisions){
		
		int pWidth = errorRecord.size() * xStretch;
		int pHight = 2*hight;
		int yValue1, yValue2;
		
		
		setGraphsSize(pWidth, pHight);
		BufferedImage img = new BufferedImage(pWidth, pHight, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D graphics = img.createGraphics();
		graphics.drawImage(finalGraph, 0, 0, null);
		
		graphBG(graphics, delta, noOfDivisions,
				divisionColor, divisionStrokeSize, 
				xStretch, divisionStrokeSize, range);
		
		String scale = new String("Maximum error = "+ super.maxError + "  Minimum Error = " + super.minError );
		graphics.setFont(font);
		graphics.drawString(scale, 10, delta/2);
		
		graphics.setStroke(new BasicStroke(strokeSize));
		graphics.setPaint(colour);

		for(int i = 0, k = 0; i < errorRecord.size() - 1; i++, k+= xStretch){
			yValue1 = calculateHight(delta, errorRecord.get(i));
			yValue2 = calculateHight(delta, errorRecord.get(i+1));
			
			if(incrementDecrementIsSet){
				if(yValue1 >= yValue2){
					graphics.setPaint(decrement);
				} else {
					graphics.setPaint(increment);
				}
			}
			
			
			graphics.drawLine(k, yValue1, k+xStretch, yValue2);
		}
		
		super.finalGraph = img;
		return this;
	}
	
}
