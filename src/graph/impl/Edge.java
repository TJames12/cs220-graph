package graph.impl;

import graph.INode;

public class Edge implements Comparable<Edge>{
	
	INode startNode;
	INode endNode;
	int cost;
	
	public Edge(INode startNode, INode endNode, int cost) {
		this.startNode = startNode;
		this.endNode = endNode;
		this.cost = cost;
	}
	
	public int compareTo(Edge other) {
		return this.cost - other.cost;
	}
	
	

}
