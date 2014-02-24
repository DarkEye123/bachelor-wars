package ui;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import mapping.UnitPicMap;
import objects.Base;
import objects.Unit;


public class GamePanel extends JPanel implements Informative{
	private static final long serialVersionUID = 1552746400473185110L;
	public static final int PADDING_X = 20;
	public static final int PADDING_Y = 10;

	
	List<JPanel> unitList = new ArrayList<JPanel>();
	GridBagConstraints constraints;
	
	public void init() {
		this.setLayout(new GridBagLayout());
		constraints = new GridBagConstraints();
		
		for (UnitPicMap map:Unit.AVAILABLE_UNITS) {
			JPanel panel = new JPanel();
			panel.setName(map.getUnit()+"");
			
		}
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
		for (UnitPicMap map:Unit.AVAILABLE_UNITS) {
			
		}
		
	}

	public void showUnitContext() {
		// TODO Auto-generated method stub
		
	}

}
