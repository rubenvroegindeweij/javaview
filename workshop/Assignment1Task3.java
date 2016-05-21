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

	/** First surface to be registered. */
	PgElementSet	m_surfP;
	/** Second surface to be registered. */
	PgElementSet	m_surfQ;


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
	public PdVector projectPointOnPlane(PdVector vertex, PdVector normal){
		double a = normal.m_data[0];
		double b = normal.m_data[1];
		double c = normal.m_data[2];
		double u = vertex.m_data[0];
		double v = vertex.m_data[1];
		double w = vertex.m_data[2];
		double d = -(a*u+b*v+c*w);
		double multiplier = (a*u+b*v+c*w+d)/(a*a+b*b+c*c);
		double x = u - a * multiplier;
		double y = v - b * multiplier;
		double z = w - c * multiplier;
		return new PdVector(x, y ,z);
	}
}
