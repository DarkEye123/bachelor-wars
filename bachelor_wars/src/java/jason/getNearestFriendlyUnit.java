// Internal action code for project bachelor_wars

package jason;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

import java.util.LinkedList;

import mapping.Node;
import mapping.Wrapper;
import objects.units.Unit;
import ui.GameMap;

public class getNearestFriendlyUnit extends DefaultInternalAction {
	private static final long serialVersionUID = 2318506250217139138L;
	
	Unit unit;
	Wrapper closest;

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		int unitId = (int)((NumberTerm) terms[0]).solve();
    	unit = GameMap.searchUnit(unitId);

    	LinkedList<Unit> listOfInterest = unit.base.getFriendlyUnits();
    	
    	LinkedList<Wrapper> interests = new LinkedList<>();
    	boolean isEmpty = true;
    	LinkedList<Node> path;
    	for (Unit friendlyUnit:listOfInterest) {
    		path = Node.searchPath(unit.getNode(), friendlyUnit.getNode(), false);
    		interests.add(new Wrapper(unit, friendlyUnit, path));
    		if (isEmpty && !path.isEmpty())
    			isEmpty = false;
    	}
    	
    	if (isEmpty) {
    		for (Unit friendlyUnit:listOfInterest) {
        		path = Node.searchPath(unit.getNode(), friendlyUnit.getNode(), true);
        		interests.add(new Wrapper(unit, friendlyUnit, path));
        	}
    	}
    	
    	if (interests.isEmpty()) //there is no free Knowledge resource (not much possible because others are fighting for them constantly)
    		return false;
    	else {
    		Wrapper.sort(interests);
    		closest = interests.getFirst();
        	un.unifies(terms[1], ListTermImpl.parseList(closest.to.toString()));
        	return true;
    	}
    }
}
