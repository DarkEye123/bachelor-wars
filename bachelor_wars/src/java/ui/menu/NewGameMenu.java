package ui.menu;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import ui.GameView;
import env.GameEnv;
import mapping.GameSettings;


public class NewGameMenu extends Menu {
	JPanel pane;

	public NewGameMenu(GameEnv env) {
		super(env);
	}

	public void init() {
		super.init();
		
		pane = new JPanel();
		
		pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
		panel.setLayout(new GridLayout(2,1,10,10));
		
		panel.add(buttonStart);
		panel.add(buttonBack);
		
		buttonStart.setText("Create Game");
		
		buttonStart.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				//TODO add window with game settings
				GameSettings settings = new GameSettings();
				settings.defaultInit();
				env.setView(new GameView(settings));
			}
		});
		
		buttonBack.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				new MainMenu(env).init();
			}
		});
		
		pane.add(Box.createVerticalGlue());
		pane.add(Box.createHorizontalGlue());
		pane.add(panel);
		pane.add(Box.createVerticalGlue());
		pane.add(Box.createHorizontalGlue());
		
		frame.getContentPane().add(pane);
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);
	}
}
