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

public class Assignment2_IP extends PjWorkshop_IP implements ActionListener {

	protected   Button			m_bGetGradientMatrix, m_bUndo;
	protected	TextField		tf1, tf2, tf3, tf4, tf5, tf6, tf7, tf8, tf9;
	protected	PdMatrix		A;

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
		return "Mesh Editing";
	}

	public void setParent(PsUpdateIf parent) {
		super.setParent(parent);
		m_a2 = (Assignment2)parent;

		addSubTitle("Please edit the matrix below to get expected mesh deformation.");

		tf1 = new TextField("1", 2);
		tf2 = new TextField("0", 2);
		tf3 = new TextField("0", 2);
		tf4 = new TextField("0", 2);
		tf5 = new TextField("1", 2);
		tf6 = new TextField("0", 2);
		tf7 = new TextField("0", 2);
		tf8 = new TextField("0", 2);
		tf9 = new TextField("1", 2);

		Panel panel1 = new Panel(new FlowLayout(FlowLayout.CENTER));
		panel1.add(tf1);
		panel1.add(tf2);
		panel1.add(tf3);
		add(panel1);

		Panel panel2 = new Panel(new FlowLayout(FlowLayout.CENTER));
		panel2.add(tf4);
		panel2.add(tf5);
		panel2.add(tf6);
		add(panel2);

		Panel panel3 = new Panel(new FlowLayout(FlowLayout.CENTER));
		panel3.add(tf7);
		panel3.add(tf8);
		panel3.add(tf9);
		add(panel3);

		m_bGetGradientMatrix = new Button("Edit Mesh");
		m_bGetGradientMatrix.addActionListener(this);
		m_bUndo = new Button("Undo");
		m_bUndo.addActionListener(this);
		Panel panel4 = new Panel(new FlowLayout(FlowLayout.CENTER));
		panel4.add(m_bGetGradientMatrix);
		panel4.add(m_bUndo);
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

		if (source == m_bGetGradientMatrix) {
			getInputMatrixA();
			try {
				m_a2.editMesh(A);
			} catch (Exception e) {
				PsDebug.message("EXCEPTION: " + e.toString());
			}
			m_a2.m_geom.update(null);
			// PsDebug.message("Gradient Matrix: " + gradientMatrix.toString());
		}

		if (source == m_bUndo) {
			m_a2.undo();
			m_a2.m_geom.update(null);
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
	* Get the input matrix specified by the user.
	*/
	public void getInputMatrixA() {
		double a1 = new Double(tf1.getText());
		double a2 = new Double(tf2.getText());
		double a3 = new Double(tf3.getText());
		double a4 = new Double(tf4.getText());
		double a5 = new Double(tf5.getText());
		double a6 = new Double(tf6.getText());
		double a7 = new Double(tf7.getText());
		double a8 = new Double(tf8.getText());
		double a9 = new Double(tf9.getText());
		double[][] Atemp = {
			{a1, a2, a3},
			{a4, a5, a6},
			{a7, a8, a9}
		};
		A = new PdMatrix(Atemp);
	}
}
