package objects.units;
import jason.environment.grid.Location;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import models.GameModel;
import objects.Clickable;
import objects.GameObject;


public abstract class Unit extends GameObject implements Clickable {

	public static final Dimension DEFAULT_UNIT_SIZE = new Dimension(1,1); //size on grid (x,y)
	
	private static int _id_; //identifier of a unit
	
	protected static BufferedImage image = null;
	protected int id;
	protected int hp;
	protected static int maxHp;
	protected static int mov;
	protected static int cost;
	protected static int atk;
	
	public Unit() {
		
	}
		
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
	 * @param unitSize - width and height of unit in cells in grid
	 * @param cellSize - real cell size - width and height in pixels
	 * @param type - set a type of unit -> this type sets GameModel class
	 * @see GameModel
	 */
	public Unit(Location location, Dimension unitSize, Dimension cellSize, int type) {
		this(location,unitSize,cellSize);
		this.type = type;
		_id_++;
		id = _id_;
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
		g.drawImage(image, nx + 1, ny + 1, cellSizeW - 1, cellSizeH - 1, null);
		g.setColor(Color.lightGray);
        g.drawRect(nx, ny, cellSizeW, cellSizeH);
    }

	public int getMov() {
		return mov;
	}

	public int getCost() {
		return cost;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}
	
	public void addDamage(int damage) {
		hp = hp - damage >= 0 ? hp - damage : 0;
	}
	
	public void addHeal(int heal) {
		hp = hp + heal >= maxHp ? hp + heal : maxHp;
	}
	
	public boolean isDead() {
		return hp == 0;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public int getAtk() {
		return atk;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		Unit.image = image;
	}
	
	public static Unit getPrototype() {
		return null;
	}
}
