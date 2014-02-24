package env;


import jason.asSyntax.Structure;
import jason.environment.Environment;

import java.util.logging.Logger;

import ui.GameView;
import ui.menu.MainMenu;
import models.GameModel;

public class GameEnv extends Environment {
	
	public static final String VERSION = "0.0.1";

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
        logger.info("executing: "+action+", but not implemented!");
        return true;
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
    	view.init();
    	model.setView(this.view.getGameMap());
    	//view.setBackground(Color.pink);
    	//System.out.println(view.getSize());
    	//model.setView(view);
    	updatePercepts();
    	/*view.getContentPane().add(BorderLayout.EAST,new JPanel().add(new JButton("EAST")));
    	view.getContentPane().add(BorderLayout.WEST,new JPanel().add(new JButton("WEST")));
    	JPanel south = new JPanel();
    	south.setSize(view.getWidth(), 300);
    	south.add(new JButton("KURVA"));
    	south.add(new JButton("SOUTH"));
    	System.out.println(view.getCanvas().getSize());
    	view.getDrawArea().setMaximumSize(new Dimension(200, 200));
    	System.out.println(view.getCanvas().getSize());
    	view.repaint();
    	view.getContentPane().add(BorderLayout.SOUTH,south);*/
    }

	private void updatePercepts() {
		
	}
}
