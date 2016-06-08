package workshop;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import jv.number.PuDouble;
import jv.object.PsConfig;
import jv.object.PsDialog;
import jv.object.PsUpdateIf;
import jvx.project.PjWorkshop_IP;
import jv.vecmath.PdMatrix;
import jvx.numeric.PnSparseMatrix;

import jv.object.PsDebug;

public class Assignment3_IP extends PjWorkshop_IP implements ActionListener {

	protected   Button			m_bIterativeAveraging, m_bExplicitMCF, m_bImplicitMCF, m_bReset;
	protected	Label			m_lStepWidth;
	protected	TextField		tf1;

	Assignment3 m_a3;

	public Assignment3_IP() {
		super();
		if (getClass() == Assignment3_IP.class)
			init();
	}

	public void init() {
		super.init();
		setTitle("Assignment 3");
	}

	public String getNotice() {
		return "Surface Smoothing";
	}

	public void setParent(PsUpdateIf parent) {
		super.setParent(parent);
		m_a3 = (Assignment3)parent;

		addSubTitle("Please set the stepwidth used for surface smoothing, and choose a method by clicking the corresponding button.");

		m_lStepWidth = new Label("Stepwidth");
		tf1 = new TextField("0.5", 2);
		Panel panel1 = new Panel(new FlowLayout(FlowLayout.CENTER));
		panel1.add(m_lStepWidth);
		panel1.add(tf1);
		add(panel1);

		m_bIterativeAveraging = new Button("Iterative Averaging");
		m_bIterativeAveraging.addActionListener(this);
		Panel panel2 = new Panel(new FlowLayout(FlowLayout.CENTER));
		panel2.add(m_bIterativeAveraging);
		add(panel2);

		m_bExplicitMCF = new Button("Explicit MCF");
		m_bExplicitMCF.addActionListener(this);
		Panel panel3 = new Panel(new FlowLayout(FlowLayout.CENTER));
		panel3.add(m_bExplicitMCF);
		add(panel3);

		m_bImplicitMCF = new Button("Implicit MCF");
		m_bImplicitMCF.addActionListener(this);
		Panel panel4 = new Panel(new FlowLayout(FlowLayout.CENTER));
		panel4.add(m_bImplicitMCF);
		add(panel4);

		m_bReset = new Button("Reset");
		m_bReset.addActionListener(this);
		Panel panel5 = new Panel(new FlowLayout(FlowLayout.CENTER));
		panel5.add(m_bReset);
		add(panel5);

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

		if (source == m_bIterativeAveraging) {
			double stepwidth = getStepWidth();
			try {
				m_a3.iterativeAveraging(stepwidth);
			} catch (Exception e) {
				PsDebug.message("EXCEPTION: " + e.toString());
			}
			m_a3.m_geom.update(null);
		}

		if (source == m_bExplicitMCF) {
			double stepwidth = getStepWidth();
			try {
				m_a3.explicitMCF(stepwidth);
			} catch (Exception e) {
				PsDebug.message("EXCEPTION: " + e.toString());
			}
			m_a3.m_geom.update(null);
		}

		if (source == m_bImplicitMCF) {
			double stepwidth = getStepWidth();
			try {
				m_a3.implicitMCF(stepwidth);
			} catch (Exception e) {
				PsDebug.message("EXCEPTION: " + e.toString());
			}
			m_a3.m_geom.update(null);
		}


		if (source == m_bReset) {
			m_a3.reset();
			m_a3.m_geom.update(null);
		}

	}
	/**
	 * Get information which bottom buttons a dialog should create
	 * when showing this info panel.
	 */
	protected int getDialogButtons()		{
		return PsDialog.BUTTON_OK;
	}


	/**
	* Get the stepwidth specified by the user.
	*/
	public double getStepWidth() {
		double stepwidth;
		return stepwidth = new Double(tf1.getText());
	}
}
