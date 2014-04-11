package objects;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;

import objects.units.Unit;
import ui.GameMap;
import jason.environment.grid.Location;

/**
 * Represents player base on game map
 * 
 * @author DarkEye
 *
 */
public class Base extends GameObject implements Clickable{
	
	public final static int DEFAULT_SLOT_SIZE = 6;
	public final static int DEFAULT_KNOWLEDGE = 100;
	public final static Dimension DEFAULT_BASE_SIZE = new Dimension(2,2); //size on grid (x,y)
	private static final int ARC_W = 12, ARC_H = 12;
	
	
	private LinkedList<Unit> unitList = new LinkedList<Unit>();
	private LinkedList<Knowledge> knowledgeList = new LinkedList<Knowledge>();
	
	protected int freeSlots = DEFAULT_SLOT_SIZE; //number of free slots to create new units
	protected int maxSlots = DEFAULT_SLOT_SIZE; //actual maximum possible slots.
	protected Color color;
	protected int knowledge = DEFAULT_KNOWLEDGE; //this represents how many "money" player has. 
	protected int mapWidth, mapHeight; //set number of cells in a row and column
	protected String agent;
	
	
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

	public void addKnowledge(int knowledge) {
		this.knowledge += knowledge;
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
	
	public void addUnit(Unit unit) {
		unitList.add(unit);
		deleteFreeSlot();
	}
	

	/**
	 * Sets a width of gameMap - for computation of size
	 * @param mapWidth - basically it's a number of cells in a single row
	 */
	public void setMapWidth(int mapWidth) {
		this.mapWidth = mapWidth;
	}
	
	/**
	 * Sets a height of gameMap - for computation of size
	 * @param mapHeight - basically it's a number of cells in a single column
	 */
	public void setMapHeight(int mapHeight) {
		this.mapHeight = mapHeight;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public LinkedList<Knowledge> getKnowledgeList() {
		return knowledgeList;
	}

	public void setKnowledgeList(LinkedList<Knowledge> knowledgeList) {
		this.knowledgeList = knowledgeList;
	}
	
	/**
	 * Seek for instance of base with given owner
	 * @param owner - id of owner (PLAYER, Agent1 etc...)
	 * @param baseList - list of bases where to seek
	 * @return {@link Base} Base of given owner or null if this owner doesn't have a base - in this case owner doesn't exist
	 */
	public static Base getOwnerBase(int owner, LinkedList<Base> baseList) {
		for (Base base:baseList) {
			if (base.owner == owner)
				return base;
		}
		return null;
	}
	
	@Override
	public void draw(Graphics g, int cellSizeW, int cellSizeH) {
		this.cellSizeW = cellSizeW;
		this.cellSizeH = cellSizeH;
		g.setColor(getColor());
		int width = mapWidth * cellSizeW; //we need width of grid not width of component
		int height = mapHeight * cellSizeH; // we need height of grid not height of component
		int posX = x * cellSizeW;
		int posY = y * cellSizeH;
		
		if (posX + cellSizeW * 2 > width)
			posX = width - cellSizeW * 2;
		if (posY + cellSizeH * 2 > height)
			posY = height - cellSizeH * 2;
		
		g.fillRoundRect(posX, posY, cellSizeW*2, cellSizeH*2, ARC_W, ARC_H);
		g.setColor(Color.lightGray);
		g.drawRoundRect(posX, posY, cellSizeW*2, cellSizeH*2, ARC_W, ARC_H);
		g.setColor(Color.black);
		GameMap.drawString(g, Math.round(posX+(0.3f*cellSizeW)), Math.round(posY+(0.5f*cellSizeH)), GameMap.defaultFont, getName());
	}

	/**
	 * In this case ID of base is same as owner, basically it's the same thing
	 */
	@Override
	public int getId() {
		return owner;
	}
}
