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

import mapping.GameSettings;
import objects.Base;

public class BaseInfoPanel extends JPanel {
	private static final long serialVersionUID = -8594392739063585185L;
	
	Base base;
	
	JLabel baseName, freeSlots, actualKnowledge;
	GridBagConstraints constraints;
	ControlPanel controlPanel;
	
	//TODO check if controlPanel is usable here or not in some way
	public BaseInfoPanel(Base base, ControlPanel controlPanel) {
		this.controlPanel = controlPanel;
		this.base = base;
		baseName = new JLabel("Player: " + base.getName());
		freeSlots = new JLabel("Units to create: " + base.getFreeSlots()+"/"+base.getMaxSlots());
		actualKnowledge = new JLabel("Knowledge to use: " + base.getKnowledge());
		constraints = new GridBagConstraints();
		
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipady = 20;
		constraints.insets = new Insets(0, 20, 0, 20);
		constraints.anchor = GridBagConstraints.CENTER;
		
		this.setLayout(new GridBagLayout());
		this.add(baseName,constraints);
		constraints.gridy = 1;
		this.add(freeSlots,constraints);
		constraints.gridy = 2;
		constraints.gridheight = GridBagConstraints.REMAINDER;
		this.add(actualKnowledge,constraints);
	}
}
