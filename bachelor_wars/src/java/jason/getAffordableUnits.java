// Internal action code for project bachelor_wars

package jason;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

import java.util.Collections;
import java.util.LinkedList;

import mapping.GameSettings;
import objects.Base;
import objects.units.Unit;
import ui.GameMap;

public class getAffordableUnits extends DefaultInternalAction {
	private static final long serialVersionUID = -92287919131743061L;
	protected boolean canUnify = true; //to be able unify the result for inherited actions, this class can not unify its result
	LinkedList<Unit> available;
	Base base;
	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
    	int agentID = (int)((NumberTerm) terms[0]).solve();
    	base = GameMap.searchBase(agentID);
    	available = new LinkedList<>();
    	
    	for (Unit u:GameSettings.AVAILABLE_UNITS) {
//    		System.out.println("base knowledge: " + base.getKnowledge() + " unit knowledge: " + u.getCost());
    		if (base.getKnowledge() >= u.getCost()) {
    			available.add(u);
    		}
    	}
    	
    	if (available.isEmpty())
    		return false;
    	
    	
    	Collections.sort(available);
    	ListTerm list = ListTermImpl.parseList(available.toString());
    	System.out.println(canUnify);
    	if (canUnify)
    		un.unifies(terms[1], list);
    	
    	return true;
    	
    }
}
