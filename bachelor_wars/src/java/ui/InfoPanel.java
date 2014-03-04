package ui;


import jason.environment.grid.Location;

import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import mapping.GameSettings;
import models.GameModel;

public class InfoPanel extends JPanel implements Informative{
	private static final long serialVersionUID = 1895792502596588154L;
	public static final int NOTHING = 0;
	public static final int UNIT = 1;

	ControlPanel controlPanel;
	BaseInfoPanel basePanel;
	ButtonMouseInputAdapter buttonMouseInputAdapter;
	
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
		buttonMouseInputAdapter = new ButtonMouseInputAdapter();

		JButton addButton = new JButton("add");
		addButton.setName(addButton.getText());
		addButton.addMouseListener(buttonMouseInputAdapter);
		
		this.add(addButton);
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
		buttonMouseInputAdapter.text = "Player: created unit " + type;
		buttonMouseInputAdapter.type = type;
	}
	
	class ButtonMouseInputAdapter extends MouseInputAdapter {
		Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");
	    String text;
	    int type;
	    
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getComponent().getName().equals("add")) {
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
