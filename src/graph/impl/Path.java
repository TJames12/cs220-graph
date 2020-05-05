package graph.impl;

import graph.INode;

public class Path implements Comparable<Path>{
	
	private INode destination;
	private int cost;
	
	public Path(INode destination, int cost) {
		this.destination = destination;
		this.cost = cost;
	}
	
	public int compareTo(Path other) {
		return this.cost - other.cost;
	}
	
	public INode getNode() {
		return this.destination;
	}
	
	public int getCost() {
		return this.cost;
	}
}
