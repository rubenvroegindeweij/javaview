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

public class Assignment2 extends PjWorkshop {

	PgElementSet m_geom;
	PgElementSet m_geomSave;

	public Assignment2() {
		super("Assignment 2");
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
	
	public PdMatrix getGradientMatrix(PiVector triangle){
		PdVector p1 = m_geom.getVertex(triangle.getEntry(0));
		PdVector p2 = m_geom.getVertex(triangle.getEntry(1));
		PdVector p3 = m_geom.getVertex(triangle.getEntry(2));
		
		// http://math.stackexchange.com/questions/305642/how-to-find-surface-normal-of-a-triangle
		PdVector v = PdVector.subNew(p2, p1);
		PdVector w = PdVector.subNew(p3, p1);

		double nX = (v.getEntry(1)*w.getEntry(2) - v.getEntry(2)*w.getEntry(1));
		double nY = (v.getEntry(2)*w.getEntry(0) - v.getEntry(0)*w.getEntry(2));
		double nZ = (v.getEntry(0)*w.getEntry(1) - v.getEntry(1)*w.getEntry(0));

		PdVector n = new PdVector(nX, nY, nZ);
		n.normalize();

		PdVector e1 = PdVector.subNew(p3, p2);
		PdVector e2 = PdVector.subNew(p1, p3);
		PdVector e3 = PdVector.subNew(p2, p1);

		double area = PdVector.crossNew(v, w).length()/2;

		PdMatrix gradientMatrix = new PdMatrix(3);
		gradientMatrix.setColumn(0, PdVector.crossNew(n, e1));
		gradientMatrix.setColumn(1, PdVector.crossNew(n, e2));
		gradientMatrix.setColumn(2, PdVector.crossNew(n, e3));
		gradientMatrix.multScalar(1d/(2*area));
		
		return gradientMatrix;
	}
}