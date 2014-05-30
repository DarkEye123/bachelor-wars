// Internal action code for project bachelor_wars

package jason;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;
import objects.Base;
import ui.GameMap;

public class hasMoreUnitsThan extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
    	int baseID = (int)((NumberTerm) terms[0]).solve();
    	int limit = (int)((NumberTerm) terms[1]).solve();
    	Base base = GameMap.searchBase(baseID);
    	
    	if (base.getUnitList().size() > limit)
    		return true;
    	else
    		return false;
    }
}
