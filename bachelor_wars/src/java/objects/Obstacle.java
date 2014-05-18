package objects;

import jason.environment.grid.Location;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Obstacle extends GameObject {
	
	private static int _id_ = 10000; //identifier of a unit
	private int id;
	
	public Obstacle(Location location, Dimension cellSize) {
		super(location, new Dimension(1,1), cellSize);
		_id_++;
		id = _id_;
	}

	@Override
	public void draw(Graphics g, int width, int height) {
//		System.out.println(g);
		int nx = x * cellSizeW;
		int ny = y * cellSizeH;
		cellSizeW = width;
		cellSizeH = height;
//		g.setColor(Color.darkGray);
		g.setColor(Color.gray);
        g.fillRect(nx, ny, cellSizeW, cellSizeH);
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "[" + getId() + ", " + getX() + ", " + getY() + "]";
	}

}
