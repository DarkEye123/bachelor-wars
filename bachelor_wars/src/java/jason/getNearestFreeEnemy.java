// Internal action code for project bachelor_wars

package jason;

import java.util.LinkedList;

import mapping.Node;
import mapping.Wrapper;
import objects.Knowledge;
import objects.units.Unit;
import ui.GameMap;
import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class getNearestFreeEnemy extends DefaultInternalAction {
	private static final long serialVersionUID = -4523166593804191840L;

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
    	int unitId = (int)((NumberTerm) terms[0]).solve();
    	
    	Unit unit = GameMap.searchUnit(unitId);

    	LinkedList<Unit> listOfInterest = (LinkedList<Unit>) GameMap.getUnitList().clone();
    	listOfInterest.removeAll(unit.base.getUnitList()); //remove our units
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
    	
    	LinkedList<Wrapper> interests = new LinkedList<>();
    	boolean isEmpty = true;
    	LinkedList<Node> path;
    	for (Unit enemyUnit:listOfInterest) {
    		path = Node.searchPath(unit.getNode(), enemyUnit.getNode(), false);
    		interests.add(new Wrapper(unit, enemyUnit, path));
    		if (isEmpty && !path.isEmpty())
    			isEmpty = false;
    	}
    	if (isEmpty) {
    		interests.clear();
    		for (Unit enemyUnit:listOfInterest) {
        		path = Node.searchPath(unit.getNode(), enemyUnit.getNode(), true);
        		interests.add(new Wrapper(unit, enemyUnit, path));
        	}
    	}
    	
    	if (interests.isEmpty()) //there is no free Knowledge resource (not much possible because others are fighting for them constantly)
    		return false;
    	else {
    		Wrapper.sort(interests);
        	un.unifies(terms[1], ListTermImpl.parseList(interests.getFirst().to.toString()));
        	return true;
    	}
    }
}
