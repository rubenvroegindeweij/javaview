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
public class Assignment1Task3_IP extends Assignment1Task2_IP implements ActionListener{
	protected Assignment1Task3 m_a1t3;
	
	/** Constructor */
	public Assignment1Task3_IP () {
		super();
		if (getClass() == Assignment1Task3_IP.class)
			init();
	}

	/**
	 * Informational text on the usage of the dialog.
	 * This notice will be displayed if this info panel is shown in a dialog.
	 * The text is split at line breaks into individual lines on the dialog.
	 */
	public String getNotice() {
		return "This text should explain what the workshop is about and how to use it.";
	}
	
	/** Assign a parent object. */
	public void setParent(PsUpdateIf parent) {
		super.setParent(parent);
		m_a1t3 = (Assignment1Task3)parent;
		
		addSubTitle("Select Surfaces to be Registered");

		updateGeomList();
		validate();
	}
		
	/** Initialisation */
	public void init() {
		super.init();
		setTitle("Surface Registration");
		
	}

	/**
	 * Handle action events fired by buttons etc.
	 */
	public void actionPerformed(ActionEvent event) {
		super.actionPerformed(event);
		Object source = event.getSource();
		

	}
	
	/**
	 * Get information which bottom buttons a dialog should create
	 * when showing this info panel.
	 */
	protected int getDialogButtons()		{
		return PsDialog.BUTTON_OK;
	}
}
