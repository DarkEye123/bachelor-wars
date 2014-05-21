// Internal action code for project bachelor_wars

package jason;

import java.util.LinkedList;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;
import objects.Base;
import objects.units.Unit;
import ui.GameMap;

public class getUnitsInQuadrant extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
    	int agentId = (int)((NumberTerm) terms[0]).solve();
    	Base base = GameMap.searchBase(agentId);
    	LinkedList<Unit> inQuadrant = base.getUsableUnitsInQuadrant();
    	if (!inQuadrant.isEmpty()) {
    		un.unifies(terms[1], ListTermImpl.parseList(inQuadrant.toString()));
    		return true;
    	}
    	else
    		return false;
    }
}
