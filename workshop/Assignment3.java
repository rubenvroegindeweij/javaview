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

	// smooth the surface based on input stepwidth
	public void iterativeAveraging(double stepwidth) throws Exception {
		this.stepwidth = stepwidth;
		// make a copy of the original model, and store the vertices in an array
		PgElementSet clonedGeom = (PgElementSet) m_geom.clone();
		PdVector[] newVertices = (PdVector[]) clonedGeom.getVertices();
		// for each vertice, find its neighbours and store them in an array
		PiVector[] NeighbouringVertices = PgVertexStar.makeVertexNeighbours(m_geom);
		int numOfVertices = m_geom.getNumVertices();
		for (int i = 0; i < numOfVertices; i++) {
			PdVector currentVertex = m_geom.getVertex(i);
			PdVector tempVertex = new PdVector(0d, 0d, 0d);
			int numOfNeighbours = NeighbouringVertices[i].getSize();
			for (int j = 0; j < numOfNeighbours; j++) {
				PdVector oneNeighbour = m_geom.getVertex(NeighbouringVertices[i].getEntry(j));
				tempVertex.add(oneNeighbour);
			}
			// compute the average coordinate of its neighbors
			tempVertex.multScalar(1d / (double)numOfNeighbours);
			// find the difference between the average coordiante and the original one
			tempVertex.sub(currentVertex);
			// multiply with stepwidth
			tempVertex.multScalar(stepwidth);
			// calculate the new coordinates
			newVertices[i].add(tempVertex);
		}
		// update the model with the new vertices
		m_geom.setVertices(newVertices);
	}

	public void explicitMCF(double stepwidth) throws Exception {

	}

	public void implicitMCF(double stepwidth) throws Exception {

	}

	public void reset() {
		m_geom.setVertices(m_geomSave.getVertices());
	}
}