package ui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import objects.Base;

public class BaseInfoPanel extends JPanel {
	private static final long serialVersionUID = -8594392739063585185L;
	private static final int INTERNAL_PADDING_Y = 9;
	private static final Insets DEFAULT_INSETS = new Insets(0, 5, 0, 5);
	
	Base base;
	
	JLabel baseName, freeSlots, actualKnowledge, killedEnemies, incomePerRound;
	GridBagConstraints constraints;
	
	public BaseInfoPanel(Base base) {
		this.base = base;
		Font font = new Font("VL Gothic", 0, 15);
		baseName = new JLabel("Player: " + base.getName());
		freeSlots = new JLabel("Units to create: " + base.getFreeSlots()+"/"+base.getMaxSlots());
		actualKnowledge = new JLabel("Knowledge to use: " + base.getKnowledge());
		killedEnemies = new JLabel("Killed enemies: " + base.getKilledEnemies());
		incomePerRound = new JLabel("Income per Round: " + base.computeIncome());
		constraints = new GridBagConstraints();
		
		baseName.setFont(font);
		freeSlots.setFont(font);
		actualKnowledge.setFont(font);
		killedEnemies.setFont(font);
		incomePerRound.setFont(font);
		constraints = new GridBagConstraints();
		
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipady = INTERNAL_PADDING_Y;
		constraints.insets = DEFAULT_INSETS;
		constraints.anchor = GridBagConstraints.LINE_START;
		
		this.setLayout(new GridBagLayout());
		this.add(baseName,constraints);
		constraints.gridy = 1;
		this.add(freeSlots,constraints);
		constraints.gridy = 2;
		this.add(killedEnemies,constraints);
		constraints.gridy = 3;
		this.add(incomePerRound,constraints);
		constraints.gridy = 4;
		constraints.gridheight = GridBagConstraints.REMAINDER;
		this.add(actualKnowledge,constraints);
	}
}
