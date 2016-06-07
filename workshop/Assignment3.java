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
		// the code for smoothing goes here
	}

	public void undo() {
		m_geom.setVertices(m_geomSave.getVertices());
	}
}