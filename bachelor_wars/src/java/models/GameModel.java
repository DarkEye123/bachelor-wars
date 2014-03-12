package models;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

import java.util.LinkedList;
import java.util.List;

import mapping.Dictionary;
import mapping.GameSettings;
import objects.units.FirstYear;
import objects.units.Unit;
import ui.GameMap;


/**
 * 
 * @author DarkEye
 * 
 * Class that represents used game model - it's invisible grid with positions of agents, bases and units etc.
 */
public class GameModel extends GridWorldModel {
	
	//TODO add new units, should be 6 units, but now it's only one
	public static final List<Dictionary<Integer, Unit>> AVAILABLE_UNITS = new LinkedList<Dictionary<Integer, Unit>>();
	
	public static final int GSize = 24;
	
	public static final int PLAYER = 0; // types of players - needed for creation of bases
	public static final int SIMPLE_AI = 1;
	public static final int MEDIUM_AI = 2;
	public static final int ADVANCED_AI = 3;
	
    // constants for the grid objects
    public static final int BASE = 16; //common id for Bases
    public static final int UNIT = 32; //common id for UNITs
    
    public static final int FIRST_YEAR_STUDENT = 64;
    
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
		AVAILABLE_UNITS.add(new Dictionary<Integer, Unit>(GameModel.FIRST_YEAR_STUDENT,FirstYear.getPrototype()));
		AVAILABLE_UNITS.add(new Dictionary<Integer, Unit>(GameModel.FIRST_YEAR_STUDENT,FirstYear.getPrototype()));
		AVAILABLE_UNITS.add(new Dictionary<Integer, Unit>(GameModel.FIRST_YEAR_STUDENT,FirstYear.getPrototype()));
		AVAILABLE_UNITS.add(new Dictionary<Integer, Unit>(GameModel.FIRST_YEAR_STUDENT,FirstYear.getPrototype()));
		AVAILABLE_UNITS.add(new Dictionary<Integer, Unit>(GameModel.FIRST_YEAR_STUDENT,FirstYear.getPrototype()));
		AVAILABLE_UNITS.add(new Dictionary<Integer, Unit>(GameModel.FIRST_YEAR_STUDENT,FirstYear.getPrototype()));
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
