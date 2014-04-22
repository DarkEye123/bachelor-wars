// Internal action code for project bachelor_wars

package jason;

import java.util.LinkedList;

import mapping.Wrapper;
import ui.GameMap;
import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class getUnitEnemyPairs extends jason.getPairs {
	private static final long serialVersionUID = 5130917823826872927L;

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
    	super.execute(ts, un, terms);
		LinkedList<Wrapper> finalList = getShortestPaths(GameMap.getUnitList(), base.getUnitList(), base);
		Wrapper.sort(finalList); //get sorted paths to all knowledge resources (unit:knowledge .. etc) so this are best pairs for given round
		
		LinkedList<LinkedList<Wrapper>> groups = groupByPathLength(finalList); //group objects with same path lengths
		
		finalList = evaluateBestOption(groups); // find best movement variation for actual round for given targets
		
		un.unifies(terms[1], ListTermImpl.parseList(finalList.toString()));
		
		if (finalList.isEmpty())
			return false;
		else 
			return true;
    }
}
