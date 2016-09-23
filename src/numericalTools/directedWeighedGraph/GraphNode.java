/*
 * copyright (c) 2016 K Sreram, All rights reserved
 */
package numericalTools.directedWeighedGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GraphNode < Weights > {
	
	private Factory <Weights> newWeightInstance;
		
	
	public GraphNode (Factory <Weights> pNewWeightInstance){
		nodeIndex = newNodeIndex();
		newWeightInstance = pNewWeightInstance;
	}
	
	public Weights newWeightInstance(){
		return newWeightInstance.newInstance();
	}
	
	private Map<Integer, Weights> weightValues = new HashMap<Integer, Weights>();
	
	private Map<Integer, GraphNode < Weights > > parentNodesIndexed = 
											new HashMap<Integer,GraphNode < Weights > >();
	
	private Map<Integer, GraphNode <Weights> > childNodesIndexed = 
											new HashMap < Integer, GraphNode <Weights> >();
	
	private ArrayList< GraphNode < Weights > > parentNodes;
	
	private ArrayList< GraphNode < Weights > > childNodes;
	
	private static Integer curUnusedIndex = new Integer(0);

	
	private final Integer nodeIndex;
	
	
	public Integer getNodeIndex(){
		return nodeIndex;
	}
	
	
	public static Integer newNodeIndex(){
		if(curUnusedIndex == null){
			curUnusedIndex = new Integer(0);
		}
		Integer result = curUnusedIndex;
		curUnusedIndex = new Integer(curUnusedIndex.intValue() + 1);
		return result;
	}
	
	
	
	public void connectBackwardUniDirected(GraphNode < Weights > nextNode){
		this.addParentNode(nextNode);
	}
	
	public void connectBackwardWeightedUniDirected(Weights weight, GraphNode<Weights> nextNode){
		connectBackwardUniDirected(nextNode);
		setWeightValue(nextNode.getNodeIndex(), weight);
	}
	
	
	public void connectBackwardWeightedBiDirected(Weights weight, GraphNode<Weights> nextNode){
		connectBackwardWeightedUniDirected(weight, nextNode);
		nextNode.connectForwardUniDirected(this);
	}
	
	public void connectBackwardBiDirected(GraphNode<Weights> nextNode){
		connectBackwardUniDirected(nextNode);
		nextNode.connectForwardUniDirected(this);
	}
	
	public void connectBackwardBiWeightedBiDirected(Weights previousNodeToCurrent,
													Weights currentNodeToPrevious, 
													GraphNode<Weights> nextNode){
		connectBackwardWeightedUniDirected(currentNodeToPrevious, nextNode);
		nextNode.connectForwardWeightedUniDirected(previousNodeToCurrent, this);
	}
	
	public void connectForwardUniDirected(GraphNode<Weights> nextNode){
		addChildNode(nextNode);
	}
	
	public void connectForwardWeightedUniDirected(Weights weight, GraphNode<Weights> nextNode){
		connectForwardUniDirected(nextNode);
		setWeightValue(nextNode.getNodeIndex(), weight);
	}
	
	public void connectForwardWeightedBidirected(Weights pWeight, GraphNode < Weights > nextNode){
		connectForwardWeightedUniDirected(pWeight, nextNode);
		nextNode.connectBackwardUniDirected(this);
	}
	
	
	public void connectForwardBiDirected(GraphNode<Weights> nextNode){
		connectForwardUniDirected(nextNode);
		nextNode.connectBackwardUniDirected(this);
	}
	
	public void connecForwardBiWeightedBiDirected(Weights nextNodeToCurrent,
													Weights currentNodeToNext, 
													GraphNode<Weights> nextNode){
		connectForwardWeightedUniDirected(currentNodeToNext, nextNode);
		nextNode.connectBackwardWeightedUniDirected(nextNodeToCurrent, this);
	}

	public Weights getWeightValue(int pWeightValue) {
		return weightValues.get(pWeightValue);
	}

	public void setWeightValue(int pIndex, Weights pWeightValue) {
		this.weightValues.put(pIndex, pWeightValue);
	}

	public GraphNode < Weights > getParentNode(int pIndex) {
		return parentNodes.get(pIndex);
	}

	private void addParentNode(GraphNode < Weights > pParentNode) {
		this.parentNodes.add(pParentNode);
		this.parentNodesIndexed.put(pParentNode.getNodeIndex(), pParentNode);
	}

	public GraphNode < Weights > getChildNode(int pIndex) {
		return childNodes.get(pIndex);
	}

	private void addChildNode(GraphNode < Weights > pChildNode) {
		this.childNodes.add(pChildNode);
		this.childNodesIndexed.put(pChildNode.getNodeIndex(), pChildNode);
	}
	
	
	
	
}
