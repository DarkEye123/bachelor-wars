// Internal action code for project bachelor_wars

package jason;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;
import objects.units.Unit;
import ui.GameMap;

public class hasIntention extends DefaultInternalAction {
	private static final long serialVersionUID = 2325344610813047367L;

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
    	int unitId = (int)((NumberTerm) terms[0]).solve();
    	Unit unit = GameMap.searchUnit(unitId);
//    	System.out.println(unit.getIntentions() + " " + unit.getId() + " " + unit.hasIntention());
        return unit.hasIntention();
    }
}
