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

	public int getValencesSum(int [] valences) {
		// int[] valences = getValences();
		int valencesSum = 0;
		for (int j = 0; j < valences.length; j++) {
			valencesSum += valences[j];
		}
		return valencesSum;
	}

	public double getMeanValences() {
		int[] valences = getValences();
		int valencesSum = getValencesSum(valences);
		double valencesMean = valencesSum / (double) valences.length;
		return valencesMean;
	}

	public double getSDofValences() {
		int[] valences = getValences();
		int valencesSum = getValencesSum(valences);
		double valencesMean = valencesSum / (double) valences.length;
		int sumValencesSquare = 0;
		for (int k = 0; k < valences.length; k++) {
			sumValencesSquare += Math.pow(valences[k], 2);
		}
		double standardDeviation = Math.sqrt(sumValencesSquare / (double) valences.length - Math.pow(valencesMean, 2));
		return standardDeviation;
	}

	public double computeVolume() {
		double volume = m_geom.getVolume();
		return volume;
	}

	public double[] computeShapeRegularity() {

		PiVector[] elements = m_geom.getElements();
		int sizeElements = m_geom.getNumElements();
		double[] shapeRegularities = new double[sizeElements];
		for (int i = 0; i < sizeElements; i++) {
			if (elements[i].getSize() != 3) {
				shapeRegularities[i] = -1;
				continue; //if not a triange
			}

			double angle_a = m_geom.getVertexAngle(i, 0);
			double angle_b = m_geom.getVertexAngle(i, 1);
			double angle_c = m_geom.getVertexAngle(i, 2);

			double sAngle = Math.min(Math.min(angle_a, angle_b), angle_c); //smallest angle of current triangle
			double temp = (2.0 / Math.sin(Math.toRadians(sAngle))); //Shape regularity of current triangle
			shapeRegularities[i] = temp;
		}

		return shapeRegularities;
	}

	public double getMinimumShapeRegularity(double[] shapeRegularities) {

		double min = Double.MAX_VALUE;
		for (int i = 0; i < shapeRegularities.length; i++) {

			if (min > shapeRegularities[i]) {
				min = shapeRegularities[i];
			}
		}
		return min;
	}

	public double getMaximumShapeRegularity(double[] shapeRegularities) {

		double max = 0;
		for (int i = 0; i < shapeRegularities.length; i++) {

			if (max < shapeRegularities[i]) {
				max = shapeRegularities[i];
			}
		}
		return max;
	}

	public double getAverageShapeRegularity(double[] shapeRegularities) {

		double total = 0;

		for (int i = 0; i < shapeRegularities.length; i++) {
			total += shapeRegularities[i];
		}

		double average = total / (double)shapeRegularities.length;

		return average;

	}

	public double getSDofShapeRegularities(double[] shapeRegularities) {

		double sumOfRegularitiesSquare = 0;
		int amountOfTriangles = 0;

		for (int i = 0; i < shapeRegularities.length; i++) {

			if (shapeRegularities[i] != -1d) {
				amountOfTriangles++;
				sumOfRegularitiesSquare += Math.pow(shapeRegularities[i], 2);
			}
		}

		double averageShapeRegularity = getAverageShapeRegularity(shapeRegularities);
		double standardDeviation = Math.sqrt(sumOfRegularitiesSquare / amountOfTriangles - Math.pow(averageShapeRegularity, 2));
		return standardDeviation;
	}

	public void colorRegularities(double[] shapeRegularities) {

		double minS = getMinimumShapeRegularity(shapeRegularities);

		double maxS = getMaximumShapeRegularity(shapeRegularities);

		//assure that the color array is allocated
		m_geom.assureElementColors();

		for (int i = 0; i < shapeRegularities.length; i++) {
			float shade = (float)((shapeRegularities[i] - minS) / (maxS - minS));
			Color c = Color.getHSBColor(0.5f, 0.5f, shade);
			m_geom.setElementColor(i, c);
		}

		m_geom.showElementColorFromVertices(false);
		m_geom.showElementColors(true);
		m_geom.showSmoothElementColors(false);
	}
}