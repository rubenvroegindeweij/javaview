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

public class Assignment2 extends PjWorkshop {

	PgElementSet m_geom;
	PgElementSet m_geomSave;
	PnSparseMatrix gradientMatrix;
	PdVector xGradientTilda;
	PdVector yGradientTilda;
	PdVector zGradientTilda;
	PdVector x;
	PdVector y;
	PdVector z;
	PnSparseMatrix Mv;
	PdVector xSolved;
	PdVector ySolved;
	PdVector zSolved;
	PdMatrix A;

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
	
	// Fills the gradient matrix G (3m by n) by all the small gradient matrices and also fills the Mv (3m by 3m) matrix with triangle areas on the diagonal.
	public PnSparseMatrix getGradientMatrix(PdMatrix A){
		PiVector[] elements = m_geom.getElements();
		int sizeElements = m_geom.getNumElements();
		int numOfVertices = m_geom.getNumVertices();
		
		gradientMatrix = new PnSparseMatrix((sizeElements*3),numOfVertices,3);
		Mv = new PnSparseMatrix((sizeElements*3),(sizeElements*3),1);
		
		for(int i = 0; i< elements.length;i++){
			
			PdMatrix temp = getSmallGradientMatrixAndFillMv(elements[i], i);
			
			for(int j = 0; j<3;j++){
				
				PdVector tempColumn  = temp.getColumn(j);
				
				int startPos = (i*3);
				
				double x = tempColumn.getEntry(0);
				double y = tempColumn.getEntry(1);
				double z = tempColumn.getEntry(2);
				
				int currentVertexIndex = elements[i].getEntry(j);
				
				gradientMatrix.addEntry(startPos,currentVertexIndex,x);
				gradientMatrix.addEntry(startPos+1,currentVertexIndex,y);
				gradientMatrix.addEntry(startPos+2,currentVertexIndex,z);	
			}
						
		}
		return gradientMatrix;
}

	// Computes for every triangle the small gradient matrix (3 by 3) and fills for every triangle the Mv.
	public PdMatrix getSmallGradientMatrixAndFillMv(PiVector triangle, int currentTriangleIndex){
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

		PdMatrix triangleGradientMatrix = new PdMatrix(3);
		triangleGradientMatrix.setColumn(0, PdVector.crossNew(n, e1));
		triangleGradientMatrix.setColumn(1, PdVector.crossNew(n, e2));
		triangleGradientMatrix.setColumn(2, PdVector.crossNew(n, e3));
		triangleGradientMatrix.multScalar(1d/(2*area));
		
		// Filles the Mv matrix with area values.
		int startMv = (currentTriangleIndex*3);
		Mv.addEntry(startMv,startMv,area);
		Mv.addEntry(startMv+1,startMv+1,area);
		Mv.addEntry(startMv+2,startMv+2,area);
		
		return triangleGradientMatrix;
	}
	
	// Multiplies the the gradient matrix (3m by n) with all x, y and z values (n by 1) and if the triangle is selected computes new values by multiplying with A.
	public void contstructGTilda(){
		int n = m_geom.getNumVertices();
		x = new PdVector(n);
		y = new PdVector(n);
		z = new PdVector(n);
		
		for(int i = 0; i < n; i++){
			x.setEntry(i, m_geom.getVertex(i).getEntry(0));
			y.setEntry(i, m_geom.getVertex(i).getEntry(1));
			z.setEntry(i, m_geom.getVertex(i).getEntry(2));
		}
		
		xGradientTilda = PnSparseMatrix.rightMultVector(gradientMatrix, x, new PdVector());
		yGradientTilda = PnSparseMatrix.rightMultVector(gradientMatrix, y, new PdVector());
		zGradientTilda = PnSparseMatrix.rightMultVector(gradientMatrix, z, new PdVector());
		
		for(int i = 0; i < m_geom.getNumElements(); i++){
			PiVector element = m_geom.getElement(i);
			if(element.hasTag(PsObject.IS_SELECTED)){
				PdVector triangleX = new PdVector(xGradientTilda.getEntry(i*3), xGradientTilda.getEntry(i*3+1), xGradientTilda.getEntry(i*3+2));
				PdVector tempx = PdVector.copyNew(triangleX).leftMultMatrix(A);
				xGradientTilda.setEntry(i*3, tempx.getEntry(0));
				xGradientTilda.setEntry(i*3+1, tempx.getEntry(1));
				xGradientTilda.setEntry(i*3+2, tempx.getEntry(2));
				
				PdVector triangleY = new PdVector(yGradientTilda.getEntry(i*3), yGradientTilda.getEntry(i*3+1), yGradientTilda.getEntry(i*3+2));
				PdVector tempy = PdVector.copyNew(triangleY).leftMultMatrix(A);
				yGradientTilda.setEntry(i*3, tempy.getEntry(0));
				yGradientTilda.setEntry(i*3+1, tempy.getEntry(1));
				yGradientTilda.setEntry(i*3+2, tempy.getEntry(2));
				
				PdVector triangleZ = new PdVector(zGradientTilda.getEntry(i*3), zGradientTilda.getEntry(i*3+1), zGradientTilda.getEntry(i*3+2));
				PdVector tempz = PdVector.copyNew(triangleZ).leftMultMatrix(A);
				zGradientTilda.setEntry(i*3, tempz.getEntry(0));
				zGradientTilda.setEntry(i*3+1, tempz.getEntry(1));
				zGradientTilda.setEntry(i*3+2, tempz.getEntry(2));
			}
		}
	}
	
	// Solves GT Mv G x = GT Mv gx.
	public void solveSystem() throws Exception{
		PnSparseMatrix GTMv = PnSparseMatrix.multMatrices(gradientMatrix.transposeNew(), Mv, new PnSparseMatrix());
		PnSparseMatrix S = PnSparseMatrix.multMatrices(GTMv, gradientMatrix, new PnSparseMatrix());
		PdVector bx = PnSparseMatrix.rightMultVector(GTMv, xGradientTilda, new PdVector());
		PdVector by = PnSparseMatrix.rightMultVector(GTMv, yGradientTilda, new PdVector());
		PdVector bz = PnSparseMatrix.rightMultVector(GTMv, zGradientTilda, new PdVector());
		int n = m_geom.getNumVertices();
		xSolved = new PdVector(n);
		ySolved = new PdVector(n);
		zSolved = new PdVector(n);
		//long factorization = PnMumpsSolver.factor(S, PnMumpsSolver.Type.GENERAL_SYMMETRIC);
		//PnMumpsSolver.solve(factorization, xSolved, bx);
		//PnMumpsSolver.solve(factorization, ySolved, by);
		//PnMumpsSolver.solve(factorization, zSolved, bz);
		PnMumpsSolver.solve(S, xSolved, bx, PnMumpsSolver.Type.GENERAL_SYMMETRIC);
		PnMumpsSolver.solve(S, ySolved, by, PnMumpsSolver.Type.GENERAL_SYMMETRIC);
		PnMumpsSolver.solve(S, zSolved, bz, PnMumpsSolver.Type.GENERAL_SYMMETRIC);
	}
	
	// Changes vertices based on the computed x, y and z values.
	public void changeVertices(){
		for(int i = 0; i < xSolved.getSize(); i++){
			m_geom.setVertex(i, xSolved.getEntry(i), ySolved.getEntry(i), zSolved.getEntry(i));
		}
	}
	
	// Edits Mesh based on input matrix A.
	public void editMesh(PdMatrix A) throws Exception{
		this.A = A;
		getGradientMatrix(A);
		contstructGTilda();
		solveSystem();
		changeVertices();
	}
	
	public void undo(){
		m_geom.setVertices(m_geomSave.getVertices());
	}
}