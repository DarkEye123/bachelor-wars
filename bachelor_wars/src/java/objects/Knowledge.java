package objects;

import jason.environment.grid.Location;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ui.GameMap;

public class Knowledge extends GameObject {
	
	private BufferedImage image;
	private static int kPerR = 5;
	private Base base;
	private static int _id_ = 100; //identifier of a unit
	protected int id;
	public int STATE = GameMap.ROUND;

	public Knowledge(Location location, Dimension cellSize) {
		super(location, new Dimension(1,1), cellSize);
		try {
			image = ImageIO.read(new File("pics/knowledge.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		name = "Source of knowledge";
		_id_++;
		id = _id_;
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
        if (base != null) {
        	g.setColor(base.getColor());
        	g.fillOval(nx, ny, cellSizeW / 5, cellSizeH / 5);
        }
	}



	public int getKnowledgePerRound() {
		return kPerR;
	}

	public static void setKnowledgePerRound(int knowledge) {
		kPerR = knowledge;
	}



	public Base getBase() {
		synchronized (countLock) {
			return base;
		}
	}



	public void setBase(Base base) {
		synchronized (countLock) {
			if (this.base != null) 
				this.base.getKnowledgeList().remove(this);
			if (base != null)
				base.getKnowledgeList().add(this);
			this.base = base;
		}
	}



	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "[" + getId() + ", " + getX() + ", " + getY() + "]";
	}
}
