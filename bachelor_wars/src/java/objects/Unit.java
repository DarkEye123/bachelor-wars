package objects;
import jason.environment.grid.Location;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import mapping.Dictionary;
import mapping.UnitPicMap;
import models.GameModel;


public class Unit extends GameObject implements Clickable {

	public static final int DEFAULT_LIFE = 50;
	public static final int DEFAULT_MOVE_RANGE = 4;
	public static final Dimension DEFAULT_UNIT_SIZE = new Dimension(1,1); //size on grid (x,y)
	
	private static int _id_; //identifier of a unit
	
	protected BufferedImage unitPic = null;
	protected int id;
	protected int hp = DEFAULT_LIFE;
	protected int moveRange = DEFAULT_MOVE_RANGE;
	protected int cost;
	protected int atk;
		
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
		init();
	}

	private void init() {
		//set picture for unit
		for (UnitPicMap map: GameModel.AVAILABLE_UNITS) {
			if (type == map.getType()) {
				unitPic = map.getPicture();
				break;
			}
		}
		//set name for unit
		for (Dictionary<Integer, String> map:GameModel.UNIT_NAMES) {
			if (map.getIndex() == type) {
				name = map.getValue()+" "+id;
			}
		}
		
		for (Dictionary<Integer, Integer> map:GameModel.UNIT_COST) {
			if (map.getIndex() == type) {
				cost = map.getValue();
			}
		}
		
		for (Dictionary<Integer, Integer> map:GameModel.UNIT_ATK) {
			if (map.getIndex() == type) {
				atk = map.getValue();
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

	public int getMoveRange() {
		return moveRange;
	}

	public void setMoveRange(int numMoves) {
		this.moveRange = numMoves;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}
}
