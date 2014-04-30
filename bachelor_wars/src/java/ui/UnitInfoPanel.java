package ui;

import jason.environment.grid.Location;

import java.awt.Font;
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

import mapping.GameSettings;
import objects.Base;
import objects.units.Unit;


public class UnitInfoPanel extends JPanel {
	private static final long serialVersionUID = -994376929960143428L;
	private static final int INTERNAL_PADDING_Y = -1;
	private static final Insets DEFAULT_INSETS = new Insets(1, 20, 1, 20);
	
	private static final float PIC_WIDTH = 0.1f;
	private static final float PIC_HEIGHT = 0.5f;
	
	JLabel name, cost, hp, moveRange, atk;
	JButton addButton;
	JButton cancelButton;
	JButton doneButton;
	GridBagConstraints constraints;
	ControlPanel controlPanel;
	ImagePanel image;
	Unit unit;
	private ButtonMouseInputAdapter buttonMouseInputAdapter;

	public UnitInfoPanel(Unit unit, ControlPanel controlPanel) {
		this.unit = unit;
		this.controlPanel = controlPanel;
		image = new ImagePanel(unit.getImage(), controlPanel.getWidth() * PIC_WIDTH, controlPanel.getHeight() * PIC_HEIGHT);
		init();
	}
	
	public UnitInfoPanel(int type, ControlPanel controlPanel) {
		this.controlPanel = controlPanel;
		for (Unit u:GameSettings.AVAILABLE_UNITS) {
			if (u.getType() == type) {
				unit = u;
				image = new ImagePanel(unit.getImage(), controlPanel.getWidth() * PIC_WIDTH, controlPanel.getHeight() * PIC_HEIGHT);
			}
		}
		
		buttonMouseInputAdapter = new ButtonMouseInputAdapter(this);
		addButton = new JButton("buy unit");
		addButton.setName(addButton.getText());
		addButton.addMouseListener(buttonMouseInputAdapter);
		
		init();
		
//		constraints.gridheight = GridBagConstraints.REMAINDER;
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.gridwidth = 1;
		this.add(addButton, constraints);
	}
	
	private void init() {
		Font font = new Font("VL Gothic", 0, 13);
		this.setLayout(new GridBagLayout());
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipady = INTERNAL_PADDING_Y;
		constraints.insets = DEFAULT_INSETS;
		constraints.anchor = GridBagConstraints.LINE_START;
		
		name = new JLabel(unit.getName());
		hp = new JLabel("HP: " + unit.getHp());
		atk = new JLabel("ATK: " + unit.getAtk());
		moveRange = new JLabel("MOV: " + unit.getMov());
		cost = new JLabel("COST: " + unit.getCost());
		
		name.setFont(new Font("VL Gothic", 0, 14));
		hp.setFont(font);
		atk.setFont(font);
		moveRange.setFont(font);
		cost.setFont(font);
		

//		constraints.gridheight = GridBagConstraints.REMAINDER;
		constraints.gridheight = 5;
		this.add(image, constraints);
//		constraints.gridheight = GridBagConstraints.RELATIVE;
//		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.gridx = 1;
		constraints.gridheight = 1;
		this.add(name, constraints);
		constraints.gridy = 1;
		this.add(hp, constraints);
		constraints.gridy = 2;
		this.add(atk, constraints);
		constraints.gridy = 3;
		this.add(moveRange, constraints);
		constraints.gridy = 4;
		this.add(cost, constraints);
		
		if (unit.getOwner() == GameSettings.PLAYER && unit.base.getUsableUnits().contains(unit)) {
			buttonMouseInputAdapter = new ButtonMouseInputAdapter(this);
			doneButton = new JButton("done");
			doneButton.setName(doneButton.getText());
			doneButton.addMouseListener(buttonMouseInputAdapter);
			cancelButton = new JButton("cancel");
			cancelButton.setName(cancelButton.getText());
			cancelButton.addMouseListener(buttonMouseInputAdapter);
			
			constraints.gridx = 0;
			constraints.gridy = 5;
//			constraints.anchor = GridBagConstraints.CENTER;
			constraints.gridwidth = 1;
			this.add(cancelButton, constraints);
			constraints.gridx = 1;
			this.add(doneButton, constraints);
		}
		
//		GameView.setComponentSize(controlPanel.getSize(), this);
	}
	
	@Override
	public void repaint() {
		super.repaint();
		if (addButton != null) {
			Base player = controlPanel.view.getGameMap().getPlayerBase();
			boolean canBuy = unit.getCost() <= player.getKnowledge();
			boolean hasFreeSlot = player.getFreeSlots() > 0;
			boolean isMapEnabled = controlPanel.view.getGameMap().canManipulate();
			boolean containUnit = player.getNode().containUnit();
			addButton.setEnabled(isMapEnabled & hasFreeSlot & canBuy && !containUnit);
		}
		if (doneButton != null && cancelButton != null) {
			boolean isEnabled = (unit.getOwner() == GameSettings.PLAYER && unit.base.getUsableUnits().contains(unit));
			doneButton.setEnabled(isEnabled);
			cancelButton.setEnabled(isEnabled);
		}
	}

	class ButtonMouseInputAdapter extends MouseInputAdapter {
		Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");
	    String text;
	    int type;
	    UnitInfoPanel unitInfoPanel;
	    
		public ButtonMouseInputAdapter(UnitInfoPanel unitInfoPanel) {
			this.unitInfoPanel = unitInfoPanel;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			Base playerBase = controlPanel.view.getGameMap().getPlayerBase();
			boolean isMapEnabled = controlPanel.view.getGameMap().canManipulate();
			if (isMapEnabled) {
				String name = e.getComponent().getName();
				if (name.equals("buy unit")) {
					boolean canBuy = unit.getCost() <= playerBase.getKnowledge();
					boolean hasFreeSlot = playerBase.getFreeSlots() > 0;
					boolean containUnit = playerBase.getNode().containUnit();
					if (hasFreeSlot && canBuy && !containUnit) {
						unit = controlPanel.view.gameMap.createUnit(playerBase.getNode(), GameSettings.PLAYER_ID, unit.getType());
						controlPanel.statusArea.append(ft.format(dNow) + ": Player: created unit" + unit.getName());
					}
				} else if (name.equals("done")) {
					unit.setOldLocation(unit.getLocation());
					controlPanel.view.getGameMap().clearMovement();
					unit.base.getUsableUnits().remove(unit);
					controlPanel.view.repaint();
					controlPanel.view.getGameMap().repaint();
					unitInfoPanel.repaint();
					
				} else if (name.equals("cancel")) {
					unit.setLocation(unit.getOldLocation());
					controlPanel.view.getGameMap().clearMovement();
					controlPanel.view.repaint();
					controlPanel.view.getGameMap().repaint();
				}
			}
		}
	}
}
