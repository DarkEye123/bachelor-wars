package models;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import mapping.Dictionary;
import mapping.GameSettings;
import mapping.UnitPicMap;
import ui.GameMap;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;


/**
 * 
 * @author DarkEye
 * 
 * Class that represents used game model - it's invisible grid with positions of agents, bases and units etc.
 */
public class GameModel extends GridWorldModel {
	
	//TODO add new units, should be 6 units, but now it's only one
	public static final UnitPicMap [] AVAILABLE_UNITS = 
		{
			new UnitPicMap(GameModel.FIRST_YEAR_STUDENT,new File("pics/first_class128.png")),
			new UnitPicMap(GameModel.FIRST_YEAR_STUDENT,new File("pics/first_class128.png")),
			new UnitPicMap(GameModel.FIRST_YEAR_STUDENT,new File("pics/first_class128.png")),
			new UnitPicMap(GameModel.FIRST_YEAR_STUDENT,new File("pics/first_class128.png")),
			new UnitPicMap(GameModel.FIRST_YEAR_STUDENT,new File("pics/first_class128.png")),
			new UnitPicMap(GameModel.FIRST_YEAR_STUDENT,new File("pics/first_class128.png"))
		};
	
	public static List<Dictionary<Integer, String>> UNIT_NAMES = new LinkedList<Dictionary<Integer, String>>();
	public static List<Dictionary<Integer, Integer>> UNIT_COST = new LinkedList<Dictionary<Integer, Integer>>();
	public static List<Dictionary<Integer, Integer>> UNIT_ATK = new LinkedList<Dictionary<Integer, Integer>>();
	
	
	public static final int GSize = 24;
	
	public static final int PLAYER = 0; // types of players - needed for creation of bases
	public static final int SIMPLE_AI = 1;
	public static final int MEDIUM_AI = 2;
	public static final int ADVANCED_AI = 3;
	
    // constants for the grid objects
    public static final int BASE = 16; //common id for Bases
    public static final int UNIT = 32; //common id for UNITs
    
    public static final int FIRST_YEAR_STUDENT = 64;
    public static final int FIRST_YEAR_STUDENT_COST = 20;
    public static final int FIRST_YEAR_STUDENT_ATK = 5;
    public static final String FIRST_YEAR_STUDENT_NAME = "First Year Student";
    
    protected GameMap view;
    protected GameSettings settings;
    
	public GameModel (GameSettings settings) {
		this(GSize, GSize, settings.getNumPlayers());
		this.settings = settings;
		initUnits();
		
		initBases();
		setAgPos(0, 5, 5);
	}

	protected GameModel(int w, int h, int nbAgs) {
		super(GSize, GSize, nbAgs);
	}
	
	private void initUnits() {
		UNIT_NAMES.add(new Dictionary<Integer, String>(GameModel.FIRST_YEAR_STUDENT, FIRST_YEAR_STUDENT_NAME));
		UNIT_COST.add(new Dictionary<Integer, Integer>(GameModel.FIRST_YEAR_STUDENT, FIRST_YEAR_STUDENT_COST));
		UNIT_ATK.add(new Dictionary<Integer, Integer>(GameModel.FIRST_YEAR_STUDENT, FIRST_YEAR_STUDENT_ATK));
		
	}
	
	//TODO - need to be rewritten, it's temporary code
	public void initBases() {
		for (Location baseLocation:settings.getBaseLocations()) {
			add(BASE,baseLocation);
		}
	}

	
	public void setView(GameMap v) {
        view = v;
    }
	
	public int[][] getData() {
		return this.data;
	}
}
