package ui;
import jason.environment.grid.Location;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import mapping.GameSettings;
import mapping.Node;
import objects.Base;
import objects.Knowledge;
import objects.units.FirstYear;
import objects.units.Unit;
import env.GameEnv;


public class GameMap extends JPanel {

	private static final long serialVersionUID = 6282676586123792528L;
	public static final int BASE_DEFAULT_SIZE = 2;
	
	GameView view;
	private PopupMenu menu;
	private Unit cunit = null;

	//protected GridCanvas drawArea;
	
	protected int cellSizeW = 0;
	protected int cellSizeH = 0;
	
	private LinkedList<Unit> unitList = new LinkedList<Unit>();
	private LinkedList<Base> baseList = new LinkedList<Base>();
	private LinkedList<Knowledge> knowledgeList = new LinkedList<Knowledge>();
	private LinkedList<Location> movementLocations = new LinkedList<Location>();
	
	MapMouseInputAdapter mouseListener;
	
	
	public static final float WIDTH_MULTIPLIER = 1f ;
	public static final float HEIGHT_MULTIPLIER = 0.7f ;
	
	public static Font defaultFont = new Font("Arial", Font.BOLD, 10);
	protected GameSettings settings;
	
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
			x = rand.nextInt( (settings.getMapColumns() - 2 * GameSettings.DEFAULT_KNOWLEDGE_PADDING) + GameSettings.DEFAULT_KNOWLEDGE_PADDING - 1);
			y = rand.nextInt( (settings.getMapRows() - 2 * GameSettings.DEFAULT_KNOWLEDGE_PADDING) + GameSettings.DEFAULT_KNOWLEDGE_PADDING - 1);
			Knowledge knowledge = new Knowledge(new Location(x,y), new Dimension(cellSizeW,cellSizeH));
			knowledgeList.add( knowledge );
			Node.getNode(x, y).add(knowledge);
		}
	}

	private void initBases() {
		Dimension gridSize = new Dimension(cellSizeW,cellSizeH);
		Base base = null;
		int[] indexes = {0, 0, 0}; //for setting names for bases (basic name is for example SIMPLE_AI and base name is SIMPLE_AI 1
		for (int x=0; x < settings.getNumPlayers(); ++x) {
			base = new Base(settings.getBaseLocations().get(x), Base.DEFAULT_BASE_SIZE, gridSize);
			base.setColor(settings.getColors().get(x));
			base.setType(settings.getPlayers().get(x));
			base.setOwner(x);
			
			
			//set names for players (AI, real player ..) 
			if (base.getType() == GameSettings.PLAYER) {
				base.setName(settings.getPlayerName());
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
			base.setMapWidth(settings.getMapColumns()); //set number of cells in a row
			base.setMapHeight(settings.getMapRows()); //set number of cells in a column
			Node.getNode(base.getX(), base.getY()).add(base);
		}
		repaint();
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
    
    //TODO change it to the circles or something
    public void drawMovementGrid(LinkedList<Location> locations) {
    	Graphics g = this.getGraphics();
        for (Location location:locations) {
        	g.setColor(Color.GREEN);
        	int centerX = Math.round(location.x * cellSizeW);
        	int centerY = Math.round(location.y * cellSizeH);
        	g.fillOval(centerX, centerY, cellSizeW, cellSizeH);
        }
        ///drawString(g, x, y, defaultFont, mov+"");
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
     * @param gridLocation - Coordinates on the grid (0,0) means upper left corner
     * @param owner - owner of this unit (player, agent1, etc ...)
     * @param type - type of unit
     * @return Unit if creation is successful, null otherwise.
     * @see objects.units.Unit Unit
     */
    public Unit createUnit(Location gridLocation, int owner, int type) {
    	assert !Node.getNode(gridLocation.x, gridLocation.y).containUnit() : "Can't create unit for owner: " + owner;
    	if (! Node.getNode(gridLocation.x, gridLocation.y).containUnit()) {
	    	Dimension cellSize = new Dimension(cellSizeW, cellSizeH);
	    	Unit unit = new FirstYear(gridLocation, Unit.DEFAULT_UNIT_SIZE, cellSize);
	    	unit.setOwner(owner);
	    	Base.getOwnerBase(owner,baseList).addUnit(unit); //list of units of actual player
	    	unitList.add(unit); //list of all units
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
    	repaint();
    }
    
    private void findPossibleLocations(Location loc, int act, int max, LinkedList<Location> locations, LinkedList<Location> forbidden) {
    	if (act <= max && !forbidden.contains(loc)) {
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
        for (Unit unit:unitList) {
        	unit.draw(g, cellSizeW, cellSizeH);
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


	public LinkedList<Unit> getUnitList() {
		return unitList;
	}

	public void setUnitList(LinkedList<Unit> unitList) {
		this.unitList = unitList;
	}

	public LinkedList<Base> getBaseList() {
		return baseList;
	}

	public void setBaseList(LinkedList<Base> baseList) {
		this.baseList = baseList;
	}
	
	public LinkedList<Location> getMovementLocations() {
		return movementLocations;
	}

	public void setMovementLocations(LinkedList<Location> movementLocations) {
		this.movementLocations = movementLocations;
	}

	class MapMouseInputAdapter extends MouseInputAdapter {
		Base cbase = null;
		
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) { 
				if (movementLocations.isEmpty()) {
					for (Base base:baseList) {
//						System.out.println(base.getName() + " " + base.wasSelected( e.getX(),  e.getY()));
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
							break;
						}
					}
					if (cunit != null)
						drawPossibleMovement(cunit);
				} else {
					Location loc = new Location(e.getX() / cellSizeW, e.getY() / cellSizeH); //get a cell where mouse clicked
					if (movementLocations.contains(loc)) {
						cunit.setLocation(loc);
						movementLocations.clear();
						view.env.addPercept(GameEnv.CA);
					} else
						movementLocations.clear();
					cunit = null;
					e.getComponent().getParent().repaint(); //view
//					e.getComponent().repaint();
				}
			}
			if (e.getButton() == MouseEvent.BUTTON3) {
				menu.show(e.getComponent(), e.getX(),  e.getY());
			}
			
		}
	}
}
