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
import jv.vecmath.PdMatrix;
import jvx.numeric.PnSparseMatrix;

import jv.object.PsDebug;

public class Assignment2_IP extends PjWorkshop_IP implements ActionListener {

	protected   Button			m_bGetGradientMatrix;
	
	Assignment2 m_a2;
	
	public Assignment2_IP() {
		super();
		if (getClass() == Assignment2_IP.class)
			init();
	}

	public void init() {
		super.init();
		setTitle("Assignment 2");
	}

	public String getNotice() {
		return "Explain workshop";
	}

	public void setParent(PsUpdateIf parent) {
		super.setParent(parent);
		m_a2 = (Assignment2)parent;

		addSubTitle("Click buttons to get corresponding properties of the model.");

		m_bGetGradientMatrix = new Button("Get Gradient Matrix");
		m_bGetGradientMatrix.addActionListener(this);
		Panel panel1 = new Panel(new FlowLayout(FlowLayout.CENTER));
		panel1.add(m_bGetGradientMatrix);
		add(panel1);
		
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
		
		if (source == m_bGetGradientMatrix) {
			PnSparseMatrix gradientMatrix = m_a2.getGradientMatrix();
			PsDebug.message("Gradient Matrix: " + gradientMatrix.toString());
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
