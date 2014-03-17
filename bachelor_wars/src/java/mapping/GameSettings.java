package mapping;
import jason.environment.grid.Location;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import objects.units.FirstYear;
import objects.units.Unit;


public class GameSettings {
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
	
	public static final int DEFAULT_PLAYERS = 2;
	public static final int PLAYER_ID = 0; //living player has always ID 1 as an owner of base
	public static final String PLAYER_NAME = "DarkEye";
	public static final String [] AI_NAMES = {"Simple AI", "Medium AI", "Advanced AI"};
	
	
	private int numPlayers; // used to set ID's for players (agents and living player) up to this number from 0 - indicates owners of bases
	private ArrayList<Integer> players; //players in game
	private ArrayList<Color> colors;
	private ArrayList<Location> baseLocations;
	String playerName;
	
	
	private int mapColumns, mapRows;
	private int width = GSize, height = GSize;
	
	public GameSettings() {
		setPlayers(new ArrayList<Integer>());
		setColors(new ArrayList<Color>());
		setBaseLocations(new ArrayList<Location>());
	}
	
	/**
	 * Default game settings to increase developing speed
	 */
	public void defaultInit() {
		setNumPlayers(DEFAULT_PLAYERS);
		getPlayers().add(PLAYER);
		getPlayers().add(SIMPLE_AI);
		getColors().add(Color.green);
		getColors().add(Color.RED);
		getBaseLocations().add(new Location(0,0));
		getBaseLocations().add(new Location(GSize-2,GSize-2));
		playerName = PLAYER_NAME;
		
		mapRows = GSize;
		mapColumns = GSize;
		
		initUnits();
	}
	
	private void initUnits() {
		AVAILABLE_UNITS.add(new Dictionary<Integer, Unit>(FIRST_YEAR_STUDENT,FirstYear.getPrototype()));
		AVAILABLE_UNITS.add(new Dictionary<Integer, Unit>(FIRST_YEAR_STUDENT,FirstYear.getPrototype()));
		AVAILABLE_UNITS.add(new Dictionary<Integer, Unit>(FIRST_YEAR_STUDENT,FirstYear.getPrototype()));
		AVAILABLE_UNITS.add(new Dictionary<Integer, Unit>(FIRST_YEAR_STUDENT,FirstYear.getPrototype()));
		AVAILABLE_UNITS.add(new Dictionary<Integer, Unit>(FIRST_YEAR_STUDENT,FirstYear.getPrototype()));
		AVAILABLE_UNITS.add(new Dictionary<Integer, Unit>(FIRST_YEAR_STUDENT,FirstYear.getPrototype()));
	}
	
	public int getMapWidth() {
		return width;
	}
	
	public int getMapHeight() {
		return height;
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}

	public ArrayList<Location> getBaseLocations() {
		return baseLocations;
	}

	public void setBaseLocations(ArrayList<Location> baseLocations) {
		this.baseLocations = baseLocations;
	}

	public ArrayList<Color> getColors() {
		return colors;
	}

	public void setColors(ArrayList<Color> colors) {
		this.colors = colors;
	}

	public ArrayList<Integer> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Integer> players) {
		this.players = players;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getMapColumns() {
		return mapColumns;
	}

	public int getMapRows() {
		return mapRows;
	}
}
