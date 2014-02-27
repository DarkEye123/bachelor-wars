package env;


import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.environment.Environment;
import jason.environment.grid.Location;

import java.util.LinkedList;
import java.util.logging.Logger;

import objects.Base;
import ui.GameView;
import ui.menu.MainMenu;
import models.GameModel;

public class GameEnv extends Environment {
	
	public static final String VERSION = "0.0.1";
	
	public static final Literal CAT = Literal.parseLiteral("can_act(True)");
	public static final Literal CAF = Literal.parseLiteral("can_act(False)");
	public static final Literal CURR = Literal.parseLiteral("create_unit(random,random)");

    private Logger logger = Logger.getLogger("bachelor_wars."+GameEnv.class.getName());
    
    GameModel model;
    GameView view;

    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {
    	
        super.init(args);
        
        if (args.length == 1 && args[0].equals("gui")) { 
        	MainMenu menu = new MainMenu(this);
        	menu.init();
        }
        
    }

    @Override
    public boolean executeAction(String agName, Structure action) {
        logger.info("executing: "+action);
        boolean result = false;
        if (action.equals(CURR)) { // of = open(fridge)
        	Base base = view.getGameMap().getBaseList().get(1);
        	Location loc = base.getLocation();
            view.getGameMap().createUnit(loc, base.getOwner(), GameModel.FIRST_YEAR_STUDENT);
            result = true;
        }
        return result;
    }

	public void updatePercepts(LinkedList<Literal> list) {
		clearAllPercepts();
		for (Literal lit:list) {
			addPercept(lit);
		}
	}
	
	/** Called before the end of MAS execution */
	@Override
	public void stop() {
		super.stop();
	}
	
	public void setModel(GameModel model) {
		this.model = model;
	}
	
	public void setView(GameView view) {
		this.view = view;
		view.init(this);
		model.setView(this.view.getGameMap());
	}
}
