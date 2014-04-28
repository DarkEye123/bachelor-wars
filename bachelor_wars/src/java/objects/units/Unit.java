package objects.units;
import jason.environment.grid.Location;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import mapping.GameSettings;
import mapping.Intention;
import mapping.Node;
import objects.Base;
import objects.Clickable;
import objects.GameObject;
import ui.GameMap;
import ui.GameView;

/**
 * Abstract class that represents a unit in general view. It provide fields that all units should share.
 * @author Matej Leško <xlesko04@stud.fit.vutbr.cz>
 */
public abstract class Unit extends GameObject implements Clickable {
	private static final int TIME_TO_WAIT = 50;
	private static final int HEALTH_QUOTE = 50;
	
	/*
	 * --------------------------------------------------------Classes-----------------------------------------------------------------------------------
	 * These value can be combined, f.e. unit can be tank with one healing ability with certain restrictions, like only self heal and only limited amount of hp etc.
	 * TODO add these into graphical representation
	 */
	public static final int TANK = 1;
	public static final int HEALER = 2;
	public static final int DAMAGE_DEALER = 4;
	public static final int SUPPORTER = 8;
	
	/*
	 * --------------------------------------------------------Intentions-------------------------------------------------------------------------------
	 */
	public static final int	KILL 	= 0; //damage with atk
	public static final int	HEAL 	= 1; 
	public static final int	SEIZE 	= 2;
	public static final int	BUFF 	= 3; //use a power with given intention TODO try to find out if necessary
	public static final int	SUPPORT = 4; //Support copy intentions of friendly target unit. Do not forget to remove this intention after that

	public static final Dimension DEFAULT_UNIT_SIZE = new Dimension(1,1); //size on grid (x,y)
	private static final int ARC_W = 12, ARC_H = 12;
	
	private static int _id_; //identifier of a unit
	private static final Object countLock = new Object();

	/*
	 * --------------------------------------------------------Stats------------------------------------------------------------------------------------
	 */
	protected static Image image = null;
	protected int id;
	protected int hp;
	protected int basicAtkRange = 1;
	protected int maxHp;
	protected int mov;
	protected int cost;
	protected int atk;
	protected int sp;
	protected int maxSp;//TODO if will powers will be used, add this to UnitInfoPanel
	protected int uClass; //Healer, Damage, Support, Defense
	public Base base; //it's like owner from GameObject but due to some dependencies is better set a base on it's own too
	private boolean canFillMovement = true;
	private Location oldLocation;
	
	HashMap<GameObject, Intention> intentions; //id of object and id of intention
	
	public Unit() {
		
	}
		
	/**
	 * Constructor of Unit
	 * @param location - coordinates in grid where unit starts
	 * @param unitSize - width and height of unit in cells in grid
	 * @param cellSize - real cell size - width and height in pixels
	 */
	public Unit(Location location, Dimension unitSize, Dimension cellSize) {
		super(location, unitSize, cellSize);
		oldLocation = location;
	}

	/**
	 * Constructor of Unit
	 * @param location - coordinates in grid where unit starts
	 * @param unitSize - width and height of unit in cells in grid
	 * @param cellSize - real cell size - width and height in pixels
	 * @param type - set a type of unit -> this type sets GameSettings class
	 * @see GameSettings
	 */
	public Unit(Location location, Dimension unitSize, Dimension cellSize, int type) {
		this(location,unitSize,cellSize);
		this.type = type;
		_id_++;
		id = _id_;
		intentions = new HashMap<>();
	}

	/**
	 * Draws a unit at position on the grid. 
	 * @param g
	 * @param width - actual width of visible cell (in pixels)
	 * @param height - actual height of visible cell (in pixels)
	 */
	@Override
	public void draw(Graphics g, int width, int height) {
		cellSizeW = width;
		cellSizeH = height;
		int nx = x * cellSizeW;
		int ny = y * cellSizeH;
		g.setColor(base.getColor());
		g.fillRoundRect(nx, ny, cellSizeW, cellSizeH, ARC_W, ARC_H);
		g.drawImage(image, nx + 1, ny + 1, cellSizeW - 1, cellSizeH - 1, null);
		g.setColor(Color.lightGray);
        g.drawRoundRect(nx, ny, cellSizeW, cellSizeH, ARC_W, ARC_H);
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
	
	/**
	 * Adds damage to this unit, if unit is dead (hp ==0) is deleted from map and from its base. Free slot is added too.
	 * @param damage - value that represents damage this unit
	 */
	public void addDamage(int damage) {
		hp = hp - damage >= 0 ? hp - damage : 0;
		if (isDead()) { //unit is dead
			base.getUnitList().remove(this);
			base.getUsableUnits().remove(this);
			base.addFreeSlot();
			for (Unit u: GameMap.getUnitList()) {
				u.getIntentions().remove(this);
			}
			GameMap.removeUnit(this);
			getNode().remove(this);
		}
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

	public Image getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		Unit.image = image;
	}
	
	public static Unit getPrototype() {
		return null;
	}

	public int getSp() {
		return sp;
	}

	public void setSp(int sp) {
		this.sp = sp;
	}

	public int getBasicAtkRange() {
		return basicAtkRange;
	}

	public void setBasicAtkRange(int basicAtkRange) {
		this.basicAtkRange = basicAtkRange;
	}

	public int getId() {
		return id;
	}
	
	public void addIntention(GameObject key, Intention value) {
		synchronized (countLock) {
			intentions.put(key, value);
		}
	}
	
	public HashMap<GameObject, Intention> getIntentions() {
		synchronized (countLock) {
			return intentions;
		}
	}
	
	
	/**
	 * @return true if unit has any intention
	 */
	public boolean hasIntention() {
		if (!getIntentions().isEmpty())
			return true;
		else
			return false;
	}
	
	/**
	 * 
	 * @param key - GameObject which is target of intention
	 * @return true if unit has some intetion with given GameObject
	 */
	public boolean hasIntention(GameObject key) {
		synchronized (countLock) {
//			System.out.println("Looking for: " + key + " in: " + intentions );
			if (intentions.get(key) != null)
				return true;
			else
				return false;
		}
	}
	
	public void waitForDraw() {
    	synchronized(this) {
			try {
				this.wait(TIME_TO_WAIT);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }

	/**
	 * Finds a path and sets position on the grid to given node or to the nearest postion to that node.
	 * If there is a unit in the node, and unit is able to get there in actual round, position is set to the nearest available position to that unit to be able to attack it.
	 * @param node - node containing a goal object.
	 */
	public void setLocation(Node node, GameView view) {
//		setLocation(new Location(node.getX(), node.getY()));
		LinkedList<Node> path = Node.searchPath(getNode(), node, false);
		
		if (path.isEmpty()) { //there is no route to given object (but there could be path, but is temporarily blocked, so find this path till blocking unit is reached)
			path = Node.searchPath(getNode(), node, true); //here ignore units (in practice find path till blocking unit)
		}
		Graphics g = view.getGameMap().getGraphics();
//		System.out.println("PAAAAAAAAAAAAAAAATH for: " + this.getId() + " " + path.size());
		for (int x = 0; x < path.size() && x < getMov(); ++x) {
			g.setColor(Color.red);
			g.drawRoundRect(node.getX() * cellSizeW, node.getY() * cellSizeH, cellSizeW, cellSizeH, ARC_W, ARC_H); //draw target intention
			waitForDraw();
			Node t = path.get(x);
			setLocation(new Location(t.getX(), t.getY()));
			waitForDraw();
			view.repaint();
			synchronized (GameMap.countLock) {
				view.getGameMap().getAtkLocations().clear();
			}
			view.getGameMap().repaint();
		}
	}

	public int getUnitClass() {
		return uClass;
	}

	public void setUnitClass(int uClass) {
		this.uClass = uClass;
	}
	
	public GameObject searchIntentionTargetById(int id) {
		for (GameObject o:getIntentions().keySet()) {
			if (o.getId() == id)
				return o;
		}
		return null;
	}
	
	public boolean needHeal() {
		if (getHp()/(getMaxHp()/100.0f) < HEALTH_QUOTE)
			return true;
		else
			return false;
	}
	
	//TODO add class of unit (like heal, damage, etc)
	@Override
	//type, id, cost, hp, atk, mov, atkRange, sp, x, y, owner - later class
	public String toString() {
		return "[" + getType() + ", " + getId() + ", " + getCost() + ", " + getHp() + ", " +
				getAtk() + ", " + getMov() + ", " + getBasicAtkRange() + ", " + getSp() + ", " + 
				getX() + ", " + getY() + ", "  + getOwner() + ", "  + getUnitClass() +  "]";
	}

	public boolean canFillMovement() {
		return canFillMovement;
	}
	
	public void setFillMovement(boolean canFillMovement) {
		this.canFillMovement = canFillMovement;
	}

	public void drawUsableSign(Graphics graphics) {
		int nx = x * cellSizeW;
		int ny = y * cellSizeH;
		graphics.setColor(Color.black);
		graphics.fillOval(nx, ny, cellSizeW / 5, cellSizeH / 5);
	}

	public Location getOldLocation() {
		return oldLocation;
	}

	public void setOldLocation(Location oldLocation) {
		this.oldLocation = oldLocation;
	}
}
