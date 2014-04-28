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

public class getEnemyBases extends DefaultInternalAction {
	private static final long serialVersionUID = 945106733130062292L;

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
    	int agetID = (int) ((NumberTerm) terms[0]).solve();
    	Base base = GameMap.searchBase(agetID);
    	un.unifies(terms[1], ListTermImpl.parseList(base.getEnemies().toString()));
        return true;
    }
}
