package ui;

import jason.environment.grid.Location;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import mapping.Dictionary;
import mapping.GameSettings;
import models.GameModel;
import objects.units.Unit;


public class UnitInfoPanel extends JPanel {
	private static final long serialVersionUID = -994376929960143428L;
	private static final int INTERNAL_PADDING_Y = 20;
	private static final Insets DEFAULT_INSETS = new Insets(0, 20, 0, 20);
	
	private static final float PIC_WIDTH = 0.2f;
	private static final float PIC_HEIGHT = 0.5f;
	
	JLabel name, cost, hp, moveRange, atk;
	JButton addButton;
	GridBagConstraints constraints;
	ControlPanel controlPanel;
	ImagePanel image;
	Unit unit;
	private ButtonMouseInputAdapter buttonMouseInputAdapter;

	public UnitInfoPanel(Unit unit, ControlPanel controlPanel) {
		this.unit = unit;
		this.controlPanel = controlPanel;
		image = new ImagePanel(unit.getImage(), controlPanel.getWidth() * PIC_WIDTH, controlPanel.getHeight() * PIC_HEIGHT);
		
		constraints = new GridBagConstraints();
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipady = INTERNAL_PADDING_Y;
		constraints.insets = DEFAULT_INSETS;
		constraints.anchor = GridBagConstraints.CENTER;
		
		name = new JLabel("Unit Name: " + unit.getName());
		hp = new JLabel("HP: " + unit.getHp());
		atk = new JLabel("ATK: " + unit.getAtk());
		moveRange = new JLabel("MOV: " + unit.getMov());
		cost = new JLabel("COST: " + unit.getCost());
		
		this.add(image, constraints);
	}
	
	public UnitInfoPanel(int type, ControlPanel controlPanel) {
		this.controlPanel = controlPanel;
		for (Dictionary<Integer, Unit> map:GameModel.AVAILABLE_UNITS) {
			if (map.getIndex() == type) {
				unit = map.getValue();
				image = new ImagePanel(unit.getImage(), controlPanel.getWidth() * PIC_WIDTH, controlPanel.getHeight() * PIC_HEIGHT);
			}
		}
		
		buttonMouseInputAdapter = new ButtonMouseInputAdapter();
		addButton = new JButton("buy unit");
		addButton.setName(addButton.getText());
		addButton.addMouseListener(buttonMouseInputAdapter);
		
		this.setLayout(new GridBagLayout());
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipady = INTERNAL_PADDING_Y;
		constraints.insets = DEFAULT_INSETS;
		constraints.anchor = GridBagConstraints.CENTER;
		
		name = new JLabel("Unit Name: " + unit.getName());
		hp = new JLabel("HP: " + unit.getHp());
		atk = new JLabel("ATK: " + unit.getAtk());
		moveRange = new JLabel("MOV: " + unit.getMov());
		cost = new JLabel("COST: " + unit.getCost());
		

//		constraints.gridheight = GridBagConstraints.REMAINDER;
		this.add(image, constraints);
//		constraints.gridheight = GridBagConstraints.RELATIVE;
//		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.gridx = 1;
		this.add(name, constraints);
		constraints.gridy = 1;
		this.add(hp, constraints);
		constraints.gridy = 2;
		this.add(atk, constraints);
		constraints.gridy = 3;
		this.add(moveRange, constraints);
		constraints.gridy = 4;
		this.add(cost, constraints);
		constraints.gridy = 5;
		constraints.gridheight = GridBagConstraints.REMAINDER;
		constraints.gridwidth = 2;
		this.add(addButton, constraints);
		
		GameView.setComponentSize(controlPanel.getSize(), this);
	}
	
	class ButtonMouseInputAdapter extends MouseInputAdapter {
		Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");
	    String text;
	    int type;
	    
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getComponent().getName().equals("buy unit")) {
				Location loc = controlPanel.view.settings.getBaseLocations().get(0); //player location must be EVERYTIME FIRST
				unit = controlPanel.view.gameMap.createUnit(loc, GameSettings.PLAYER_ID, type);
				controlPanel.statusArea.append(ft.format(dNow) + ": Player: created unit" + unit.getName());
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
