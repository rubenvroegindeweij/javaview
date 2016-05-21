package workshop;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import jv.number.PuDouble;
import jv.object.PsConfig;
import jv.object.PsDialog;
import jv.object.PsUpdateIf;
import jvx.project.PjWorkshop_IP;

import jv.object.PsDebug;

public class Assignment1Task1_IP extends PjWorkshop_IP implements ActionListener {

	// Genus components.
	protected Button m_bComputeGenus;
	protected Label m_lGenus;

	// Valence components.
	protected Button m_bgetValences;
	protected Button m_bgetvalencesVectorSize;
	protected Button m_bgetMaxValences;
	protected Button m_bgetMinValences;
	protected Button m_bgetMeanValences;
	protected Button m_bstandardDeviation;
	protected Label m_lValences;
	protected Label m_lvalencesVectorSize;
	protected Label m_lMaxValences;
	protected Label m_lMinValences;
	protected Label m_lMeanValences;
	protected Label m_lstandardDeviation;

	// Shape regularities components.
	protected Button m_bgetShapeRegularites;
	protected Button m_bgetMaxShapeRegularity;
	protected Button m_bgetMinShapeRegularity;
	protected Button m_bgetMeanShapeRegularity;
	protected Button m_bstandardDeviationShapeRegularity;
	protected Button m_bcolorShapeRegularities;
	protected Label m_lShapeRegularities;
	protected Label m_lMaxShapeRegularity;
	protected Label m_lMinShapeRegularity;
	protected Label m_lMeanShapeRegularity;
	protected Label m_lstandardDeviationShapeRegularity;

	// Volume components.
	protected Button m_bComputeVolume;
	protected Label m_lVolume;

	Assignment1Task1 m_a1t1;

	public Assignment1Task1_IP() {
		super();
		if (getClass() == Assignment1Task1_IP.class)
			init();
	}

	public void init() {
		super.init();
		setTitle("Assignment 1 Task 1");
	}

	public String getNotice() {
		return "In this application you are able to compoute genus, volume, shape regularites, and valences of all vertices of the current model.";
	}

	public void setParent(PsUpdateIf parent) {
		super.setParent(parent);
		m_a1t1 = (Assignment1Task1)parent;

		addSubTitle("Click buttons to get corresponding properties of the model.");

		// Genus components.
		m_bComputeGenus = new Button("Compute genus");
		m_bComputeGenus.addActionListener(this);
		m_lGenus = new Label("genus");
		Panel panel1 = new Panel(new FlowLayout(FlowLayout.CENTER));
		panel1.add(m_bComputeGenus);
		panel1.add(m_lGenus);
		add(panel1);

		// Valence components.
		m_bgetValences = new Button("Get Valences");
		m_bgetValences.addActionListener(this);
		m_bgetvalencesVectorSize = new Button("Get Valences Vector Size");
		m_bgetvalencesVectorSize.addActionListener(this);
		m_bgetMaxValences = new Button("Get Maximum Valences");
		m_bgetMaxValences.addActionListener(this);
		m_bgetMinValences = new Button("Get Minimum Valences");
		m_bgetMinValences.addActionListener(this);
		m_bgetMeanValences = new Button("Get Mean of Valences");
		m_bgetMeanValences.addActionListener(this);
		m_bstandardDeviation = new Button("Calculate the Standard Deviation");
		m_bstandardDeviation.addActionListener(this);
		m_lValences = new Label("valences");
		m_lvalencesVectorSize = new Label("size");
		m_lMaxValences = new Label("maxValences");
		m_lMinValences = new Label("minValences");
		m_lMeanValences = new Label("meanValences");
		m_lstandardDeviation = new Label("standardDeviation");
		Panel panel2 = new Panel(new FlowLayout(FlowLayout.CENTER));
		//panel1.add(m_bgetValences);
		//panel1.add(m_lValences);
		//panel1.add(m_bgetvalencesVectorSize);
		//panel1.add(m_lvalencesVectorSize);
		panel2.add(m_bgetMaxValences);
		panel2.add(m_lMaxValences);
		panel2.add(m_bgetMinValences);
		panel2.add(m_lMinValences);
		panel2.add(m_bgetMeanValences);
		panel2.add(m_lMeanValences);
		panel2.add(m_bstandardDeviation);
		panel2.add(m_lstandardDeviation);
		add(panel2);

		// Shape regularities components.
		m_bgetShapeRegularites = new Button("Get Shape Regularities");
		m_bgetShapeRegularites.addActionListener(this);
		m_bgetMaxShapeRegularity = new Button("Get Maximum Shape Regularity");
		m_bgetMaxShapeRegularity.addActionListener(this);
		m_bgetMinShapeRegularity = new Button("Get Minimum Shape Regularity");
		m_bgetMinShapeRegularity.addActionListener(this);
		m_bgetMeanShapeRegularity = new Button("Get Mean of Shape Regularity");
		m_bgetMeanShapeRegularity.addActionListener(this);
		m_bstandardDeviationShapeRegularity = new Button("Calculate the Standard Deviation");
		m_bstandardDeviationShapeRegularity.addActionListener(this);
		m_bcolorShapeRegularities = new Button("Color shape regularities");
		m_bcolorShapeRegularities.addActionListener(this);
		m_lShapeRegularities = new Label("shape regularities");
		m_lMaxShapeRegularity = new Label("max shape regularities");
		m_lMinShapeRegularity = new Label("min shape regularities");
		m_lMeanShapeRegularity = new Label("mean shape regularities");
		m_lstandardDeviationShapeRegularity = new Label("standardDeviation");
		Panel panel3 = new Panel(new FlowLayout(FlowLayout.CENTER));
		panel3.add(m_bgetMaxShapeRegularity);
		panel3.add(m_lMaxShapeRegularity);
		panel3.add(m_bgetMinShapeRegularity);
		panel3.add(m_lMinShapeRegularity);
		panel3.add(m_bgetMeanShapeRegularity);
		panel3.add(m_lMeanShapeRegularity);
		panel3.add(m_bstandardDeviationShapeRegularity);
		panel3.add(m_lstandardDeviationShapeRegularity);
		panel3.add(m_bcolorShapeRegularities);
		add(panel3);

		// Volume components.
		m_bComputeVolume = new Button("Compute Volume");
		m_bComputeVolume.addActionListener(this);
		m_lVolume = new Label("volume");
		Panel panel4 = new Panel(new FlowLayout(FlowLayout.CENTER));
		panel4.add(m_bComputeVolume);
		panel4.add(m_lVolume);
		add(panel4);

		validate();
	}


	public boolean update(Object event) {
		return super.update(event);
	}

	/**
	 * Handle action events fired by buttons etc.
	 */
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();

		// Genus action.
		if (source == m_bComputeGenus) {
			int genus = m_a1t1.computeGenus();
			m_lGenus.setText(Integer.toString(genus));
			m_a1t1.m_geom.update(m_a1t1.m_geom);
			return;
		}

		// Valence action.
		if (source == m_bgetvalencesVectorSize) {
			int valencesVectorSize = m_a1t1.getValencesVectorSize();
			m_lvalencesVectorSize.setText(Integer.toString(valencesVectorSize));
			m_a1t1.m_geom.update(m_a1t1.m_geom);
			return;
		}
		if (source == m_bgetMaxValences) {
			int maxValences = m_a1t1.getMaxValences();
			m_lMaxValences.setText(Integer.toString(maxValences));
			m_a1t1.m_geom.update(m_a1t1.m_geom);
			return;
		}
		if (source == m_bgetMinValences) {
			int minValences = m_a1t1.getMinValences();
			m_lMinValences.setText(Integer.toString(minValences));
			m_a1t1.m_geom.update(m_a1t1.m_geom);
			return;
		}
		if (source == m_bgetMeanValences) {
			double meanValences = m_a1t1.getMeanValences();
			m_lMeanValences.setText(Double.toString(meanValences));
			m_a1t1.m_geom.update(m_a1t1.m_geom);
			return;
		}
		if (source == m_bstandardDeviation) {
			double standardDeviation = m_a1t1.getSDofValences();
			m_lstandardDeviation.setText(Double.toString(standardDeviation));
			m_a1t1.m_geom.update(m_a1t1.m_geom);
			return;
		}

		// Shape regularities action.
		if (source == m_bgetMaxShapeRegularity) {
			double[] shapeRegularities = m_a1t1.computeShapeRegularity();
			double maxShapeRegularity = m_a1t1.getMaximumShapeRegularity(shapeRegularities);
			m_lMaxShapeRegularity.setText(Double.toString(maxShapeRegularity));
			m_a1t1.m_geom.update(m_a1t1.m_geom);
			return;
		}
		if (source == m_bgetMinShapeRegularity) {
			double[] shapeRegularities = m_a1t1.computeShapeRegularity();
			double minShapeRegularity = m_a1t1.getMinimumShapeRegularity(shapeRegularities);
			m_lMinShapeRegularity.setText(Double.toString(minShapeRegularity));
			m_a1t1.m_geom.update(m_a1t1.m_geom);
			return;
		}
		if (source == m_bgetMeanShapeRegularity) {
			double[] shapeRegularities = m_a1t1.computeShapeRegularity();
			double meanShapeRegularity = m_a1t1.getAverageShapeRegularity(shapeRegularities);
			m_lMeanShapeRegularity.setText(Double.toString(meanShapeRegularity));
			m_a1t1.m_geom.update(m_a1t1.m_geom);
			return;
		}
		if (source == m_bstandardDeviationShapeRegularity) {
			double[] shapeRegularities = m_a1t1.computeShapeRegularity();
			double standardDeviation = m_a1t1.getSDofShapeRegularities(shapeRegularities);
			m_lstandardDeviationShapeRegularity.setText(Double.toString(standardDeviation));
			m_a1t1.m_geom.update(m_a1t1.m_geom);
			return;
		}
		if (source == m_bcolorShapeRegularities) {
			double[] shapeRegularities = m_a1t1.computeShapeRegularity();
			m_a1t1.colorRegularities(shapeRegularities);
			m_a1t1.m_geom.update(m_a1t1.m_geom);
			return;
		}

		// Volume action.
		if (source == m_bComputeVolume) {
			double volume = m_a1t1.computeVolume();
			m_lVolume.setText(Double.toString(volume));
			m_a1t1.m_geom.update(m_a1t1.m_geom);
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
