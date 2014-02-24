package ui;

import java.awt.Component;

import javax.swing.JPanel;

import objects.Base;
import objects.Unit;

public class InfoPanel extends JPanel implements Informative{
	private static final long serialVersionUID = 1895792502596588154L;

	/**
	 * Shows context information for component - for example if Base is selected it shows a possible amount of units to create, how much resources is available etc.
	 * @param component
	 */
	public void showContext(Component component) {
		if (component.getClass().equals(Base.class)) {
			showBaseContext();
		} else if (component.getClass().equals(Unit.class)) {
			showUnitContext();
		}
	}

	public void showBaseContext() {
		// TODO Auto-generated method stub
		
	}

	public void showUnitContext() {
		// TODO Auto-generated method stub
		
	}

}
