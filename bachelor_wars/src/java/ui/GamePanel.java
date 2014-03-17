package ui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import objects.units.Unit;
import mapping.Dictionary;
import mapping.GameSettings;


public class GamePanel extends JPanel{
	private static final long serialVersionUID = 1552746400473185110L;
	public static final float ICON_PADDING_X = 0.2f;
	public static final float ICON_PADDING_Y = 0.35f;
	public static final int PADDING_X = 10;
	public static final int PADDING_Y = 10;

	ControlPanel controlPanel;
	List<JPanel> unitList = new ArrayList<JPanel>();
	GridBagConstraints constraints;
	IconMouseInputAdapter iconMouseInputAdapter;
	
	public GamePanel(ControlPanel controlPanel) {
		this.controlPanel = controlPanel;
	}

	public void init() {
		this.setLayout(new GridBagLayout());
		iconMouseInputAdapter = new IconMouseInputAdapter();
		Random rand = new Random();
		constraints = new GridBagConstraints();
		GridBagConstraints iconstraints = new GridBagConstraints();
		iconstraints.fill = GridBagConstraints.NONE;
		iconstraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		//constraints.ipadx = ICON_PADDING_X;
		//constraints.ipady = ICON_PADDING_Y;
		int x = 0;
		int y = 0;;
		int col = GameSettings.AVAILABLE_UNITS.size()/2;
		int width = this.getWidth() / col;
		width = width + (this.getWidth() % col);
		int height = this.getHeight() / 2;
		
		int iwidth = Math.round(width - (width * ICON_PADDING_X));
		int iheight = Math.round(height - (height * ICON_PADDING_Y));
		
		for (Dictionary<Integer, Unit> map:GameSettings.AVAILABLE_UNITS) {
			
			ImagePanel picture = new ImagePanel(map.getValue().getImage(), iwidth, iheight);
			picture.setName(map.getIndex()+"");
			//GameView.setComponentSize(new Dimension(iwidth, iheight), picture);
			picture.setOpaque(true);
			picture.addMouseListener(iconMouseInputAdapter);
			
			JLabel name = new JLabel();
			name.setText(map.getValue().getName());
			
			JPanel panel = new JPanel();
			GameView.setComponentSize(new Dimension(width,height), panel);
			panel.setLayout(new GridBagLayout());
			
			if ( y == 0 && x >= col) {
				y = 1;
				x = 0;
			}
			
			constraints.gridx = x;
			constraints.gridy = y;
			iconstraints.gridy = 0;
			panel.add(name, iconstraints);
			iconstraints.gridy = 1;
			panel.add(picture, iconstraints);
			++x;
			this.add(panel, constraints);
			panel.setBackground(new Color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat()));
			panel.repaint();
		}
	}
	
	class IconMouseInputAdapter extends MouseInputAdapter {
		Color bak;
		
		public void mouseClicked(MouseEvent e) {
			if (e.getComponent().getName().equals(GameSettings.FIRST_YEAR_STUDENT+"")) {
				controlPanel.infoPanel.showUnitContext(GameSettings.FIRST_YEAR_STUDENT);
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			bak = e.getComponent().getBackground();
			e.getComponent().setBackground(new Color(220, 208, 242));
			((ImagePanel) e.getComponent()).setCanDraw(true);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			e.getComponent().setBackground(bak);
			((ImagePanel) e.getComponent()).setCanDraw(false);
		}
		
	}

}
