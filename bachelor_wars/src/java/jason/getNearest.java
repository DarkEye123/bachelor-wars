// Internal action code for project bachelor_wars

package jason;

import java.util.LinkedList;

import mapping.Node;
import mapping.Wrapper;
import objects.GameObject;
import objects.units.Unit;
import ui.GameMap;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

public abstract class getNearest extends DefaultInternalAction {
	Unit unit;
	Wrapper closest;

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
    	int unitId = (int)((NumberTerm) terms[0]).solve();
    	unit = GameMap.searchUnit(unitId);
        return true;
    }
    
    public LinkedList<Wrapper> findInterests(LinkedList<? extends GameObject> listOfInterest) {
    	LinkedList<Wrapper> interests = new LinkedList<>();
    	boolean isEmpty = true;
    	LinkedList<Node> path;
    	for (GameObject gameObject:listOfInterest) {
    		path = Node.searchPath(unit.getNode(), gameObject.getNode(), false);
    		if (!path.isEmpty() || unit.getNode().distance(gameObject.getNode()) == 1 ) //if there is path, or unit stands next to its target
    			interests.add(new Wrapper(unit, gameObject, path));
    		if (isEmpty && !path.isEmpty())
    			isEmpty = false;
    	}
    	if (isEmpty) {
    		interests.clear();
    		for (GameObject gameObject:listOfInterest) {
        		path = Node.searchPath(unit.getNode(), gameObject.getNode(), true);
        		if (!path.isEmpty() || unit.getNode().distance(gameObject.getNode()) == 1 )
        			interests.add(new Wrapper(unit, gameObject, path));
        	}
    	}
    	return interests;
    }
    
    protected boolean decideUnifier(Unifier un, Term[] terms, LinkedList<Wrapper> interests) {
		if (interests.isEmpty())
    		return false;
    	else {
    		Wrapper.sort(interests);
    		closest = interests.getFirst();
    		System.out.println(interests);
        	un.unifies(terms[1], ListTermImpl.parseList(closest.to.toString()));
        	return true;
    	}
	}
}
