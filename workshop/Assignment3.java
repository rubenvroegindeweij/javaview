package workshop;

import java.awt.Color;
import java.util.Arrays;
import java.util.Random;

import jv.geom.PgElementSet;
import jv.project.PgGeometry;
import jv.vecmath.PdVector;
import jv.vecmath.PiVector;
import jvx.project.PjWorkshop;
import jv.vecmath.PdMatrix;

import jv.object.PsDebug;
import jvx.numeric.PnSparseMatrix;
import java.util.ArrayList;

import dev6.numeric.PnMumpsSolver;
import jvx.numeric.PnSparseMatrix;
import jv.object.PsObject;
import jvx.geom.PgVertexStar;

public class Assignment3 extends PjWorkshop {

	PgElementSet m_geom;
	PgElementSet m_geomSave;
	double stepwidth;

	public Assignment3() {
		super("Assignment 3");
		init();
	}

	@Override
	public void setGeometry(PgGeometry geom) {
		super.setGeometry(geom);
		m_geom 		= (PgElementSet)super.m_geom;
		m_geomSave 	= (PgElementSet)super.m_geomSave;
	}

	public void init() {
		super.init();
	}

	// Change vertices based on the new x, y and z coordinates.
	/*
	public void changeVertices() {
		for (int i = 0; i < xNew.getSize(); i++) {
			m_geom.setVertex(i, xNew.getEntry(i), yNew.getEntry(i), zNew.getEntry(i));
		}
	}
	*/

	// Smooth the surface based on input stepwidth.
	public void smoothSurface(double stepwidth) throws Exception {
		this.stepwidth = stepwidth;
		// ======== the code for smoothing goes under here ========
		
		// Generate a PiVector[] which contains a list of adjacent vertices for each vertex of the geometry
		/*
		PiVector[] NeighbouringVertices = makeVertexNeighbours(m_geom);
		int numOfVertices = m_geom.getNumVertices();
		PsDebug.message("Length of NeighbouringVertices[]: " + Integer.toString(NeighbouringVertices.length));
		PsDebug.message("Size of numOfVertices" + Integer.toString(numOfVertices));
		*/

		// for each vertice, find its neighbors

		// compute the average coordinate of its neighbors

		// find the difference between the avg coordiante and the original one

		// multiply with stepwidth

		// compute the new coordinates

		// change the original model
	}

	public void undo() {
		m_geom.setVertices(m_geomSave.getVertices());
	}
}