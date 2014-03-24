package env;


import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.environment.Environment;
import jason.environment.grid.Location;

import java.util.LinkedList;
import java.util.logging.Logger;

import mapping.GameSettings;
import objects.Base;
import objects.units.Unit;
import ui.GameView;
import ui.menu.MainMenu;

public class GameEnv extends Environment {
	
	public static final String VERSION = "0.0.1";
	
	private static final int TIME_TO_WAIT = 800;
	
	public static final Literal CA = Literal.parseLiteral("can_act");
	public static final Literal CU = Literal.parseLiteral("create_unit");
	public static final Literal UPDATE = Literal.parseLiteral("update_percepts");
	public static final Literal MOV = Literal.parseLiteral("move_units");
	public static final Literal EKNOW = Literal.parseLiteral("enough_knowledge");

    private Logger logger = Logger.getLogger("bachelor_wars."+GameEnv.class.getName());
    
    GameView view;

    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {
    	
        //super.init(args);
        
        if (args.length == 1 && args[0].equals("gui")) { 
        	MainMenu menu = new MainMenu(this);
        	menu.init();
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

    @Override
    public boolean executeAction(String agName, Structure action) {
        logger.info("["+agName+"] executing: "+action);
        if (action.equals(UPDATE)) {
        	for (Base base:view.getGameMap().getBaseList()) {
        		if (base.getOwner() != GameSettings.PLAYER_ID) {
        			clearPercepts(base.getAgent());
			        addPercept(base.getAgent(), Literal.parseLiteral("actualKnowledge("+10+")"));
			        addPercept(base.getAgent(), Literal.parseLiteral("freeSlots("+base.getFreeSlots()+")"));
			        addPercept(base.getAgent(), Literal.parseLiteral("maximumSlots("+base.getMaxSlots()+")"));
			        addPercept(base.getAgent(), EKNOW);
			        System.out.println("updating percepts for: " + base.getAgent());
        		}
        	}
	        return true;
        } else if (action.equals(CU)) {
        	for (Base base:view.getGameMap().getBaseList()) {
        		if (base.getOwner() != GameSettings.PLAYER_ID) {
		        	Location loc = base.getLocation();
		            view.getGameMap().createUnit(loc, base.getOwner(), GameSettings.FIRST_YEAR_STUDENT);
		            clearPercepts(base.getAgent());
        		}
        	}
            return true;
        } else if (action.equals(MOV)) {
        	for (Base base:view.getGameMap().getBaseList()) {
        		if (base.getOwner() != GameSettings.PLAYER_ID) {
        			for (Unit unit:base.getUnitList()) {
        				waitForDraw();
        				view.getGameMap().drawPossibleMovement(unit);
        				waitForDraw();
        				boolean repeat = true;
        				while (repeat) {
        					for (Location loc:view.getGameMap().getMovementLocations()) {
        						double test = Math.random();
        						if (test > 0.5) {
        							unit.setLocation(loc);
        							repeat = false;
        							view.repaint();
        							view.getGameMap().repaint();
        							break;
        						}
        					}
        				}
        			}
        			clearPercepts(base.getAgent());
        		}
        	}
        	return true;
        } else {
        	for (Base base:view.getGameMap().getBaseList()) {
        		if (base.getOwner() != GameSettings.PLAYER_ID) {
        			clearPercepts(base.getAgent());
        		}
        	}
        	return true;
        }
    }

	public void updatePercepts(LinkedList<Literal> list) {
		
	}
	
	/** Called before the end of MAS execution */
	@Override
	public void stop() {
		super.stop();
	}
	
	public void setView(GameView view) {
		this.view = view;
		view.init(this);
	}
}
