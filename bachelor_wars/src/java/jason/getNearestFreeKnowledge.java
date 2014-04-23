// Internal action code for project bachelor_wars

package jason;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;

import java.util.LinkedList;

import mapping.Node;
import mapping.Wrapper;
import objects.Knowledge;
import objects.units.Unit;
import ui.GameMap;

/**
 * Finds out nearest knowledge resource that is not owned by agent or no unit has intention of seizing it
 * @author DarkEye
 *
 */
public class getNearestFreeKnowledge extends DefaultInternalAction {
	private static final long serialVersionUID = 3624829097798698211L;
	
	protected Wrapper closest;
	protected Unit unit;

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
    	int unitId = (int)((NumberTerm) terms[0]).solve();
    	
    	unit = GameMap.searchUnit(unitId);

    	LinkedList<Knowledge> listOfInterest = (LinkedList<Knowledge>) GameMap.getKnowledgeList().clone();
    	listOfInterest.removeAll(unit.base.getKnowledgeList()); //remove already owned knowledge resources
    	LinkedList<Knowledge> toRemove = new LinkedList<>();
    	
    	for (Knowledge knowledge:listOfInterest) { //search knowledge resources that are free, but have some unit with intention assigned
    		for (Unit u:unit.base.getUnitList()) { //search for units from agent base that already have this 
        		if ( u.hasIntention(knowledge) ) {
        			toRemove.add(knowledge);
        			break;
        		}
        	}
    	}
    	listOfInterest.removeAll(toRemove); //remove already assigned knowledge resources
    	
    	LinkedList<Wrapper> interests = new LinkedList<>();
    	boolean isEmpty = true;
    	LinkedList<Node> path;
    	for (Knowledge knowledge:listOfInterest) {
    		path = Node.searchPath(unit.getNode(), knowledge.getNode(), false);
    		interests.add(new Wrapper(unit, knowledge, path));
    		if (isEmpty && !path.isEmpty())
    			isEmpty = false;
    	}
    	
    	if (isEmpty) {
    		for (Knowledge knowledge:listOfInterest) {
        		path = Node.searchPath(unit.getNode(), knowledge.getNode(), true);
        		interests.add(new Wrapper(unit, knowledge, path));
        	}
    	}
    	
    	if (interests.isEmpty()) //there is no free Knowledge resource (not much possible because others are fighting for them constantly)
    		return false;
    	else {
    		Wrapper.sort(interests);
    		closest = interests.getFirst();
        	un.unifies(terms[1], ListTermImpl.parseList(interests.getFirst().to.toString()));
        	return true;
    	}
    }
}
