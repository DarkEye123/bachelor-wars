package mapping;
import jason.environment.grid.Location;

import java.awt.Color;
import java.util.ArrayList;

import models.GameModel;


public class GameSettings {
	public static final int DEFAULT_PLAYERS = 2;
	
	private int numPlayers;
	private ArrayList<Integer> players; //players in game
	private ArrayList<Color> colors;
	private ArrayList<Location> baseLocations;
	
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
		getPlayers().add(GameModel.PLAYER);
		getPlayers().add(GameModel.SIMPLE_AI);
		getColors().add(Color.green);
		getColors().add(Color.RED);
		getBaseLocations().add(new Location(0,0));
		getBaseLocations().add(new Location(GameModel.GSize-2,GameModel.GSize-2));
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
}
