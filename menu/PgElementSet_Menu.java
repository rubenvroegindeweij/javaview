package menu;

import jv.geom.PgPointSet_Menu;
import jv.geom.PgElementSet;
import jv.geom.PgPointSet;
import jv.object.PsDebug;
import jv.object.PsDialog;
import jv.objectGui.PsMethodMenu;
import jv.object.PsObject;
import jv.project.PgGeometryIf;
import jv.project.PvDisplayIf;
import jv.project.PvViewerIf;
import jv.vecmath.PdVector;
import jvx.project.PjWorkshop_Dialog;
import workshop.*;

public class PgElementSet_Menu extends PgPointSet_Menu {

	private enum MenuEntry {
		// MyWorkshop			("MyWorkshop..."),
		// Registration		("Surface Registration..."),
		Assignment1Task1	("Assignment1Task1..."),
		Assignment1Task2	("Assignment1Task2..."),
		Assignment1Task3	("Assignment1Task3..."),
		Assignment2			("Assignment2..."),
		Assignment3			("Assignment3...")
		// Additional entries...
		;
		protected final String name;
		MenuEntry(String name) { this.name  = name; }

		public static MenuEntry fromName(String name) {
			for (MenuEntry entry : MenuEntry.values()) {
				if (entry.name.equals(name)) return entry;
			}
			return null;
		}
	}

	protected PgElementSet m_elementSet;

	protected PvViewerIf m_viewer;

	public void init(PsObject anObject) {
		super.init(anObject);
		m_elementSet = (PgElementSet)anObject;

		String menuDev = "My Workshops";
		addMenu(menuDev);
		for (MenuEntry entry : MenuEntry.values()) {
			addMenuItem(menuDev, entry.name);
		}
	}

	public boolean applyMethod(String aMethod) {
		if (super.applyMethod(aMethod))
			return true;

		if (PsDebug.NOTIFY) PsDebug.notify("trying method = " + aMethod);

		PvDisplayIf currDisp = null;
		if (getViewer() == null) {
			if (PsDebug.WARNING) PsDebug.warning("missing viewer");
		} else {
			currDisp = getViewer().getDisplay();
			if (currDisp == null) PsDebug.warning("missing display.");
		}

		PsDialog dialog;
		MenuEntry entry = MenuEntry.fromName(aMethod);
		if (entry == null) return false;
		switch (entry) {
		/*
		case MyWorkshop:
		MyWorkshop ws = new MyWorkshop();
		ws.setGeometry(m_elementSet);
		if (currDisp == null) {
			if (PsDebug.WARNING) PsDebug.warning("missing display.");
		} else
			ws.setDisplay(currDisp);
		dialog = new PjWorkshop_Dialog(false);
		dialog.setParent(ws);
		dialog.update(ws);
		dialog.setVisible(true);
		break;
		case Registration:
		Registration reg = new Registration();
		reg.setGeometry(m_elementSet);
		if (currDisp == null) {
			if (PsDebug.WARNING) PsDebug.warning("missing display.");
		} else
			reg.setDisplay(currDisp);
		dialog = new PjWorkshop_Dialog(false);
		dialog.setParent(reg);
		dialog.update(reg);
		dialog.setVisible(true);
		break;
		*/
		case Assignment1Task1:
			Assignment1Task1 a1t1 = new Assignment1Task1();
			a1t1.setGeometry(m_elementSet);
			if (currDisp == null) {
				if (PsDebug.WARNING) PsDebug.warning("missing display.");
			} else
				a1t1.setDisplay(currDisp);
			dialog = new PjWorkshop_Dialog(false);
			dialog.setParent(a1t1);
			dialog.update(a1t1);
			dialog.setVisible(true);
			break;
		case Assignment1Task2:
			Assignment1Task2 a1t2 = new Assignment1Task2();
			a1t2.setGeometry(m_elementSet);
			if (currDisp == null) {
				if (PsDebug.WARNING) PsDebug.warning("missing display.");
			} else
				a1t2.setDisplay(currDisp);
			dialog = new PjWorkshop_Dialog(false);
			dialog.setParent(a1t2);
			dialog.update(a1t2);
			dialog.setVisible(true);
			break;
		case Assignment1Task3:
			Assignment1Task3 a1t3 = new Assignment1Task3();
			a1t3.setGeometry(m_elementSet);
			if (currDisp == null) {
				if (PsDebug.WARNING) PsDebug.warning("missing display.");
			} else
				a1t3.setDisplay(currDisp);
			dialog = new PjWorkshop_Dialog(false);
			dialog.setParent(a1t3);
			dialog.update(a1t3);
			dialog.setVisible(true);
			break;
		case Assignment2:
			Assignment2 a2 = new Assignment2();
			a2.setGeometry(m_elementSet);
			if (currDisp == null) {
				if (PsDebug.WARNING) PsDebug.warning("missing display.");
			} else
				a2.setDisplay(currDisp);
			dialog = new PjWorkshop_Dialog(false);
			dialog.setParent(a2);
			dialog.update(a2);
			dialog.setVisible(true);
			break;
		case Assignment3:
			Assignment3 a3 = new Assignment3();
			a3.setGeometry(m_elementSet);
			if (currDisp == null) {
				if (PsDebug.WARNING) PsDebug.warning("missing display.");
			} else
				a3.setDisplay(currDisp);
			dialog = new PjWorkshop_Dialog(false);
			dialog.setParent(a3);
			dialog.update(a3);
			dialog.setVisible(true);
			break;
		}

		return true;
	}

	public PvViewerIf getViewer() { return m_viewer; }

	public void setViewer(PvViewerIf viewer) { m_viewer = viewer; }

}