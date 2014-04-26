package ui;
import jason.asSyntax.Literal;
import jason.environment.grid.Location;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import mapping.GameSettings;
import mapping.Node;
import objects.Base;
import objects.GameObject;
import objects.Knowledge;
import objects.units.FirstYear;
import objects.units.Unit;
import env.GameEnv;


public class GameMap extends JPanel {

	private static final long serialVersionUID = 6282676586123792528L;
	public static final int BASE_DEFAULT_SIZE = 2;
	public static final float WIDTH_MULTIPLIER = 1f ;
	public static final float HEIGHT_MULTIPLIER = 0.7f ;
	
	GameView view;
	private PopupMenu menu;
	private Unit cunit = null;
	private Base playerBase;

	//protected GridCanvas drawArea;
	
	protected int cellSizeW = 0;
	protected int cellSizeH = 0;
	
	private static LinkedList<Unit> unitList = new LinkedList<Unit>();
	private static LinkedList<Base> baseList = new LinkedList<Base>();
	private static LinkedList<Base> activeBasesInRound = new LinkedList<Base>();
	private static LinkedList<Knowledge> knowledgeList = new LinkedList<Knowledge>();
	private LinkedList<Location> movementLocations = new LinkedList<Location>();
	private LinkedList<Location> atkLocations = new LinkedList<Location>();
	private static final Object countLock = new Object();
	
	MapMouseInputAdapter mouseListener;
	
	
	
	public static Font defaultFont = new Font("Arial", Font.BOLD, 10);
	protected GameSettings settings;
	public static int ROUND = 1;
	
	public GameMap(GameView gameView) {
		view = gameView;
		settings = view.settings;
	}
	
	public void init() {
		Node.generateGrid(settings.getMapColumns(), settings.getMapRows());
		mouseListener = new MapMouseInputAdapter();
		initBases();
		generateKnowledgeResources(settings.getNumKnowledgeResources());
		menu = new PopupMenu();
		menu.add(new MenuItem("Test"));
		this.add(menu);
		this.addMouseListener(mouseListener);
	}
	
	private void generateKnowledgeResources(int numKnowledgeResources) {
		Random rand = new Random();
		int x;
		int y;
		
		for (int i = 0; i < numKnowledgeResources; ++i) {
			x = rand.nextInt( (settings.getMapColumns() - 2 * GameSettings.DEFAULT_KNOWLEDGE_PADDING) ) + GameSettings.DEFAULT_KNOWLEDGE_PADDING - 1;
			y = rand.nextInt( (settings.getMapRows() - 2 * GameSettings.DEFAULT_KNOWLEDGE_PADDING) ) + GameSettings.DEFAULT_KNOWLEDGE_PADDING - 1;
			Knowledge knowledge = new Knowledge(new Location(x,y), new Dimension(cellSizeW,cellSizeH));
			knowledgeList.add( knowledge );
			Node.getNode(x, y).add(knowledge);
		}
	}

	private void initBases() {
		Dimension gridSize = new Dimension(cellSizeW,cellSizeH);
		Base base = null;
		int[] indexes = {0, 0, 0}; //for setting names for bases (basic name is for example SIMPLE_AI and base name is SIMPLE_AI 1
		boolean incrementOwner = settings.getPlayers().get(0) != GameSettings.PLAYER; //real player is everytime first
		for (int x=0; x < settings.getNumPlayers(); ++x) {
			base = new Base(settings.getBaseLocations().get(x), Base.DEFAULT_BASE_SIZE, gridSize);
			base.setColor(settings.getColors().get(x));
			base.setType(settings.getPlayers().get(x));
			if (incrementOwner)
				base.setOwner(x + 1);
			else
				base.setOwner(x);
			
			//set names for players (AI, real player ..) 
			if (base.getType() == GameSettings.PLAYER) {
				base.setName(settings.getPlayerName());
				playerBase = base;
			} else if (base.getType() == GameSettings.SIMPLE_AI) {
				indexes[GameSettings.SIMPLE_AI - 1]++;
				base.setName(GameSettings.AI_NAMES[GameSettings.SIMPLE_AI - 1] + " " + indexes[GameSettings.SIMPLE_AI - 1]);
				base.setAgent(GameSettings.AI_AGENTS[GameSettings.SIMPLE_AI - 1]);
			} else if (base.getType() == GameSettings.MEDIUM_AI) {
				indexes[GameSettings.MEDIUM_AI - 1]++;
				base.setName(GameSettings.AI_NAMES[GameSettings.MEDIUM_AI - 1] + " " + indexes[GameSettings.MEDIUM_AI - 1]);
				base.setAgent(GameSettings.AI_AGENTS[GameSettings.MEDIUM_AI - 1]);
			} else {
				indexes[GameSettings.ADVANCED_AI - 1]++;
				base.setName(GameSettings.AI_NAMES[GameSettings.ADVANCED_AI - 1] + " " + indexes[GameSettings.ADVANCED_AI - 1]);
				base.setAgent(GameSettings.AI_AGENTS[GameSettings.ADVANCED_AI - 1]);
			}
			
			baseList.add(base);
			if (base.getType() != GameSettings.PLAYER) {
				view.env.addAgent(base.getName(), base.getAgent()); //add agent to the game
				base.setAgent(base.getName());
				view.env.addPercept(base.getAgent(), Literal.parseLiteral("agentID("+base.getOwner()+")"));
			}
			base.setMapWidth(settings.getMapColumns()); //set number of cells in a row
			base.setMapHeight(settings.getMapRows()); //set number of cells in a column
			Node.getNode(base.getX(), base.getY()).add(base);
		}
		repaint();
		reinitActiveBases();
		sortIntoTeams();
		if (incrementOwner) //there is now real player, let fight begin!! 
			GameMap.allowActions(GameMap.getActiveBases(), view.env);
	}
	
	private void sortIntoTeams() {
		Set<String> keys = settings.getTeams().keySet();
		HashMap<String, ArrayList<Integer>> map = settings.getTeams();
		for (Base base:baseList) {
			base.setEnemies( (LinkedList<Base>) getBaseList().clone() ); //all players are enemies by default
			base.getEnemies().remove(base);
			for (String s:keys) {
				if (map.get(s).contains(base.getOwner())) {
					if (!s.equals("--")) { 
						for (Integer i:map.get(s)) {
							if (i != base.getOwner()) {
								Base pomBase = searchBase(i);
								base.getAllies().add(pomBase);
								base.getEnemies().remove(pomBase);
							}
						}
					}
				}
			}
			System.out.println("Base: " + base + " enemies: " + base.getEnemies() + " allies: " + base.getAllies());
		}
	}

	public static void reinitActiveBases() {
		synchronized (countLock) {
			activeBasesInRound = (LinkedList<Base>) getBaseList().clone();
			if (activeBasesInRound.getFirst().getOwner() == GameSettings.PLAYER)
				activeBasesInRound.removeFirst(); //this is player, there is need only for AI bases
		}
	}
	
	public static void removeActiveBase() {
		synchronized (countLock) {
			activeBasesInRound.removeFirst();
		}
	}
	
	public static LinkedList<Base> getActiveBases() {
		synchronized (countLock) {
			return activeBasesInRound;
		}
	}

	public int getCellSizeW() {
		return this.cellSizeW;
	}
	
	public int getCellSizeH() {
		return this.cellSizeH;
	}

	public void setCellSizeW(int cellSizeW) {
		this.cellSizeW = cellSizeW;
	}

	public void setCellSizeH(int cellSizeH) {
		this.cellSizeH = cellSizeH;
	}
	
	/** updates only one position of the grid */
    public void update(int x, int y) {
        drawEmpty(this.getGraphics(), x, y);
    }

    public void drawObstacle(Graphics g, int x, int y) {
        g.setColor(Color.darkGray);
        g.fillRect(x * cellSizeW + 1, y * cellSizeH+1, cellSizeW-1, cellSizeH-1);
        g.setColor(Color.black);
        g.drawRect(x * cellSizeW + 2, y * cellSizeH+2, cellSizeW-4, cellSizeH-4);
    }

    public void drawResource(Graphics g, int x, int y, Color c, int id) {
        g.setColor(c);
        g.fillOval(x * cellSizeW + 2, y * cellSizeH + 2, cellSizeW - 4, cellSizeH - 4);
        if (id >= 0) {
            g.setColor(Color.black);
            drawString(g, x, y, defaultFont, String.valueOf(id+1));
        }
    }
    
    public static void drawString(Graphics g, int x, int y, Font f, String s) {
        g.setFont(f);
        g.drawString(s, x, y);
    }

    public void drawEmpty(Graphics g, int x, int y) {
        g.setColor(Color.white);
        g.fillRect(x * cellSizeW + 1, y * cellSizeH+1, cellSizeW-1, cellSizeH-1);
        g.setColor(Color.lightGray);
        g.drawRect(x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH);
    }
    
    public void drawBasicAtkRange(Location loc, int max) {
    	findPossibleLocations(loc, 0, max, atkLocations, null);
    	atkLocations.remove(loc);
    	
    	Graphics g = this.getGraphics();
        for (Location location:atkLocations) {
        	g.setColor(Color.red);
        	int x = Math.round(location.x * cellSizeW);
        	int y = Math.round(location.y * cellSizeH);
        	g.drawRect(x + 1, y + 1, cellSizeW - 1, cellSizeH - 1);
        }
//        repaint();
    }
    
    public void drawMovementGrid(LinkedList<Location> locations) {
    	Graphics g = this.getGraphics();
        for (Location location:locations) {
        	g.setColor(Color.GREEN);
        	int centerX = Math.round(location.x * cellSizeW);
        	int centerY = Math.round(location.y * cellSizeH);
        	g.fillOval(centerX, centerY, cellSizeW, cellSizeH);
        }
    }

    
    public int getNormalizedX(int x) {
    	x = x / cellSizeW; //it's int so here we get rid off that float part
    	x = x * cellSizeW;
    	return x;
    }
    
    public int getNormalizedy(int y) {
    	y = y / cellSizeH; //it's int so here we get rid off that float part
    	y = y * cellSizeH;
    	return y;
    }
    
    /**
     * creates a unit with given type and add it into grid. It checks if given node doesn't contain a unit already. If so, it will return null, or created unit otherwise
     * @param node - Coordinates on the grid (0,0) means upper left corner
     * @param owner - owner of this unit (player, agent1, etc ...)
     * @param type - type of unit
     * @return Unit if creation is successful, null otherwise.
     * @see objects.units.Unit Unit
     */
    public Unit createUnit(Node node, int owner, int type) {
    	assert !node.containUnit() : "Can't create unit for owner: " + owner;
    	if (! node.containUnit()) {
	    	Dimension cellSize = new Dimension(cellSizeW, cellSizeH);
	    	Unit unit = null; //TODO here should be some parametrized function that can make instances from types by generic way
	    	if (type == FirstYear.TYPE)
	    		unit = new FirstYear(node.getLocation(), Unit.DEFAULT_UNIT_SIZE, cellSize);
	    	unit.setOwner(owner);
	    	Base base = Base.getOwnerBase(owner,baseList); //seek for base
	    	base.addUnit(unit); //list of units of actual player
	    	unit.base = base; //set a base for unit (it's like owner from GameObject but due to some dependencies is better set a base on it's own too)
	    	synchronized (countLock) {
	    		unitList.add(unit); //list of all units
	    	}
	    	Node.getNode(unit.getX(),unit.getY()).add(unit);
	    	repaint();
	    	return unit;
    	}
//    	System.out.println("RETURN NULL");
    	return null;
    }
    
    /**
     * This draws a grid - supposed for debug only
     * @param g
     */
    @SuppressWarnings("unused")
	private void drawGrid(Graphics g) {
        int mwidth = settings.getMapColumns();
        int mheight = settings.getMapRows();
    	g.setColor(Color.lightGray);
        for (int l = 1; l <= mheight; l++) { //paint rows
            g.drawLine(0, l * cellSizeH, mwidth * cellSizeW, l * cellSizeH);
        } //paint columns
        for (int c = 1; c <= mwidth; c++) {
            g.drawLine(c * cellSizeW, 0, c * cellSizeW, mheight * cellSizeH);
        }
    }
    
    private void drawPossibleMovement(Location loc, int max) {
    	findPossibleLocations(loc, 1, max, movementLocations, getForbiddenLocations());
    	movementLocations.removeAll(getForbiddenLocations());	
    	movementLocations.remove(loc);
    	drawMovementGrid(movementLocations);
//    	repaint();
    }
    
    private void findPossibleLocations(Location loc, int act, int max, LinkedList<Location> locations, LinkedList<Location> forbidden) {
    	boolean contain = false;
    	if (forbidden != null && forbidden.contains(loc))
    		contain = true;
    	if (act <= max && !contain) {
    		if (! locations.contains(loc))
    			locations.add(loc);
    		if (loc.x + 1 < settings.getMapColumns())
    			findPossibleLocations(new Location(loc.x + 1, loc.y), act+1, max, locations, forbidden);
    		if (loc.x - 1 >= 0)
    			findPossibleLocations(new Location(loc.x - 1, loc.y), act+1, max, locations, forbidden);
    		if (loc.y + 1 < settings.getMapRows())
    			findPossibleLocations(new Location(loc.x, loc.y + 1), act+1, max, locations, forbidden);
    		if (loc.y - 1 >= 0)
    			findPossibleLocations(new Location(loc.x, loc.y - 1), act+1, max, locations, forbidden);
    	}
    }
    
    private LinkedList<Location> getForbiddenLocations() {
    	LinkedList<Location> sumList = new LinkedList<Location>();
    	for (Unit unit:unitList) {
    		if (unit != cunit) {
    			sumList.add(unit.getLocation());
    		}
    	}
		return sumList;
    }
    
    public void drawPossibleMovement(Unit unit) {
    	cunit = unit;
    	drawPossibleMovement(unit.getLocation(), unit.getMov());
    }
    
    public void paint(Graphics g) {
    	int mwidth = settings.getMapColumns();
    	int mheight = settings.getMapRows();
        cellSizeW = this.getWidth() / mwidth;
        cellSizeH = this.getHeight() / mheight;
        for (Base base:baseList) {
        	base.draw(g, cellSizeW, cellSizeH);
        }
        for (Knowledge knowledge:knowledgeList) {
        	knowledge.draw(g, cellSizeW, cellSizeH);
        }
        synchronized (countLock) {
	        for (Unit unit:unitList) {
	        	unit.draw(g, cellSizeW, cellSizeH);
	        };
        }
    }
    
    public void repaint() {
        if (view != null) {
	    	cellSizeW = this.getWidth() / settings.getMapColumns();
	        cellSizeH = this.getHeight() / settings.getMapRows();
	        for (Base base:baseList) {
	        	base.draw(this.getGraphics(), cellSizeW, cellSizeH);
	        }
	        for (Knowledge knowledge:knowledgeList) {
	        	knowledge.draw(this.getGraphics(), cellSizeW, cellSizeH);
	        }
	        for (Unit unit:unitList) {
	        	unit.draw(this.getGraphics(), cellSizeW, cellSizeH);
	        }
        }
    }


	public static LinkedList<Unit> getUnitList() {
		synchronized (countLock) {
			return unitList;
		}
	}
	
	public static void removeUnit(Unit unit) {
		synchronized (countLock) {
			unitList.remove(unit);
		}
	}

	public static LinkedList<Base> getBaseList() {
		synchronized (countLock) {
			return baseList;
		}
	}

	public static LinkedList<Knowledge> getKnowledgeList() {
		synchronized (countLock) {
			return knowledgeList;
		}
	}

	public LinkedList<Location> getMovementLocations() {
		synchronized (countLock) {
			return movementLocations;
		}
	}
	
	public static void allowActions(LinkedList<Base> bases, GameEnv env) {
		for (Base base:bases) {
			if (base.getOwner() != GameSettings.PLAYER) {
				env.addPercept(base.getAgent(), GameEnv.CA); //add percept to every agent in a game in a proper order
				return;
			}
		}
	}

	class MapMouseInputAdapter extends MouseInputAdapter {
		Base cbase = null;
		Node test1 = null;
		Node test2 = null;
		int testCounter = 0;
		
		private void canDebugPath(boolean debug, MouseEvent e) {
			if (debug) {
				if (testCounter++ % 2 == 0) {
					test1 = Node.getNode(e.getX() / cellSizeW, e.getY() / cellSizeH);
				}
				else {
					test2 = Node.getNode(e.getX() / cellSizeW, e.getY() / cellSizeH);
					debugPath(test1, test2);
				}
			}
		}
		
		public void mouseClicked(MouseEvent e) {
			if (isEnabled()) {
				if (e.getButton() == MouseEvent.BUTTON1) { 
					canDebugPath(false, e);
					
					if (movementLocations.isEmpty()) {
						for (Base base:baseList) {
							if ( base.wasSelected( e.getX(),  e.getY()) ) {
								cbase = base;
								view.controlPanel.infoPanel.showBaseContext(base);
								break;
							}
						}
						for (Unit unit:unitList) {
							if ( unit.wasSelected( e.getX(),  e.getY()) ) {
								cunit = unit;
								view.controlPanel.infoPanel.showUnitContext(cunit);
								if (cunit.getOwner() != GameSettings.PLAYER)
									cunit = null;
								break;
							}
						}
						if (cunit != null && playerBase.getUsableUnits().contains(cunit)) {
							if (cunit.canMove())
								drawPossibleMovement(cunit);
							drawBasicAtkRange(cunit.getLocation(), cunit.getBasicAtkRange());
						} else {
							cunit = null;
						}
					} else {
						Location loc = new Location(e.getX() / cellSizeW, e.getY() / cellSizeH); //get a cell where mouse clicked
						if (movementLocations.contains(loc)) {
							cunit.setLocation(loc);
							movementLocations.clear();
							atkLocations.clear();
							cunit.setCanMove(false);
						} else {
							movementLocations.clear();
							if (atkLocations.contains(loc)) {
								Unit u = Node.getNode(loc.x, loc.y).getUnit();
								if (u.getOwner() != cunit.getOwner()) {
									Node.getNode(loc.x, loc.y).getUnit().addDamage(cunit.getAtk());
									playerBase.getUsableUnits().remove(cunit);
								}
							}
						}
						atkLocations.clear();
						cunit = null;
						e.getComponent().getParent().repaint(); //view
					}
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					menu.show(e.getComponent(), e.getX(),  e.getY());
				}
			}
		}
	}

	public void clearMovement() {
		cunit = null;
		movementLocations.clear();
		atkLocations.clear();
	}
	
	private static GameObject searchObject(int id, LinkedList<? extends GameObject > objects) {
		for (GameObject u:objects) {
			if (u.getId() == id)
				return u;
		}
		return null;
	}
	
	public static Unit searchUnit(int id) {
		return (Unit) searchObject(id, getUnitList());
	}
	
	public static Knowledge searchKnowledge(int id) {
		return (Knowledge) searchObject(id, getKnowledgeList());
	}
	
	public static Base searchBase(int id) {
		return (Base) searchObject(id, getBaseList());
	}
	
	@SuppressWarnings("unused")
	private void debugPath(Node from, Node to) {
		Graphics g = this.getGraphics();
		LinkedList<Node> path = Node.searchPath(from, to, false);
		int x = 0;
		for (Node act:path) {
			if (x == 0)
				g.setColor(Color.red);
			else
				g.setColor(Color.blue);
			++x;
			g.fillRect(act.getX() * cellSizeW, act.getY() * cellSizeH, cellSizeW, cellSizeH);
			g.setColor(Color.black);
			g.drawRect(act.getX() * cellSizeW, act.getY() * cellSizeH, cellSizeW, cellSizeH);
			act = act.getPredecessor();
		}
		Node.removePredecessors();
	}

	public void printWinner(String winner) {
		Graphics g = this.getGraphics();
		Font defaultFont = new Font("Arial", Font.BOLD, 32);
		g.setColor(Color.MAGENTA);
		g.setFont(defaultFont);
		String pom = "Winner: " + winner;
		g.drawString(pom, this.getWidth()/2 - ((defaultFont.getSize()*pom.length())/3), this.getHeight()/2);
	}

	public Base getPlayerBase() {
		return playerBase;
	}
}
