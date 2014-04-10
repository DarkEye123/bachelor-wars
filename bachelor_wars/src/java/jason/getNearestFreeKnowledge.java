// Internal action code for project bachelor_wars

package jason;

import java.util.LinkedList;

import mapping.Node;
import mapping.Wrapper;
import objects.units.Unit;
import objects.Knowledge;
import ui.GameMap;
import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;

/**
 * Finds out nearest knowledge resource that is not owned by agent or no unit has intention of seizing it
 * @author DarkEye
 *
 */
public class getNearestFreeKnowledge extends DefaultInternalAction {
	private static final long serialVersionUID = 3624829097798698211L;

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
    	int unitId = (int)((NumberTerm) terms[0]).solve();
    	Unit unit = null;
    	for (Unit u:GameMap.getUnitList()) {
    		if (unitId == u.getId()) {
    			unit = u;
    		}
    	}
    	LinkedList<Knowledge> listOfInterest = (LinkedList<Knowledge>) GameMap.getKnowledgeList().clone();
    	listOfInterest.removeAll(unit.base.getKnowledgeList()); //remove already owned knowledge resources
    	LinkedList<Knowledge> toRemove = new LinkedList<>();
    	
    	for (Knowledge knowledge:listOfInterest) {
    		for (Unit u:GameMap.getUnitList()) {
        		if ( u.hasIntention(knowledge.getId()) ) {
        			toRemove.add(knowledge);
        			break;
        		}
        	}
    	}
    	listOfInterest.removeAll(toRemove); //remove already assigned knowledge resources
    	
    	LinkedList<Wrapper> interests = new LinkedList<>();
    	for (Knowledge knowledge:listOfInterest) {
    		interests.add(new Wrapper(unit, knowledge, Node.searchPath(unit.getNode(), knowledge.getNode())));
    	}
    	
    	if (interests.isEmpty()) //there is no free Knowledge resource (not much possible because others are fighting for them constantly)
    		return false;
    	else {
    		Wrapper.sort(interests);
        	un.unifies(terms[1], new NumberTermImpl(interests.getFirst().to.getId()));
        	return true;
    	}
    }
}
