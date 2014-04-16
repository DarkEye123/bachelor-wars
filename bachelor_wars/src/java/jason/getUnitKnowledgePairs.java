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
import objects.Base;
import objects.Knowledge;
import objects.units.Unit;
import ui.GameMap;

public class getUnitKnowledgePairs extends DefaultInternalAction {
	private static final long serialVersionUID = 4836533164553341855L;

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		int agentID = (int)((NumberTerm) terms[0]).solve();
		LinkedList<Wrapper> objects = new LinkedList<>();
		Base base = null;
		for (Base b:GameMap.getBaseList()) {
			if (b.getOwner() == agentID) {
				base = b;
				break;
			}
		}
		
		LinkedList<Knowledge> listOfInterest = (LinkedList<Knowledge>) GameMap.getKnowledgeList().clone(); //get all knowledge
		listOfInterest.removeAll(base.getKnowledgeList()); //remove that sources, that are already own
		for (Unit unit:base.getUnitList()) {
			for (Knowledge knowledge:GameMap.getKnowledgeList()) {
				objects.add(new Wrapper(unit, knowledge, Node.searchPath(unit.getNode(), knowledge.getNode(), false)));
			}
		}
		
		Wrapper.sort(objects);
		
		un.unifies(terms[1], ListTermImpl.parseList(objects.toString()));
		
		if (objects.isEmpty())
			return false;
		else 
			return true;
    }
}
