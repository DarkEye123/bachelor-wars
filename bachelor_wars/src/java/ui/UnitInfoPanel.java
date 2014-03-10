package ui;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import objects.Unit;


public class UnitInfoPanel extends JPanel {
	private static final long serialVersionUID = -994376929960143428L;
	private static final int INTERNAL_PADDING_Y = 20;
	private static final Insets DEFAULT_INSETS = new Insets(0, 20, 0, 20);
	
	JLabel name, cost, hp, moveRange;
	JButton addButton;
	GridBagConstraints constraints;
	ControlPanel controlPanel;
	Unit unit;

	public UnitInfoPanel(Unit unit, ControlPanel controlPanel) {
		this.unit = unit;
		this.controlPanel = controlPanel;
		
		constraints = new GridBagConstraints();
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipady = INTERNAL_PADDING_Y;
		constraints.insets = DEFAULT_INSETS;
		constraints.anchor = GridBagConstraints.CENTER;
		
		name = new JLabel("Unit Name: " + unit.getName());
		hp = new JLabel("Unit Name: " + unit.getName());
		name = new JLabel("Unit Name: " + unit.getName());
		name = new JLabel("Unit Name: " + unit.getName());
	}
}
