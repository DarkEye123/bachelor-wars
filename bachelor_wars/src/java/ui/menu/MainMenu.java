package ui.menu;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import mapping.GameSettings;
import ui.GameView;
import env.GameEnv;

public class MainMenu extends Menu {

	public static final String TITLE = "Bachelor Wars " + GameEnv.VERSION;
	
	JPanel pane;
	
	public MainMenu(GameEnv gameEnv) {
		super(gameEnv);
	}

	public void init() {
		super.init();
		
		pane = new JPanel();
		
		
		frame.setTitle(TITLE);
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		pane.setLayout(new GridLayout(2,1,10,10));
		
		pane.add(buttonStart);
		
		pane.add(buttonExit);
		
		//pane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
		
		
		panel.add(Box.createVerticalGlue());
		panel.add(Box.createHorizontalGlue());
		panel.add(pane);
		panel.add(Box.createHorizontalGlue());
		panel.add(Box.createVerticalGlue());
		
		frame.getContentPane().add(panel);
		
		//frame.pack();
        frame.setVisible(true);
        
        frame.setLocationRelativeTo(null);
        
        buttonExit.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				System.exit(0);
			}
		});
        
        buttonStart.setText("Create Game");
        buttonStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				new GameSettingsMenu(env);
			}
		});
	}
}
