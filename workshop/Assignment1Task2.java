package workshop;

import java.awt.Color;
import java.util.Vector;

import jv.geom.PgBndPolygon;
import jv.geom.PgElementSet;
import jv.geom.PgPolygonSet;
import jv.geom.PgVectorField;
import jv.geom.PuCleanMesh;
import jv.number.PdColor;
import jv.object.PsConfig;
import jv.object.PsDebug;
import jv.object.PsObject;
import jv.project.PgGeometry;
import jv.vecmath.PdVector;
import jv.vecmath.PiVector;
import jv.vecmath.PuMath;
import jv.viewer.PvDisplay;
import jv.project.PvGeometryIf;

import jvx.project.PjWorkshop;

/**
 *  Workshop for surface registration
 */

public class Assignment1Task2 extends PjWorkshop {

	/** First surface to be registered. */
	PgElementSet	m_surfP;
	/** Second surface to be registered. */
	PgElementSet	m_surfQ;


	/** Constructor */
	public Assignment1Task2() {
		super("Assignment 1 Task 2");
		if (getClass() == Assignment1Task2.class) {
			init();
		}
	}

	/** Initialization */
	public void init() {
		super.init();
	}


	/** Set two Geometries. */
	public void setGeometries(PgElementSet surfP, PgElementSet surfQ) {
		m_surfP = surfP;
		m_surfQ = surfQ;
	}


	public PdVector searchClosestVertices(PdVector point) {

		PdVector[] pointsQ = m_surfQ.getVertices();
		PdVector minV = pointsQ[0];
		double min = pointsQ[0].dist(point);
		for (int i = 1; i < m_surfQ.getNumVertices(); i++) {
			PdVector curPoint = pointsQ[i];

			double distance = curPoint.dist(point);
			if (min > distance) {
				min = distance;
				minV = curPoint;
			}
		}
		return minV;
	}

	public PdVector[] getRandomVerticesFromP(int n){
		if(n > m_surfP.getNumVertices())
			n = m_surfP.getNumVertices();
		 PdVector[] vertices = m_surfP.getVertices();
		 PdVector[] randomVertices = new PdVector[n];
		 for(int i = 0; i < n; i++){
			 int randomNumber = (int)(Math.random()*(m_surfP.getNumVertices()-1));
			 randomVertices[i] = vertices[randomNumber];
		 }
		 return randomVertices;
	}
	
	public PdVector[] getClosestVerticesFromQ(PdVector[] vertices){
		PdVector[] closestVertices = new PdVector[vertices.length];
		for(int i = 0; i < vertices.length; i++){
			closestVertices[i] = searchClosestVertices(vertices[i]);
		}
		return closestVertices;
	}
	
	public double[] getDistances(PdVector[] set1, PdVector[] set2){
		if(set1.length != set2.length)
			return null;
		double[] result = new double[set1.length];
		for(int i = 0; i < set1.length; i++){
			result[i] = PdVector.dist(set1[i], set2[i]);
		}
		return result;
	}
}
