package mapping;
import jason.environment.grid.Location;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import objects.Base;
import objects.units.CommonBachelor;
import objects.units.FirstYear;
import objects.units.FitBachelor;
import objects.units.FourthYear;
import objects.units.SecondYear;
import objects.units.ThirdYear;
import objects.units.Unit;


public class GameSettings {
	public static final int INFINITE = 0;
	public static final int BASE_SEIZE_ROUNDS = 1;
	public static final int KNOWLEDGE_SEIZE_ROUNDS = 1;
	//TODO add new units, should be 6 units, but now it's only one
	public static final LinkedList<Unit> AVAILABLE_UNITS = new LinkedList<Unit>();
	public static final int GSize = 24;
	public static final int WINDOW_WIDTH = 1024;
	public static final int WINDOW_HEIGHT = 768;
	
	
	public static final int DEFAULT_KNOWLEDGE_RESOURCES = 6;
	public static final int DEFAULT_KNOWLEDGE_PADDING = Base.DEFAULT_BASE_SIZE.width + 1; //how much space in cells should be to be able insert knowledge
	
	public static final int PLAYER = 0; // types of players - needed for creation of bases
	public static final int SIMPLE_AI = 1;
	public static final int MEDIUM_AI = 2;
	public static final int ADVANCED_AI = 3;

	//-----------------------------------------------------------------------MODES----------------------------------------------------------------------
	public static final int DOMINATION = 0, ANIHLIATION = 1, MADNESS = 2;
	public static final int DOMINATION_WIN_ROUNDS = 3;//, ANIHLIATION = 1, MADNESS = 2;
	public static final float DOMINATION_WIN_PERCENTAGE = 80.0f;
	public static final int MADNESS_KILL = 25;
	
	
	public static final int DEFAULT_PLAYERS = 2;
	public static final int PLAYER_ID = 0; //living player has always ID 1 as an owner of base
	public static final String PLAYER_NAME = "DarkEye";
	public static final String [] AI_NAMES = {"Simple AI", "Medium AI", "Advanced AI"};
	public static final String [] AI_AGENTS = {"simple_ai.asl", "medium_ai.asl", "advanced_ai.asl"};
	
	private int maxRounds = INFINITE; //default
	
	private int numPlayers; // used to set ID's for players (agents and living player) up to this number from 0 - indicates owners of bases
	private ArrayList<Integer> players; //players in game
	private ArrayList<Color> colors;
	private ArrayList<Location> baseLocations;
	private String playerName;
	private int IncomePerRound = 10;
	
	private int mapColumns = GSize, mapRows = GSize; //grid size
	private int width = WINDOW_WIDTH, height = WINDOW_HEIGHT; // pixel size of screen
	private int numKnowledgeResources = DEFAULT_KNOWLEDGE_RESOURCES;
	private boolean boostEnabled;
	private float boostProbability;
	private int mode;
	private int treshold = INFINITE;
	private HashMap<String, ArrayList<Integer>> teams;
	private int winQuota;
	
	public GameSettings() {
		setPlayers(new ArrayList<Integer>());
		setColors(new ArrayList<Color>());
		setBaseLocations(new ArrayList<Location>());
	}
	
	/**
	 * Default game settings to increase developing speed
	 */
	public void defaultInit() {
		addPlayer(PLAYER, Color.green, new Location(0,0));
		addPlayer(SIMPLE_AI, Color.RED, new Location(GSize-2,GSize-2));
		playerName = PLAYER_NAME;
		mapRows = GSize;
		mapColumns = GSize;
		initDefaultUnits();
	}
	
	public void init() {
		initDefaultUnits();
	}
	
	private void initDefaultUnits() {
		AVAILABLE_UNITS.add(FirstYear.getPrototype());
		AVAILABLE_UNITS.add(SecondYear.getPrototype());
		AVAILABLE_UNITS.add(ThirdYear.getPrototype());
		AVAILABLE_UNITS.add(FourthYear.getPrototype());
		AVAILABLE_UNITS.add(CommonBachelor.getPrototype());
		AVAILABLE_UNITS.add(FitBachelor.getPrototype());
	}
	
	public void addPlayer(int aiType, Color color , Location location) {
		getPlayers().add(aiType);
		getColors().add(color);
		getBaseLocations().add(location);
		numPlayers++;
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

	public int getNumKnowledgeResources() {
		return numKnowledgeResources;
	}

	public void setNumKnowledgeResources(int numKnowledgeResources) {
		this.numKnowledgeResources = numKnowledgeResources;
	}

	public int getIncomePerRound() {
		return IncomePerRound;
	}

	public void setIncomePerRound(int incomePerRound) {
		IncomePerRound = incomePerRound;
	}

	public int getMaxRounds() {
		return maxRounds;
	}

	public void setMaxRounds(int maxRounds) {
		this.maxRounds = maxRounds;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setMapColumns(int mapColumns) {
		this.mapColumns = mapColumns;
	}

	public void setMapRows(int mapRows) {
		this.mapRows = mapRows;
	}

	public void setBoostEnabled(boolean enabled) {
		boostEnabled = enabled;
	}

	public void setBoostProbability(int value) {
		boostProbability = (float) value / 100;
	}

	public boolean isBoostEnabled() {
		return boostEnabled;
	}

	public float getBoostProbability() {
		return boostProbability;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public int getTreshold() {
		if (treshold == INFINITE)
			treshold = (int) (((float)getNumKnowledgeResources()/100.0f) * DOMINATION_WIN_PERCENTAGE);
		return treshold;
	}

	public void addTeams(HashMap<String, ArrayList<Integer>> teams) {
		this.teams = teams;
	}
	
	public HashMap<String, ArrayList<Integer>> getTeams() {
		return teams;
	}

	public int getWinQuota() {
		return winQuota;
	}
	
	public void setWinQuota(int quota) {
		winQuota = quota;
	}
}
