// Internal action code for project bachelor_wars

package jason;

import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

import java.util.LinkedList;

import mapping.Wrapper;
import objects.units.Unit;
import ui.GameMap;

public class getNearestFreeEnemy extends jason.getNearest {
	private static final long serialVersionUID = -4523166593804191840L;

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		super.execute(ts, un, terms);
    	LinkedList<Unit> listOfInterest = (LinkedList<Unit>) GameMap.getUnitList().clone();
    	listOfInterest.removeAll(unit.base.getFriendlyUnits()); //remove our units
    	LinkedList<Unit> toRemove = new LinkedList<>();
    	
    	for (Unit enemyUnit:listOfInterest) { //search knowledge resources that are free, but have some unit with intention assigned
    		for (Unit friendlyUnit:unit.base.getUnitList()) {
        		if ( friendlyUnit.hasIntention(enemyUnit) ) {
        			toRemove.add(enemyUnit);
        			break;
        		}
        	}
    	}
    	listOfInterest.removeAll(toRemove); //remove already assigned knowledge resources
    	
    	LinkedList<Wrapper> interests = findInterests(listOfInterest);
    	
    	return decideUnifier(un, terms, interests);
    }
}
