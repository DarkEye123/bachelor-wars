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
import java.awt.event.MouseListener;
import java.io.File;
import java.util.LinkedList;

import javax.swing.JPanel;

import mapping.GameSettings;
import models.GameModel;
import objects.Base;
import objects.Unit;


public class GameMap extends JPanel {

	private static final long serialVersionUID = 6282676586123792528L;
	public static final int BASE_DEFAULT_SIZE = 2;
	
	GameModel model;
	GameView view;
	private PopupMenu menu;

	//protected GridCanvas drawArea;
	
	protected int cellSizeW = 0;
	protected int cellSizeH = 0;
	
	private LinkedList<Unit> unitList = new LinkedList<Unit>();
	private LinkedList<Base> baseList = new LinkedList<Base>();
	
	
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
		initBases();
		menu = new PopupMenu();
		menu.add(new MenuItem("Test"));
		this.add(menu);
		this.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseClicked(MouseEvent e) {
				//System.out.println( (int)(e.getX() / map.getCellSizeW()) + " " + (int)(e.getY() / map.getCellSizeH()) );
				//System.out.println(map.getCellSizeW() + " " + map.getCellSizeH());
				//map.drawEmpty(map.getGraphics(), e.getX() / map.getCellSizeW(), e.getY() / map.getCellSizeH());
				//createUnit(e.getComponent().getGraphics(),e.getX(),  e.getY(), GameModel.FIRST_YEAR_STUDENT);
				if (e.getButton() == MouseEvent.BUTTON1) {
					for (Base base:baseList) {
						//System.out.println(base.wasSelected( e.getX(),  e.getY()));
						view.controlPanel.statusArea.append(base.wasSelected( e.getX(),  e.getY())+"");
					}
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					menu.show(e.getComponent(), e.getX(),  e.getY());
				}
				
			}
		});
	}
	
	private void initBases() {
		Dimension baseSize = new Dimension(Base.DEFAULT_BASE_SIZE,Base.DEFAULT_BASE_SIZE);
		Dimension gridSize = new Dimension(cellSizeW,cellSizeH);
		Base base = null;
		for (int x=0; x < settings.getNumPlayers(); ++x) {
			base = new Base(settings.getBaseLocations().get(x), baseSize, gridSize);
			base.setBaseColor(settings.getColors().get(x));
			base.setType(settings.getPlayers().get(x));
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
    
    public void createUnit(Graphics g, int x, int y, int type) {
    	Location location = new Location(x / cellSizeW, y / cellSizeH);
    	Dimension baseSize = new Dimension(1,1);
    	Dimension cellSize = new Dimension(cellSizeW, cellSizeH);
    	Unit unit = new Unit(location, baseSize, cellSize, type);
    	unitList.add(unit);
    	repaint();
    }
    
    public void draw(Graphics g, int x, int y, int object) {
	    if ( (GameModel.BASE & object) != 0) {
	    	for (Base base:baseList) {
	    		if (x == base.getX() && y == base.getY()) { //seek for painted base
	    			if (base.getType() == GameModel.PLAYER)
	    				drawBase(g, x, y, base.getBaseColor(), "Player Base");
	    			else
	    				drawBase(g, x, y, base.getBaseColor(), "Agent Base: " + base.getType());
	    			
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

	
}
