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

public class Assignment2 extends PjWorkshop {

	PgElementSet m_geom;
	PgElementSet m_geomSave;
	PnSparseMatrix gradientMatrix;
	ArrayList<PdMatrix> modifiedGradientMatrices;
	PdVector xModifiedGradients;
	PdVector yModifiedGradients;
	PdVector zModifiedGradients;
	PnSparseMatrix Mv;

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
		
		int startMv = (currentTriangleIndex*3);
		Mv.addEntry(startMv,startMv,area);
		Mv.addEntry(startMv+1,startMv+1,area);
		Mv.addEntry(startMv+2,startMv+2,area);
		

		PdMatrix gradientMatrix = new PdMatrix(3);
		gradientMatrix.setColumn(0, PdVector.crossNew(n, e1));
		gradientMatrix.setColumn(1, PdVector.crossNew(n, e2));
		gradientMatrix.setColumn(2, PdVector.crossNew(n, e3));
		gradientMatrix.multScalar(1d/(2*area));
		
		// Saves the small modified gradient matrices.
		PdMatrix modiefiedGradientMatrix = PdMatrix.copyNew(gradientMatrix);
		modiefiedGradientMatrix.leftMult(A);
		modifiedGradientMatrices.add(modiefiedGradientMatrix);
		
		// Filles the Mv matrix with area values.
		
		return gradientMatrix;
	}
	
	public void setModifiedVectors(){
		
	}
}