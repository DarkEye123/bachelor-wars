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
	
	public boolean canRemoveWithIntention = true;
	public boolean ignoreFriendlySeized = false;
	public boolean calledByChildClass = false;
	
	
	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		super.execute(ts, un, terms);
    	LinkedList<Knowledge> listOfInterest;
    	
    	Base base = unit.base;
    	if (!base.getAllies().isEmpty() && !base.getRole().equals("unknown")) { //if agent have allies and there is somebody that understand his communication API too
    			if (!calledByChildClass) {
    				listOfInterest = base.getAvailableKnowledge();
    			} else { //percept all knowledge on the map to be able tell, if some of them is in reach
    				listOfInterest = (LinkedList<Knowledge>) GameMap.getKnowledgeList().clone();
    			}
    	} else {
    		listOfInterest = (LinkedList<Knowledge>) GameMap.getKnowledgeList().clone();
    	}
    	
    	listOfInterest.removeAll(unit.base.getKnowledgeList()); //remove already owned knowledge resources
    	
    	if (canRemoveWithIntention) {
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
    	
    	if (ignoreFriendlySeized && !base.getRole().equals("seizer")) { //ignore already seized resources by ally, if actual base is seizer, this request is not proper
    		for (Base b:base.getAllies()) {
    			listOfInterest.removeAll(b.getKnowledgeList());
    		}
    	}
    	
    	LinkedList<Wrapper> interests = findInterests(listOfInterest);
    	
    	return decideUnifier(un, terms, interests);
    }
}
