package env;

import java.util.HashMap;
import java.util.LinkedList;

import objects.Knowledge;
import objects.Base;
import objects.units.Unit;
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
	
	private LinkedList<Base> getSeizedBases() {
		LinkedList<Base> toRemove = new LinkedList<>();
		for (Base base:GameMap.getBaseList()) {
			if (base.isSeized()) {
				baseCounter = statistics.get(base) + 1;
				statistics.put(base, baseCounter);
				if (baseCounter >= GameSettings.BASE_SEIZE_ROUNDS) { //TODO
					toRemove.add(base);
				}
			} else {
				statistics.put(base, 0);
			}
		}
		return toRemove;
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
	public boolean analyzeEnvironment(boolean isRoundEnd) {
		if (!isRoundEnd) {
			seizeKnowledge(GameMap.getActiveBases().getFirst());
			deleteSeizedBases(getSeizedBases());
			if (GameMap.getBaseList().size() == 1) { // there is only one left, so it is winner
				environment.view.getGameMap().setEnabled(false);
				winner = GameMap.getBaseList().getFirst().getName();
				environment.view.getGameMap().printWinner(winner);
				return true;
			}
		}
		else {
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
		}
		return false;
	}
	
	public void seizeKnowledge(Base activeBase) {
    	for (Knowledge k:GameMap.getKnowledgeList()) {
    		for (Unit u:activeBase.getUnitList()) {
    			if (k.getNode().equals(u.getNode()) && k.STATE < GameMap.ROUND) //seized last round
    				k.setBase(activeBase);
    		}
    	}
	}

	private void deleteSeizedBases(LinkedList<Base> seizedBases) {
		for (Base seizedBase:seizedBases) {
			for (Base ally:seizedBase.getAllies()) {
				ally.getAllies().remove(seizedBase);
			}
			for (Base enemy:seizedBase.getEnemies()) {
				enemy.getEnemies().remove(seizedBase);
			}
			
			GameMap.getUnitList().removeAll(seizedBase.getUnitList());
			for (Unit u: GameMap.getUnitList()) {
				u.getIntentions().remove(seizedBase);
			}
			
			for (Knowledge k:seizedBase.getKnowledgeList())
				k.setBase(null);
		}
		GameMap.getBaseList().removeAll(seizedBases);
		GameMap.getActiveBases().removeAll(seizedBases);
	}
	
}
