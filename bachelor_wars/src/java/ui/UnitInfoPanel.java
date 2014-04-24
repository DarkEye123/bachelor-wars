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
	
	JLabel name, cost, hp, moveRange, atk, sp;
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
		
		buttonMouseInputAdapter = new ButtonMouseInputAdapter();
		addButton = new JButton("buy unit");
		addButton.setName(addButton.getText());
		addButton.addMouseListener(buttonMouseInputAdapter);
		
		init();
		
//		constraints.gridheight = GridBagConstraints.REMAINDER;
		constraints.gridx = 0;
		constraints.gridy = 6;
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
		sp = new JLabel("SP: " + unit.getSp());
		atk = new JLabel("ATK: " + unit.getAtk());
		moveRange = new JLabel("MOV: " + unit.getMov());
		cost = new JLabel("COST: " + unit.getCost());
		
		name.setFont(new Font("VL Gothic", 0, 14));
		hp.setFont(font);
		sp.setFont(font);
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
		this.add(sp, constraints);
		constraints.gridy = 3;
		this.add(atk, constraints);
		constraints.gridy = 4;
		this.add(moveRange, constraints);
		constraints.gridy = 5;
		this.add(cost, constraints);
		
//		GameView.setComponentSize(controlPanel.getSize(), this);
	}
	
	@Override
	public void repaint() {
		super.repaint();
		if (addButton != null) {
			Base player = controlPanel.view.getGameMap().getPlayerBase();
			boolean canBuy = unit.getCost() <= player.getKnowledge();
			boolean hasFreeSlot = player.getFreeSlots() > 0;
			boolean isMapEnabled = controlPanel.view.getGameMap().isEnabled();
			addButton.setEnabled(isMapEnabled & hasFreeSlot & canBuy);
		}
	}

	class ButtonMouseInputAdapter extends MouseInputAdapter {
		Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");
	    String text;
	    int type;
	    
		@Override
		public void mouseClicked(MouseEvent e) {
			if (controlPanel.view.getGameMap().isEnabled()) {
				Base playerBase = controlPanel.view.getGameMap().getPlayerBase();
				if (playerBase.getFreeSlots() > 0 && unit.getCost() <= playerBase.getKnowledge()) {
					Location loc = playerBase.getLocation(); //player location must be EVERYTIME FIRST
					unit = controlPanel.view.gameMap.createUnit(loc, GameSettings.PLAYER_ID, unit.getType());
					controlPanel.statusArea.append(ft.format(dNow) + ": Player: created unit" + unit.getName());
				}
			}
		}
	}
}
