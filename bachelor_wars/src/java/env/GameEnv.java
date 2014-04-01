package env;


import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.environment.Environment;
import jason.environment.TimeSteppedEnvironment;
import jason.environment.grid.Location;
import jason.mas2j.AgentParameters;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Logger;

import mapping.GameSettings;
import objects.Base;
import objects.Knowledge;
import objects.units.Unit;
import ui.GameMap;
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
        
        getEnvironmentInfraTier().getRuntimeServices().killAgent("simple_ai", "simple_ai");
//        addAgent("test", "simple_ai.asl");
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
        addAgent("test", "simple_ai.asl");
        if (action.equals(UPDATE)) {
        	for (Base base:view.getGameMap().getBaseList()) {
        		if (base.getOwner() != GameSettings.PLAYER_ID) {
        			clearPercepts(base.getAgent());
			        addPercept(base.getAgent(), Literal.parseLiteral("actualKnowledge("+base.getKnowledge()+")"));
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
        				int index = new Random().nextInt(view.getGameMap().getMovementLocations().size());
        				unit.setLocation(view.getGameMap().getMovementLocations().get(index));
        				view.repaint();
        				view.getGameMap().repaint();
        				view.getGameMap().getMovementLocations().clear();
        			}
        			clearPercepts(base.getAgent());
        		}
        	}
        	return true;
        } else if (action.equals(MD)) {
        	marker++;
//        	System.out.println(view.getSettings().getNumPlayers() -1 + " and marker is: " + marker);
        	if (marker == view.getSettings().getNumPlayers() -1 ) { //-1 cos we have a living player too 
        		view.getGameMap().setEnabled(true);
        		marker = 0;
        		for (Base base:view.getGameMap().getBaseList()) {
        			int sum = view.getSettings().getIncomePerRound();
        			for (Knowledge knowledge:base.getKnowledgeList())
        				sum += knowledge.getKnowledgePerRound();
        			base.addKnowledge(sum);
        		}
        		GameMap.ROUND++;
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
	
	public void addAgent(String name, String agent) {
//		jason.mas2j.AgentParameters.this.
		LinkedList<String> cs = new LinkedList<String>();
		try {
			getEnvironmentInfraTier().getRuntimeServices().createAgent(name, "src/asl/" + agent, null, cs, null, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		jason.mas2j.parser.mas2j.class.getSigners()getaddAgent(par);
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
