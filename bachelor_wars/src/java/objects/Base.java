package objects;
import java.awt.Color;
import java.awt.Dimension;

import jason.environment.grid.Location;

/**
 * Represents player base on game map
 * 
 * @author DarkEye
 *
 */
public class Base extends GameObject implements Clickable{
	
	public final static int DEFAULT_SLOT_SIZE = 6;
	public final static Dimension DEFAULT_BASE_SIZE = new Dimension(2,2); //size on grid (x,y)
	
	protected int freeSlots = DEFAULT_SLOT_SIZE; //number of free slots to create new units
	protected Color color;
	int videoRecords = 0; //this represents how many resources on the map player owns. 
	
	
	/**
	 * Constructor of Base
	 * @param location - coordinates in grid where base starts
	 * @param baseSize - width and height of base in cells in grid
	 * @param cellSize - real cell size - width and height in pixels
	 */
	public Base(Location location, Dimension baseSize, Dimension cellSize) {
		super(location, baseSize, cellSize);
	}
	
	public int getFreeSlots() {
		return freeSlots;
	}

	public void setFreeSlots(int freeSlots) {
		this.freeSlots = freeSlots;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color baseColor) {
		this.color = baseColor;
	}
}
