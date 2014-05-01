// Internal action code for project bachelor_wars

package jason;

import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

import java.util.LinkedList;

import mapping.Wrapper;
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
	
	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		super.execute(ts, un, terms);
    	LinkedList<Knowledge> listOfInterest = (LinkedList<Knowledge>) GameMap.getKnowledgeList().clone();
    	listOfInterest.removeAll(unit.base.getKnowledgeList()); //remove already owned knowledge resources
    	
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
