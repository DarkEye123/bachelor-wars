// Internal action code for project bachelor_wars

package jason;

import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

import java.util.LinkedList;
import java.util.TreeMap;

import mapping.Wrapper;
import objects.Base;
import objects.Knowledge;
import objects.units.Unit;
import ui.GameMap;

/**
 * Finds out nearest knowledge resource that is not owned by agent or no unit has intention of seizing it
 * @author DarkEye
 *
 */
public class getNearestFreeKnowledge extends jason.getNearest {
	private static final long serialVersionUID = 3624829097798698211L;
	
	public boolean canRemove = true;
	public boolean ignoreFriendlySeized = false;
	
	private static final float SEIZER_PERCENTAGE = 80.0f;
	
	public LinkedList<Knowledge> get80() {
		LinkedList<Knowledge> output = new LinkedList<>();
		TreeMap<Integer, Integer> source = new TreeMap<>();
		int treshold = (int) (((float)GameMap.getKnowledgeList().size()/100.0f) * SEIZER_PERCENTAGE);
		for (Knowledge k:GameMap.getKnowledgeList()) {
			source.put(k.getId(), unit.base.getNode().distance(k.getNode()));
		}
		
		int num = GameMap.getKnowledgeList().size();
		while (num > treshold) {
			--num;
			int min = Integer.MAX_VALUE;
			int keyToRemove = 0;
			for (Integer key:source.keySet()) {
				if (source.get(key) < min) {
					min = source.get(key);
					keyToRemove = key;
				}
			}
			source.remove(keyToRemove);
		}
		for (Knowledge k:GameMap.getKnowledgeList()) {
			if ( source.keySet().contains(k.getId()) )
				output.add(k);
		}
		return output;
	}
	
	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		super.execute(ts, un, terms);
    	LinkedList<Knowledge> listOfInterest = (LinkedList<Knowledge>) GameMap.getKnowledgeList().clone();
    	listOfInterest.removeAll(unit.base.getKnowledgeList()); //remove already owned knowledge resources
    	
    	if (ignoreFriendlySeized) {
    		for (Base base:unit.base.getAllies()) {
    			listOfInterest.removeAll(base.getKnowledgeList());
    		}
    	}
    	
    	if (canRemove) {
    		LinkedList<Knowledge> toRemove = new LinkedList<>();
	    	for (Knowledge knowledge:listOfInterest) { //search knowledge resources that are free, but have some unit with intention assigned
	    		for (Unit u:unit.base.getUnitList()) { //search for units from agent base that already have this 
	        		if ( u.hasIntention(knowledge) ) {
	        			toRemove.add(knowledge);
	        			break;
	        		}
	        	}
	    	}
	    	listOfInterest.removeAll(toRemove); //remove already assigned knowledge resources
    	}
    	LinkedList<Wrapper> interests = findInterests(listOfInterest);
    	
    	return decideUnifier(un, terms, interests);
    }
}
