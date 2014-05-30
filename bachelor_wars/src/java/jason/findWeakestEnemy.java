// Internal action code for project bachelor_wars

package jason;

import objects.Base;
import ui.GameMap;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.Term;

public class findWeakestEnemy extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
    	int baseID = (int)((NumberTerm) terms[0]).solve();
    	Base base = GameMap.searchBase(baseID);
    	
    	Base ret = base.getEnemies().getFirst();
    	
    	for (Base b:base.getEnemies()) { //find the weakest base
    		if (ret.getUnitList().size() > b.getUnitList().size())
    			ret = b;
    	}
    	
    	un.unifies( terms[1], ASSyntax.createNumber(ret.getOwner()) );
    	
        return true;
    }
}
