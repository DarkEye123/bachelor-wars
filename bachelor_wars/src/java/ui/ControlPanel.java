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
	public static final float TEXT_AREA_WIDTH_MULTIPLIER = 0.2f;
	public static final float GAME_PANEL_WIDTH_MULTIPLIER = 0.3f;
	public static final float INFO_PANEL_WIDTH_MULTIPLIER = 0.3f;
	
	
	
	GameView view;
	BTextPane statusArea;
	JScrollPane scrollArea;
	GridBagConstraints constraints;
	GamePanel gamePanel;
	InfoPanel infoPanel;

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
		GameView.setComponentSize(new Dimension(Math.round(getWidth() * TEXT_AREA_WIDTH_MULTIPLIER), getHeight()), scrollArea);
		this.add(scrollArea, constraints);
		
		constraints.gridx = 1;
		constraints.weightx = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		
		infoPanel = new InfoPanel(this);
		GameView.setComponentSize(new Dimension(Math.round(getWidth() * INFO_PANEL_WIDTH_MULTIPLIER), getHeight()), infoPanel);
		infoPanel.setBackground(Color.GRAY);
		this.add(infoPanel, constraints);
		infoPanel.init();
		
		constraints.gridx = 2;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.weightx = 0;
		
		gamePanel = new GamePanel(this);
		GameView.setComponentSize(new Dimension(Math.round(getWidth() * GAME_PANEL_WIDTH_MULTIPLIER), getHeight()), gamePanel);
		gamePanel.setBackground(Color.GREEN);
		this.add(gamePanel, constraints);
		gamePanel.init();
		
	}

}
