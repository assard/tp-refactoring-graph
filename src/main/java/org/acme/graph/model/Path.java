package org.acme.graph.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Path {
	
	private List<Edge> edges;
	
	public Path() {
		this.edges = new ArrayList<Edge>();
	}
	
	public void addEdge(Edge edge) {
		this.edges.add(edge);
	}
	
	public void reverseEdges() {
		Collections.reverse(this.edges);
	}
	
	public boolean isEmpty() {
		return this.edges.size() == 0;
	}
	
	public String toString() {
		String str = "";
		for(Edge edge : this.edges) {
			str += edge.getSource().getId() + " -> " + edge.getTarget().getId() + "\n";
		}
		return str;
	}
	
	public int size() {
		return this.edges.size();
	}
	
	public Edge getEdgeN(int n) {
		return this.edges.get(n);
	}

}
