package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ImagePanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 6644886751888735917L;
	
	Image image;
	int width;
	int height;
	boolean canDraw = false;
	
	public ImagePanel(Image image, int width, int height) {
		this.image = image;
		this.width = width;
		this.height = height;
		GameView.setComponentSize(new Dimension(width, height), this);
		new Timer(100, this).start();
	}
	
	public ImagePanel(Image image, float width, float height) {
		this(image, Math.round(width), Math.round(height));
	}
	
	public void drawBounds(Graphics g, boolean draw) {
		if (draw) {
			g.drawRect(0, 0, width-2, height-2);
		}
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, width, height, null);
		drawBounds(g, canDraw);
	}

	public boolean isCanDraw() {
		return canDraw;
	}

	public void setCanDraw(boolean canDraw) {
		this.canDraw = canDraw;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}
	
	


}
