package objects;

import jason.environment.grid.Location;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Knowledge extends GameObject {
	
	private BufferedImage image;
	private int kPerR = 10;
	
	

	public Knowledge(Location location, Dimension cellSize) {
		super(location, new Dimension(1,1), cellSize);
		try {
			image = ImageIO.read(new File("pics/knowledge.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		name = "Source of knowledge";
	}



	@Override
	public void draw(Graphics g, int width, int height) {
		int nx = x * cellSizeW;
		int ny = y * cellSizeH;
		cellSizeW = width;
		cellSizeH = height;
		g.drawImage(image, nx + 1, ny + 1, cellSizeW - 1, cellSizeH - 1, null);
		g.setColor(Color.lightGray);
        g.drawRect(nx, ny, cellSizeW, cellSizeH);
	}



	public int getKnowledgePerRound() {
		return kPerR;
	}

	public void setKnowledgePerRound(int knowledge) {
		this.kPerR = knowledge;
	}

}