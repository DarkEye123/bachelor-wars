package ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mapping.UnitNameMap;
import mapping.UnitPicMap;
import models.GameModel;
import objects.Base;
import objects.Unit;


public class GamePanel extends JPanel implements Informative{
	private static final long serialVersionUID = 1552746400473185110L;
	public static final float ICON_PADDING_X = 0.2f;
	public static final float ICON_PADDING_Y = 0.35f;
	public static final int PADDING_X = 10;
	public static final int PADDING_Y = 10;

	
	List<JPanel> unitList = new ArrayList<JPanel>();
	GridBagConstraints constraints;
	
	public void init() {
		this.setLayout(new GridBagLayout());
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
		int col = GameModel.AVAILABLE_UNITS.length/2;
		int width = this.getWidth() / col;
		//System.out.println(width + " " + this.getWidth() + " " + col);
		width = width + (this.getWidth() % col);
		//System.out.println(width + " " + this.getWidth() + " " + col);
		int height = this.getHeight() / 2;
		
		int iwidth = Math.round(width - (width * ICON_PADDING_X));
		int iheight = Math.round(height - (height * ICON_PADDING_Y));
		
		for (UnitPicMap map:GameModel.AVAILABLE_UNITS) {
			JLabel name = new JLabel();
			PicturePanel picture = new PicturePanel(map.getPicture(), iwidth, iheight);
			
			picture.setName(map.getType()+"");
			
			picture.setMinimumSize(new Dimension(iwidth, iheight));
			picture.setMaximumSize(new Dimension(iwidth,iheight));
			picture.setPreferredSize(new Dimension(iwidth,iheight));
			picture.setSize(new Dimension(iwidth,iheight));
			
			for (UnitNameMap nmap:GameModel.UNIT_NAMES) {
				if (nmap.getType() == map.getType())
					name.setText(nmap.getName());
			}
			
			JPanel panel = new JPanel();
			panel.setMinimumSize(new Dimension(width,height));
			panel.setMaximumSize(new Dimension(width,height));
			panel.setPreferredSize(new Dimension(width,height));
			panel.setSize(new Dimension(width,height));
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
	
	/**
	 * Shows context menu for component - for example if Base is selected it shows menu with a possible units to create
	 * @param component
	 */
	public void showContext(Component component) {
		if (component.getClass().equals(Base.class)) {
			showBaseContext();
		} else if (component.getClass().equals(Unit.class)) {
			showUnitContext();
		}
	}

	public void showBaseContext() {
		for (UnitPicMap map:GameModel.AVAILABLE_UNITS) {
			
		}
		
	}

	public void showUnitContext() {
		// TODO Auto-generated method stub
		
	}

}
