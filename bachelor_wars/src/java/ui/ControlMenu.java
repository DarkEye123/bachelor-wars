package ui;
import java.awt.Dimension;

import javax.swing.JPanel;


public class ControlMenu extends JPanel {

	private static final long serialVersionUID = -2262922170788475187L;
	
	public static final float HEIGHT_MULTIPLIER = 0.05f;

	GameView view;
	
	public ControlMenu(GameView gameView) {
		view = gameView;
	}
}
