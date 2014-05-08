// Internal action code for project bachelor_wars

package jason;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.StringTermImpl;
import jason.asSyntax.Term;
import objects.Base;
import ui.GameMap;

public class setRole extends DefaultInternalAction {
	private static final long serialVersionUID = 7916747150499005371L;

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
    	int agentID = (int)((NumberTerm) terms[0]).solve();
    	String role = ((StringTermImpl) terms[1]).getString();
    	Base base  = GameMap.searchBase(agentID);
    	
    	base.setRole(role);
        return true;
    }
}
