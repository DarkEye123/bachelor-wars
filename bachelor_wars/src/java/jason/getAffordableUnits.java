// Internal action code for project bachelor_wars

package jason;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

import java.util.LinkedList;

import mapping.GameSettings;
import objects.Base;
import objects.GameObject;
import objects.units.Unit;
import ui.GameMap;

public class getAffordableUnits extends DefaultInternalAction {
	private static final long serialVersionUID = -92287919131743061L;

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
    	int agentID = (int)((NumberTerm) terms[0]).solve();
    	Base base = GameMap.searchBase(agentID);
    	LinkedList<Unit> available = new LinkedList<>();
    	
    	for (Unit u:GameSettings.AVAILABLE_UNITS) {
    		if (base.getKnowledge() >= u.getCost())
    			available.add(u);
    	}
    	
    	if (available.isEmpty())
    		return false;
    	
    	ListTerm list = ListTermImpl.parseList(GameObject.createListFromObjects(available));
    	un.unifies(terms[1], list);
    	
    	return true;
    	
    }
}
