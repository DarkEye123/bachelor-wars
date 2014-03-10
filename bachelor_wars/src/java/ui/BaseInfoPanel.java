package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import objects.Base;

public class BaseInfoPanel extends JPanel {
	private static final long serialVersionUID = -8594392739063585185L;
	private static final int INTERNAL_PADDING_Y = 20;
	private static final Insets DEFAULT_INSETS = new Insets(0, 20, 0, 20);
	
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
		constraints.ipady = INTERNAL_PADDING_Y;
		constraints.insets = DEFAULT_INSETS;
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
