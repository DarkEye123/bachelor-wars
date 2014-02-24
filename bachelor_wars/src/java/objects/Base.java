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
	public final static int DEFAULT_BASE_SIZE = 2;
	
	protected int freeSlots = DEFAULT_SLOT_SIZE; //number of free slots to create new units
	protected int baseType; //0 - player, 1 simple AI, 2 medium AI, 3 advanced AI
	protected Color baseColor;
	
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

	public int getBaseType() {
		return baseType;
	}

	public void setBaseType(int baseType) {
		this.baseType = baseType;
	}

	public Color getBaseColor() {
		return baseColor;
	}

	public void setBaseColor(Color baseColor) {
		this.baseColor = baseColor;
	}
}
