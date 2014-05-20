package env;


import jason.NoValueException;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Literal;
import jason.asSyntax.LiteralImpl;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;
import jason.environment.Environment;

import java.util.LinkedList;
import java.util.logging.Logger;

import mapping.GameSettings;
import mapping.Node;
import objects.Base;
import objects.GameObject;
import objects.units.Unit;
import ui.GameMap;
import ui.GameView;
import ui.menu.GameSettingsMenu;

public class GameEnv extends Environment {
	
	public static final String VERSION = "0.0.1";
	private static final int TIME_TO_WAIT = 50; //800
	public static final Literal CA = Literal.parseLiteral("can_act");
	
    private Logger logger = Logger.getLogger("bachelor_wars."+GameEnv.class.getName());
    
    GameView view;

	public EnvAnalyzer analyzer; //initialized in GameView

	private boolean isEnd;

    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {
    	
//    	System.out.println(test);
    	
        if (args.length == 1 && args[0].equals("gui")) { 
        	GameSettingsMenu menu = new GameSettingsMenu(this);
//        	GameSettingsMenu.init();
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
        } else if (action.getFunctor().equals("mark_start")) {
        	isEnd = analyzer.analyzeEnvironment();
        	if (isEnd) {
        		view.getGameMap().setLivingPlayer(false);
            	return false;
        	}
        	else
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
    	synchronized (GameMap.countLock) {
	    	try {
		    	int unitId = (int)(((NumberTerm)action.getTerm(0)).solve());
				int gameObjectId = (int)(((NumberTerm)action.getTerm(1)).solve());
				
				Unit unit = GameMap.searchUnit(unitId);
				GameObject target = unit.searchIntentionTargetById(gameObjectId);
				
				System.out.println("ID : " + gameObjectId + " " + unit.getIntentions());
				
				if (unit.getIntentions().get(target).intention == Unit.KILL) {
					if (unit.getNode().distance(target.getNode()) <= unit.getBasicAtkRange()) { // unit is able to do damage
						((Unit)target).addDamage(unit.getAtk());
						if (((Unit)target).isDead())
							unit.base.addKilledEnemy();
					}
					for (GameObject object:unit.getIntentions().keySet()) { //unit may stand on the knowledge, so it is seizing too
						if (unit.getIntentions().get(object).intention == Unit.SEIZE) {
							if (unit.getNode().distance(object.getNode()) == 0 ) {//we are at position
								unit.getIntentions().remove(object);
								break;
							}
						}
					}
				}
				else if (unit.getIntentions().get(target).intention == Unit.SEIZE) {
					if (unit.getNode().distance(target.getNode()) == 0 ) //we are at position
						unit.getIntentions().remove(target);
					
					for (GameObject object:unit.getIntentions().keySet()) { //unit may stand on the knowledge, so it is seizing too
						if (unit.getIntentions().get(object).intention == Unit.KILL) {
							if (unit.getNode().distance(object.getNode()) <= unit.getBasicAtkRange() ) {//we are damage range
								((Unit)object).addDamage(unit.getAtk());
								if (((Unit)object).isDead())
									unit.base.addKilledEnemy();
								break;
							}
						}
					}
				}
				
	    	} catch (NoValueException e) {
				e.printStackTrace();
			}
    	}
    }
    
    private void markDone(Structure action) {
    	if (! GameMap.getBaseList().isEmpty()) { //game was not ended
	    	GameMap.removeActiveBase();
	    	GameMap.allowActions(GameMap.getActiveBases(), this);
	//    	if (marker == view.getSettings().getNumPlayers() -1 ) { //-1 cos we have a living player too 
	    	if (GameMap.getActiveBases().isEmpty()) {
	    		view.getGameMap().setCanManipulate(true);
				for (Base base:GameMap.getBaseList()) {
	    			base.reInit();
	    		}
	    		GameMap.ROUND++;
	    		GameMap.reinitActiveBases();
	    		if (GameMap.getBaseList().getFirst().getOwner() != GameSettings.PLAYER) //if there is no real player play new round
	    			GameMap.allowActions(GameMap.getActiveBases(), this);
	    		else {
	    			analyzer.analyzeEnvironment(GameMap.getBaseList().getFirst()); //player need to fulfill winning conditions too - it's new round for him
	    		}
	    	}
    	}
    }
    
    private void createUnit(Structure action) {
    	try {
			int agentID = (int)(((NumberTerm)action.getTerm(0)).solve());
			int type = (int)(((NumberTerm)action.getTerm(1)).solve());
			Base base = GameMap.searchBase(agentID);
			Unit u = view.getGameMap().createUnit(base.getNode(), agentID, type);
			clearPercepts(base.getAgent());
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
//			waitForDraw();
			view.getGameMap().drawPossibleMovement(unit);
//			waitForDraw();
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
		        addPercept(base.getAgent(), Literal.parseLiteral("fightingPower("+base.getFightingPower()+")."));
		        addPercept(base.getAgent(), Literal.parseLiteral("movingCapability("+base.getMovingCapability()+")."));
		        addPercept(base.getAgent(), Literal.parseLiteral("round("+GameMap.ROUND+")."));
		        addPercept(base.getAgent(), Literal.parseLiteral("team("+base.getTeam()+")."));
		        LinkedList<String> allies = new LinkedList<>();
		        for (Base b:base.getAllies()) {
		        	allies.add(b.getAgent());
		        }
		        Literal lit = new LiteralImpl("allies");
		        Term t = ListTermImpl.parseList(allies.toString());
		        lit.addTerm(t);
		        addPercept(base.getAgent(), lit);
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

	public void setAnalyzer(EnvAnalyzer envAnalyzer) {
		this.analyzer = envAnalyzer;
	}
}
