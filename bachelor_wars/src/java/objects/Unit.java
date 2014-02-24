package objects;
import jason.environment.grid.Location;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import models.GameModel;


public class Unit extends GameObject implements Clickable {

	BufferedImage unitPic = null;
	
	public static final int[] AVAILABLE_UNITS = {GameModel.FIRST_YEAR_STUDENT};
	
	int type; //sets type of unit 

	/**
	 * Constructor of Unit
	 * @param location - coordinates in grid where unit starts
	 * @param baseSize - width and height of unit in cells in grid
	 * @param cellSize - real cell size - width and height in pixels
	 */
	public Unit(Location location, Dimension baseSize, Dimension cellSize) {
		super(location, baseSize, cellSize);
	}

	/**
	 * Constructor of Unit
	 * @param location - coordinates in grid where unit starts
	 * @param baseSize - width and height of unit in cells in grid
	 * @param cellSize - real cell size - width and height in pixels
	 * @param type - set a type of unit -> this type sets GameModel class
	 * @see GameModel
	 */
	public Unit(Location location, Dimension baseSize, Dimension cellSize, int type) {
		this(location,baseSize,cellSize);
		this.type = type;
	}

	public void drawUnit(Graphics g, int width, int height) {
		int nx = x * cellSizeW;
		int ny = y * cellSizeH;
		cellSizeW = width;
		cellSizeH = height;
		g.drawImage(unitPic, nx + 1, ny + 1, cellSizeW - 1, cellSizeH - 1, null);
		g.setColor(Color.lightGray);
        g.drawRect(nx, ny, cellSizeW, cellSizeH);
    }

	public void setUnitPic(File f) {
		try {
			unitPic = ImageIO.read(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
