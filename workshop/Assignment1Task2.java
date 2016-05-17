package workshop;

import java.awt.Color;
import java.util.Vector;
import java.util.Arrays;
import java.util.ArrayList;

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
import jv.vecmath.PdMatrix;

import jv.vecmath.PdMatrix;

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


	public PdVector searchClosestVerticesInQ(PdVector point) {
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

	public PdVector[] findClosestVerticesForSelectedPoints(PdVector[] randomVerticesInP){
		// PdVector[] randomVerticesInP = getRandomVerticesFromP(m);
		int m = randomVerticesInP.length;
		PdVector[] closestVerticesInQ = new PdVector[m];
		for (int i=0; i<m; i++) {
			closestVerticesInQ[i] = searchClosestVerticesInQ(randomVerticesInP[i]);
		}
		return closestVerticesInQ;
	}

	public double computeMedianDistanceInS(PdVector[] vInP, PdVector[] vInQ){
		double[] distance = getDistances(vInP, vInQ);
		Arrays.sort(distance);
		if(vInQ.length%2==1){
			int index = (vInQ.length+1)/2;
			return distance[index];
		}
		else{
			int index1 = vInQ.length/2;
			int index2 = index1+1;
			return (distance[index1]+distance[index2])/2;
		}
	}
	
	public double[] getDistances(PdVector[] vInP, PdVector[] vInQ){
		double[] distances = new double[vInQ.length];
		for (int i=0; i<vInQ.length; i++){
			distances[i] = vInQ[i].dist(vInQ[i]);
		}
		return distances;
	}
	
	public double[] removeDistances(double[] distances, double median, double k){
		double threshhold = median * k;
		double result[] = new double[distances.length];
		for(int i = 0; i < distances.length; i++){
			if(distances[i] <= threshhold)
				result[i] = distances[i];
			else
				result[i] = -1d;
		}
		return result;
	}
	public PdMatrix computeCovarianceMatrix(pointsPin, pointsQin, PdVector pAverage, PdVector qAverage, int n){
		
		PdVector[] pointsP = pointsPin;
		PdVector[] pointsQ = pointsQin;
		 
		PdMatrix m = new PdMatrix();
		
		for(int i = 0; i++; i < n){

			PdMatrix m_temp = new PdMatrix();
			m_temp.addJoint(subNew(pointsP[i],pAverage)),(subNew(pointsQ[i],qAverage)));
			
			m.add(m_temp);
		}

		return m;
		
	}
	
	
	public PdVector[] removeVectors(PdVector[] vectors, double[] distances){
		ArrayList<PdVector> vectorsAL = new ArrayList<PdVector>();
		for(int i = 0; i < distances.length; i++){
			if(distances[i] != -1d)
				vectorsAL.add(vectors[i]);
		}
		return (PdVector[])vectorsAL.toArray();
	}
	
	public PdVector computeCentroid(PdVector[] vectors){
		PdVector result = (PdVector)vectors[0].clone();
		for(int i =1; i < vectors.length; i++){
			result.add(vectors[i]);
		}
		result.multScalar(1/((double)vectors.length));
		return result;
	}
	
}
