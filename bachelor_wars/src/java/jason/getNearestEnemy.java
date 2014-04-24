package jason;

import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Term;

import java.util.LinkedList;

import mapping.Wrapper;
import objects.units.Unit;
import ui.GameMap;

public class getNearestEnemy extends jason.getNearest {
	private static final long serialVersionUID = 6603532388222303165L;
	
	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
    	super.execute(ts, un, terms);
    	LinkedList<Unit> listOfInterest = (LinkedList<Unit>) GameMap.getUnitList().clone();
    	listOfInterest.removeAll(unit.base.getUnitList()); //remove our units
    	
    	LinkedList<Wrapper> interests = findInterests(listOfInterest);
    	
    	return decideUnifier(un, terms, interests);
    }
}
