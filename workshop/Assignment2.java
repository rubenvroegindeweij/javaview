package workshop;

import java.awt.Color;
import java.util.Arrays;
import java.util.Random;

import jv.geom.PgElementSet;
import jv.project.PgGeometry;
import jv.vecmath.PdVector;
import jv.vecmath.PiVector;
import jvx.project.PjWorkshop;

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
	
	public PdMatrix getGradientMatrix(){
		
		
		PiVector[] elements = m_geom.getElements();
		int sizeElements = m_geom.getNumElements();
		
		int numOfVertices = m_geom.getNumVertices();
		
		PdMatrix gradientMatrix = new PdMatrix((sizeElements*3), numOfVertices);
		
		for(int i = 0; i< elements.length;i++){
			
			PdMatrix temp = getGradientMatrix(elements[i]);
			
			for(int j = 0; j<3;j++){
				
				PdVector tempColumn  = temp.getColumn(j);
				
				int startPos = (i*3);
				
				double x = tempColumn.getEntry(0);
				double y = tempColumn.getEntry(1);
				double z = tempColumn.getEntry(2);
				
				int currentVertexIndex = elements[i].getEntry(j);
				
				gradientMatrix.setEntry(startPos,currentVertexIndex,x);
				gradientMatrix.setEntry(startPos+1,currentVertexIndex,y);
				gradientMatrix.setEntry(startPos+2,currentVertexIndex,z);
				
			}
						
		}
		return gradientMatrix;
		
	}
}