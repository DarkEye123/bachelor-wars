package ui.menu;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import env.GameEnv;

public class Menu {

	JFrame frame;
	JPanel panel;
	JButton buttonBack;
	JButton buttonExit;
	JButton buttonStart;
	
	GameEnv env;
	
	public Menu(GameEnv gameEnv) {
		env = gameEnv;
	}
	
	public void init() {
		frame = new JFrame();
		panel = new JPanel();
		
		buttonBack = new JButton("back");
		buttonExit = new JButton("Exit");
		buttonStart = new JButton("Start");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 150);
	}
	
}
