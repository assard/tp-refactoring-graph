package org.acme.graph.controllers;

import org.acme.graph.model.Graph;
import org.acme.graph.model.Path;
import org.acme.graph.model.Vertex;
import org.acme.graph.routing.DijkstraPathFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FindPathController {

	@Autowired
	private Graph graph;

	@RequestMapping("/")
	public String hello() {
		return "La documentation swagger de l'API devrait apparaître ici (voir springfox-swagger)...";
	}

	@RequestMapping("/find-path")
	public Path findPath(
		@RequestParam(value = "origin", required = true)
		String originId,
		@RequestParam(value = "destination", required = true)
		String destinationId
	) {
		DijkstraPathFinder pathFinder = new DijkstraPathFinder(graph);
		Vertex origin = graph.findVertex(originId);
		Vertex destination = graph.findVertex(destinationId);
		return pathFinder.findPath(origin, destination);
	}

}
