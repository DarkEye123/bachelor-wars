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
	protected Base winner;
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
	
	private Base findDominantBase(Base activeBase) {
		if (settings.getMode() == GameSettings.DOMINATION) {
			return findBestKnowledgeHolder(activeBase);
		} else if (settings.getMode() == GameSettings.MADNESS) {
			return findBestKillingHolder(activeBase);
		} else {
			return findBestBaseSeizer(activeBase);
		}
	}
	
	private Base findBestBaseSeizer(Base activeBase) {
		if (winningBase == null) {  //if there is no winning base, find it
			int max = Integer.MIN_VALUE;
			Base ret = null;
			for (Base b: GameMap.getBaseList()) {
				if (b.getSeizedBases() > max) {
					max = b.getSeizedBases();
					ret = b;
				}
			}
			return ret;
		} else {  //if there is some winning base, just compare actual one with the winning
			if (winningBase.getSeizedBases() >= activeBase.getSeizedBases())
				return winningBase;
			else
				return activeBase;
		}
	}

	private Base findBestKillingHolder(Base activeBase) {
		if (winningBase == null) {  //if there is no winning base, find it
			int max = Integer.MIN_VALUE;
			Base ret = null;
			for (Base b: GameMap.getBaseList()) {
				if (b.getKilledEnemies() > max) {
					max = b.getKilledEnemies();
					ret = b;
				}
			}
			return ret;
		} else {  //if there is some winning base, just compare actual one with the winning
			if (winningBase.getKilledEnemies() >= activeBase.getKilledEnemies())
				return winningBase;
			else
				return activeBase;
		}
	}

	private LinkedList<Base> getSeizedBases(Base activeBase) {
		LinkedList<Base> toRemove = new LinkedList<>();
		for (Base base:GameMap.getBaseList()) {
			if (!base.equals(activeBase)) {
				if (base.isSeized(activeBase)) {
					toRemove.add(base);
					activeBase.addKilledEnemy(base.getUnitList().size());
					activeBase.addSeizedBase();
				} 
			}
		}
		return toRemove;
	}

	private Base findBestKnowledgeHolder(Base activeBase) {
		if (winningBase == null) { //if there is no winning base, find it
			int max = Integer.MIN_VALUE;
			Base ret = null;
			for (Base base:GameMap.getBaseList()) {
				if (base.getKnowledgeList().size() > max) {
					max = base.getKnowledgeList().size();
					ret = base;
				}
			}
			return ret;
		} else { //if there is some winning base, just compare actual one with the winning
			if (activeBase.getKnowledgeList().size() >= winningBase.getKnowledgeList().size()) {
				return activeBase;
			} else {
				return winningBase;
			}
		}
	}
	
	public boolean analyzeEnvironment() {
		return this.analyzeEnvironment(GameMap.getActiveBases().getFirst());
	}
	
	public boolean isFriendlyOnly(Base activeBase) {
		if (activeBase.getTeam() != null) {
			if (GameMap.getBaseList().size() > 1) {
				for (Base base:GameMap.getBaseList()) {
					if (!activeBase.getTeam().equals(base.getTeam()))
						return false;
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
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
	public boolean analyzeEnvironment(Base activeBase) {
			seizeKnowledge(activeBase);
			deleteSeizedBases(getSeizedBases(activeBase));
			if (GameMap.getBaseList().size() == 1 || isFriendlyOnly(activeBase)) { // there is only one left, so it is winner
				environment.view.getGameMap().setCanManipulate(false);
				winner = GameMap.getBaseList().getFirst();
				environment.view.getGameMap().printWinner(winner);
				return true;
			}
			
			if (settings.getMode() == GameSettings.DOMINATION) {
				Base base = findDominantBase(activeBase);
					if (conditionCounter == settings.getWinQuota() && base.equals(winningBase)) {
						environment.view.getGameMap().setCanManipulate(false);
						winner = winningBase;
						environment.view.getGameMap().printWinner(winner);
						return true;
					} else {
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
			} else if (settings.getMode() == GameSettings.MADNESS) {
				
				winningBase = findDominantBase(activeBase);
				
				if (winningBase.getKilledEnemies() >= settings.getWinQuota()) {
					environment.view.getGameMap().setCanManipulate(false);
					winner = winningBase;
					environment.view.getGameMap().printWinner(winner);
					return true;
				}
			} else {
				winningBase = findDominantBase(activeBase);
			}
			
			//when game ended due to round limit
			if(settings.getMaxRounds() != GameSettings.INFINITE && GameMap.ROUND >= settings.getMaxRounds()) { //we are at limit of rounds
				environment.view.getGameMap().setCanManipulate(false);
				winner = winningBase == null ? findDominantBase(activeBase) : winningBase;
				environment.view.getGameMap().printWinner(winner);
				return true;
			} 
		return false;
	}
	
	public void seizeKnowledge(Base activeBase) {
		synchronized (GameMap.countLock) {
	    	for (Knowledge k:GameMap.getKnowledgeList()) {
	    		for (Unit u:activeBase.getUnitList()) {
	    			if (k.getNode().equals(u.getNode()) && k.STATE < GameMap.ROUND) {//seized last round
	    				k.setBase(activeBase);
	    				for (Unit unit:activeBase.getUnitList()) { //agent seized this resource, delete intentions for seizing it
	    					unit.getIntentions().remove(k);
	    				}
	    			}
	    		}
	    	}
	    	int sum = settings.getIncomePerRound();
	    	for (Knowledge k:activeBase.getKnowledgeList())
	    		sum += k.getKnowledgePerRound();
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
