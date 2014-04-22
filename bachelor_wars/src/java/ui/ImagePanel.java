package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private static final long serialVersionUID = 6644886751888735917L;
	
	ImageIcon image;
	int width;
	int height;
	boolean canDraw = false;
	
	public ImagePanel(Image image, int width, int height) {
		this.image = new ImageIcon(image);;
		this.width = width;
		this.height = height;
		GameView.setComponentSize(new Dimension(width, height), this);
//		image.get
		JLabel l = new JLabel(this.image);
		GameView.setComponentSize(new Dimension(width, height), l);
		this.add(l);
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
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(image.getImage(), 0, 0, width, height, image.getImageObserver());
		drawBounds(g, canDraw);
	}

	public boolean isCanDraw() {
		return canDraw;
	}

	public void setCanDraw(boolean canDraw) {
		this.canDraw = canDraw;
	}
	
	


}
