package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class PicturePanel extends JPanel {
	private static final long serialVersionUID = 6644886751888735917L;
	
	BufferedImage picture;
	int width;
	int height;
	
	public PicturePanel(BufferedImage picture, int width, int height) {
		this.picture = picture;
		this.width = width;
		this.height = height;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(picture, 0, 0, width, height, null);
	}
	
	


}
