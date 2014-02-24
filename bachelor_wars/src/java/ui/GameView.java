package ui;
import jason.environment.grid.GridWorldModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ui.ControlMenu;
import ui.ControlPanel;
import ui.menu.MainMenu;
import mapping.GameSettings;
import models.GameModel;


public class GameView extends JPanel {

	private static final long serialVersionUID = 4375482311151482280L;
	
	public static final int WINDOW_WIDTH = 1024;
	public static final int WINDOW_HEIGHT = 768;
	
	protected Font defaultFont = new Font("Arial", Font.BOLD, 10);
	
	GameModel model;
	GameMap gameMap;
	GameSettings settings;
	JFrame frame;
	ControlMenu controlMenu;
	ControlPanel controlPanel;
	
	GridBagConstraints constraints;
	
	public GameView(GridWorldModel model, String title) {
		
		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		frame.setLayout(new BorderLayout());
		frame.setLocationRelativeTo(null);
		frame.getContentPane().add(this, BorderLayout.CENTER);
		this.setSize(frame.getSize());
		this.setPreferredSize(frame.getSize());

	}

	public GameView(GameModel model, GameSettings settings) {
		this(model, MainMenu.TITLE);
		this.model = model;
		this.settings = settings;
	}
	
	public void init() {
		this.setLayout(new GridBagLayout());
		constraints = new GridBagConstraints();
		//constraints.gridheight = 3;
		//constraints.gridwidth = 1;
		
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		
		controlMenu = createControlMenu(constraints);

		constraints.gridy = 1;
		gameMap = createGameMap(constraints);
		
		constraints.gridy = 2;
		constraints.gridheight = GridBagConstraints.REMAINDER;
		controlPanel = createControlPanel(constraints);
		
	}
	
	public ControlMenu createControlMenu(GridBagConstraints constraints) {
		ControlMenu controlMenu = new ControlMenu(this);
		controlMenu.setBackground(Color.yellow);
		Dimension size = new Dimension(this.getWidth(), Math.round(this.getHeight() * ControlMenu.HEIGHT_MULTIPLIER));
		controlMenu.setMinimumSize(size);
		controlMenu.setMaximumSize(size);
		controlMenu.setPreferredSize(size);
		controlMenu.setSize(size);
		this.add(controlMenu, constraints);
		return controlMenu;
	}
	
	public GameMap createGameMap(GridBagConstraints constraints) {
		GameMap gameMap = new GameMap(this);
		gameMap.setBackground(Color.green);
		Dimension size = new Dimension(Math.round(this.getWidth() * GameMap.WIDTH_MULTIPLIER), Math.round(this.getHeight() * GameMap.HEIGHT_MULTIPLIER));
		gameMap.setMinimumSize(size);
		gameMap.setMaximumSize(size);
		gameMap.setPreferredSize(size);
		gameMap.setSize(size);
		this.add(gameMap, constraints);
		gameMap.init();
		return gameMap;
	}
	
	public ControlPanel createControlPanel(GridBagConstraints constraints) {
		ControlPanel controlPanel = new ControlPanel(this);
		controlPanel.setBackground(Color.red);
		Dimension size = new Dimension(this.getWidth(), Math.round(this.getHeight() * ControlPanel.HEIGHT_MULTIPLIER));
		controlPanel.setMinimumSize(size);
		controlPanel.setMaximumSize(size);
		controlPanel.setPreferredSize(size);
		controlPanel.setSize(size);
		this.add(controlPanel, constraints);
		controlPanel.init();
		return controlPanel;
	}

	public GameModel getModel() {
		return model;
	}

	public void setModel(GameModel model) {
		this.model = model;
	}

	public GameMap getGameMap() {
		return gameMap;
	}

	public void setGameMap(GameMap gameMap) {
		this.gameMap = gameMap;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public ControlMenu getControlMenu() {
		return controlMenu;
	}

	public void setControlMenu(ControlMenu controlMenu) {
		this.controlMenu = controlMenu;
	}

	public ControlPanel getControlPanel() {
		return controlPanel;
	}

	public void setControlPanel(ControlPanel controlPanel) {
		this.controlPanel = controlPanel;
	}
	
}
