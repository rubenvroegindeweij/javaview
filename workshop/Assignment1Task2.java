package workshop;

import java.awt.Color;
import java.util.Vector;
import java.util.Arrays;
import java.util.ArrayList;

import jv.geom.PgBndPolygon;
import jv.geom.PgElementSet;
import jv.geom.PgPolygonSet;
import jv.geom.PgVectorField;
import jv.geom.PuCleanMesh;
import jv.number.PdColor;
import jv.object.PsConfig;
import jv.object.PsDebug;
import jv.object.PsObject;
import jv.project.PgGeometry;
import jv.vecmath.PdVector;
import jv.vecmath.PiVector;
import jv.vecmath.PuMath;
import jv.viewer.PvDisplay;
import jv.project.PvGeometryIf;
import jv.vecmath.PdMatrix;

import jv.vecmath.PdMatrix;

import jvx.project.PjWorkshop;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

/**
 *  Workshop for surface registration
 */

public class Assignment1Task2 extends PjWorkshop {

	/** First surface to be registered. */
	PgElementSet	m_surfP;
	/** Second surface to be registered. */
	PgElementSet	m_surfQ;


	/** Constructor */
	public Assignment1Task2() {
		super("Assignment 1 Task 2");
		if (getClass() == Assignment1Task2.class) {
			init();
		}
	}

	public Assignment1Task2(String s) {
		super(s);
		if (getClass() == Assignment1Task2.class) {
			init();
		}
	}

	/** Initialization */
	public void init() {
		super.init();
	}


	/** Set two Geometries. */
	public void setGeometries(PgElementSet surfP, PgElementSet surfQ) {
		m_surfP = surfP;
		m_surfQ = surfQ;
	}


	public PdVector searchClosestVerticesInQ(PdVector point) {
		PdVector[] pointsQ = m_surfQ.getVertices();
		PdVector minV = pointsQ[0];
		double min = pointsQ[0].dist(point);
		for (int i = 1; i < m_surfQ.getNumVertices(); i++) {
			PdVector curPoint = pointsQ[i];
			double distance = curPoint.dist(point);
			if (min > distance) {
				min = distance;
				minV = curPoint;
			}
		}
		return minV;
	}

	public PdVector[] getRandomVerticesFromP(int n) {
		if (n > m_surfP.getNumVertices())
			n = m_surfP.getNumVertices();
		PdVector[] vertices = m_surfP.getVertices();
		PdVector[] randomVertices = new PdVector[n];
		for (int i = 0; i < n; i++) {
			int randomNumber = (int)(Math.random() * (m_surfP.getNumVertices() - 1));
			randomVertices[i] = vertices[randomNumber];
		}
		return randomVertices;
	}

	public PdVector[] findClosestVerticesForSelectedPoints(PdVector[] randomVerticesInP) {
		int m = randomVerticesInP.length;
		PdVector[] closestVerticesInQ = new PdVector[m];
		for (int i = 0; i < m; i++) {
			closestVerticesInQ[i] = searchClosestVerticesInQ(randomVerticesInP[i]);
		}
		return closestVerticesInQ;
	}

	public double computeMedianDistanceInS(double[] distance) {
		Arrays.sort(distance);
		if (distance.length % 2 == 1) {
			int index = (distance.length + 1) / 2 - 1;
			return distance[index];
		} else {
			int index1 = distance.length / 2 - 1;
			int index2 = index1 + 1;
			return (distance[index1] + distance[index2]) / 2;
		}
	}

	public double[] getDistances(PdVector[] vInP, PdVector[] vInQ) {
		double[] distances = new double[vInQ.length];
		for (int i = 0; i < vInQ.length; i++) {
			distances[i] = vInP[i].dist(vInQ[i]);
		}
		return distances;
	}

	public double[] removeDistances(double[] distances, double median, double k) {
		double threshhold = median * k;
		double result[] = new double[distances.length];
		for (int i = 0; i < distances.length; i++) {
			if (distances[i] <= threshhold)
				result[i] = distances[i];
			else
				result[i] = -1d;
		}
		return result;
	}
	public PdMatrix computeCovarianceMatrix(PdVector[] pointsP, PdVector[] pointsQ) {
		PdVector pAverage = computeCentroid(pointsP);
		PdVector qAverage = computeCentroid(pointsQ);

		PdMatrix m = new PdMatrix(3);

		for (int i = 0; i < pointsP.length; i++) {

			PdMatrix m_temp = new PdMatrix();
			PdVector pTemp = PdVector.subNew(pointsP[i], pAverage);
			PdVector qTemp = PdVector.subNew(pointsQ[i], qAverage);
			// Compute pTemp * qTemp^t
			m_temp.adjoint(pTemp, qTemp);

			m.add(m_temp);
		}

		// PsDebug.message("Covariance before division: " + m.toString());

		m.multScalar(1d / ((double)pointsP.length));

		// PsDebug.message("Divider: " + Double.toString(1d/((double)pointsP.length)));

		// PsDebug.message("Covariance after division: " + m.toString());

		return m;

	}


	public PdVector[] removeVectors(PdVector[] vectors, double[] distances) {
		ArrayList<PdVector> vectorsAL = new ArrayList<PdVector>();
		for (int i = 0; i < distances.length; i++) {
			if (distances[i] != -1d)
				vectorsAL.add(vectors[i]);
		}
		return vectorsAL.toArray(new PdVector[0]);
	}

	public PdVector computeCentroid(PdVector[] vectors) {
		PdVector result = (PdVector)vectors[0].clone();
		for (int i = 1; i < vectors.length; i++) {
			result.add(vectors[i]);
		}
		result.multScalar(1 / ((double)vectors.length));
		return result;
	}

	public SingularValueDecomposition SVD(PdMatrix M) {
		double[][] m = M.getEntries();
		Matrix jamaM = new Matrix(m);
		return jamaM.svd();
	}

	public PdMatrix computeOptimalRotation(SingularValueDecomposition svd) {
		Matrix tempMatrix = Matrix.identity(3, 3);
		Matrix U = svd.getU();
		Matrix V = svd.getV();
		double detVUt = V.times(U.transpose()).det();
		// PsDebug.message("Determinant: " + Double.toString(detVUt));
		tempMatrix.set(2, 2, detVUt);
		Matrix rotationOptimal = V.times(tempMatrix).times(U.transpose());
		PdMatrix optimalRotation = new PdMatrix(rotationOptimal.getArrayCopy());
		// PsDebug.message("Optimal Rotation Matrix: " + optimalRotation.toString());
		return  optimalRotation;
	}

	public PdVector computeOptimalTranslation(PdVector[] pointsP, PdVector[] pointsQ, SingularValueDecomposition svd, PdMatrix optimalRotation) {
		PdVector pAverage = computeCentroid(pointsP);
		PdVector qAverage = computeCentroid(pointsQ);
		// PdMatrix optimalRotation = computeOptimalRotation(svd);
		PdVector optimalTranslation = PdVector.subNew(qAverage, optimalRotation.leftMultMatrix(null, pAverage));
		// PsDebug.message("Optimal Translation Vector: " + optimalTranslation.toString());
		return optimalTranslation;
	}

	public void rotateP(PdMatrix rotation) {
		PdVector[] vertices = m_surfP.getVertices();
		for (int i = 0; i < vertices.length; i++) {
			vertices[i].leftMultMatrix(rotation);
		}
	}

	public void translateP(PdVector translation) {
		PdVector[] vertices = m_surfP.getVertices();
		for (int i = 0; i < vertices.length; i++) {
			vertices[i].add(translation);
		}
	}

	public double calculateError(PdVector[] randomVerticesInP, PdVector[] closestVerticesInQ) {
		double errorAll = 0;
		for (int i = 0; i < randomVerticesInP.length; i++) {
			double distance = randomVerticesInP[i].dist(closestVerticesInQ[i]);
			errorAll += distance * distance;
		}
		// PsDebug.message("errorAll: " + Double.toString(errorAll));
		double errorAvg = errorAll / randomVerticesInP.length;
		// PsDebug.message("errorAvg: " + Double.toString(errorAvg));
		return errorAvg;
	}
}
