// Internal action code for project bachelor_wars

package jason;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;
import objects.Base;
import ui.GameMap;

public class getUsableUnits extends DefaultInternalAction {
	private static final long serialVersionUID = 3308189868368203320L;

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
    	int agentId = (int)((NumberTerm) terms[0]).solve();
    	Base base = GameMap.searchBase(agentId);
    	un.unifies(terms[1], ListTermImpl.parseList(base.getUsableUnits().toString()));
    	
    	System.out.println(base.getUsableUnits());
    	if (base.getUsableUnits().isEmpty()) {
    		return false;
    	}
    	else
    		return true;
    }
}
