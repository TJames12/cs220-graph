package graph.impl;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import graph.IGraph;
import graph.INode;
import graph.NodeVisitor;

/**
 * A basic representation of a graph that can perform BFS, DFS, Dijkstra,
 * and Prim-Jarnik's algorithm for a minimum spanning tree.
 * 
 * @author jspacco
 *
 */
public class Graph implements IGraph
{
    
	private Map<String, INode> nodes = new HashMap<>();
	
    /**
     * Return the {@link Node} with the given name.
     * 
     * If no {@link Node} with the given name exists, create
     * a new node with the given name and return it. Subsequent
     * calls to this method with the same name should
     * then return the node just created.
     * 
     * @param name
     * @return
     */
    public INode getOrCreateNode(String name) {
    	
        if (!nodes.containsKey(name)) {   
        	INode node = new Node(name);
        	nodes.put(name, node);
        }
        
        return nodes.get(name);
    }

    /**
     * Return true if the graph contains a node with the given name,
     * and false otherwise.
     * 
     * @param name
     * @return
     */
    public boolean containsNode(String name) {
        return nodes.containsKey(name);
    }

    /**
     * Return a collection of all of the nodes in the graph.
     * 
     * @return
     */
    public Collection<INode> getAllNodes() {
        return nodes.values();
    }
    
    //SEARCHING ALGORITHMS
    
    /**
     * Perform a breadth-first search on the graph, starting at the node
     * with the given name. The visit method of the {@link NodeVisitor} should
     * be called on each node the first time we visit the node.
     * 
     * 
     * @param startNodeName
     * @param v
     */
    public void breadthFirstSearch(String startNodeName, NodeVisitor v)
    {
        Set<INode> visited = new HashSet<>();
        Queue<INode> toVisit = new LinkedList<INode>();
        toVisit.add(this.getOrCreateNode(startNodeName));
        
        while(!toVisit.isEmpty()) { //Go through all nodes in queue
        	INode curr = toVisit.poll();
        	
        	if(visited.contains(curr)) //If node has already been visited
        		continue;
        	
        	v.visit(curr); //Visit node and mark it as visited
        	visited.add(curr);
        	
        	for(INode temp : curr.getNeighbors()) //Add node's neighbors to the queue
        		if(!visited.contains(temp))
        			toVisit.add(temp);
        }
    }
      
    /**
     * Perform a depth-first search on the graph, starting at the node
     * with the given name. The visit method of the {@link NodeVisitor} should
     * be called on each node the first time we visit the node.
     * 
     * 
     * @param startNodeName
     * @param v
     */
    public void depthFirstSearch(String startNodeName, NodeVisitor v)
    {
    	Set<INode> visited = new HashSet<>();
        Stack<INode> toVisit = new Stack<>();
        toVisit.push(this.getOrCreateNode(startNodeName));
        
        while(!toVisit.isEmpty()) { //Go through all nodes in stack
        	INode curr = toVisit.pop();
        	
        	if(visited.contains(curr)) //If node has already been visited
        		continue;
        	
        	v.visit(curr); //Visit node and mark it as visited
        	visited.add(curr);
        	
        	for(INode temp : curr.getNeighbors()) //Add node's neighbors to the stack
        		if(!visited.contains(temp))
        			toVisit.push(temp);
        }
    }

    /**
     * Perform Dijkstra's algorithm for computing the cost of the shortest path
     * to every node in the graph starting at the node with the given name.
     * Return a mapping from every node in the graph to the total minimum cost of reaching
     * that node from the given start node.
     * 
     * <b>Hint:</b> Creating a helper class called Path, which stores a destination
     * (String) and a cost (Integer), and making it implement Comparable, can be
     * helpful. Well, either than or repeated linear scans.
     * 
     * @param startName
     * @return
     */
    public Map<INode,Integer> dijkstra(String startName) {
        Map<INode, Integer> result = new HashMap<>();
        PriorityQueue<Path> toDo = new PriorityQueue<>();
        
        toDo.add(new Path(getOrCreateNode(startName), 0)); //Create first path from given starting node and a weight of 0
        
        while(result.size() < nodes.size()) { //Continue until all nodes have been visited and are in the new tree
        	
        	Path nextPath = toDo.poll(); //Get the path from the queue
        	INode curr = nextPath.getNode(); //Get the node from the path
        	
        	if(result.containsKey(curr)) //If node has already been visited
        		continue;
        	
        	int cost = nextPath.getCost();
        	
        	result.put(curr, cost); //Add the visited node and it's cost into the map as a path
        	
        	for(INode node : curr.getNeighbors()) //Add neighbors into the queue
        		toDo.add(new Path(node, cost + curr.getWeight(node)));
        }
  
        return result;
    }

 
    /**
     * Perform Prim-Jarnik's algorithm to compute a Minimum Spanning Tree (MST).
     * 
     * The MST is itself a graph containing the same nodes and a subset of the edges 
     * from the original graph.
     * 
     * @return
     */
    public IGraph primJarnik() {
        IGraph result = new Graph();
        PriorityQueue<Edge> toDo = new PriorityQueue<>();
        
        INode startNode = nodes.values().iterator().next(); //Start at a "random" node in the graph
        
        for(INode node : startNode.getNeighbors()) //Add all edges from the start node to it's neighbors into the queue 
        	toDo.add(new Edge(startNode, node, startNode.getWeight(node)));
        
        while (result.getAllNodes().size() < this.getAllNodes().size()) { //Continue until all nodes have been visited and are in the new graph
        	Edge nextEdge = toDo.poll();
        	
        	INode start = nextEdge.startNode; //Retrieve start and end nodes from the current edge
        	String startName = start.getName();
        	INode end = nextEdge.endNode;
        	String endName = end.getName();
        	
        	if(result.containsNode(startName) && result.containsNode(endName)) //If the nodes are already accounted for
        		continue;
        	
        	INode finStart = result.getOrCreateNode(startName); 
        	INode finEnd = result.getOrCreateNode(endName);
        	finStart.addUndirectedEdgeToNode(finEnd, nextEdge.cost); //Add the final start and end nodes into the new graph
        	
        	for(INode node : end.getNeighbors()) //Add neighbors to the queue
        		if(!node.equals(start))
        			toDo.add(new Edge(end,node,end.getWeight(node)));
 
        }
        
        return result;
    }
    
    
    
    
    
    

}