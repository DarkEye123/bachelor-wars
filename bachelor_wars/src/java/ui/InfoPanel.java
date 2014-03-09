package ui;


import jason.environment.grid.Location;

import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import objects.Base;
import mapping.GameSettings;
import models.GameModel;

public class InfoPanel extends JPanel implements Informative{
	private static final long serialVersionUID = 1895792502596588154L;
	public static final int NOTHING = 0;
	public static final int UNIT = 1;

	ControlPanel controlPanel;
	BaseInfoPanel basePanel;
	List<Base> baseList;
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
		baseList = controlPanel.view.gameMap.getBaseList();
		
		for (Base base:baseList) {
			if (base.getOwner() == GameModel.PLAYER) {
				actualBase = base;
				break;
			}
		}
		this.add(new BaseInfoPanel(actualBase, controlPanel));
	}
	
	/**
	 * Shows context information for component - for example if Base is selected it shows a possible amount of units to create, how much resources is available etc.
	 * @param component
	 */
	public void showContext(int component, int type) {
		if (component == GameModel.BASE) {
			showBaseContext(type);
		} else if (component == GameModel.UNIT) {
			showUnitContext(type);
		}
	}

	public void showBaseContext(int type) {
		
	}

	public void showUnitContext(int type) {
		//buttonMouseInputAdapter.text = "Player: created unit " + type;
		//buttonMouseInputAdapter.type = type;
	}
	class ButtonMouseInputAdapter extends MouseInputAdapter {
		Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");
	    String text;
	    int type;
	    
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getComponent().getName().equals("add unit")) {
				controlPanel.statusArea.append(ft.format(dNow) + ": " + text);
				Location loc = controlPanel.view.settings.getBaseLocations().get(0); //player location must be EVERYTIME FIRST
				controlPanel.view.gameMap.createUnit(loc, GameSettings.PLAYER_ID, type);
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
		
	}
}
