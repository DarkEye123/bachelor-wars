package jason;

import java.util.LinkedList;

import mapping.Node;
import mapping.Wrapper;
import objects.units.Unit;
import ui.GameMap;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

public class getNearestEnemy extends DefaultInternalAction {
	private static final long serialVersionUID = 6603532388222303165L;
	
	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
    	int unitId = (int)((NumberTerm) terms[0]).solve();
    	
    	Unit unit = GameMap.searchUnit(unitId);

    	LinkedList<Unit> listOfInterest = (LinkedList<Unit>) GameMap.getUnitList().clone();
    	listOfInterest.removeAll(unit.base.getUnitList()); //remove our units
    	
    	LinkedList<Wrapper> interests = new LinkedList<>();
    	for (Unit enemyUnit:listOfInterest) {
    		interests.add(new Wrapper(unit, enemyUnit, Node.searchPath(unit.getNode(), enemyUnit.getNode(), false)));
    	}
    	
    	if (interests.isEmpty()) //there is no free Knowledge resource (not much possible because others are fighting for them constantly)
    		return false;
    	else {
    		Wrapper.sort(interests);
        	un.unifies(terms[1], ListTermImpl.parseList(interests.getFirst().to.toString()));
        	return true;
    	}
    }
	
}
