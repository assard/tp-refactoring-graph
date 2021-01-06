package org.acme.graph.model;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

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
	
	private LineString geometry;

	public Edge(Vertex source, Vertex target){
		if(source == null || target == null) {
			System.out.println("error : you can't set the source or target of an edge at null");
			return;
		}
		this.source = source;
		this.target = target;
		source.getOutEdges().add(this);
		target.getInEdges().add(this);
		GeometryFactory gf = new GeometryFactory();
        this.geometry = (LineString)gf.createLineString(new Coordinate[] {
            getSource().getCoordinate(),
            getTarget().getCoordinate()
        });
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	@JsonIdentityInfo(
        generator=ObjectIdGenerators.PropertyGenerator.class, 
        property="id"
    )
    @JsonIdentityReference(alwaysAsId=true)
	public Vertex getSource() {
		return source;
	}
	
	@JsonIdentityInfo(
	    generator=ObjectIdGenerators.PropertyGenerator.class, 
	    property="id"
	)
	@JsonIdentityReference(alwaysAsId=true)
	public Vertex getTarget() {
		return target;
	}

	/**
	 * dijkstra - coût de parcours de l'arc (distance géométrique)
	 * 
	 * @return
	 */
	public double getCost() {
		return geometry.getLength();
	}

	@Override
	public String toString() {
		return id + " (" + source + "->" + target + ")";
	}
	
	@JsonSerialize(using = GeometrySerializer.class)
	public LineString getGeometry() {
		return this.geometry;
	}
	
	public void setGeometry(LineString geometry) {
		this.geometry = geometry;
	}

}
