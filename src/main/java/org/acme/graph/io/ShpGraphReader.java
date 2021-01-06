package org.acme.graph.io;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.acme.graph.model.Graph;
import org.acme.graph.model.Vertex;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

/**
 * Lecture d'un fichier d'arc correspondant au modèle ROUTE500 de l'IGN
 * 
 * @author MBorne
 *
 */
public class ShpGraphReader {

	/**
	 * Lecture du fichier shapefile
	 * 
	 * @param file
	 * @return
	 */
	public static Graph read(File file) throws Exception {
		Graph graph = new Graph();
		Map<String, Object> map = new HashMap<>();
		map.put("url", file.toURI().toURL());

		DataStore dataStore = DataStoreFinder.getDataStore(map);
		String typeName = dataStore.getTypeNames()[0];

		FeatureSource<SimpleFeatureType, SimpleFeature> dataSource = dataStore.getFeatureSource(typeName);
		Filter filter = Filter.INCLUDE;

		FeatureCollection<SimpleFeatureType, SimpleFeature> collection = dataSource.getFeatures(filter);
		try (FeatureIterator<SimpleFeature> features = collection.features()) {
			while (features.hasNext()) {
				SimpleFeature feature = features.next();

				String id = feature.getID();
				// String sens = (String)feature.getAttribute("SENS");
				LineString geometry = toLineString(feature);

				/* Création ou récupération des sommets initiaux et finaux */
				Vertex source = getOrCreateVertex(graph, geometry.getStartPoint().getCoordinate());
				Vertex target = getOrCreateVertex(graph, geometry.getEndPoint().getCoordinate());

				/* Création de l'arc pour le parcours en sens direct */
				graph.createEdge(source, target, id + "-direct");

				/* Création de l'arc pour le parcours en sens opposé */
				graph.createEdge(source, target, id + "-reverse");
			}
		}
		return graph;
	}

	/**
	 * Récupération ou création d'un sommet en assurant l'unicité
	 * 
	 * @param graph
	 * @param coordinate
	 * @return
	 */
	private static Vertex getOrCreateVertex(Graph graph, Coordinate coordinate) {
		Vertex vertex = graph.findVertex(coordinate);
		if (vertex == null) {
			/* création d'un nouveau sommet car non trouvé */
			vertex = graph.createVertex(coordinate, Integer.toString(graph.getVertices().size()));
		}
		return vertex;
	}

	/**
	 * Récupération de la géométrie de l'arc à partir de la feature
	 * 
	 * @param feature
	 * @return
	 */
	private static LineString toLineString(SimpleFeature feature) {
		Geometry geometry = (Geometry) feature.getDefaultGeometryProperty().getValue();
		if (geometry instanceof LineString) {
			return (LineString) geometry;
		} else if (geometry instanceof MultiLineString) {
			MultiLineString mls = (MultiLineString) geometry;
			return (LineString) mls.getGeometryN(0);
		} else {
			throw new RuntimeException("Unsupported geometry type : " + geometry.getGeometryType());
		}
	}
}
