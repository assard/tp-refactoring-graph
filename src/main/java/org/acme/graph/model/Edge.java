package org.acme.graph.model;

/**
 * 
 * Un arc matérialisé par un sommet source et un sommet cible
 * 
 * @author MBorne
 *
 */
public class Edge {
	/**
	 * Identifiant de l'arc
	 */
	private String id;

	/**
	 * Sommet initial
	 */
	private Vertex source;

	/**
	 * Sommet final
	 */
	private Vertex target;

	public Edge(Vertex source, Vertex target){
		if(source == null || target == null) {
			System.out.println("error : you can't set the source or target of an edge at null");
			return;
		}
		this.source = source;
		this.target = target;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Vertex getSource() {
		return source;
	}

	public Vertex getTarget() {
		return target;
	}

	/**
	 * dijkstra - coût de parcours de l'arc (distance géométrique)
	 * 
	 * @return
	 */
	public double getCost() {
		return source.getCoordinate().distance(target.getCoordinate());
	}

	@Override
	public String toString() {
		return id + " (" + source + "->" + target + ")";
	}

}
