package env;


import jason.NoValueException;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Structure;
import jason.environment.Environment;
import jason.environment.grid.Location;

import java.util.LinkedList;
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
	
	public static final Literal CA = Literal.parseLiteral("can_act");
	public static final Literal CU = Literal.parseLiteral("create_unit");
	public static final Literal UPDATE = Literal.parseLiteral("update_percepts");
	public static final Literal MD = Literal.parseLiteral("mark_done");

    private Logger logger = Logger.getLogger("bachelor_wars."+GameEnv.class.getName());
    private int marker;
    
    GameView view;

    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {
    	
        //super.init(args);
        
        if (args.length == 1 && args[0].equals("gui")) { 
        	MainMenu menu = new MainMenu(this);
        	menu.init();
        }
        
//        addAgent("test", "simple_ai.asl");
//        addPercept("simple_ai", Literal.parseLiteral("possibleUnits([[1,2],[3,4]])"));
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
//        for (String name: getEnvironmentInfraTier().getRuntimeServices().getAgentsNames())
//        	System.out.println(name);
//        System.out.println(getEnvironmentInfraTier().getRuntimeServices().killAgent("simple_ai", null));
        if (action.equals(UPDATE)) {
			for (Base base:GameMap.getBaseList()) {
        		if (base.getOwner() != GameSettings.PLAYER_ID) {
        			clearPercepts(base.getAgent());
			        addPercept(base.getAgent(), Literal.parseLiteral("actualKnowledge("+base.getKnowledge()+")"));
			        addPercept(base.getAgent(), Literal.parseLiteral("freeSlots("+base.getFreeSlots()+")"));
			        addPercept(base.getAgent(), Literal.parseLiteral("maximumSlots("+base.getMaxSlots()+")"));
			        System.out.println("updating percepts for: " + base.getAgent());
        		}
        	}
	        return true;
        } else if (action.getFunctor().equals("create_unit")) {
        	try {
				int agentID = (int)(((NumberTerm)action.getTerm(0)).solve());
				int type = (int)(((NumberTerm)action.getTerm(1)).solve());
				Base base = GameMap.searchBase(agentID);
				Unit u = view.getGameMap().createUnit(base.getLocation(), agentID, type);
				clearPercepts(base.getAgent());
				addPercept(base.getAgent(), Literal.parseLiteral("created_unit(" + u.getId() + ")"));
			} catch (NoValueException e) {
				e.printStackTrace();
			}
            return true;
        } else if (action.equals(MD)) {
        	marker++;
//        	System.out.println(view.getSettings().getNumPlayers() -1 + " and marker is: " + marker);
        	if (marker == view.getSettings().getNumPlayers() -1 ) { //-1 cos we have a living player too 
        		view.getGameMap().setEnabled(true);
        		marker = 0;
				for (Base base:GameMap.getBaseList()) {
        			int sum = view.getSettings().getIncomePerRound();
        			for (Knowledge knowledge:base.getKnowledgeList())
        				sum += knowledge.getKnowledgePerRound();
        			base.addKnowledge(sum);
        		}
        		GameMap.ROUND++;
        	}
        	return true;
        } else if (action.getFunctor().equals("move")) {
        	try {
				int unitID = (int)(((NumberTerm)action.getTerm(0)).solve());
				Unit unit = GameMap.searchUnit(unitID);
				Node node = Node.getNode(action.getTermsArray());
				waitForDraw();
				view.getGameMap().drawPossibleMovement(unit);
				waitForDraw();
				reInit();
//				System.out.println("Node: " + node.getX() + " " + node.getY());
				unit.setLocation(node, view);
				clearPercepts(unit.base.getAgent());
			} catch (NoValueException e) {
				e.printStackTrace();
			}
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
    
    private void reInit() {
    	view.repaint();
		view.getGameMap().repaint();
		view.getGameMap().clearMovement();
    }

	public void updatePercepts(LinkedList<Literal> list) {
		
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
