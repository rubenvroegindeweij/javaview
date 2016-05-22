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

import Jama.Matrix;
import Jama.SingularValueDecomposition;

/**
 *  Workshop for surface registration
 */

public class Assignment1Task3 extends Assignment1Task2 {

	/** Constructor */
	public Assignment1Task3() {
		super("Assignment 1 Task 3");
		if (getClass() == Assignment1Task3.class) {
			init();
		}
	}

	/** Initialization */
	public void init() {
		super.init();
	}
	
	//http://www.9math.com/book/projection-point-plane
	//https://en.wikipedia.org/wiki/Plane_(geometry)
	public PdVector projectPointOnPlane(PdVector vertexP, PdVector vertexQ, PdVector normalQ){
		double xQ = vertexQ.m_data[0];
		double yQ = vertexQ.m_data[1];
		double zQ = vertexQ.m_data[2];
		// Plane equation.
		double a = normalQ.m_data[0];
		double b = normalQ.m_data[1];
		double c = normalQ.m_data[2];
		double d = -(a*xQ+b*yQ+c*zQ);
		// Projection of a point on a plane.
		double xP = vertexQ.m_data[0];
		double yP = vertexQ.m_data[1];
		double zP = vertexQ.m_data[2];
		double multiplier = (a*xP+b*yP+c*zP+d)/(a*a+b*b+c*c);
		double x = xP - a * multiplier;
		double y = yP - b * multiplier;
		double z = zP - c * multiplier;
		return new PdVector(x, y ,z);
	}
	
	public PdVector searchClosestVerticesInQ(PdVector point) {
		PdVector[] pointsQ = m_surfQ.getVertices();
		PdVector[] normalsQ = m_surfQ.getVertexNormals();
		PdVector minV = projectPointOnPlane(point, pointsQ[0], normalsQ[0]);
		double min = minV.dist(point);
		for (int i = 1; i < m_surfQ.getNumVertices(); i++) {
			PdVector curPoint = projectPointOnPlane(point, pointsQ[i], normalsQ[i]);
			double distance = curPoint.dist(point);
			if (min > distance) {
				min = distance;
				minV = curPoint;
			}
		}
		return minV;
	}
}
