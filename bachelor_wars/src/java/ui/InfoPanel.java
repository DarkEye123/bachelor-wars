package ui;


import java.awt.BorderLayout;
import java.awt.Component;
import java.util.LinkedList;

import javax.swing.JPanel;

import objects.Base;
import objects.units.Unit;

public class InfoPanel extends JPanel implements Informative{
	private static final long serialVersionUID = 1895792502596588154L;
	public static final int NOTHING = 0;
	public static final int UNIT = 1;

	ControlPanel controlPanel;
	BaseInfoPanel basePanel;
	LinkedList<Base> baseList;
	Base actualBase;
	
	int actualType = NOTHING;
	
	public InfoPanel(ControlPanel controlPanel) {
		this.controlPanel = controlPanel;
	}

	/**
	 * Initialize info panel for different types of information.
	 * Info panel contains panel displaying information
	 * about actual chosen unit, about actual chosen base.
	 * By default information about players base is displayed.
	 */
	public void init() {
		/*
		 * Initialize panel for base
		 */
		baseList = GameMap.getBaseList();
		this.add(new BaseInfoPanel(baseList.getFirst(), controlPanel));
	}
	
	/**
	 * Shows context information for component - for example if Base is selected it shows a possible amount of units to create, how much resources is available etc.
	 * @param component
	 */
//	public void showContext(int component, int type) {
//		if (component == GameModel.BASE) {
//			showBaseContext(type);
//		} else if (component == GameModel.UNIT) {
//			showUnitContext(type);
//		}
//	}

	public void showBaseContext(Base base) {
		eraseContainer();
		this.add(new BaseInfoPanel(base, controlPanel));
	}
	
	//TODO try real erase
	private void eraseContainer() {
		for (Component comp:this.getComponents()) {
			comp.setVisible(false);
		}
	}

	/**
	 * Shows context information for Unit (after click on GamePanel) how much resources costs etc.
	 * @param type - type of unit to exactly identify which information to show
	 */
	public void showUnitContext(int type) {
		eraseContainer();
		this.add(new UnitInfoPanel(type, controlPanel), BorderLayout.CENTER);
	}
	
	/**
	 * Shows context information for Unit how much hp left etc.
	 * @param unit - unit object to show info about
	 */
	public void showUnitContext(Unit unit) {
		eraseContainer();
		this.add(new UnitInfoPanel(unit, controlPanel), BorderLayout.CENTER);
	}
	
}
