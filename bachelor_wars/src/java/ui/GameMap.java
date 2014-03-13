package ui;
import jason.environment.grid.Location;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import mapping.GameSettings;
import models.GameModel;
import objects.Base;
import objects.units.FirstYear;
import objects.units.Unit;
import env.GameEnv;


public class GameMap extends JPanel {

	private static final long serialVersionUID = 6282676586123792528L;
	public static final int BASE_DEFAULT_SIZE = 2;
	
	GameModel model;
	GameView view;
	private PopupMenu menu;
	private Unit cunit = null;

	//protected GridCanvas drawArea;
	
	protected int cellSizeW = 0;
	protected int cellSizeH = 0;
	
	private LinkedList<Unit> unitList = new LinkedList<Unit>();
	private LinkedList<Base> baseList = new LinkedList<Base>();
	private LinkedList<Location> movementLocations = new LinkedList<Location>();
	
	MapMouseInputAdapter mouseListener;
	
	
	public static final float WIDTH_MULTIPLIER = 1f ;
	public static final float HEIGHT_MULTIPLIER = 0.7f ;
	
    private static int limit = (int)Math.pow(2,14);
	protected Font defaultFont = new Font("Arial", Font.BOLD, 10);
	protected GameSettings settings;
	
	public GameMap(GameView gameView) {
		model = gameView.model;
		view = gameView;
		settings = view.settings;
	}
	
	public void init() {
		mouseListener = new MapMouseInputAdapter();
		initBases();
		menu = new PopupMenu();
		menu.add(new MenuItem("Test"));
		this.add(menu);
		this.addMouseListener(mouseListener);
	}
	
	private void initBases() {
		Dimension gridSize = new Dimension(cellSizeW,cellSizeH);
		Base base = null;
		int[] indexes = {0, 0, 0};
		for (int x=0; x < settings.getNumPlayers(); ++x) {
			base = new Base(settings.getBaseLocations().get(x), Base.DEFAULT_BASE_SIZE, gridSize);
			base.setColor(settings.getColors().get(x));
			base.setType(settings.getPlayers().get(x));
			base.setOwner(x);
			
			
			//set names for players (AI, real player ..) 
			if (base.getType() == GameModel.PLAYER) {
				base.setName(settings.getPlayerName());
			} else if (base.getType() == GameModel.SIMPLE_AI) {
				indexes[0]++;
				base.setName(GameSettings.AI_NAMES[0]+indexes[0]);
			} else if (base.getType() == GameModel.MEDIUM_AI) {
				indexes[1]++;
				base.setName(GameSettings.AI_NAMES[1]+indexes[1]);
			} else {
				indexes[2]++;
				base.setName(GameSettings.AI_NAMES[2]+indexes[2]);
			}
			
			baseList.add(base);
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
        draw(this.getGraphics(), x, y);
    }

    public void drawObstacle(Graphics g, int x, int y) {
        g.setColor(Color.darkGray);
        g.fillRect(x * cellSizeW + 1, y * cellSizeH+1, cellSizeW-1, cellSizeH-1);
        g.setColor(Color.black);
        g.drawRect(x * cellSizeW + 2, y * cellSizeH+2, cellSizeW-4, cellSizeH-4);
    }

    public void drawAgent(Graphics g, int x, int y, Color c, int id) {
        g.setColor(c);
        g.fillOval(x * cellSizeW + 2, y * cellSizeH + 2, cellSizeW - 4, cellSizeH - 4);
        if (id >= 0) {
            g.setColor(Color.black);
            drawString(g, x, y, defaultFont, String.valueOf(id+1));
        }
    }
    
    public void drawString(Graphics g, int x, int y, Font f, String s) {
        g.setFont(f);
        FontMetrics metrics = g.getFontMetrics();
        int width = metrics.stringWidth( s );
        int height = metrics.getHeight();
        g.drawString( s, x*cellSizeW+(cellSizeW/2-width/2), y*cellSizeH+(cellSizeH/2+height/2));
    }

    public void drawEmpty(Graphics g, int x, int y) {
        g.setColor(Color.white);
        g.fillRect(x * cellSizeW + 1, y * cellSizeH+1, cellSizeW-1, cellSizeH-1);
        g.setColor(Color.lightGray);
        g.drawRect(x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH);
    }
    
    public void drawMovementGrid(LinkedList<Location> locations) {
    	Graphics g = this.getGraphics();
        for (Location location:locations) {
        	g.setColor(Color.GREEN);
	        g.fillRect(location.x * cellSizeW + 1, location.y * cellSizeH+1, cellSizeW-1, cellSizeH-1);
	        g.setColor(Color.lightGray);
	        g.drawRect(location.x * cellSizeW, location.y * cellSizeH, cellSizeW, cellSizeH);
        }
        ///drawString(g, x, y, defaultFont, mov+"");
    }

    public void drawBase(Graphics g, int x, int y, Color c, String baseName) {
        g.setColor(c);
        int width = model.getWidth()*cellSizeW; //we need width of grid not width of component
        int height = model.getHeight()*cellSizeH; // we need height of grid not height of component
        int posX = x * cellSizeW;
        int posY = y * cellSizeH;
        
        if (posX + cellSizeW * 2 > width)
        	posX = width - cellSizeW * 2;
        if (posY + cellSizeH * 2 > height)
        	posY = height - cellSizeH * 2;
        
        g.fillRect(posX + 1, posY + 1, cellSizeW*2-1, cellSizeH*2-1);
        g.setColor(Color.lightGray);
        g.drawRect(posX, posY, cellSizeW*2, cellSizeH*2);
        g.setColor(Color.black);
    	drawString(g, x, y, defaultFont, baseName);
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
    
//    @SuppressWarnings("unused")
//    public void createUnit(Graphics g, int x, int y, int type) {
//    	Location location = new Location(x / cellSizeW, y / cellSizeH);
//    	Dimension baseSize = new Dimension(1,1);
//    	Dimension cellSize = new Dimension(cellSizeW, cellSizeH);
//    	Unit unit = new Unit(location, baseSize, cellSize, type);
//    	unitList.add(unit);
//    	repaint();
//    }
    
    public Unit createUnit(Location gridLocation, int owner, int type) {
    	Dimension cellSize = new Dimension(cellSizeW, cellSizeH);
    	Unit unit = new FirstYear(gridLocation, Unit.DEFAULT_UNIT_SIZE, cellSize);
    	unit.setOwner(owner);
    	Base.getOwnerBase(owner,baseList).getUnitList().add(unit); //list of units of actual player
    	unitList.add(unit); //list of all units
    	repaint();
    	return unit;
    }
    
    public void draw(Graphics g, int x, int y, int object) {
	    if ( (GameModel.BASE & object) != 0) {
	    	for (Base base:baseList) {
	    		if (x == base.getX() && y == base.getY()) { //seek for painted base
	    			if (base.getType() == GameModel.PLAYER)
	    				drawBase(g, x, y, base.getColor(), "Player Base");
	    			else
	    				drawBase(g, x, y, base.getColor(), "Agent Base: " + base.getType());
	    			
	    		}
	    	}
	    }
	}
    
    private void draw(Graphics g, int x, int y) {
    	if (model.getData()[x][y] != GameModel.CLEAN) {
	    	if ((model.getData()[x][y] & GameModel.OBSTACLE) != 0) {
	    		drawObstacle(g, x, y);
	    	}
	    	
	    	else if ((model.getData()[x][y] & GameModel.AGENT) != 0) {
	    		//System.out.println("drawing agent at: " + x + " " + y);
	    		drawAgent(g, x, y, Color.blue, model.getAgAtPos(x, y));
	    	}
	   
	    	int vl = GameModel.OBSTACLE*2;
	    	while (vl < limit) {
	    		if ((model.getData()[x][y] & vl) != 0) {
	    			draw(g, x, y, vl);
	    		}
	    		vl *= 2;
	    	}
    	}
    	
    }
    
    /**
     * This draws a grid - supposed for debug only
     * @param g
     */
    @SuppressWarnings("unused")
	private void drawGrid(Graphics g) {
        int mwidth = model.getWidth();
        int mheight = model.getHeight();
    	g.setColor(Color.lightGray);
        for (int l = 1; l <= mheight; l++) { //paint rows
            g.drawLine(0, l * cellSizeH, mwidth * cellSizeW, l * cellSizeH);
        } //paint columns
        for (int c = 1; c <= mwidth; c++) {
            g.drawLine(c * cellSizeW, 0, c * cellSizeW, mheight * cellSizeH);
        }
    }
    
    private void drawPossibleMovement(Location loc, int act, int max) {
    	if (act <= max) {
    		findPossibleLocations(loc, act, max, movementLocations, getForbiddenLocations());
    		movementLocations.removeAll(getForbiddenLocations());	
    		drawMovementGrid(movementLocations);
    		repaint();
    	}
    }
    
    private void findPossibleLocations(Location loc, int act, int max, LinkedList<Location> locations, LinkedList<Location> forbidden) {
    	if (act <= max && !forbidden.contains(loc)) {
    		if (! locations.contains(loc))
    			locations.add(loc);
    		if (loc.x + 1 < model.getWidth())
    			findPossibleLocations(new Location(loc.x + 1, loc.y), act+1, max, locations, forbidden);
    		if (loc.x - 1 >= 0)
    			findPossibleLocations(new Location(loc.x - 1, loc.y), act+1, max, locations, forbidden);
    		if (loc.y + 1 < model.getHeight())
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
    	drawPossibleMovement(unit.getLocation(), 1, unit.getMov());
    }
    
    public void paint(Graphics g) {
        cellSizeW = this.getWidth() / model.getWidth();
        cellSizeH = this.getHeight() / model.getHeight();
        int mwidth = model.getWidth();
        int mheight = model.getHeight();

        //drawGrid(g);
        
        for (int x = 0; x < mwidth; x++) {
            for (int y = 0; y < mheight; y++) {
                draw(g,x,y);
            }
        }
        for (Unit unit:unitList) {
        	unit.drawUnit(g, cellSizeW, cellSizeH);
        }
        //g.drawImage(myPicture, x * cellSizeW + 1, y * cellSizeH+1, cellSizeW-1, cellSizeH-1, null);
    }
    
    public void repaint() {
        if (view != null) {
	    	cellSizeW = this.getWidth() / model.getWidth();
	        cellSizeH = this.getHeight() / model.getHeight();
	        for (Unit unit:unitList) {
	        	unit.drawUnit(this.getGraphics(), cellSizeW, cellSizeH);
	        }
	        for (Base base:baseList) {
	        	base.setCellSizeW(cellSizeW);
	        	base.setCellSizeH(cellSizeH);
	        }
        }
        //super.repaint();
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
	
	class MapMouseInputAdapter extends MouseInputAdapter {
		Base cbase = null;
		
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) { 
				if (movementLocations.isEmpty()) {
					for (Base base:baseList) {
						if ( base.wasSelected( e.getX(),  e.getY()) ) {
							cbase = base;
							view.controlPanel.statusArea.append("Base: " + base.wasSelected( e.getX(),  e.getY())+"");
							break;
						}
					}
					for (Unit unit:unitList) {
						if ( unit.wasSelected( e.getX(),  e.getY()) ) {
							cunit = unit;
							view.controlPanel.statusArea.append("Unit: " + unit.wasSelected( e.getX(),  e.getY())+"");
							view.controlPanel.infoPanel.showUnitContext(cunit);
							break;
						}
					}
					if (cunit != null)
						drawPossibleMovement(cunit);
				} else {
					Location loc = new Location(e.getX() / cellSizeW, e.getY() / cellSizeH);
					if (movementLocations.contains(loc)) {
						cunit.setLocation(loc);
						view.env.addPercept(GameEnv.CA);
					}
					movementLocations.clear();
					cunit = null;
					e.getComponent().getParent().repaint();
				}
			}
			if (e.getButton() == MouseEvent.BUTTON3) {
				menu.show(e.getComponent(), e.getX(),  e.getY());
			}
			
		}
	}
}
