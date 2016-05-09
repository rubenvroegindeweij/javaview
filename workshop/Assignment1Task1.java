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
	
	public int[] getValences() {

		return m_geom.getVertexValence(m_geom).getEntries();
	}

	public int getValencesVectorSize() {
		return m_geom.getVertexValence(m_geom).getSize();
	}

	public int getMaxValences() {
		return m_geom.getVertexValence(m_geom).max();
	}

	public int getMinValences() {
		return m_geom.getVertexValence(m_geom).min();
	}

	public int getValencesSum() {
		int[] valences = getValences();
		int valencesSum = 0;
		for (int j = 0; j < valences.length; j++) {
			valencesSum += valences[j];
		}
		return valencesSum;
	}
	
	public double getMeanValences() {
		int[] valences = getValences();
		int valencesSum = getValencesSum();
		double valencesMean = valencesSum / valences.length;
		return valencesMean;
	}

	public double getStandardDeviation() {
		int[] valences = getValences();
		int valencesSum = getValencesSum();
		double valencesMean = valencesSum / valences.length;
		int sumValencesSquare = 0;
		for (int k = 0; k < valences.length; k++) {
			sumValencesSquare += Math.pow(valences[k], 2);
		}
		double standardDeviation = Math.sqrt(sumValencesSquare / valences.length - Math.pow(valencesMean, 2));
		return standardDeviation;
	}
	
	public double computeVolume() {
		double volume = m_geom.getVolume();
		return volume;
	}
}