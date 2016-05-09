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

public class Assignment1Task1_IP extends PjWorkshop_IP implements ActionListener {

	protected Button m_bComputeGenus;
	protected Label m_lGenus;
	
	Assignment1Task1 m_a1t1;
	
	public Assignment1Task1_IP() {
		super();
		if(getClass() == Assignment1Task1_IP.class)
			init();
	}
	
	public void init() {
		super.init();
		setTitle("Assignment 1 Task 1");
	}
	
	public String getNotice() {
		return "This text should explain what the workshop is about and how to use it.";
	}
	
	public void setParent(PsUpdateIf parent) {
		super.setParent(parent);
		m_a1t1 = (Assignment1Task1)parent;
	
		addSubTitle("Example of a subtitle");
		
		m_bComputeGenus = new Button("Compute genus");
		m_bComputeGenus.addActionListener(this);
		m_lGenus = new Label("label");
		Panel panel1 = new Panel(new FlowLayout(FlowLayout.CENTER));
		panel1.add(m_bComputeGenus);
		panel1.add(m_lGenus);
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
		if (source == m_bComputeGenus) {
			int genus = m_a1t1.computeGenus();
			m_lGenus.setText(Integer.toString(genus));
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
