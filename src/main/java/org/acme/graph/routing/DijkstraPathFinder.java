package org.acme.graph.routing;

import org.acme.graph.model.Edge;
import org.acme.graph.model.Graph;
import org.acme.graph.model.Path;
import org.acme.graph.model.Vertex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * Utilitaire pour le calcul du plus court chemin dans un graphe
 * 
 * @author MBorne
 *
 */
public class DijkstraPathFinder {

	private static final Logger log = LogManager.getLogger(DijkstraPathFinder.class);

	private Graph graph;

	public DijkstraPathFinder(Graph graph) {
		this.graph = graph;
	}

	/**
	 * Calcul du plus court chemin entre une origine et une destination
	 * 
	 * @param origin
	 * @param destination
	 * @return
	 */
	public Path findPath(Vertex origin, Vertex destination) {
		log.info("findPath({},{})...",origin,destination);
		initGraph(origin);
		Vertex current;
		Path path = new Path();
		while ((current = findNextVertex()) != null) {
			visit(current);
			if (destination.getReachingEdge() != null) {
				log.info("findPath({},{}) : path found",origin,destination);
				return buildPath(destination,path);
			}
		}
		log.info("findPath({},{}) : path not found",origin,destination);
		return path;
	}

	/**
	 * Parcourt les arcs sortants pour atteindre les sommets avec le meilleur coût
	 * 
	 * @param vertex
	 */
	private void visit(Vertex vertex) {
		log.trace("visit({})", vertex);
		/*
		 * On étudie chacun des arcs sortant pour atteindre de nouveaux sommets ou
		 * mettre à jour des sommets déjà atteint si on trouve un meilleur coût
		 */
		for (Edge outEdge : vertex.getOutEdges()) {
			Vertex reachedVertex = outEdge.getTarget();
			/*
			 * Convervation de arc permettant d'atteindre le sommet avec un meilleur coût
			 * sachant que les sommets non atteint ont pour coût "POSITIVE_INFINITY"
			 */
			double newCost = vertex.getCost() + outEdge.getCost();
			if (newCost < reachedVertex.getCost()) {
				reachedVertex.setCost(newCost);
				reachedVertex.setReachingEdge(outEdge);
			}
		}
		/*
		 * On marque le sommet comme visité
		 */
		vertex.setVisited(true);
	}

	/**
	 * Construit le chemin en remontant les relations incoming edge
	 * 
	 * @param target
	 * @return
	 */
	private Path buildPath(Vertex target, Path path) {

		Edge current = target.getReachingEdge();
		do {
			path.addEdge(current);
			current = current.getSource().getReachingEdge();
		} while (current != null);

		path.reverseEdges();
		return path;
	}

	/**
	 * Prépare le graphe pour le calcul du plus court chemin
	 * 
	 * @param source
	 */
	private void initGraph(Vertex source) {
		log.trace("initGraph({})", source);
		for (Vertex vertex : graph.getVertices()) {
			vertex.setCost(source == vertex ? 0.0 : Double.POSITIVE_INFINITY);
			vertex.setReachingEdge(null);
			vertex.setVisited(false);
		}
	}

	/**
	 * Recherche le prochain sommet à visiter. Dans l'algorithme de Dijkstra, ce
	 * sommet est le sommet non visité le plus proche de l'origine du calcul de plus
	 * court chemin.
	 * 
	 * @return
	 */
	private Vertex findNextVertex() {
		double minCost = Double.POSITIVE_INFINITY;
		Vertex result = null;
		for (Vertex vertex : graph.getVertices()) {
			// sommet déjà visité?
			if (vertex.isVisited()) {
				continue;
			}
			// sommet non atteint?
			if (vertex.getCost() == Double.POSITIVE_INFINITY) {
				continue;
			}
			// sommet le plus proche de la source?
			if (vertex.getCost() < minCost) {
				result = vertex;
			}
		}
		return result;
	}

}
