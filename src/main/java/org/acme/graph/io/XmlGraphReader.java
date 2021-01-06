package org.acme.graph.io;

import java.io.File;

import org.acme.graph.model.Graph;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * 
 * Lecture d'un graphe dans un format XML ad-hoc
 * 
 * (voir /src/test/resources/graph/*.xml pour des exemples)
 * 
 * @author MBorne
 *
 */
public class XmlGraphReader {

	/**
	 * Lecture d'un graphe dans un fichier XML spécifique à l'application
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static Graph read(File file) throws Exception {
		SAXBuilder saxBuilder = new SAXBuilder();
		Document document = saxBuilder.build(file);

		Element root = document.getRootElement();

		Graph graph = new Graph();
		// parse vertices
		{
			XPath xpath = XPath.newInstance("./vertices/vertex");
			for (Object node : xpath.selectNodes(root)) {
				Element vertexElement = (Element) node;
				graph.createVertex(
						new Coordinate(Double.valueOf(vertexElement.getAttribute("x").getValue()),
						Double.valueOf(vertexElement.getAttribute("y").getValue())),
						vertexElement.getAttribute("id").getValue());
			}
		}

		// parse edges
		{
			XPath xpath = XPath.newInstance("./edges/edge");
			for (Object node : xpath.selectNodes(root)) {
				Element edgeElement = (Element) node;
				
				String sourceId = edgeElement.getAttribute("source").getValue();
				String targetId = edgeElement.getAttribute("target").getValue();
				
				graph.createEdge(graph.findVertex(sourceId),
								graph.findVertex(targetId),
								edgeElement.getAttribute("id").getValue());

			}
		}

		return graph;
	}

}
