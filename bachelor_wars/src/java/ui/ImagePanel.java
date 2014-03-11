package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private static final long serialVersionUID = 6644886751888735917L;
	
	BufferedImage image;
	int width;
	int height;
	boolean canDraw = false;
	
	public ImagePanel(BufferedImage image, int width, int height) {
		this.image = image;
		this.width = width;
		this.height = height;
		GameView.setComponentSize(new Dimension(width, height), this);
	}
	
	public ImagePanel(BufferedImage image, float width, float height) {
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
		g.drawImage(image, 0, 0, width, height, null);
		drawBounds(g, canDraw);
	}

	public boolean isCanDraw() {
		return canDraw;
	}

	public void setCanDraw(boolean canDraw) {
		this.canDraw = canDraw;
	}
	
	


}
