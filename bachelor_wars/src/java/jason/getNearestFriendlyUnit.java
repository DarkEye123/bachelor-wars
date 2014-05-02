// Internal action code for project bachelor_wars

package jason;

import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

import java.util.LinkedList;

import mapping.Wrapper;
import objects.units.Unit;
public class getNearestFriendlyUnit extends jason.getNearest {
	private static final long serialVersionUID = 2318506250217139138L;

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		super.execute(ts, un, terms);
    	LinkedList<Unit> listOfInterest = unit.base.getFriendlyUnits();
    	
    	LinkedList<Wrapper> interests = findInterests(listOfInterest);
    	
    	return decideUnifier(un, terms, interests);
    }
}
