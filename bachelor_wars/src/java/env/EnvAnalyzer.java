package env;

import java.util.HashMap;

import objects.Knowledge;
import objects.Base;
import ui.GameMap;
import mapping.GameSettings;

public class EnvAnalyzer {
	protected GameEnv environment;
	protected GameSettings settings;
	protected String winner;
	private HashMap<Base, Integer> statistics = new HashMap<>();
	private int conditionCounter;
	private int baseCounter;
	
	public EnvAnalyzer(GameEnv environment, GameSettings settings) {
		this.environment = environment;
		this.settings = settings;
		initStatistic();
	}
	
	private void initStatistic() {
		for (Base b:GameMap.getBaseList()) {
			statistics.put(b, 0);
		}		
	}

	public String getWinner() {
		return null;
	}
	
	public boolean isWinner() {
		if (getWinner() == null) {
			return false;
		} else {
			return true;
		}
	}
	
	private Base findDominantBase() {
		if (settings.getMode() == GameSettings.DOMINATION) {
			return findBestKnowledgeHolder();
		}
		return null;
	}
	
	private Base findBestKnowledgeHolder() {
		Base ret = null;
		int max = Integer.MIN_VALUE;
		for (Base base:GameMap.getBaseList()) {
			if (base.getKnowledgeList().size() > max) {
				max = base.getKnowledgeList().size();
				ret = base;
			}
		}
		return ret;
	}

	/*
	 * For mode Domination is winner if he posses atleast 80 percent of all resources for 3 rounds or 
	 * if there is a limited number of rounds and nobody was able to possess them -> the one which
	 * possess the most knowledge at the end is the winner
	 */
	/**
	 * 
	 * @return true if there is winner
	 */
	public boolean analyzeEnvironment() {
		if (baseCounter >= GameSettings.DEFAULT_SEIZE_ROUNDS) {
//			GameMap.getBaseList().remove
		}
		
		
		
		if (settings.getMode() == GameSettings.DOMINATION) {
				if (conditionCounter == GameSettings.DOMINATION_WIN_ROUNDS) {
					environment.view.getGameMap().setEnabled(false);
					winner = findDominantBase().getName();
					environment.view.getGameMap().printWinner(winner);
					return true;
				} else {
					Base base = findDominantBase();
					if (base.getKnowledgeList().size() >= settings.getTreshold()) {
						conditionCounter++;
					} else {
						conditionCounter = 0; //winning rounds were cut off
					}
				}
			if(settings.getMaxRounds() != GameSettings.INFINITE && GameMap.ROUND >= settings.getMaxRounds()) { //we are at limit of rounds
				environment.view.getGameMap().setEnabled(false);
				winner = findDominantBase().getName();
				environment.view.getGameMap().printWinner(winner);
				return true;
			} 
		}
		return false;
	}
	
}
