package objects;
import java.awt.Color;
import java.awt.Dimension;
import java.util.LinkedList;

import objects.units.Unit;
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
	
	
	private LinkedList<Unit> unitList = new LinkedList<Unit>();
	
	protected int freeSlots = DEFAULT_SLOT_SIZE; //number of free slots to create new units
	protected int maxSlots = DEFAULT_SLOT_SIZE; //actual maximum possible slots.
	protected Color color;
	int knowledge = 0; //this represents how many resources on the map player owns. 
	
	
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
	
	/**
	 * Increase usable slot space by 1. Can't be higher than maxSlots
	 */
	public void addFreeSlot() {
		if (freeSlots < maxSlots)
			++freeSlots;
	}
	
	/**
	 * Decrease usable slot space by 1. Can't be lower than 0.
	 */
	public void deleteFreeSlot() {
		if (freeSlots > 0)
			freeSlots--;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color baseColor) {
		this.color = baseColor;
	}

	public int getKnowledge() {
		return knowledge;
	}

	public void setKnowledge(int knowledge) {
		this.knowledge = knowledge;
	}

	public int getMaxSlots() {
		return maxSlots;
	}

	public void setMaxSlots(int maxSlots) {
		this.maxSlots = maxSlots;
	}

	public LinkedList<Unit> getUnitList() {
		return unitList;
	}

	public void setUnitList(LinkedList<Unit> unitList) {
		this.unitList = unitList;
	}
	
	/**
	 * Seek for instance of base with given owner
	 * @param owner - id of owner (PLAYER, Agent1 etc...)
	 * @param baseList - list of bases where to seek
	 * @return
	 */
	public static Base getOwnerBase(int owner, LinkedList<Base> baseList) {
		for (Base base:baseList) {
			if (base.owner == owner)
				return base;
		}
		return null;
	}
}
