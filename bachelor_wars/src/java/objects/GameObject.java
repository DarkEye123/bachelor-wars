package objects;
import jason.environment.grid.Location;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;

import mapping.Node;

/**
 * Represents object on game map
 * 
 * @author DarkEye
 *
 */
public abstract class GameObject implements Clickable{
	protected int x, y; //coordinates in grid
	protected int width, height;// object size in cells
	protected int cellSizeW, cellSizeH; //real cell size - width and height in pixels
	protected int type; //sets type of object - for example which AI type is used for base etc
	protected int owner; //owner of GameObject .. it could be Player agent1 .. agentx - it doesn't indicate AI level, that is up to "type"
	protected Location location;
	protected String name; //Objects name - TODO generate some file with names and read it
	
	public GameObject() {
		
	}

	/**
	 * Constructor of GameObject
	 * @param location - coordinates in grid where object starts
	 * @param objectSize - width and height of object in cells in grid
	 * @param cellSize - real cell size - width and height in pixels
	 */
	public GameObject(Location location, Dimension objectSize, Dimension cellSize) {
		this.x = location.x;
		this.y = location.y;
		this.location = location;
		this.width = objectSize.width;
		this.height = objectSize.height;
		cellSizeW = cellSize.width;
		cellSizeH = cellSize.height;
	}
	
	/**
	 * Can tell, if player clicked on object or not.
	 * @param x - x coordinates of point, where mouse clicked
	 * @param y - y coordinates of point, where mouse clicked
	 * @return - true if given coordinates are within area that belongs to the object
	 * @see Clickable
	 */
	public boolean wasSelected(int x, int y) {
		int minX = this.x * cellSizeW; //get cell most left side
		int maxX = minX + (width * cellSizeW); //get cell most right side
		int minY = this.y * cellSizeH; //get cell most upper side
		int maxY = minY + (height * cellSizeH); //get cell most lower side
//		System.out.println("minX: " + minX + " maxX: " + maxX + " minY: " + minY + " maxY: " + maxY + " x: " + x + " y: " + y + " this.x: " + this.x + " this.y: " + this.y);
		if ( (x >= minX && x <= maxX) && (y >= minY && y <= maxY))
			return true;
		else
			return false;
	}
	
	public abstract void draw(Graphics g, int x, int y);
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setCellSizeW(int cellSizeW) {
		this.cellSizeW = cellSizeW;
	}

	@Override
	public String toString() {
		return name;
	}

	public void setCellSizeH(int cellSizeH) {
		this.cellSizeH = cellSizeH;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public Node getNode() {
		return Node.getNode(x, y);
	}
	
	public void setLocation(Location loc) {
		this.location = loc;
		Node.getNode(loc.x, loc.y).add(this); //add this unit to the new node, but before its grid coordinates are changed (old coordinates are for knowing in which node to delete unit)
		this.x = loc.x;
		this.y = loc.y;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public abstract int getId();
	
}
