package ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import objects.Base;

public class BaseInfoPanel extends JPanel {
	private static final long serialVersionUID = -8594392739063585185L;
	
	Base base;
	
	JLabel baseName;
	JLabel freeSlots;
	
	public BaseInfoPanel(Base base) {
		this.base = base;
		baseName = new JLabel("Player: " + base.getName());
		freeSlots = new JLabel("Units to create: " + base.getFreeSlots());
		
	}
}
