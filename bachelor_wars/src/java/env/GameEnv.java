package env;


import jason.NoValueException;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Structure;
import jason.environment.Environment;
import jason.environment.grid.Location;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Logger;

import mapping.GameSettings;
import mapping.Node;
import objects.Base;
import objects.GameObject;
import objects.Knowledge;
import objects.units.FirstYear;
import objects.units.Unit;
import ui.GameMap;
import ui.GameView;
import ui.menu.MainMenu;

public class GameEnv extends Environment {
	
	public static final String VERSION = "0.0.1";
	
	private static final int TIME_TO_WAIT = 50; //800
	private static final Object countLock = new Object();
	
	public static final Literal CA = Literal.parseLiteral("can_act");
	
    private Logger logger = Logger.getLogger("bachelor_wars."+GameEnv.class.getName());
    private int marker;
    
    GameView view;

    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {
    	
//    	System.out.println(test);
    	
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
        if (action.getFunctor().equals("update_percepts")) {
        	updatePercepts(action);
	        return true;
        } else if (action.getFunctor().equals("create_unit")) {
        	createUnit(action);
            return true;
        } else if (action.getFunctor().equals("mark_done")) {
        	markDone(action);
        	return true;
        } else if (action.getFunctor().equals("move")) {
        	move(action);
        	return true;
        } else if (action.getFunctor().equals("do_intention_if_possible")) {
        	doInteractionIfPossible(action);
        	return true;
        } else {
        	view.getGameMap();
			for (Base base:GameMap.getBaseList()) {
        		if (base.getOwner() != GameSettings.PLAYER_ID) {
        			clearPercepts(base.getAgent());
        		}
        	}
        	return true;
        }
    }
    
    private void reInit(boolean clear) {
    	view.repaint();
		view.getGameMap().repaint();
		if (clear)
			view.getGameMap().clearMovement();
    }
    
    private void doInteractionIfPossible(Structure action) {
//    	System.out.println("here");
    	synchronized (countLock) {
	    	try {
		    	int unitId = (int)(((NumberTerm)action.getTerm(0)).solve());
				int gameObjectId = (int)(((NumberTerm)action.getTerm(1)).solve());
				
				Unit unit = GameMap.searchUnit(unitId);
				GameObject target = unit.searchIntentionTargetById(gameObjectId);
				
				System.out.println("ID : " + gameObjectId + " " + unit.getIntentions());
				if (target == null) { //THIS SHOULD NEVER HAPPEN!!!
					System.out.println("WAITING");
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if (unit.getIntentions().get(target).intention == Unit.KILL) {
					if (unit.getNode().distance(target.getNode()) <= unit.getBasicAtkRange()) { // unit is able to do damage TODO special attacks
						((Unit)target).addDamage(unit.getAtk());
					}
				}
				else if (unit.getIntentions().get(target).intention == Unit.SEIZE) {
					if (unit.getNode().distance(target.getNode()) == 0 ) //we are at position
						unit.getIntentions().remove(target);
				}
				
	    	} catch (NoValueException e) {
				e.printStackTrace();
			}
    	}
    }
    private void markDone(Structure action) {
    	marker++;
    	GameMap.removeActiveBase();
    	GameMap.allowActions(GameMap.getActiveBases(), this);
//    	if (marker == view.getSettings().getNumPlayers() -1 ) { //-1 cos we have a living player too 
    	if (GameMap.getActiveBases().isEmpty()) {
    		view.getGameMap().setEnabled(true);
    		marker = 0;
			for (Base base:GameMap.getBaseList()) {
    			int sum = view.getSettings().getIncomePerRound();
    			for (Knowledge knowledge:base.getKnowledgeList())
    				sum += knowledge.getKnowledgePerRound();
    			base.addKnowledge(sum);
    			base.reInit();
    		}
    		GameMap.ROUND++;
    		GameMap.reinitActiveBases();
    	}
    }
    
    private void createUnit(Structure action) {
    	try {
			int agentID = (int)(((NumberTerm)action.getTerm(0)).solve());
			int type = (int)(((NumberTerm)action.getTerm(1)).solve());
			Base base = GameMap.searchBase(agentID);
			Unit u = view.getGameMap().createUnit(base.getLocation(), agentID, type);
			clearPercepts(base.getAgent());
			base.getUsableUnits().add(u);
			addPercept(base.getAgent(), Literal.parseLiteral("created_unit(" + u + ")"));
		} catch (NoValueException e) {
			e.printStackTrace();
		}
    }
    
    private void move(Structure action) {
    	try {
			int unitID = (int)(((NumberTerm)action.getTerm(0)).solve());
			Unit unit = GameMap.searchUnit(unitID);
			Node node = Node.getNode(action.getTermsArray());
			waitForDraw();
			view.getGameMap().drawPossibleMovement(unit);
			waitForDraw();
			reInit(false);
//			System.out.println("Node: " + node.getX() + " " + node.getY());
			unit.setLocation(node, view);
			reInit(true);
			unit.base.getUsableUnits().remove(unit);
			clearPercepts(unit.base.getAgent());
		} catch (NoValueException e) {
			e.printStackTrace();
		}
    }

	private void updatePercepts(Structure action) {
		for (Base base:GameMap.getBaseList()) {
    		if (base.getOwner() != GameSettings.PLAYER_ID) {
    			clearPercepts(base.getAgent());
		        addPercept(base.getAgent(), Literal.parseLiteral("actualKnowledge("+base.getKnowledge()+")"));
		        addPercept(base.getAgent(), Literal.parseLiteral("freeSlots("+base.getFreeSlots()+")"));
		        addPercept(base.getAgent(), Literal.parseLiteral("maximumSlots("+base.getMaxSlots()+")"));
		        addPercept(base.getAgent(), Literal.parseLiteral("agentID("+base.getOwner()+")"));
		        addPercept(base.getAgent(), Literal.parseLiteral("mode("+view.getSettings().getMode()+")"));
		        System.out.println("updating percepts for: " + base.getAgent());
    		}
    	}
	}
	
	public void addAgent(String name, String agent) {
		LinkedList<String> cs = new LinkedList<String>();
		try {
			getEnvironmentInfraTier().getRuntimeServices().createAgent(name, "src/asl/" + agent, null, cs, null, null);
			// Starts new agent
			getEnvironmentInfraTier().getRuntimeServices().startAgent(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
