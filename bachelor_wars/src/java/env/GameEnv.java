package env;


import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.environment.Environment;
import jason.environment.grid.Location;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import objects.Base;
import ui.GameView;
import ui.menu.MainMenu;
import models.GameModel;

public class GameEnv extends Environment {
	
	public static final String VERSION = "0.0.1";
	
	public static final Literal CA = Literal.parseLiteral("can_act");
	public static final Literal CU = Literal.parseLiteral("create_unit");

    private Logger logger = Logger.getLogger("bachelor_wars."+GameEnv.class.getName());
    
    GameModel model;
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

    @Override
    public boolean executeAction(String agName, Structure action) {
        logger.info("["+agName+"] executing: "+action);
        if (action.equals(CU)) { // of = open(fridge)
        	Base base = view.getGameMap().getBaseList().get(1);
        	Location loc = base.getLocation();
            view.getGameMap().createUnit(loc, base.getOwner(), GameModel.FIRST_YEAR_STUDENT);
            clearPercepts();
            return true;
        } else {
        	clearPercepts();
        	return true;
        }
    	
//    	if (action.equals(CU)) {
//    		clearPercepts();
//    		return true;
//    	} else {
//    		clearPercepts();
//    		return true;
//    	}
    }

	public void updatePercepts(LinkedList<Literal> list) {
		//clearPercepts("simple_ai");
//		for (Literal lit:list) {
//			addPercept("simple_ai",lit);
//		}
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
