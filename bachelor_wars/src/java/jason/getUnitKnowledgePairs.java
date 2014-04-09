// Internal action code for project bachelor_wars

package jason;

import java.util.LinkedList;

import mapping.Node;
import mapping.Wrapper;
import objects.Base;
import objects.GameObject;
import objects.Knowledge;
import objects.units.Unit;
import ui.GameMap;
import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class getUnitKnowledgePairs extends DefaultInternalAction {
	private static final long serialVersionUID = 4836533164553341855L;

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		int agentID = (int)((NumberTerm) terms[0]).solve();
		LinkedList<Wrapper> objects = new LinkedList<>();
		Base base = null;
		for (Base b:GameMap.getBaseList()) {
			if (b.getOwner() == agentID)
				base = b;
		}
		
		for (Unit unit:base.getUnitList()) {
			for (Knowledge knowledge:base.getKnowledgeList()) {
				objects.add(new Wrapper(unit, knowledge, Node.searchPath(unit.getNode(), knowledge.getNode())));
			}
		}
		
		Wrapper.sort(objects);
		
		String finalList = "[";
		for (Wrapper w:objects) {
			finalList = finalList + w;
		}
		
		if (objects.isEmpty())
			return false;
		else 
			return true;
    }
}
