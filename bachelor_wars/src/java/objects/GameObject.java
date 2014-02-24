package objects;
import jason.environment.grid.Location;

import java.awt.Dimension;

/**
 * Represents object on game map
 * 
 * @author DarkEye
 *
 */
public class GameObject implements Clickable{
	protected int x, y; //coordinates
	protected int width, height;// object size in cells
	protected int cellSizeW, cellSizeH; //real cell size - width and height in pixels
	
	/**
	 * Constructor of GameObject
	 * @param location - coordinates in grid where object starts
	 * @param baseSize - width and height of object in cells in grid
	 * @param cellSize - real cell size - width and height in pixels
	 */
	public GameObject(Location location, Dimension baseSize, Dimension cellSize) {
		this.x = location.x;
		this.y = location.y;
		this.width = baseSize.width;
		this.height = baseSize.height;
		cellSizeW = cellSize.width;
		cellSizeH = cellSize.height;
	}
	
	public boolean wasSelected(int x, int y) {
		int minX = this.x * cellSizeW; //get cell most left side
		int maxX = minX + (width * cellSizeW); //get cell most right side
		int minY = this.y * cellSizeH; //get cell most upper side
		int maxY = minY + (height * cellSizeH); //get cell most lower side
		if ( (x >= minX && x <= maxX) && (y >= minY && y <= maxY))
			return true;
		else
			return false;
	}
	
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

	public void setCellSizeH(int cellSizeH) {
		this.cellSizeH = cellSizeH;
	}
}
