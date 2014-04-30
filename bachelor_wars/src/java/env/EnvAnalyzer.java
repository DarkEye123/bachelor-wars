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
	private int conditionCounter = 1;
	private int baseCounter;
	public Base winningBase;
	
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
	
	private LinkedList<Base> getSeizedBases(Base activeBase) {
		LinkedList<Base> toRemove = new LinkedList<>();
		for (Base base:GameMap.getBaseList()) {
			if (!base.equals(activeBase)) {
				if (base.isSeized(activeBase)) { //TODO if more than 1 round for seizing will be needed, here will be need some hash<activeBase<targetBase,Integer>>
					toRemove.add(base);
				} 
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
	public boolean analyzeEnvironment() {
			seizeKnowledge(GameMap.getActiveBases().getFirst());
			deleteSeizedBases(getSeizedBases(GameMap.getActiveBases().getFirst()));
			if (GameMap.getBaseList().size() == 1) { // there is only one left, so it is winner
				environment.view.getGameMap().setCanManipulate(false);
				winner = GameMap.getBaseList().getFirst().getName();
				environment.view.getGameMap().printWinner(winner);
				return true;
			}
			if (settings.getMode() == GameSettings.DOMINATION) {
					if (conditionCounter == GameSettings.DOMINATION_WIN_ROUNDS) {
						environment.view.getGameMap().setCanManipulate(false);
						winner = winningBase.getName();
						environment.view.getGameMap().printWinner(winner);
						return true;
					} else {
						Base base = findDominantBase();
						if (winningBase == null)
							winningBase = base;
						
//						System.out.println(base.getKnowledgeList().size() + " " + settings.getTreshold());
						
						if (winningBase.equals(base) && base.getKnowledgeList().size() >= settings.getTreshold()) {
							conditionCounter++;
						} else {
							if (base.getKnowledgeList().size() >= settings.getTreshold()) { //there is another base, that gets into leading in this round - need to has same conditions
								winningBase = base;
								conditionCounter = 2; 
							} else { //there is no base with sufficient domination, counter is reseted
								winningBase = null;
								conditionCounter = 1; //winning rounds were cut off
							}
						}
					}
				if(settings.getMaxRounds() != GameSettings.INFINITE && GameMap.ROUND >= settings.getMaxRounds()) { //we are at limit of rounds
					environment.view.getGameMap().setCanManipulate(false);
					winner = winningBase == null ? findDominantBase().getName() : winningBase.getName();
					environment.view.getGameMap().printWinner(winner);
					return true;
				} 
			}
		return false;
	}
	
	public void seizeKnowledge(Base activeBase) {
		synchronized (GameMap.countLock) {
			int sum = settings.getIncomePerRound();
	    	for (Knowledge k:GameMap.getKnowledgeList()) {
	    		for (Unit u:activeBase.getUnitList()) {
	    			if (k.getNode().equals(u.getNode()) && k.STATE < GameMap.ROUND) {//seized last round
	    				k.setBase(activeBase);
	    				sum += k.getKnowledgePerRound();
	    			}
	    		}
	    	}
	    	activeBase.addKnowledge(sum);
		}
	}

	private void deleteSeizedBases(LinkedList<Base> seizedBases) {
		synchronized (GameMap.countLock) {
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
				LinkedList<Knowledge> test = (LinkedList<Knowledge>) seizedBase.getKnowledgeList().clone();
				for (Knowledge k:test)
					k.setBase(null);
			}
			GameMap.getBaseList().removeAll(seizedBases);
			GameMap.getActiveBases().removeAll(seizedBases);
		}
	}

	public int getConditionCounter() {
		return conditionCounter;
	}
	
}
