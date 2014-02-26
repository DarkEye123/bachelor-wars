package objects;
import jason.environment.grid.Location;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import mapping.UnitPicMap;
import models.GameModel;


public class Unit extends GameObject implements Clickable {

	public static final int DEFAULT_LIFE = 50;
	
	private static int _id_; //identifier of a unit
	
	protected String name;
	protected BufferedImage unitPic = null;
	protected int id;
	protected int lives = DEFAULT_LIFE;
		
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
		_id_++;
		id = _id_;
		init();
	}

	private void init() {
		for (UnitPicMap map: GameModel.AVAILABLE_UNITS) {
			if (type == map.getType()) {
				unitPic = map.getPicture();
				break;
			}
		}
	}

	/**
	 * Draws a unit at position on the grid. 
	 * @param g
	 * @param width - actual width of visible cell (in pixels)
	 * @param height - actual height of visible cell (in pixels)
	 */
	public void drawUnit(Graphics g, int width, int height) {
		int nx = x * cellSizeW;
		int ny = y * cellSizeH;
		cellSizeW = width;
		cellSizeH = height;
		g.drawImage(unitPic, nx + 1, ny + 1, cellSizeW - 1, cellSizeH - 1, null);
		g.setColor(Color.lightGray);
        g.drawRect(nx, ny, cellSizeW, cellSizeH);
    }
}
