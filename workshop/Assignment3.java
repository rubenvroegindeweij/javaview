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
	PnSparseMatrix LaplacianMatrix;
	PdVector newVertices_x;
	PdVector newVertices_y;
	PdVector newVertices_z;
	
	PnSparseMatrix gradientMatrix;
	PnSparseMatrix Mv;
	PnSparseMatrix M;
	PnSparseMatrix S;
	PnSparseMatrix L;

	PdVector xSolved;
	PdVector ySolved;
	PdVector zSolved;
	
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
	
	// Fills the gradient matrix G (3m by n) by all the small gradient matrices and also fills the Mv (3m by 3m) matrix with triangle areas on the diagonal.
		public PnSparseMatrix getGradientMatrix() {
			PiVector[] elements = m_geom.getElements();
			int sizeElements = m_geom.getNumElements();
			int numOfVertices = m_geom.getNumVertices();

			gradientMatrix = new PnSparseMatrix((sizeElements * 3), numOfVertices, 3);
			M = new PnSparseMatrix(numOfVertices, numOfVertices, 1);
			Mv = new PnSparseMatrix((sizeElements * 3), (sizeElements * 3), 1);

			for (int i = 0; i < elements.length; i++) {

				PdMatrix temp = getSmallGradientMatrixAndFillMv(elements[i], i);

				for (int j = 0; j < 3; j++) {

					PdVector tempColumn  = temp.getColumn(j);

					int startPos = (i * 3);

					double x = tempColumn.getEntry(0);
					double y = tempColumn.getEntry(1);
					double z = tempColumn.getEntry(2);

					int currentVertexIndex = elements[i].getEntry(j);

					gradientMatrix.addEntry(startPos, currentVertexIndex, x);
					gradientMatrix.addEntry(startPos + 1, currentVertexIndex, y);
					gradientMatrix.addEntry(startPos + 2, currentVertexIndex, z);
				}

			}
			return gradientMatrix;
		}

		// Computes for every triangle the small gradient matrix (3 by 3) and fills for every triangle the Mv.
		public PdMatrix getSmallGradientMatrixAndFillMv(PiVector triangle, int currentTriangleIndex) {
			PdVector p1 = m_geom.getVertex(triangle.getEntry(0));
			PdVector p2 = m_geom.getVertex(triangle.getEntry(1));
			PdVector p3 = m_geom.getVertex(triangle.getEntry(2));

			// http://math.stackexchange.com/questions/305642/how-to-find-surface-normal-of-a-triangle
			PdVector v = PdVector.subNew(p2, p1);
			PdVector w = PdVector.subNew(p3, p1);

			double nX = (v.getEntry(1) * w.getEntry(2) - v.getEntry(2) * w.getEntry(1));
			double nY = (v.getEntry(2) * w.getEntry(0) - v.getEntry(0) * w.getEntry(2));
			double nZ = (v.getEntry(0) * w.getEntry(1) - v.getEntry(1) * w.getEntry(0));

			PdVector n = new PdVector(nX, nY, nZ);
			n.normalize();

			PdVector e1 = PdVector.subNew(p3, p2);
			PdVector e2 = PdVector.subNew(p1, p3);
			PdVector e3 = PdVector.subNew(p2, p1);

			double area = PdVector.crossNew(v, w).length() / 2;

			PdMatrix triangleGradientMatrix = new PdMatrix(3);
			triangleGradientMatrix.setColumn(0, PdVector.crossNew(n, e1));
			triangleGradientMatrix.setColumn(1, PdVector.crossNew(n, e2));
			triangleGradientMatrix.setColumn(2, PdVector.crossNew(n, e3));
			triangleGradientMatrix.multScalar(1d / (2 * area));

			
			double inputM1 = (area/3) + M.getEntry(triangle.getEntry(0),triangle.getEntry(0));
			M.setEntry(triangle.getEntry(0), triangle.getEntry(0), inputM1);
			
			double inputM2 = (area/3) + M.getEntry(triangle.getEntry(1),triangle.getEntry(1));
			M.setEntry(triangle.getEntry(1), triangle.getEntry(1), inputM2);
			
			double inputM3 = (area/3) + M.getEntry(triangle.getEntry(2),triangle.getEntry(2));
			M.setEntry(triangle.getEntry(2), triangle.getEntry(2), inputM3);
			
			// Filles the Mv matrix with area values.
			int startMv = (currentTriangleIndex * 3);
			Mv.addEntry(startMv, startMv, area);
			Mv.addEntry(startMv + 1, startMv + 1, area);
			Mv.addEntry(startMv + 2, startMv + 2, area);

			return triangleGradientMatrix;
		}


	public void explicitMCF(double stepwidth) throws Exception {
		
		calculateMatrices();
		this.stepwidth = stepwidth;
		// make a copy of the original model, and store the vertices in an array
		PgElementSet clonedGeom = (PgElementSet) m_geom.clone();
		PdVector[] newVertices = (PdVector[]) clonedGeom.getVertices();
		int numOfVertices = m_geom.getNumVertices();

		LaplacianMatrix = L;	

		newVertices_x = new PdVector(numOfVertices);
		newVertices_y = new PdVector(numOfVertices);
		newVertices_z = new PdVector(numOfVertices);
		for (int i = 0; i < numOfVertices; i++) {
			newVertices_x.setEntry(i, m_geom.getVertex(i).getEntry(0));
			newVertices_y.setEntry(i, m_geom.getVertex(i).getEntry(1));
			newVertices_z.setEntry(i, m_geom.getVertex(i).getEntry(2));
		}

		PdVector tempNewVertices_x = PnSparseMatrix.rightMultVector(LaplacianMatrix, newVertices_x, new PdVector());
		tempNewVertices_x.multScalar(stepwidth);
		newVertices_x.sub(tempNewVertices_x);
		PdVector tempNewVertices_y = PnSparseMatrix.rightMultVector(LaplacianMatrix, newVertices_y, new PdVector());
		tempNewVertices_y.multScalar(stepwidth);
		newVertices_y.sub(tempNewVertices_y);
		PdVector tempNewVertices_z = PnSparseMatrix.rightMultVector(LaplacianMatrix, newVertices_z, new PdVector());
		tempNewVertices_z.multScalar(stepwidth);
		newVertices_z.sub(tempNewVertices_z);

		for (int i = 0; i < numOfVertices; i++) {
			m_geom.setVertex(i, newVertices_x.getEntry(i), newVertices_y.getEntry(i), newVertices_z.getEntry(i));
		}
	}
	
	public void calculateMatrices(){
		getGradientMatrix();
		
	//	PsDebug.message("G Matrix: ");
		
		int numOfElements = m_geom.getNumElements();
		
		PnSparseMatrix GTMv = PnSparseMatrix.multMatrices(gradientMatrix.transposeNew(), Mv, new PnSparseMatrix());
	//	PsDebug.message("GTMV Matrix: ");
		S = PnSparseMatrix.multMatrices(GTMv, gradientMatrix, new PnSparseMatrix());
	//	PsDebug.message("S Matrix: -- " + S.getNumCols() + " X " + S.getNumRows());
	//	PsDebug.message("Mv Matrix: -- " + Mv.getNumCols() + " X " + Mv.getNumRows());
		PnSparseMatrix MInverse = new PnSparseMatrix(M.getNumRows(), M.getNumCols(), 1);
		
		//calculate M-inverse
		for(int i = 0; i< M.getNumCols();i++){
			
			double newValue = (1.0/M.getEntry(i,i));
			
			MInverse.setEntry(i, i, newValue);
			
		}
		//PsDebug.message("M inverse: -- " + MInverse.getNumCols() + " X " + MInverse.getNumRows());
		//PsDebug.message("Minverse Matrix: ");
		L = PnSparseMatrix.multMatrices(MInverse, S, new PnSparseMatrix());
		//PsDebug.message("L Matrix: ");
		
	}

	// Changes vertices based on the computed x, y and z values.
		public void changeVertices() {
			for (int i = 0; i < xSolved.getSize(); i++) {
				m_geom.setVertex(i, xSolved.getEntry(i), ySolved.getEntry(i), zSolved.getEntry(i));
			}
		}

	public void implicitMCF(double stepwidth) throws Exception {
		
		PsDebug.message("Calculating matrix values ....");
		this.stepwidth = stepwidth;
		calculateMatrices();
		
		// make a copy of the original model, and store the vertices in an array
		PgElementSet clonedGeom = (PgElementSet) m_geom.clone();
		PdVector[] newVertices = (PdVector[]) clonedGeom.getVertices();
		
		int numOfVertices = m_geom.getNumVertices();
		
		//construct A = M + tS
		PnSparseMatrix A = new PnSparseMatrix(numOfVertices,numOfVertices);
		A.copy(S);
		A.multScalar(stepwidth);
		A.add(M);
		 
		// construct three vectors containing coordinate system specific values from every vertex
		PdVector x = new PdVector(numOfVertices);
		PdVector y = new PdVector(numOfVertices);
		PdVector z = new PdVector(numOfVertices);
		
		for (int i = 0; i < numOfVertices; i++) {
			x.setEntry(i, m_geom.getVertex(i).getEntry(0));
			y.setEntry(i, m_geom.getVertex(i).getEntry(1));
			z.setEntry(i, m_geom.getVertex(i).getEntry(2));
		}
		
		//B = Mx (x depending on the coördinate system)
		PdVector Bx = PnSparseMatrix.rightMultVector(M, x, new PdVector());
		PdVector By = PnSparseMatrix.rightMultVector(M, y, new PdVector());
		PdVector Bz = PnSparseMatrix.rightMultVector(M, z, new PdVector());
		
		xSolved = new PdVector(numOfVertices);
		ySolved = new PdVector(numOfVertices);
		zSolved = new PdVector(numOfVertices);
		
		long factorization = PnMumpsSolver.factor(A, PnMumpsSolver.Type.UNSYMMETRIC);
		PnMumpsSolver.solve(factorization, xSolved, Bx);
		PnMumpsSolver.solve(factorization, ySolved, By);
		PnMumpsSolver.solve(factorization, zSolved, Bz);
		
		//PnMumpsSolver.solve(A, xSolved, Bx, PnMumpsSolver.Type.GENERAL_SYMMETRIC);
		//PnMumpsSolver.solve(A, ySolved, By, PnMumpsSolver.Type.GENERAL_SYMMETRIC);
		//PnMumpsSolver.solve(A, zSolved, Bz, PnMumpsSolver.Type.GENERAL_SYMMETRIC);
		
		PsDebug.message("Changing vertices....");	
		changeVertices();
	}

	public void reset() {
		m_geom.setVertices(m_geomSave.getVertices());
	}
}