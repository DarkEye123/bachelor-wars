package ui;
import java.awt.Component;

import javax.swing.JPanel;

import objects.Base;
import objects.Unit;


public class GamePanel extends JPanel implements Informative{
	private static final long serialVersionUID = 1552746400473185110L;
	
	public void init() {
		
	}
	
	/**
	 * Shows context menu for component - for example if Base is selected it shows menu with a possible units to create
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
		for (int unit:Unit.AVAILABLE_UNITS) {
			
		}
		
	}

	public void showUnitContext() {
		// TODO Auto-generated method stub
		
	}

}
