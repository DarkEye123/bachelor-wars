// Internal action code for project bachelor_wars

package jason;

import objects.Base;
import objects.units.Unit;
import ui.GameMap;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;



public class isAmbushed extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
    	int baseID = (int)((NumberTerm) terms[0]).solve();
    	Base base = GameMap.searchBase(baseID);
    	
    	if (base.enemiesInQuadrant.size() >= base.countAlliesInbaseQuadrant() )
    		return true;
    	else 
    		return false;
    }
}
