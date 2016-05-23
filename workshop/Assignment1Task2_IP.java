package workshop;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.TextArea;
import java.util.Vector;

import jv.geom.PgElementSet;
import jv.object.PsConfig;
import jv.object.PsDialog;
import jv.object.PsUpdateIf;
import jv.objectGui.PsList;
import jv.project.PgGeometryIf;
import jv.project.PvGeometryIf;
import jv.viewer.PvDisplay;
import jvx.project.PjWorkshop_IP;

import java.awt.FlowLayout;
import jv.vecmath.PdVector;
import jv.vecmath.PdMatrix;
import jv.object.PsDebug;
import Jama.Matrix;
import Jama.SingularValueDecomposition;


/**
 * Info Panel of Workshop for surface registration
 *
 */
public class Assignment1Task2_IP extends PjWorkshop_IP implements ActionListener {
	protected	List			m_listActive;
	protected	List			m_listPassive;
	protected	Vector			m_geomList;
	protected	Assignment1Task2	m_a1t2;
	protected   Button			m_bSetSurfaces;
	protected	PdVector[] 		randomVertices = null;
	protected	PdVector[]		closestVertices = null;
	protected	double[]		distances = null;
	protected	PdVector[] 		randomVerticesAfterRemove = null;
	protected	PdVector[]		closestVerticesAfterRemove = null;
	protected	double[]		distancesAfterRemove = null;
	protected	Button			m_bTransformation;
	protected	TextArea		textAreaOutput;
	protected	double			error = Double.MAX_VALUE;

	protected int NUMBER_OF_POINTS_TO_SELECT = 100;
	protected double MAX_ERROR = 0.05d;
	protected int K_FOR_MEDIAN_DISTANCE = 4;
	protected int MAX_AMOUNT_OF_ITERATIONS = 200;

	/** Constructor */
	public Assignment1Task2_IP () {
		super();
		if (getClass() == Assignment1Task2_IP.class)
			init();
	}

	/**
	 * Informational text on the usage of the dialog.
	 * This notice will be displayed if this info panel is shown in a dialog.
	 * The text is split at line breaks into individual lines on the dialog.
	 */
	public String getNotice() {
		return "By clicking the \"Transform\" button, you can see the registration of the two selected surfaces.";
	}

	/** Assign a parent object. */
	public void setParent(PsUpdateIf parent) {
		super.setParent(parent);
		m_a1t2 = (Assignment1Task2)parent;

		addSubTitle("Select Surfaces to be Registered");

		Panel pGeometries = new Panel();
		pGeometries.setLayout(new GridLayout(1, 2));

		Panel Passive = new Panel();
		Passive.setLayout(new BorderLayout());
		Panel Active = new Panel();
		Active.setLayout(new BorderLayout());
		Label ActiveLabel = new Label("Surface P");
		Active.add(ActiveLabel, BorderLayout.NORTH);
		m_listActive = new PsList(5, true);
		Active.add(m_listActive, BorderLayout.CENTER);
		pGeometries.add(Active);
		Label PassiveLabel = new Label("Surface Q");
		Passive.add(PassiveLabel, BorderLayout.NORTH);
		m_listPassive = new PsList(5, true);
		Passive.add(m_listPassive, BorderLayout.CENTER);
		pGeometries.add(Passive);
		add(pGeometries);

		Panel pSetSurfaces = new Panel(new BorderLayout());
		m_bSetSurfaces = new Button("Set selected surfaces");
		m_bSetSurfaces.addActionListener(this);
		pSetSurfaces.add(m_bSetSurfaces, BorderLayout.CENTER);
		add(pSetSurfaces);

		m_bTransformation = new Button("Transform");
		m_bTransformation.addActionListener(this);
		textAreaOutput = new TextArea("", 5, 40);
		textAreaOutput.setEditable(false);
		Panel panel1 = new Panel(new FlowLayout(FlowLayout.CENTER));
		panel1.add(m_bTransformation);
		add(panel1);
		Panel panel2 = new Panel(new FlowLayout(FlowLayout.CENTER));
		panel2.add(textAreaOutput);
		add(panel2);

		updateGeomList();
		validate();
	}

	/** Initialisation */
	public void init() {
		super.init();
		setTitle("Assignment 1 Task 2");

	}

	/** Set the list of geometries in the lists to the current state of the display. */
	public void updateGeomList() {
		Vector displays = m_a1t2.getGeometry().getDisplayList();
		int numDisplays = displays.size();
		m_geomList = new Vector();
		for (int i = 0; i < numDisplays; i++) {
			PvDisplay disp = ((PvDisplay)displays.elementAt(i));
			PgGeometryIf[] geomList = disp.getGeometries();
			int numGeom = geomList.length;
			for (int j = 0; j < numGeom; j++) {
				if (!m_geomList.contains(geomList[j])) {
					//Take just PgElementSets from the list.
					if (geomList[j].getType() == PvGeometryIf.GEOM_ELEMENT_SET)
						m_geomList.addElement(geomList[j]);
				}
			}
		}
		int nog = m_geomList.size();
		m_listActive.removeAll();
		m_listPassive.removeAll();
		for (int i = 0; i < nog; i++) {
			String name = ((PgGeometryIf)m_geomList.elementAt(i)).getName();
			m_listPassive.add(name);
			m_listActive.add(name);
		}
	}
	/**
	 * Handle action events fired by buttons etc.
	 */
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == m_bSetSurfaces) {
			m_a1t2.setGeometries((PgElementSet)m_geomList.elementAt(m_listActive.getSelectedIndex()),
			                     (PgElementSet)m_geomList.elementAt(m_listPassive.getSelectedIndex()));
			return;
		}

		if (source == m_bTransformation) {
			int counter = 0;
			textAreaOutput.setText("Calculating ...");
			double beginTime = System.currentTimeMillis();
			while (true) {
				randomVertices = m_a1t2.getRandomVerticesFromP(NUMBER_OF_POINTS_TO_SELECT);
				closestVertices = m_a1t2.findClosestVerticesForSelectedPoints(randomVertices);
				distances = m_a1t2.getDistances(randomVertices, closestVertices);
				double median = m_a1t2.computeMedianDistanceInS(distances);
				// set the 3rd parameter to a large number if you do not want to remove any points
				distancesAfterRemove = m_a1t2.removeDistances(distances, median, K_FOR_MEDIAN_DISTANCE);
				randomVerticesAfterRemove = m_a1t2.removeVectors(randomVertices, distancesAfterRemove);
				closestVerticesAfterRemove = m_a1t2.removeVectors(closestVertices, distancesAfterRemove);
				error = m_a1t2.calculateError(randomVerticesAfterRemove, closestVerticesAfterRemove);
				if (error <= MAX_ERROR)
					break;
				PdMatrix M = m_a1t2.computeCovarianceMatrix(randomVerticesAfterRemove, closestVerticesAfterRemove);
				SingularValueDecomposition svd = m_a1t2.SVD(M);
				PdMatrix optimalRotation = m_a1t2.computeOptimalRotation(svd);
				PdVector optimalTranslation = m_a1t2.computeOptimalTranslation(randomVerticesAfterRemove, closestVerticesAfterRemove, svd, optimalRotation);
				m_a1t2.rotateP(optimalRotation);
				m_a1t2.translateP(optimalTranslation);
				counter++;
				// If you want to keep the surfaces selected, you should keep updateGeomList() commented
				//updateGeomList();
				//validate();
				m_a1t2.m_surfP.update(m_a1t2.m_surfP);
				if(counter >= MAX_AMOUNT_OF_ITERATIONS)
					break;
			}
			double endTime = System.currentTimeMillis();
			double duration = endTime - beginTime;
			//PsDebug.message("Number of Iteration: " + Integer.toString(counter));
			textAreaOutput.setText("Number of Iteration: \t");
			textAreaOutput.append(Integer.toString(counter));
			textAreaOutput.append("\nRun time (s): \t" + Double.toString(duration / 1000d));
			textAreaOutput.append("\nFinal error: \t" + Double.toString(error));
			return;
		}

	}
	/**
	 * Get information which bottom buttons a dialog should create
	 * when showing this info panel.
	 */
	protected int getDialogButtons()		{
		return PsDialog.BUTTON_OK;
	}
}
