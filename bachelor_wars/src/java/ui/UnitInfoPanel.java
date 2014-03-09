package ui;

import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import objects.Unit;


public class UnitInfoPanel extends JPanel {
	private static final long serialVersionUID = -994376929960143428L;
	
	JLabel unitName, freeSlots, actualKnowledge;
	JButton addButton;
	GridBagConstraints constraints;
	ControlPanel controlPanel;

	public UnitInfoPanel(Unit unit, ControlPanel controlPanel) {
		
	}
}
