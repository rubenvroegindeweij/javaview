package workshop;

import java.awt.Color;
import java.util.Random;

import jv.geom.PgElementSet;
import jv.project.PgGeometry;
import jv.vecmath.PdVector;
import jv.vecmath.PiVector;
import jvx.project.PjWorkshop;

public class Assignment1Task1 extends PjWorkshop {

	PgElementSet m_geom;
	PgElementSet m_geomSave;
	
	public Assignment1Task1() {
		super("Assignment 1 Task 1");
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
	
	public int computeGenus() {
		int nov = m_geom.getNumVertices();
		int noe = m_geom.getNumEdges();
		int nof = m_geom.getNumElements();
		return 1 - (nov - noe + nof) / 2;
	}
}