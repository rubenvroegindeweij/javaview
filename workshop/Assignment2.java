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
	ArrayList<PdMatrix> modifiedGradientMatrices;
	PdVector xModifiedGradients;
	PdVector yModifiedGradients;
	PdVector zModifiedGradients;
	PnSparseMatrix Mv;
	PdVector xSolved;
	PdVector ySolved;
	PdVector zSolved;

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
	
	public PnSparseMatrix getGradientMatrix(PdMatrix A){
		PiVector[] elements = m_geom.getElements();
		int sizeElements = m_geom.getNumElements();
		
		int numOfVertices = m_geom.getNumVertices();
		
	//	PdMatrix gradientMatrix = new PdMatrix((sizeElements*3), numOfVertices);
		
		gradientMatrix = new PnSparseMatrix((sizeElements*3),numOfVertices,3);
		Mv = new PnSparseMatrix((sizeElements*3),(sizeElements*3),1);
		modifiedGradientMatrices = new ArrayList<PdMatrix>();
		
		for(int i = 0; i< elements.length;i++){
			
			PdMatrix temp = getGradientMatrix(elements[i], A, i);
			
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

	public PdMatrix getGradientMatrix(PiVector triangle, PdMatrix A, int currentTriangleIndex){
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
		
		// Saves the small modified gradient matrices.
		PiVector element = m_geom.getElement(currentTriangleIndex);
		PdMatrix modiefiedGradientMatrix = PdMatrix.copyNew(triangleGradientMatrix);
		if(element.hasTag(PsObject.IS_SELECTED))
			modiefiedGradientMatrix.leftMult(A);
		modifiedGradientMatrices.add(modiefiedGradientMatrix);
		
		// Filles the Mv matrix with area values.
		int startMv = (currentTriangleIndex*3);
		Mv.addEntry(startMv,startMv,area);
		Mv.addEntry(startMv+1,startMv+1,area);
		Mv.addEntry(startMv+2,startMv+2,area);
		
		return triangleGradientMatrix;
	}
	
	public void setModifiedVectors(){
		int n = m_geom.getNumVertices();
		xModifiedGradients = new PdVector(n);
		yModifiedGradients = new PdVector(n);
		zModifiedGradients = new PdVector(n);
		for(int i = 0; i < modifiedGradientMatrices.size(); i++){
			PdMatrix modiefiedGradientMatrix = modifiedGradientMatrices.get(i);
			int currentMatrixIndex = i*3;
			xModifiedGradients.setEntry(currentMatrixIndex, modiefiedGradientMatrix.getEntry(0, 0));
			xModifiedGradients.setEntry(currentMatrixIndex+1, modiefiedGradientMatrix.getEntry(0, 1));
			xModifiedGradients.setEntry(currentMatrixIndex+2, modiefiedGradientMatrix.getEntry(0, 2));
			yModifiedGradients.setEntry(currentMatrixIndex, modiefiedGradientMatrix.getEntry(1, 0));
			yModifiedGradients.setEntry(currentMatrixIndex+1, modiefiedGradientMatrix.getEntry(1, 1));
			yModifiedGradients.setEntry(currentMatrixIndex+2, modiefiedGradientMatrix.getEntry(1, 2));
			zModifiedGradients.setEntry(currentMatrixIndex, modiefiedGradientMatrix.getEntry(2, 0));
			zModifiedGradients.setEntry(currentMatrixIndex+1, modiefiedGradientMatrix.getEntry(2, 1));
			zModifiedGradients.setEntry(currentMatrixIndex+2, modiefiedGradientMatrix.getEntry(2, 2));
		}
	}
	
	public void changeVertices(){
		for(int i = 0; i < xSolved.getSize(); i++){
			m_geom.setVertex(i, xSolved.getEntry(i), ySolved.getEntry(i), zSolved.getEntry(i));
		}
	}
	
	public void editMesh(PdMatrix A) throws Exception{
		getGradientMatrix(A);
		setModifiedVectors();
		PnSparseMatrix GTMv = PnSparseMatrix.multMatrices(gradientMatrix.transposeNew(), Mv, new PnSparseMatrix());
		PnSparseMatrix S = PnSparseMatrix.multMatrices(GTMv, gradientMatrix, new PnSparseMatrix());
		PdVector bx = PnSparseMatrix.rightMultVector(GTMv, xModifiedGradients, new PdVector());
		PdVector by = PnSparseMatrix.rightMultVector(GTMv, yModifiedGradients, new PdVector());
		PdVector bz = PnSparseMatrix.rightMultVector(GTMv, zModifiedGradients, new PdVector());
		int n = m_geom.getNumVertices();
		xSolved = new PdVector(n);
		ySolved = new PdVector(n);
		zSolved = new PdVector(n);
		long factorization = PnMumpsSolver.factor(S, PnMumpsSolver.Type.GENERAL_SYMMETRIC);
		PnMumpsSolver.solve(factorization, xSolved, bx);
		PnMumpsSolver.solve(factorization, ySolved, by);
		PnMumpsSolver.solve(factorization, zSolved, bz);
		changeVertices();
		PsDebug.message("G: " + gradientMatrix.toString());
		PsDebug.message("GT: " + gradientMatrix.transposeNew().toString());
		PsDebug.message("Mv: " + Mv.toString());
		PsDebug.message("S: " + S.toString());
		PsDebug.message("xModifiedGradients: " + xModifiedGradients.toString());
		PsDebug.message("bx: " + bx.toString());
		PsDebug.message("GTMv: " + GTMv.toString());
		PsDebug.message("factorization: " + factorization);
		PsDebug.message("bx: " + bx.toString());
		PsDebug.message("xSolved: " + xSolved.toString());
	}
}