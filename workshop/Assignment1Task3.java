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
}
