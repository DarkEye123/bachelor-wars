// Internal action code for project bachelor_wars

package jason;

import objects.units.Unit;
import ui.GameMap;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

public class shouldHeal extends DefaultInternalAction {
	private static final long serialVersionUID = -8888047598107442001L;

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		int unitID = (int)((NumberTerm)terms[0]).solve();
		Unit unit = GameMap.searchUnit(unitID);
		
		if (unit.needHeal())
			return true;
		else
			return false;
    }
}
