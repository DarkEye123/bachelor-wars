package ui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import ui.BTextPane;


public class ControlPanel extends JPanel {
	
	private static final long serialVersionUID = 3305829607373774029L;

	public static final float HEIGHT_MULTIPLIER = 0.2f;
	public static final float TEXT_AREA_WIDTH_MULTIPLIER = 0.4f;
	public static final float GAME_PANEL_WIDTH_MULTIPLIER = 0.6f;
	
	
	
	GameView view;
	BTextPane statusArea;
	JScrollPane scrollArea;
	GridBagConstraints constraints;
	GamePanel gamePanel;

	public ControlPanel(GameView gameView) {
		view = gameView;
		this.setLayout(new GridBagLayout());
		constraints = new GridBagConstraints();
		
		constraints.gridx = 0;
		constraints.fill = GridBagConstraints.BOTH;
	}

	public void init() {
		statusArea = new BTextPane();
		statusArea.setEditable(false);
		scrollArea = new JScrollPane(statusArea);
		
		
		scrollArea.setMinimumSize(new Dimension(Math.round(getWidth() * TEXT_AREA_WIDTH_MULTIPLIER), getHeight()) );
		scrollArea.setMaximumSize(new Dimension(Math.round(getWidth() * TEXT_AREA_WIDTH_MULTIPLIER), getHeight()) );
		scrollArea.setPreferredSize(new Dimension(Math.round(getWidth() * TEXT_AREA_WIDTH_MULTIPLIER), getHeight()) );
		
		this.add(scrollArea, constraints);
		
		constraints.gridx = 1;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		
		gamePanel = new GamePanel();
		
		gamePanel.setMinimumSize(new Dimension(Math.round(getWidth() * GAME_PANEL_WIDTH_MULTIPLIER), getHeight()) );
		gamePanel.setMaximumSize(new Dimension(Math.round(getWidth() * GAME_PANEL_WIDTH_MULTIPLIER), getHeight()) );
		gamePanel.setPreferredSize(new Dimension(Math.round(getWidth() * GAME_PANEL_WIDTH_MULTIPLIER), getHeight()) );
		
		gamePanel.setBackground(Color.GREEN);
		
		this.add(gamePanel, constraints);
		
	}

}
